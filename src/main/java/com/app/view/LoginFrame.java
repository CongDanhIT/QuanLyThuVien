package com.app.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.app.service.AuthService;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginFrame extends JFrame {
    private final Color BRANDING_BG = Color.decode("#1a1708"); // Màu nền yêu cầu
    private final Color FORM_BG = Color.decode("#2d2a1e");     // Màu nền Form
    private final Color AMBER_GOLD = Color.decode("#ffc845");  // Màu vàng hổ phách
    private final Color DARK_TEXT = Color.decode("#1a1708");   // Chữ trên nút

    private CardLayout cardLayout = new CardLayout();
    private JPanel formContainer;
    private JLabel lblTabLogin, lblTabRegister;

    public LoginFrame() {
        setTitle("SLMS - PREMIUM ACCESS LOGIN");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(BRANDING_BG);
        setLayout(new GridBagLayout());

        // --- TẤM THẺ TRUNG TÂM VỚI BÓNG LAN ĐỀU (AMBIENT SHADOW) ---
        JPanel mainCard = new JPanel(new GridLayout(1, 2)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth(), h = getHeight();
                int baseArc = 50; // Độ bo góc cơ bản
                int shadowSize = 8; // Bóng mỏng, tinh tế hơn
                int shadowOffset = 6; // Lệch nhẹ xuống dưới

                // 1. VẼ BÓNG ĐỔ MỊN (SOFT SHADOW LAYERS)
                Color shadowColor = new Color(0, 0, 0, 12); // Rất mờ

                for (int i = 0; i < shadowSize; i++) {
                    g2.setColor(shadowColor);
                    g2.fillRoundRect(
                        shadowSize - i,
                        shadowSize - i + (i * shadowOffset / shadowSize),
                        w - (shadowSize - i) * 2,
                        h - (shadowSize - i) * 2,
                        baseArc + i * 2,
                        baseArc + i * 2
                    );
                }

                // Vùng vẽ thẻ thực tế (thụt vào trong bóng)
                int cardX = shadowSize;
                int cardY = shadowSize;
                int cardW = w - shadowSize * 2;
                int cardH = h - shadowSize * 2;

                // 2. Vẽ nền bên TRÁI (#1a1708)
                g2.setColor(BRANDING_BG);
                g2.fillRoundRect(cardX, cardY, cardW, cardH, baseArc, baseArc);

                // 3. Vẽ nền bên PHẢI (#2d2a1e) - Dùng clip để cắt
                Shape oldClip = g2.getClip();
                g2.setClip(new Rectangle(w / 2, 0, w / 2, h));
                g2.setColor(FORM_BG);
                g2.fillRoundRect(cardX, cardY, cardW, cardH, baseArc, baseArc);
                g2.setClip(oldClip);

                g2.dispose();
            }
        };
        mainCard.setOpaque(false);
        // Tăng chiều cao một chút để chứa đủ các trường mới
        mainCard.setPreferredSize(new Dimension(1150, 720));
        mainCard.setBorder(new EmptyBorder(12, 12, 12, 12)); // Padding cho bóng

        mainCard.add(createBrandingSide());
        mainCard.add(createFormSide());

        add(mainCard);
    }

    private JPanel createBrandingSide() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.insets = new Insets(0, 0, 15, 0);

        JLabel logo = new JLabel("📖");
        logo.setFont(new Font("Serif", Font.PLAIN, 120));
        logo.setForeground(AMBER_GOLD);

        JLabel slogan = new JLabel("<html><center>Kiến thức là<br>sức mạnh vô hạn</center></html>");
        slogan.setFont(new Font("Serif", Font.ITALIC, 32));
        slogan.setForeground(Color.WHITE);

        p.add(logo, gbc);
        gbc.gridy = 1; p.add(slogan, gbc);
        return p;
    }

    private JPanel createFormSide() {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        // Điều chỉnh padding để cân đối với nội dung nhiều hơn
        p.setBorder(new EmptyBorder(60, 70, 60, 80));

        // Tab Selector
        JPanel tabs = new JPanel(new FlowLayout(FlowLayout.LEFT, 45, 0));
        tabs.setOpaque(false);
        lblTabLogin = createTabLabel("Đăng nhập", true);
        lblTabRegister = createTabLabel("Đăng ký", false);

        lblTabLogin.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { switchForm("LOGIN"); }
        });
        lblTabRegister.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { switchForm("REGISTER"); }
        });

        tabs.add(lblTabLogin); tabs.add(lblTabRegister);

        // Form Container
        formContainer = new JPanel(cardLayout);
        formContainer.setOpaque(false);
        formContainer.add(createLoginForm(), "LOGIN");
        formContainer.add(createRegisterForm(), "REGISTER");

        p.add(tabs, BorderLayout.NORTH);
        p.add(formContainer, BorderLayout.CENTER);
        return p;
    }

    private void switchForm(String key) {
        cardLayout.show(formContainer, key);
        boolean isLogin = key.equals("LOGIN");
        lblTabLogin.setForeground(isLogin ? AMBER_GOLD : Color.GRAY);
        lblTabLogin.setBorder(isLogin ? BorderFactory.createMatteBorder(0, 0, 3, 0, AMBER_GOLD) : null);
        lblTabRegister.setForeground(!isLogin ? AMBER_GOLD : Color.GRAY);
        lblTabRegister.setBorder(!isLogin ? BorderFactory.createMatteBorder(0, 0, 3, 0, AMBER_GOLD) : null);
    }

    // --- CẬP NHẬT FORM ĐĂNG NHẬP ---
    private JPanel createLoginForm() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);

        p.add(Box.createVerticalStrut(40));
        p.add(createInputLabel("Tên người dùng"));
        p.add(createInputField(new JTextField(), "Nhập tên đăng nhập"));
        p.add(Box.createVerticalStrut(20));
        p.add(createInputLabel("Mật khẩu"));
        p.add(createInputField(new JPasswordField(), "••••••••"));
        p.add(Box.createVerticalStrut(20));

        // --- MỚI: Hàng chứa Checkbox và Link ---
        JPanel optionsPanel = new JPanel(new BorderLayout());
        optionsPanel.setOpaque(false);
        optionsPanel.setMaximumSize(new Dimension(420, 30)); // Cùng chiều rộng với ô nhập liệu
        optionsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JCheckBox chkRemember = createCheckBox("Ghi nhớ đăng nhập");
        JLabel lblForgot = createLinkLabel("Quên mật khẩu?");

        optionsPanel.add(chkRemember, BorderLayout.WEST);
        optionsPanel.add(lblForgot, BorderLayout.EAST);
        p.add(optionsPanel);
        // ---------------------------------------

        p.add(Box.createVerticalStrut(40));

        JButton btn = new JButton("Tiếp tục truy cập  →");
        btn.setBackground(AMBER_GOLD);
        btn.setForeground(DARK_TEXT);
        btn.setFont(new Font("SansSerif", Font.BOLD, 16));
        btn.setMaximumSize(new Dimension(420, 60));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(btn);

        return p;
    }

    // --- CẬP NHẬT FORM ĐĂNG KÝ ---
   private JPanel createRegisterForm() {
    JPanel p = new JPanel();
    p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
    p.setOpaque(false);
    p.add(Box.createVerticalStrut(20)); // Thu hẹp khoảng cách đầu để đủ chỗ
     AuthService authService = new AuthService();
    
    // 1. Họ và tên (MỚI BỔ SUNG)
    p.add(createInputLabel("Họ và tên đầy đủ"));
    JTextField txtFullName = new JTextField();
    p.add(createInputField(txtFullName, "Ví dụ: Nguyễn Văn A"));
    p.add(Box.createVerticalStrut(15));

    // 2. Tên đăng nhập
    p.add(createInputLabel("Tên đăng nhập"));
    JTextField txtUsername = new JTextField();
    p.add(createInputField(txtUsername, "Ví dụ: abc@1145"));
    p.add(Box.createVerticalStrut(15));

    // 3. Mật khẩu quản trị
    p.add(createInputLabel("Mật khẩu quản trị"));
    JPasswordField txtPassword = new JPasswordField();
    p.add(createInputField(txtPassword, "••••••••"));
    p.add(Box.createVerticalStrut(15));

    // 4. Xác nhận mật khẩu
    p.add(createInputLabel("Xác nhận mật khẩu"));
    JPasswordField txtConfirm = new JPasswordField();
    p.add(createInputField(txtConfirm, "••••••••"));
    p.add(Box.createVerticalStrut(20));

    // 5. Điều khoản
    JCheckBox chkTerms = createCheckBox("Tôi đồng ý với Điều khoản sử dụng");
    chkTerms.setAlignmentX(Component.LEFT_ALIGNMENT);
    p.add(chkTerms);

    p.add(Box.createVerticalStrut(25));
    
    JButton btn = new JButton("Tạo tài khoản quản trị");
    btn.setBackground(AMBER_GOLD);
    btn.setForeground(DARK_TEXT);
    btn.setMaximumSize(new Dimension(420, 60));
    btn.setAlignmentX(Component.LEFT_ALIGNMENT);
    btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

    //-------------------------- ĐĂNG KÝ ACTION (CẬP NHẬT THAM SỐ) --------------------------
    btn.addActionListener(e -> {
        String fullName = txtFullName.getText().trim(); // Lấy họ tên
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        String confirm = new String(txtConfirm.getPassword());

        // Kiểm tra dữ liệu đầu vào (Validation)
        if (fullName.isEmpty() || username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ các trường!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!password.equals(confirm)) {
            JOptionPane.showMessageDialog(this, "Mật khẩu xác nhận không khớp!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!chkTerms.isSelected()) {
            JOptionPane.showMessageDialog(this, "Bạn cần đồng ý với điều khoản để tiếp tục!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // GỌI SERVICE VỚI 3 THAM SỐ
        String result = authService.register(fullName, username, password);

        if ("SUCCESS".equals(result)) {
            JOptionPane.showMessageDialog(this, "Đăng ký thành công cho: " + fullName, "Thành công", JOptionPane.INFORMATION_MESSAGE);
            switchForm("LOGIN"); 
        } else {
            JOptionPane.showMessageDialog(this, result, "Lỗi đăng ký", JOptionPane.ERROR_MESSAGE);
        }
    });

    p.add(btn);
    return p;
}

    // --- HELPER METHODS (Cũ & Mới) ---
    
    // Helper tạo Checkbox theo style tối
    private JCheckBox createCheckBox(String text) {
        JCheckBox cb = new JCheckBox(text);
        cb.setOpaque(false);
        cb.setForeground(Color.LIGHT_GRAY);
        cb.setFont(new Font("SansSerif", Font.PLAIN, 14));
        cb.setFocusPainted(false);
        cb.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return cb;
    }

    // Helper tạo Link màu vàng
    private JLabel createLinkLabel(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(AMBER_GOLD);
        l.setFont(new Font("SansSerif", Font.BOLD, 14));
        l.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return l;
    }

    private JLabel createTabLabel(String text, boolean active) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", Font.BOLD, 22));
        l.setForeground(active ? AMBER_GOLD : Color.GRAY);
        if (active) l.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, AMBER_GOLD));
        l.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return l;
    }

    private JLabel createInputLabel(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(Color.LIGHT_GRAY);
        l.setFont(new Font("SansSerif", Font.PLAIN, 14));
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private JComponent createInputField(JTextField f, String hint) {
        f.putClientProperty("JTextField.placeholderText", hint);
        f.setBackground(BRANDING_BG);
        f.setForeground(Color.WHITE);
        f.setMaximumSize(new Dimension(420, 55));
        f.setPreferredSize(new Dimension(420, 55));
        f.setAlignmentX(Component.LEFT_ALIGNMENT);
        f.setBorder(BorderFactory.createCompoundBorder(f.getBorder(), new EmptyBorder(0, 15, 0, 15)));
        return f;
    }
}