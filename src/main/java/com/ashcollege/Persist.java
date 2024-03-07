package com.ashcollege;


import com.ashcollege.entities.Client;
import com.ashcollege.entities.Friendship;
import com.ashcollege.entities.User;
import com.ashcollege.responses.BasicResponse;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.criteria.From;
import java.util.ArrayList;
import java.util.List;

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

    public BasicResponse insertUser(String username, String password) {
        BasicResponse basicResponse = new BasicResponse(false,ERROR_USERNAME_TAKEN);
        if (usernameAvailable(username)) {
            User user = new User(username,password);
            save(user);
            basicResponse.setSuccess(true);
            basicResponse.setErrorCode(0);
        }
        return basicResponse;
    }

    public List<User> searchUser(String partOfUsername) {
        List<User> users = new ArrayList<>();
        users = (List<User>) this.sessionFactory.getCurrentSession().createQuery(
                        "FROM User WHERE username LIKE :username")
                .setParameter("username", "%" + partOfUsername + "%")
                .list();
        System.out.println(users.size());
        return users;
    }

//    public List<User> showRequesterList(String secretFrom) {
//        List<User> requesters = new ArrayList<>();
//        requesters = (List<User>) this.sessionFactory.getCurrentSession().createQuery(
//                        "FROM Friendship WHERE accepter_id = :id")
//                .setParameter("id", User.secretFrom.id)
//                .list();
//    }


    public BasicResponse friendRequest(String secretFrom,String usernameTo) {
        BasicResponse basicResponse =new BasicResponse(false,0);
        User user=null;
        if (secretExist(secretFrom)) {
            user = (User) this.sessionFactory.getCurrentSession().createQuery(
                            "From User WHERE secret = :secret ")
                    .setParameter("secret", secretFrom)
                    .setMaxResults(1)
                    .uniqueResult();
        }
        else {
            basicResponse.setErrorCode(ERROR_NO_SUCH_SECRET);
            return basicResponse;
        }
        User userFriend=null;
        if (usernameExist(usernameTo)) {
            userFriend = (User) this.sessionFactory.getCurrentSession().createQuery(
                            "From User WHERE username = :username ")
                    .setParameter("username", usernameTo)
                    .setMaxResults(1)
                    .uniqueResult();
        }
        else {
            basicResponse.setErrorCode(ERROR_NO_SUCH_USERNAME);
            return basicResponse;
        }

        Friendship friendship = new Friendship(user,userFriend, WAITING_FOR_ACCEPTER);
        try {
            save(friendship);
        } catch (Exception e) {
            e.printStackTrace();
        }
        basicResponse.setSuccess(true);

        return basicResponse;
    }

    private boolean usernameAvailable(String username) {
        User user;
        user = (User) this.sessionFactory.getCurrentSession().createQuery(
                "From User WHERE username = :username ")
                .setParameter("username",username)
                .setMaxResults(1)
                .uniqueResult();
        return (user==null);

    }

    private boolean usernameExist(String username) {
        return !usernameAvailable(username);
    }

    private boolean secretExist(String secret) {
        User user;
        user = (User) this.sessionFactory.getCurrentSession().createQuery(
                        "From User WHERE secret = :secret ")
                .setParameter("secret",secret)
                .setMaxResults(1)
                .uniqueResult();
        return (user!=null);

    }
//
//    public <T> List<T> loadList(Class<T> clazz) {
//        return  this.sessionFactory.getCurrentSession().createQuery("FROM Client").list();
//    }

//    public Client getClientByFirstName (String firstName) {
//        return (Client) this.sessionFactory.getCurrentSession().createQuery(
//                "FROM Client WHERE firstName = :firstName ")
//                .setParameter("firstName", firstName)
//                .setMaxResults(1)
//                .uniqueResult();
//    }


}