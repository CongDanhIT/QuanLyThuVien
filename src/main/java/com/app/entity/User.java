package com.app.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users") 
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user") // Khớp với cột id_user trong pgAdmin
    private Long id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "role")
    private String role; // Ví dụ: "ADMIN", "LIBRARIAN"

    @Column(name = "is_active",nullable = false)
    private Boolean isActive = true; // Dùng Boolean để nhận giá trị true/false

    public User() {}

    public User(String username, String password, String fullName, String role, Boolean isActive) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.role = role;
        this.isActive = isActive;
    }

    // Getter & Setter (Bạn tự generate nhé: Alt + Shift + S -> R)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

	
}