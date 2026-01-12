package com.app.repository;
import org.hibernate.Session;
import org.hibernate.Transaction;
import com.app.entity.User;
import com.app.util.HibernateUtil;
public class UserRepository {
    public void save(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(user);
            tx.commit();
        }
    }

    public User findByUsername(String username) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM User WHERE username = :username", User.class)
                          .setParameter("username", username)
                          .uniqueResult();
        }
    }
}