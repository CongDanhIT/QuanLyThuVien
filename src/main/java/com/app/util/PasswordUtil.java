package com.app.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    /**
     * Mã hóa mật khẩu thành chuỗi băm (Hash)
     * Dùng khi ĐĂNG KÝ
     */
    public static String hashPassword(String plainPassword) {
        // gensalt() tạo ra một chuỗi muối ngẫu nhiên để tăng độ bảo mật
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));
    }

    /**
     * Kiểm tra mật khẩu nhập vào có khớp với bản băm trong DB không
     * Dùng khi ĐĂNG NHẬP
     */
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        try {
            return BCrypt.checkpw(plainPassword, hashedPassword);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}