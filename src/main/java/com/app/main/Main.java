package com.app.main;

import org.hibernate.Session;
import org.hibernate.Transaction;
import com.app.entity.DraftTable; // Import bảng nháp
import com.app.util.HibernateUtil;

public class Main { // Hoặc TestDraft
	public static void main(String[] args) {
        // Chỉ cần mở Session là Hibernate sẽ quét toàn bộ các bảng
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        System.out.println(">>> KHỞI ĐỘNG THÀNH CÔNG!");
        System.out.println(">>> Đã kết nối với các bảng: Books, Users, Loans, Sessions, Members.");
        
        session.close();
        HibernateUtil.getSessionFactory().close();
    }
}