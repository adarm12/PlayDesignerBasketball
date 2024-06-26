package com.ashcollege;
import com.ashcollege.entities.Friendship;
import com.ashcollege.entities.Play;
import com.ashcollege.entities.User;
import com.ashcollege.responses.BasicResponse;
import com.ashcollege.responses.ListUserResponse;
import com.ashcollege.responses.LoginResponse;
import com.github.javafaker.Faker;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static com.ashcollege.utils.Constants.ACTIVE_FRIEND;
import static com.ashcollege.utils.Constants.WAITING_FOR_ACCEPTER;
import static com.ashcollege.utils.Errors.*;


@Transactional
@Component
@SuppressWarnings("unchecked")
public class Persist {

    private static final Logger LOGGER = LoggerFactory.getLogger(Persist.class);
    private final SessionFactory sessionFactory;


    @Autowired
    public Persist(SessionFactory sf) {
        this.sessionFactory = sf;
    }

    public Session getQuerySession() {
        return sessionFactory.getCurrentSession();
    }

    public void save(Object object) {
        this.sessionFactory.getCurrentSession().saveOrUpdate(object);
    }

    public <T> T loadObject(Class<T> clazz, int oid) {
        return this.getQuerySession().get(clazz, oid);
    }

    public BasicResponse insertUser(String username, String password, String repeatPassword) {
        BasicResponse basicResponse = new BasicResponse(false, ERROR_MISSING_FIELDS);
        if (!username.isEmpty()) {
            if (usernameAvailable(username)) {
                        if (!password.isEmpty()) {
                            if (password.equals(repeatPassword)) {
                                if (validPassword(password).isSuccess()) {
                                        User user = new User(username, password);
                                        Faker faker = new Faker();
                                        user.setSecret(faker.random().hex(5));
                                        save(user);
                                        basicResponse.setSuccess(true);
                                        basicResponse.setErrorCode(NO_ERRORS);
                                } else basicResponse = validPassword(password);
                            } else basicResponse.setErrorCode(ERROR_PASSWORD_NOT_MATCH);
                        } else basicResponse.setErrorCode(ERROR_NO_PASSWORD);
            } else basicResponse.setErrorCode(ERROR_USERNAME_TAKEN);
        } else basicResponse.setErrorCode(ERROR_NO_USERNAME);
        return basicResponse;
    }

    public BasicResponse validPassword(String password) {
        BasicResponse basicResponse = new BasicResponse(false, null);
        if (password.length() >= 8) {
            if (password.contains("@") || password.contains("!")) {
                basicResponse.setSuccess(true);
            } else basicResponse.setErrorCode(ERROR_PASSWORD_DOES_NOT_CONTAIN_SYMBOLS);
        } else basicResponse.setErrorCode(ERROR_SHORT_PASSWORD);

        return basicResponse;
    }

    public LoginResponse login(String username, String password) {
        LoginResponse loginResponse = new LoginResponse(false, ERROR_MISSING_FIELDS);
        if (!username.isEmpty()) {
            if (usernameExist(username)) {
                if (!password.isEmpty()) {
                    if (userExists(username, password) != null) {
                        User user = userExists(username, password);
                        loginResponse.setSuccess(true);
                        loginResponse.setErrorCode(NO_ERRORS);
                        loginResponse.setId(user.getId());
                        loginResponse.setSecret(user.getSecret());
                        loginResponse.setUser(user);
                    } else loginResponse.setErrorCode(ERROR_INCORRECT_PASSWORD);
                } else loginResponse.setErrorCode(ERROR_NO_PASSWORD);
            } else loginResponse.setErrorCode(ERROR_NO_SUCH_USER);
        } else loginResponse.setErrorCode(ERROR_NO_USERNAME);
        return loginResponse;
    }


    public List<User> searchUser(String secretFrom, String partOfUsername) {
        User userSender = null;
        userSender = getUserBySecret(secretFrom);
        List<User> users = new ArrayList<>();
        users = (List<User>) this.sessionFactory.getCurrentSession().createQuery(
                        "FROM User WHERE username LIKE :username")
                .setParameter("username", "%" + partOfUsername + "%")
                .list();
        System.out.println(users.size());
        for (User user : users) {
            if (user.getUsername().equals(userSender.getUsername())) {
                users.remove(userSender);
            }
        }
        return users;
    }

    public BasicResponse acceptFriend(String secret, String usernameToAccept) {
        BasicResponse basicResponse = new BasicResponse(false, 0);
        User accepter = getUserBySecret(secret);
        if (accepter == null) {
            basicResponse.setErrorCode(ERROR_NO_SUCH_USERNAME);
            return basicResponse;
        }
        User requester = getUserByUsername(usernameToAccept);
        if (requester == null) {
            basicResponse.setErrorCode(ERROR_NO_SUCH_SECRET);
            return basicResponse;
        }
        Friendship friendship = getFriendshipByTwoUsers(requester, accepter);
        if (friendship == null) {
            basicResponse.setErrorCode(ERROR_FRIENDSHIP_DOESNT_EXIST);
            return basicResponse;
        }
        friendship.setStatus(ACTIVE_FRIEND);
        save(friendship);

        basicResponse.setSuccess(true);
        return basicResponse;
    }

    public BasicResponse showRequestersList(String secretFrom) {
        BasicResponse basicResponse;
        if (secretExist(secretFrom)) {
            List<User> requesters = new ArrayList<>();
            Session session = this.sessionFactory.getCurrentSession();

            String hql = "SELECT f.requester FROM Friendship f " +
                    "JOIN f.accepter u " +
                    "WHERE u.secret = :secret " +
                    "AND f.status = :status";

            requesters = session.createQuery(hql)
                    .setParameter("secret", secretFrom)
                    .setParameter("status", WAITING_FOR_ACCEPTER)
                    .list();
            basicResponse = new ListUserResponse(true, 0, requesters);
            return basicResponse;
        } else {
            basicResponse = new BasicResponse(false, ERROR_NO_SUCH_SECRET);
            return basicResponse;
        }
    }

    public BasicResponse friendRequest(String secretFrom, String usernameTo) {
        BasicResponse basicResponse = new BasicResponse(false, 0);
        User user = null;
        if (secretExist(secretFrom)) {
            user = getUserBySecret(secretFrom);
        } else {
            basicResponse.setErrorCode(ERROR_NO_SUCH_SECRET);
            return basicResponse;
        }
        User userFriend = null;
        if (usernameExist(usernameTo)) {
            userFriend = getUserByUsername(usernameTo);
        } else {
            basicResponse.setErrorCode(ERROR_NO_SUCH_USERNAME);
            return basicResponse;
        }

        if (!friendshipExist(user, userFriend)) {
            Friendship friendship = new Friendship(user, userFriend, WAITING_FOR_ACCEPTER);
            try {
                save(friendship);
            } catch (Exception e) {
                e.printStackTrace();
            }
            basicResponse.setSuccess(true);

            return basicResponse;
        } else {
            basicResponse.setErrorCode(ERROR_FRIENDSHIP_EXIST);
            return basicResponse;
        }
    }

    private User getUserBySecret(String secret) {
        User user;
        user = (User) this.sessionFactory.getCurrentSession().createQuery(
                        "From User WHERE secret = :secret ")
                .setParameter("secret", secret)
                .setMaxResults(1)
                .uniqueResult();
        return user;
    }

    private boolean friendshipExist(User user1, User user2) {
        Friendship friendship = null;

        // Check if user1 is the requester and user2 is the accepter
        friendship = getFriendshipByTwoUsers(user1, user2);
        if (friendship != null) {
            return true;
        }

        // Check if user2 is the requester and user1 is the accepter
        friendship = getFriendshipByTwoUsers(user2, user1);
        return friendship != null;
    }

    private Friendship getFriendshipByTwoUsers(User requester, User accepter) {
        Friendship friendship;

        Session session = this.sessionFactory.getCurrentSession();
        friendship = (Friendship) session.createQuery(
                        "FROM Friendship f WHERE f.requester.id = :id1 AND f.accepter.id = :id2")
                .setParameter("id1", requester.getId())
                .setParameter("id2", accepter.getId())
                .setMaxResults(1)
                .uniqueResult();
        return friendship;
    }

    private User getUserByUsername(String username) {
        User user;
        user = (User) this.sessionFactory.getCurrentSession().createQuery(
                        "From User WHERE username = :username ")
                .setParameter("username", username)
                .setMaxResults(1)
                .uniqueResult();
        return user;
    }


    private boolean usernameAvailable(String username) {
        User user;
        user = getUserByUsername(username);
        return (user == null);

    }

    private boolean usernameExist(String username) {
        User user;
        user = getUserByUsername(username);
        return (user != null);
    }

    private boolean secretExist(String secret) {
        return (getUserBySecret(secret) != null);

    }

    private User userExists(String username, String password) {
        User user;
        user = (User) this.sessionFactory.getCurrentSession().createQuery(
                        "From User WHERE username = :username AND password =: password ")
                .setParameter("username", username)
                .setParameter("password", password)
                .setMaxResults(1)
                .uniqueResult();
        return user;
    }

//    private boolean playExist(User user, String playName) {
//        List<Play> plays = (List<Play>) this.sessionFactory.getCurrentSession().createQuery(
//                        "From Play");
//        return plays!=null;
//    }

    public BasicResponse createPlay(String userSecret, String playName) {
        BasicResponse basicResponse = new BasicResponse(false,0);
        User user = getUserBySecret(userSecret);
        if (user==null) {
            basicResponse.setErrorCode(ERROR_NO_SUCH_USER);
            return basicResponse;
        }
//        if (playExist(user,playName)) {
//            basicResponse.setErrorCode(THERE_IS_ALREADY_PLAY_WITH_THIS_NAME);
//            return basicResponse;
//        }
        Play play = new Play(user,playName);
        save(play);
        basicResponse.setSuccess(true);
        return basicResponse;
    }


}