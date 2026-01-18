package com.app.view.components;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;

public class MemberManagementPanel extends JPanel {
    private final Color BG_COLOR = Color.decode("#1A1A14");
    private final Color CARD_BG = Color.decode("#2D2A1E");
    private final Color AMBER_GOLD = Color.decode("#FFC845");
    private final Color CRIMSON_RED = Color.decode("#800000");
    private final Color TEXT_GRAY = new Color(160, 160, 160);

    public MemberManagementPanel() {
        setLayout(new BorderLayout());
        setBackground(BG_COLOR);
        setBorder(new EmptyBorder(30, 30, 30, 30));

        // --- PHẦN TRÊN (NORTH) ---
        JPanel topContainer = new JPanel();
        topContainer.setLayout(new BoxLayout(topContainer, BoxLayout.Y_AXIS));
        topContainer.setOpaque(false);

        // 1. Header Section
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JPanel titleGroup = new JPanel(new GridLayout(2, 1));
        titleGroup.setOpaque(false);
        JLabel title = new JLabel("Quản lý độc giả");
        title.setFont(new Font("Serif", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        JLabel subTitle = new JLabel("Danh mục lưu trữ thông tin thành viên thư viện.");
        subTitle.setForeground(TEXT_GRAY);
        titleGroup.add(title);
        titleGroup.add(subTitle);

        JButton btnAdd = new JButton("+ Thêm Độc Giả");
        btnAdd.setBackground(AMBER_GOLD);
        btnAdd.setForeground(Color.BLACK);
        btnAdd.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnAdd.setBorder(new EmptyBorder(14, 24, 14, 24)); // Padding theo yêu cầu
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));

        headerPanel.add(titleGroup, BorderLayout.WEST);
        headerPanel.add(btnAdd, BorderLayout.EAST);

        // 2. Action Bar Section
        JPanel actionBar = new JPanel(new BorderLayout());
        actionBar.setOpaque(false);
        actionBar.setBorder(new EmptyBorder(25, 0, 25, 0));

        JTextField txtSearch = new JTextField();
        txtSearch.setPreferredSize(new Dimension(500, 48));
        txtSearch.setBackground(CARD_BG);
        txtSearch.setForeground(Color.WHITE);
        txtSearch.setCaretColor(AMBER_GOLD);
        txtSearch.putClientProperty("JTextField.placeholderText", "Tìm kiếm theo Mã hoặc Họ tên...");
        txtSearch.putClientProperty("JTextField.leadingIcon", safeLoadIcon("icons/search.svg", 16, 16));

        txtSearch.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(80, 80, 80), 1, true),
            new EmptyBorder(0, 15, 0, 15)
        ));

        JButton btnFilter = new JButton("Lọc danh sách");
        btnFilter.setBackground(CARD_BG);
        btnFilter.setForeground(Color.WHITE);
        btnFilter.setIcon(safeLoadIcon("icons/filter.svg", 16, 16));
        btnFilter.setPreferredSize(new Dimension(160, 48));
        btnFilter.setFocusPainted(false);

        actionBar.add(txtSearch, BorderLayout.WEST);
        actionBar.add(btnFilter, BorderLayout.EAST);

        topContainer.add(headerPanel);
        topContainer.add(actionBar);

        // --- PHẦN GIỮA (CENTER): Bảng dữ liệu ---
        String[] columns = {"MÃ ĐỘC GIẢ", "HỌ TÊN", "NGÀY SINH", "SỐ ĐIỆN THOẠI", "NGÀY ĐĂNG KÝ", "HẠN THẺ", "THAO TÁC"};
        Object[][] data = {
            {"DG2024001", "Trần Hoàng Long", "12/10/1995", "0901 234 567", "01/01/2024", "31/12/2024", ""},
            {"DG2024002", "Nguyễn Minh Châu", "24/05/1998", "0938 112 233", "15/01/2024", "15/01/2025", ""},
            {"DG2024003", "Lê Văn Hùng", "08/11/1990", "0912 345 678", "20/02/2024", "20/02/2025", ""},
            {"DG2024004", "Phạm Thúy Vy", "19/02/2002", "0987 654 321", "05/03/2024", "05/03/2025", ""},
            {"DG2024005", "Bùi Anh Tuấn", "30/07/1988", "0909 998 877", "10/03/2024", "10/03/2025", ""}
        };

        JTable table = new JTable(new DefaultTableModel(data, columns) {
            @Override public boolean isCellEditable(int r, int c) { 
                return c == 6; // Chỉ cho phép cột Thao tác được "edit" để bấm nút
            }
        });
        styleMemberTable(table);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(BG_COLOR);

        // --- PHẦN DƯỚI (SOUTH) ---
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setOpaque(false);
        footerPanel.setBorder(new EmptyBorder(20, 0, 0, 0));
        JLabel lblPage = new JLabel("TRANG 1 / 12");
        lblPage.setForeground(TEXT_GRAY);
        footerPanel.add(lblPage, BorderLayout.WEST);

        add(topContainer, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);
    }

    private Icon safeLoadIcon(String path, int w, int h) {
        try {
            return new FlatSVGIcon(path, w, h);
        } catch (Exception e) {
            return null;
        }
    }

    private void styleMemberTable(JTable table) {
        table.setRowHeight(50);
        table.setBackground(BG_COLOR);
        table.setForeground(Color.WHITE);
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(50, 50, 50));
        table.setSelectionBackground(new Color(255, 200, 69, 30));

        JTableHeader header = table.getTableHeader();
        header.setBackground(CRIMSON_RED);
        header.setForeground(AMBER_GOLD);
        header.setFont(new Font("SansSerif", Font.BOLD, 13));
        header.setPreferredSize(new Dimension(0, 45));

        // Renderer cho các cột dữ liệu thông thường
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int col) {
                JLabel c = (JLabel) super.getTableCellRendererComponent(t, v, s, f, r, col);
                c.setHorizontalAlignment(CENTER);
                if (!s) {
                    c.setBackground(r % 2 == 0 ? BG_COLOR : new Color(30, 30, 30));
                    if (col == 0) c.setForeground(AMBER_GOLD);
                    else c.setForeground(Color.WHITE);
                    if (col == 5 && v.toString().contains("2025")) c.setForeground(new Color(255, 77, 77));
                }
                return c;
            }
        });

        // Thiết lập Renderer và Editor cho cột THAO TÁC (Index 6)
        table.getColumnModel().getColumn(6).setCellRenderer(new MemberActionRenderer());
        table.getColumnModel().getColumn(6).setCellEditor(new MemberActionEditor(table));
    }

    // --- INNER CLASSES DÀNH CHO NÚT BẤM TRÊN TABLE ---

    class ActionPanel extends JPanel {
        public JButton btnEdit = new JButton("✎");
        public JButton btnDelete = new JButton("🗑");

        public ActionPanel() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 10, 8));
            setOpaque(false);

            styleButton(btnEdit, AMBER_GOLD);
            styleButton(btnDelete, new Color(255, 77, 77)); // Màu đỏ Crimson

            add(btnEdit);
            add(btnDelete);
        }

        private void styleButton(JButton btn, Color color) {
            btn.setFont(new Font("SansSerif", Font.PLAIN, 18));
            btn.setForeground(color);
            btn.setBorderPainted(false);
            btn.setContentAreaFilled(false);
            btn.setFocusPainted(false);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
    }

    class MemberActionRenderer implements TableCellRenderer {
        private final ActionPanel panel = new ActionPanel();
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            // Đồng bộ màu nền với dòng của bảng
            if (isSelected) {
                panel.setBackground(table.getSelectionBackground());
            } else {
                panel.setBackground(row % 2 == 0 ? BG_COLOR : new Color(30, 30, 30));
            }
            panel.setOpaque(true);
            return panel;
        }
    }

    class MemberActionEditor extends AbstractCellEditor implements TableCellEditor {
        private final ActionPanel panel = new ActionPanel();

        public MemberActionEditor(JTable table) {
            panel.btnEdit.addActionListener(e -> {
                int row = table.getSelectedRow();
                String name = table.getValueAt(row, 1).toString();
                JOptionPane.showMessageDialog(null, "Đang sửa thông tin độc giả: " + name);
                fireEditingStopped();
            });

            panel.btnDelete.addActionListener(e -> {
                int row = table.getSelectedRow();
                String name = table.getValueAt(row, 1).toString();
                int confirm = JOptionPane.showConfirmDialog(null, 
                    "Bạn có chắc muốn xóa độc giả: " + name + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    // Xử lý xóa ở đây
                }
                fireEditingStopped();
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            panel.setBackground(table.getSelectionBackground());
            panel.setOpaque(true);
            return panel;
        }

        @Override
        public Object getCellEditorValue() { return ""; }
    }
}