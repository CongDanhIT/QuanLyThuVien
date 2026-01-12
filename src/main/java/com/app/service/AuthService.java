package com.app.service;

import com.app.repository.UserRepository;
import com.app.util.PasswordUtil;
import com.app.entity.User;
public class AuthService {
    private UserRepository userRepo = new UserRepository();

    public String register(String fullName, String username, String password) {
        // ... kiểm tra tồn tại ...
        
        User user = new User();
        user.setFullName(fullName);
        user.setUsername(username);
        
        // MÃ HÓA TRƯỚC KHI LƯU
        String securePassword = PasswordUtil.hashPassword(password);
        user.setPassword(securePassword); 
        
        userRepo.save(user);
        return "SUCCESS";
    }
    public boolean authenticate(String username, String password) {
        User user = userRepo.findByUsername(username);
        if (user != null) {
            // So sánh mật khẩu thô với bản băm trong DB
            return PasswordUtil.checkPassword(password, user.getPassword());
        }
        return false;
    }
}