package com.app.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import com.app.entity.Book;

public class HibernateUtil {
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            // Nạp cấu hình và thêm Class Book vào quản lý
            return new Configuration()
                    .configure()
                    .addAnnotatedClass(Book.class)
                    .buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Lỗi khởi tạo Hibernate: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}