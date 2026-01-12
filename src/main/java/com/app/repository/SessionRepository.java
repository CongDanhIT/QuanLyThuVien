package com.app.repository;

import com.app.entity.Session;
import com.app.util.HibernateUtil;
import org.hibernate.Transaction;

public class SessionRepository {
    public void save(Session session) {
        Transaction transaction = null;
        try (org.hibernate.Session hibernateSession = HibernateUtil.getSessionFactory().openSession()) {
            transaction = hibernateSession.beginTransaction();
            hibernateSession.persist(session); // Lưu session mới vào DB
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }
}