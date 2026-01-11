package com.app.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sessions")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_session")
    private Long id;

    @Column(name = "id_user")
    private Long userId;

    @Column(name = "access_token")
    private String accessToken;

    @Column(name = "init_time")
    private LocalDateTime initTime;

    @Column(name = "is_active")
    private Boolean isActive;

    public Session() {}
    
    // Getter & Setter...
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getAccessToken() { return accessToken; }
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }
    public LocalDateTime getInitTime() { return initTime; }
    public void setInitTime(LocalDateTime initTime) { this.initTime = initTime; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
}