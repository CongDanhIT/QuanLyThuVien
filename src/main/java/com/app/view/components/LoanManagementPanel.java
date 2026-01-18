package com.app.view.components;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;

public class LoanManagementPanel extends JPanel {
    private final Color BG_COLOR = Color.decode("#1A1A14");
    private final Color CARD_BG = Color.decode("#2D2A1E");
    private final Color AMBER_GOLD = Color.decode("#FFC845");
    private final Color CRIMSON_RED = Color.decode("#800000");
    private final Color TEXT_GRAY = new Color(160, 160, 160);
    private final Color STATUS_GREEN = Color.decode("#50C878");
    private final Color STATUS_RED = Color.decode("#FF4D4D");

    public LoanManagementPanel() {
        setLayout(new BorderLayout());
        setBackground(BG_COLOR);
        setBorder(new EmptyBorder(30, 30, 30, 30));

        // --- 1. Header Section ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JPanel titleGroup = new JPanel(new GridLayout(2, 1));
        titleGroup.setOpaque(false);
        JLabel title = new JLabel("Mượn & Trả Sách");
        title.setFont(new Font("Serif", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        JLabel subTitle = new JLabel("Giao dịch mượn và thu hồi sách hệ thống.");
        subTitle.setForeground(TEXT_GRAY);
        titleGroup.add(title);
        titleGroup.add(subTitle);

        JLabel lblDate = new JLabel("THỨ HAI, 20/05/2024 🔔");
        lblDate.setForeground(AMBER_GOLD);
        lblDate.setFont(new Font("SansSerif", Font.BOLD, 12));

        headerPanel.add(titleGroup, BorderLayout.WEST);
        headerPanel.add(lblDate, BorderLayout.EAST);

        // --- 2. Quick Action Card (Fix Layout & Spacing) ---
        JPanel quickActionCard = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                g2.dispose();
            }
        };
        quickActionCard.setOpaque(false);
        quickActionCard.setBorder(new EmptyBorder(30, 40, 30, 40));
        // Khống chế chiều cao thẻ để không bị kéo giãn theo trục Y
        quickActionCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10);

        JLabel lblCardTitle = new JLabel("⚡ Nhập nhanh phiếu mượn/trả");
        lblCardTitle.setForeground(AMBER_GOLD);
        lblCardTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 3;
        gbc.insets = new Insets(0, 10, 20, 10);
        quickActionCard.add(lblCardTitle, gbc);

        // Labels
        gbc.gridwidth = 1; gbc.gridy = 1; gbc.insets = new Insets(5, 10, 2, 10);
        quickActionCard.add(createInputLabel("MÃ ĐỘC GIẢ"), gbc);
        gbc.gridx = 1;
        quickActionCard.add(createInputLabel("MÃ SÁCH"), gbc);

        // Inputs
        gbc.gridy = 2; gbc.gridx = 0; gbc.weightx = 1.0;
        gbc.insets = new Insets(0, 10, 0, 10);
        quickActionCard.add(createStyledField("Nhập mã thẻ..."), gbc);

        gbc.gridx = 1;
        quickActionCard.add(createStyledField("Quét mã ISBN/Mã sách..."), gbc);

        gbc.gridx = 2; gbc.weightx = 0;
        JButton btnExecute = new JButton("✔ THỰC HIỆN");
        btnExecute.setBackground(AMBER_GOLD);
        btnExecute.setForeground(Color.BLACK);
        btnExecute.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnExecute.setPreferredSize(new Dimension(160, 45));
        btnExecute.setCursor(new Cursor(Cursor.HAND_CURSOR));
        quickActionCard.add(btnExecute, gbc);

        // --- 3. Table Section ---
        JPanel tableHeader = new JPanel(new BorderLayout());
        tableHeader.setOpaque(false);
        tableHeader.setBorder(new EmptyBorder(40, 0, 15, 0));

        JLabel lblTableTitle = new JLabel("Danh sách mượn hiện hành");
        lblTableTitle.setFont(new Font("Serif", Font.BOLD, 22));
        lblTableTitle.setForeground(Color.WHITE);

        JTextField txtSearch = new JTextField();
        txtSearch.setPreferredSize(new Dimension(300, 38));
        txtSearch.putClientProperty("JTextField.placeholderText", "Tìm kiếm phiếu...");
        txtSearch.putClientProperty("JTextField.leadingIcon", safeLoadIcon("icons/search.svg", 16, 16));
        txtSearch.putClientProperty("JTextField.showClearButton", true);

        tableHeader.add(lblTableTitle, BorderLayout.WEST);
        tableHeader.add(txtSearch, BorderLayout.EAST);

        String[] columns = {"MÃ PHIẾU", "TÊN ĐỘC GIẢ", "TÊN SÁCH", "NGÀY MƯỢN", "HẠN TRẢ", "TÌNH TRẠNG", "THAO TÁC"};
        Object[][] data = {
            {"#PH8823", "Nguyễn Văn Khải", "Nhà Giả Kim", "01/05/2024", "15/05/2024", "QUÁ HẠN (5 NGÀY)", ""},
            {"#PH8845", "Lê Thị Mai Anh", "Chiến Tranh và Hòa Bình", "28/04/2024", "12/05/2024", "QUÁ HẠN (8 NGÀY)", ""},
            {"#PH8922", "Trần Minh Quân", "Đắc Nhân Tâm", "18/05/2024", "01/06/2024", "ĐANG MƯỢN", ""},
            {"#PH8934", "Phạm Hoàng Nam", "Tâm Lý Học Tội Phạm", "19/05/2024", "02/06/2024", "ĐANG MƯỢN", ""},
            {"#PH8950", "Hoàng Kim Ngân", "Suối Nguồn", "20/05/2024", "03/06/2024", "ĐANG MƯỢN", ""}
        };

        JTable table = new JTable(new DefaultTableModel(data, columns) {
            @Override public boolean isCellEditable(int r, int c) { return c == 6; }
        });
        styleLoanTable(table);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(BG_COLOR);

        // --- Assemble ---
        JPanel centerContent = new JPanel();
        centerContent.setLayout(new BoxLayout(centerContent, BoxLayout.Y_AXIS));
        centerContent.setOpaque(false);
        centerContent.add(quickActionCard);
        centerContent.add(tableHeader);
        centerContent.add(scrollPane);

        add(headerPanel, BorderLayout.NORTH);
        add(centerContent, BorderLayout.CENTER);
    }

    private JTextField createStyledField(String hint) {
        JTextField f = new JTextField();
        f.setPreferredSize(new Dimension(300, 45));
        f.setBackground(BG_COLOR);
        f.setForeground(Color.WHITE);
        f.setCaretColor(AMBER_GOLD);
        f.putClientProperty("JTextField.placeholderText", hint);
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(80, 80, 80), 1),
            new EmptyBorder(0, 15, 0, 15)
        ));
        return f;
    }

    private JLabel createInputLabel(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(TEXT_GRAY);
        l.setFont(new Font("SansSerif", Font.BOLD, 11));
        return l;
    }

    private Icon safeLoadIcon(String path, int w, int h) {
        try {
            return new FlatSVGIcon(path, w, h);
        } catch (Exception e) {
            return null; // Trả về null để không hiện ô vuông đỏ lỗi
        }
    }

    private void styleLoanTable(JTable table) {
        table.setRowHeight(65);
        table.setBackground(BG_COLOR);
        table.setForeground(Color.WHITE);
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(50, 50, 50));
        table.setSelectionBackground(new Color(255, 200, 69, 20));

        JTableHeader header = table.getTableHeader();
        header.setBackground(CRIMSON_RED);
        header.setForeground(AMBER_GOLD);
        header.setFont(new Font("SansSerif", Font.BOLD, 13));
        header.setPreferredSize(new Dimension(0, 45));

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int col) {
                JLabel c = (JLabel) super.getTableCellRendererComponent(t, v, s, f, r, col);
                c.setHorizontalAlignment(CENTER);
                c.setBorder(new EmptyBorder(0, 10, 0, 10));

                if (!s) {
                    c.setBackground(r % 2 == 0 ? BG_COLOR : new Color(30, 30, 30));
                    if (col == 0) c.setForeground(AMBER_GOLD);
                    
                    if (col == 5) {
                        String val = v.toString();
                        if (val.contains("QUÁ HẠN")) {
                            c.setForeground(STATUS_RED);
                            c.setText("⚠ " + val);
                        } else {
                            c.setForeground(STATUS_GREEN);
                        }
                    } else if (col != 0) {
                        c.setForeground(Color.WHITE);
                    }
                }
                return c;
            }
        });

        table.getColumnModel().getColumn(6).setCellRenderer(new LoanActionRenderer());
        table.getColumnModel().getColumn(6).setCellEditor(new LoanActionEditor(table));
    }

    // --- Renderer & Editor Fix (Nút không bị giãn to) ---
    class LoanActionRenderer implements TableCellRenderer {
        private final JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 12));
        private final JButton btn = new JButton("↩ TRẢ SÁCH");
        
        public LoanActionRenderer() {
            wrapper.setOpaque(true);
            btn.setBackground(Color.decode("#353535"));
            btn.setForeground(AMBER_GOLD);
            btn.setFont(new Font("SansSerif", Font.BOLD, 12));
            btn.setPreferredSize(new Dimension(140, 35));
            btn.setFocusPainted(false);
            wrapper.add(btn);
        }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            wrapper.setBackground(isSelected ? table.getSelectionBackground() : 
                (row % 2 == 0 ? BG_COLOR : new Color(30, 30, 30)));
            return wrapper;
        }
    }

    class LoanActionEditor extends AbstractCellEditor implements TableCellEditor {
        private final JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 12));
        private final JButton btn = new JButton("↩ TRẢ SÁCH");
        public LoanActionEditor(JTable table) {
            wrapper.setOpaque(true);
            btn.setBackground(Color.decode("#353535"));
            btn.setForeground(AMBER_GOLD);
            btn.setFont(new Font("SansSerif", Font.BOLD, 12));
            btn.setPreferredSize(new Dimension(140, 35));
            wrapper.add(btn);
            btn.addActionListener(e -> {
                JOptionPane.showMessageDialog(null, "Đã xử lý trả sách phiếu: " + table.getValueAt(table.getSelectedRow(), 0));
                fireEditingStopped();
            });
        }
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            wrapper.setBackground(table.getSelectionBackground());
            return wrapper;
        }
        @Override
        public Object getCellEditorValue() { return ""; }
    }
}