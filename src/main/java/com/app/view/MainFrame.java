package com.app.view;

import com.app.util.UserSession;
import com.app.view.components.BookInventoryPanel;
import com.app.view.components.DashboardPanel;
import com.app.view.components.LoanManagementPanel;
import com.app.view.components.MemberManagementPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame {
    private final Color BG_COLOR = Color.decode("#1A1A14");
    private final Color SIDEBAR_BG = Color.decode("#242424");
    private final Color CARD_BG = Color.decode("#2D2A1E");
    private final Color AMBER_GOLD = Color.decode("#FFC845");
    private final Color CRIMSON_RED = Color.decode("#990000");
    private final Color TEXT_GRAY = new Color(160, 160, 160);

    private JPanel cardPanel;
    private CardLayout cardLayout;
    private List<JButton> navButtons = new ArrayList<>();

    // --- LƯU THAM CHIẾU CÁC PANEL ĐỂ GỌI REFRESH ---
    private DashboardPanel dashboardPanel;
    private BookInventoryPanel bookPanel;
    private MemberManagementPanel memberPanel;
    private LoanManagementPanel loanPanel;

    public MainFrame() {
        setTitle("SLMS - Hệ Thống Quản Lý Thư Viện Cao Cấp");
        setSize(1300, 850);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Khởi tạo các panel một lần duy nhất
        dashboardPanel = new DashboardPanel();
        bookPanel = new BookInventoryPanel();
        memberPanel = new MemberManagementPanel();
        loanPanel = new LoanManagementPanel();

        add(createSidebar(), BorderLayout.WEST);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(BG_COLOR);

        // Thêm các tab vào CardLayout bằng tham chiếu
        cardPanel.add(dashboardPanel, "DASHBOARD"); 
        cardPanel.add(bookPanel, "BOOKS");
        cardPanel.add(memberPanel, "MEMBERS");
        cardPanel.add(loanPanel, "LOANS");

        add(cardPanel, BorderLayout.CENTER);
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(260, 0));
        sidebar.setBackground(SIDEBAR_BG);
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(50, 50, 50)));

        // Logo
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 25, 30));
        topPanel.setOpaque(false);
        JLabel lblLogo = new JLabel("📖 SLMS");
        lblLogo.setFont(new Font("Serif", Font.BOLD, 26));
        lblLogo.setForeground(AMBER_GOLD);
        topPanel.add(lblLogo);

        // Menu Panel
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setOpaque(false);
        menuPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        // Khởi tạo các nút điều hướng
        navButtons.add(createNavButton("📊  Dashboard", "DASHBOARD"));
        navButtons.add(createNavButton("📚  Quản lý sách", "BOOKS"));
        navButtons.add(createNavButton("👥  Quản lý độc giả", "MEMBERS"));
        navButtons.add(createNavButton("⇄  Mượn / Trả", "LOANS"));

        for (JButton btn : navButtons) menuPanel.add(btn);
        
        setButtonActive(navButtons.get(0));

        sidebar.add(topPanel, BorderLayout.NORTH);
        sidebar.add(menuPanel, BorderLayout.CENTER);
        sidebar.add(createProfileSection(), BorderLayout.SOUTH);

        return sidebar;
    }

    private JButton createNavButton(String text, String cardName) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(260, 50));
        btn.setPreferredSize(new Dimension(260, 50));
        btn.setFont(new Font("SansSerif", Font.PLAIN, 15));
        btn.setForeground(Color.WHITE);
        btn.setBackground(SIDEBAR_BG);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(0, 25, 0, 0));

        // Sự kiện Click: Chuyển tab và TỰ ĐỘNG LÀM MỚI DỮ LIỆU
        btn.addActionListener(e -> {
            cardLayout.show(cardPanel, cardName); 
            
            // Gọi hàm refresh tương ứng của từng Panel
            switch (cardName) {
                case "DASHBOARD": dashboardPanel.refreshData(); break;
                case "BOOKS": bookPanel.refreshTable(); break;
                case "MEMBERS": memberPanel.refreshTable(null); break;
                case "LOANS": loanPanel.refreshTable(); break;
            }

            for (JButton b : navButtons) setButtonInactive(b); 
            setButtonActive(btn); 
        });

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { 
                if (btn.getForeground() != AMBER_GOLD) btn.setForeground(AMBER_GOLD); 
            }
            public void mouseExited(MouseEvent e) { 
                if (btn.getBorder().getBorderInsets(btn).left < 5) btn.setForeground(Color.WHITE);
            }
        });

        return btn;
    }

    private void setButtonActive(JButton btn) {
        btn.setForeground(AMBER_GOLD);
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 5, 0, 0, CRIMSON_RED),
            new EmptyBorder(0, 20, 0, 0)
        ));
    }

    private void setButtonInactive(JButton btn) {
        btn.setForeground(Color.WHITE);
        btn.setBorder(new EmptyBorder(0, 25, 0, 0));
    }

    private JPanel createProfileSection() {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(20, 20, 30, 20));

        JPanel card = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(220, 80));
        
        String adminName = UserSession.isLoggedIn() ? UserSession.getCurrentUser().getFullName() : "Admin SLMS";
        
        JLabel lblIcon = new JLabel("👤");
        lblIcon.setFont(new Font("SansSerif", Font.PLAIN, 24));
        lblIcon.setForeground(AMBER_GOLD);

        JLabel lblName = new JLabel(adminName);
        lblName.setForeground(Color.WHITE);
        lblName.setFont(new Font("SansSerif", Font.BOLD, 14));

        JLabel lblRole = new JLabel("Hệ thống thư viện");
        lblRole.setForeground(TEXT_GRAY);
        lblRole.setFont(new Font("SansSerif", Font.PLAIN, 11));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 10, 0, 10);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridheight = 2;
        card.add(lblIcon, gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.gridheight = 1; gbc.anchor = GridBagConstraints.WEST;
        card.add(lblName, gbc);
        gbc.gridy = 1;
        card.add(lblRole, gbc);

        p.add(card);
        return p;
    }
}