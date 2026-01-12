package com.app.util;



import com.app.entity.User;

public class UserSession {
    // Biến static để giữ phiên đăng nhập duy nhất
    private static User currentUser;

    // Lưu thông tin người dùng khi đăng nhập thành công
    public static void login(User user) {
        currentUser = user;
    }

    // Lấy thông tin người dùng hiện tại
    public static User getCurrentUser() {
        return currentUser;
    }

    // Đăng xuất (Xóa phiên)
    public static void logout() {
        currentUser = null;
    }

    // Kiểm tra xem đã có ai đăng nhập chưa
    public static boolean isLoggedIn() {
        return currentUser != null;
    }
}