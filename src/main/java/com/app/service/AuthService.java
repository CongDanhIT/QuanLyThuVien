package com.app.service;

import com.app.repository.SessionRepository;
import com.app.repository.UserRepository;
import com.app.util.PasswordUtil;
import com.app.util.UserSession;

import java.time.LocalDateTime;
import java.util.UUID;

import com.app.entity.Session;
import com.app.entity.User;
public class AuthService {
    private UserRepository userRepo = new UserRepository();

    public String register(String fullName, String username, String password) {
        // ... logic kiểm tra tồn tại ...
        User user = new User();
        user.setFullName(fullName);
        user.setUsername(username);
        user.setRole("Thủ thư"); // <-- BỔ SUNG DÒNG NÀY
        user.setIsActive(true);
        
        String securePassword = PasswordUtil.hashPassword(password);
        user.setPassword(securePassword); 
        
        userRepo.save(user);
        return "SUCCESS";
    }
    public String authenticate(String username, String password) {
        try {
            User user = userRepo.findByUsername(username);
            
            if (user == null) {
                return "Tên đăng nhập không tồn tại!";
            }

            // Kiểm tra thêm trạng thái hoạt động (từ pgAdmin)
            if (!user.getIsActive()) {
                return "Tài khoản hiện đang bị khóa!";
            }

            // Sử dụng hàm PasswordUtil bạn đã tạo
            if (PasswordUtil.checkPassword(password, user.getPassword())) {
            	// --- THỰC HIỆN LƯU SESSION NGAY TẠI ĐÂY ---
                UserSession.login(user);
             // 2. Ghi nhật ký vào Database (để quan sát/truy vết)
                Session dbSession = new Session();
                dbSession.setUserId(user.getId());
                // Tạo một mã token ngẫu nhiên để quan sát cho chuyên nghiệp
                dbSession.setAccessToken(UUID.randomUUID().toString());
                dbSession.setInitTime(LocalDateTime.now());
                dbSession.setIsActive(true);
                
                
				SessionRepository sessionRepo = new SessionRepository();
				sessionRepo.save(dbSession); // Thực hiện lưu xuống PostgreSQL
                return "SUCCESS";
            } else {
                return "Mật khẩu không chính xác!";
            }
        } catch (Exception e) {
        	e.printStackTrace(); // Dòng này sẽ in chi tiết lỗi ra Console để bạn đọc
            return "Lỗi hệ thống: " + e.getMessage(); // Hiển thị lỗi thật lên UI để debug
        }
    }
}