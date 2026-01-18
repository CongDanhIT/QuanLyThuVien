package com.app.view.components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class DashboardPanel extends JPanel {
    private final Color AMBER_GOLD = Color.decode("#FFC845");
    private final Color CRIMSON_RED = Color.decode("#800000"); // Màu đỏ Crimson cho Header

    public DashboardPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.decode("#1A1A14"));
        setBorder(new EmptyBorder(40, 40, 40, 40));

        // --- Header ---
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel title = new JLabel("Tổng quan hệ thống");
        title.setFont(new Font("Serif", Font.BOLD, 32));
        title.setForeground(Color.WHITE);
        header.add(title, BorderLayout.WEST);
        
        JLabel date = new JLabel("THỨ HAI, 20/05/2024 🔔");
        date.setForeground(AMBER_GOLD);
        header.add(date, BorderLayout.EAST);

        // --- Stats Cards Area ---
        JPanel cards = new JPanel(new GridLayout(1, 4, 25, 0));
        cards.setOpaque(false);
        cards.setBorder(new EmptyBorder(30, 0, 40, 0));
        
        cards.add(new StatCard("Tổng số sách", "1,284", "+12 cuốn tháng này"));
        cards.add(new StatCard("Độc giả mới", "85", "+5 độc giả hôm nay"));
        cards.add(new StatCard("Sách đang mượn", "342", "28% trên tổng kho"));
        cards.add(new StatCard("Sách quá hạn", "14", "CẦN XỬ LÝ NGAY"));

        // --- Table Section ---
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setOpaque(false);
        JLabel tableTitle = new JLabel("Danh sách mượn quá hạn");
        tableTitle.setForeground(Color.WHITE);
        tableTitle.setFont(new Font("Serif", Font.BOLD, 22));
        tableTitle.setBorder(new EmptyBorder(0, 0, 15, 0));

        String[] cols = {"Mã phiếu", "Độc giả", "Tên sách", "Ngày mượn", "Hết hạn", "Trễ hạn"};
        Object[][] data = {
            {"#PH8823", "Nguyễn Văn Khải", "Nhà Giả Kim", "01/05/2024", "15/05/2024", "5 ngày"},
            {"#PH8845", "Lê Thị Mai Anh", "Chiến Tranh và Hòa Bình", "28/04/2024", "12/05/2024", "8 ngày"},
            {"#PH8901", "Trần Minh Quân", "Đắc Nhân Tâm", "05/05/2024", "19/05/2024", "1 ngày"},
            {"#PH8912", "Phạm Hoàng Nam", "Tâm Lý Học Tội Phạm", "02/05/2024", "16/05/2024", "4 ngày"}
        };
        
        JTable table = new JTable(new DefaultTableModel(data, cols));
        styleTable(table); // BỔ SUNG: Gọi hàm định dạng bảng
        
        // BỔ SUNG: Tùy chỉnh JScrollPane để không bị lộ nền trắng
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60)));
        scrollPane.getViewport().setBackground(Color.decode("#1A1A14"));
        
        tablePanel.add(tableTitle, BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        add(header, BorderLayout.NORTH);
        add(cards, BorderLayout.CENTER);
        add(tablePanel, BorderLayout.SOUTH);
    }

    // BỔ SUNG: Hàm định dạng màu sắc bảng theo yêu cầu
    private void styleTable(JTable table) {
        // 1. Định dạng Header: Nền Crimson Red, chữ Gold
        JTableHeader header = table.getTableHeader();
        header.setBackground(CRIMSON_RED);
        header.setForeground(AMBER_GOLD);
        header.setFont(new Font("SansSerif", Font.BOLD, 14));
        header.setPreferredSize(new Dimension(0, 40));
        header.setReorderingAllowed(false);

        // 2. Định dạng Dòng (Zebra rows và màu chữ)
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, 
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (isSelected) {
                    c.setBackground(AMBER_GOLD); // Màu vàng khi chọn
                    c.setForeground(Color.BLACK);
                } else {
                    // Hiệu ứng Zebra: Dòng lẻ tối (#1A1A14), dòng chẵn xám nhẹ (#2A2A2A)
                    c.setBackground(row % 2 == 0 ? Color.decode("#1A1A14") : Color.decode("#2A2A2A"));
                    c.setForeground(Color.WHITE);
                }

                // Cột "Trễ hạn" (cột index 5) hiển thị màu đỏ rực nếu có giá trị
                if (column == 5 && value != null) {
                    if (!isSelected) c.setForeground(Color.decode("#FF4D4D"));
                    setFont(getFont().deriveFont(Font.BOLD));
                }

                ((JLabel)c).setBorder(new EmptyBorder(0, 10, 0, 10)); // Padding cho chữ
                return c;
            }
        });

        table.setRowHeight(40);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
}

// Giữ nguyên class StatCard của bạn vì nó đã rất tốt rồi
class StatCard extends JPanel {
    private final Color BG_CARD = Color.decode("#2D2A1E");
    private final Color BORDER_GOLD = Color.decode("#D4AF37");
    private final Color STAT_YELLOW = Color.decode("#FFD700");
    private final Color TITLE_WHITE = Color.decode("#F5F5F5");

    public StatCard(String title, String val, String sub) {
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel t = new JLabel(title); t.setForeground(TITLE_WHITE);
        t.setFont(new Font("SansSerif", Font.PLAIN, 14));
        
        JLabel v = new JLabel(val); v.setForeground(STAT_YELLOW);
        v.setFont(new Font("SansSerif", Font.BOLD, 32));
        
        JLabel s = new JLabel(sub);
        if (sub.contains("CẦN XỬ LÝ")) s.setForeground(Color.decode("#FF4D4D"));
        else s.setForeground(BORDER_GOLD);
        s.setFont(new Font("SansSerif", Font.PLAIN, 12));

        add(t);
        add(Box.createVerticalStrut(10));
        add(v);
        add(Box.createVerticalStrut(5));
        add(s);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(BG_CARD);
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
        g2.setColor(BORDER_GOLD);
        g2.setStroke(new BasicStroke(1));
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
        g2.dispose();
    }
}