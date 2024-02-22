package com.ashcollege;


import com.ashcollege.entities.Client;
import com.ashcollege.entities.User;
import com.ashcollege.responses.BasicResponse;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.ashcollege.utils.Errors.ERROR_USERNAME_TAKEN;


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

    private boolean usernameAvailable(String username) {
        User user;
        user = (User) this.sessionFactory.getCurrentSession().createQuery(
                "From User WHERE username = :username ")
                .setParameter("username",username)
                .setMaxResults(1)
                .uniqueResult();
        return (user==null);

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