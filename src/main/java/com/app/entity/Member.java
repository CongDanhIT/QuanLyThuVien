package com.app.entity;

import jakarta.persistence.*;
import java.time.LocalDate; // Dùng LocalDate cho chuẩn mới

@Entity
@Table(name = "members")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_member") // Quan trọng: Khớp với cột id_member trong pgAdmin
    private Long id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;
    
    // Trong DB cột tên là "date", nhưng trong Java mình đặt "joinedDate" cho rõ nghĩa
    @Column(name = "date") 
    private LocalDate joinedDate;

    @Column(name = "status")
    private String status; // Ví dụ: "ACTIVE", "BANNED"

    // 1. Constructor rỗng (Bắt buộc)
    public Member() {}

    // 2. Constructor đầy đủ
    public Member(String fullName, String email, String phone, LocalDate joinedDate, String status) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.joinedDate = joinedDate;
        this.status = status;
    }

    // 3. Getter & Setter (Bấm Alt + Shift + S -> R để tạo nhanh)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public LocalDate getJoinedDate() { return joinedDate; }
    public void setJoinedDate(LocalDate joinedDate) { this.joinedDate = joinedDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}