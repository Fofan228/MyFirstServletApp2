package org.example.Account;

import org.hibernate.Session;
import org.hibernate.Transaction;
import javax.servlet.http.Cookie;

public class UserService {
    public static final UserService USER_SERVICE = new UserService();
    public UserProfile getUser(String login) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        UserProfile user = session.byNaturalId(UserProfile.class).using("login", login).load();
        session.close();
        return user;
    }
    public void addUser(UserProfile user) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.persist(user);
        transaction.commit();
        session.close();
    }

    public UserProfile getUserByCookies(Cookie[] cookies) {
        UserProfile user;
        if ((user = getUser(UserCookies.getValue(cookies, "login"))) == null || !user.getPassword().equals(UserCookies.getValue(cookies, "password"))) {
            return null;
        }
        return user;
    }
}
