package com.app.view.components;

import com.app.entity.User;
import com.app.util.UserSession;
import com.app.view.LoginFrame;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class AccountProfileDialog extends JDialog {
    private final Color BG_COLOR = Color.decode("#1A1A14");
    private final Color CARD_BG = Color.decode("#2D2A1E");
    private final Color AMBER_GOLD = Color.decode("#FFC845");
    private final Color CRIMSON_RED = Color.decode("#800000");
    private final Color TEXT_GRAY = new Color(160, 160, 160);

    public AccountProfileDialog(JFrame parent) {
        super(parent, true); // Thiết lập modal
        setUndecorated(true); // Bỏ thanh tiêu đề Windows
        setSize(500, 600);
        setLocationRelativeTo(parent);
        
        // Bo góc cho cửa sổ
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30));

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(BG_COLOR);
        contentPanel.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60), 1));

        User user = UserSession.getCurrentUser();
        String userRole = (user.getRole() != null && !user.getRole().isEmpty()) 
                ? user.getRole() 
                : "Cán bộ hệ thống";
        // --- 1. Top Section: Avatar & Name ---
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setOpaque(false);
        topPanel.setBorder(new EmptyBorder(40, 0, 30, 0));

        JLabel lblAvatar = new JLabel("👤");
        lblAvatar.setFont(new Font("SansSerif", Font.PLAIN, 100));
        lblAvatar.setForeground(AMBER_GOLD);
        lblAvatar.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblFullName = new JLabel(user != null ? user.getFullName() : "Cán bộ thư viện");
        lblFullName.setFont(new Font("Serif", Font.BOLD, 26));
        lblFullName.setForeground(Color.WHITE);
        lblFullName.setAlignmentX(Component.CENTER_ALIGNMENT);

        topPanel.add(lblAvatar);
        topPanel.add(Box.createVerticalStrut(10));
        topPanel.add(lblFullName);

        // --- 2. Center Section: Info Fields ---
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(3, 1, 0, 20));
        infoPanel.setOpaque(false);
        infoPanel.setBorder(new EmptyBorder(0, 50, 20, 50));

        infoPanel.add(createInfoField("MÃ NHÂN VIÊN", "NV-" + String.format("%05d", user.getId())));
        infoPanel.add(createInfoField("TÊN ĐĂNG NHẬP", user.getUsername()));
        infoPanel.add(createInfoField("VAI TRÒ HỆ THỐNG", user.getRole() != null ? user.getRole() : "Cán bộ hệ thống"));

        // --- 3. Bottom Section: Actions ---
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        actionPanel.setOpaque(false);
        actionPanel.setBorder(new EmptyBorder(0, 0, 30, 0));

        JButton btnClose = new JButton("ĐÓNG");
        styleButton(btnClose, Color.decode("#444444"), Color.WHITE);
        btnClose.addActionListener(e -> dispose());

        JButton btnLogout = new JButton("ĐĂNG XUẤT");
        styleButton(btnLogout, CRIMSON_RED, Color.WHITE);
        btnLogout.addActionListener(e -> handleLogout(parent));

        actionPanel.add(btnClose);
        actionPanel.add(btnLogout);

        contentPanel.add(topPanel, BorderLayout.NORTH);
        contentPanel.add(infoPanel, BorderLayout.CENTER);
        contentPanel.add(actionPanel, BorderLayout.SOUTH);

        add(contentPanel);
    }

    private JPanel createInfoField(String label, String value) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        
        JLabel lbl = new JLabel(label);
        lbl.setForeground(TEXT_GRAY);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 11));
        
        JLabel val = new JLabel(value);
        val.setForeground(Color.WHITE);
        val.setFont(new Font("SansSerif", Font.PLAIN, 16));
        val.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(60, 60, 60)));

        p.add(lbl, BorderLayout.NORTH);
        p.add(val, BorderLayout.CENTER);
        return p;
    }

    private void styleButton(JButton btn, Color bg, Color fg) {
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setPreferredSize(new Dimension(140, 45));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder());
    }

    private void handleLogout(JFrame parent) {
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn đăng xuất?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            UserSession.logout();
            this.dispose();
            parent.dispose(); // Đóng cửa sổ chính
            new LoginFrame().setVisible(true); // Quay lại màn hình đăng nhập
        }
    }
}