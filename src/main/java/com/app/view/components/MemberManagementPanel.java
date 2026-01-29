package com.app.view.components;

import com.app.entity.Member;
import com.app.service.MemberService;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class MemberManagementPanel extends JPanel {
    private final Color BG_COLOR = Color.decode("#1A1A14");
    private final Color CARD_BG = Color.decode("#2D2A1E");
    private final Color AMBER_GOLD = Color.decode("#FFC845");
    private final Color CRIMSON_RED = Color.decode("#800000");
    private final Color TEXT_GRAY = new Color(160, 160, 160);

    private MemberService memberService = new MemberService();
    private JTable table;
    private Timer searchTimer;
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public MemberManagementPanel() {
        setLayout(new BorderLayout());
        setBackground(BG_COLOR);
        setBorder(new EmptyBorder(30, 30, 30, 30));

        // --- Header ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        JLabel title = new JLabel("Quản lý độc giả");
        title.setFont(new Font("Serif", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        
        JButton btnAdd = new JButton("+ Thêm Độc Giả");
        btnAdd.setBackground(AMBER_GOLD);
        btnAdd.setForeground(Color.BLACK);
        btnAdd.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnAdd.setBorder(new EmptyBorder(14, 24, 14, 24));
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAdd.addActionListener(e -> showMemberDialog(null));

        headerPanel.add(title, BorderLayout.WEST);
        headerPanel.add(btnAdd, BorderLayout.EAST);

        // --- Search Bar ---
        JTextField txtSearch = new JTextField();
        txtSearch.setPreferredSize(new Dimension(500, 48));
        txtSearch.setBackground(CARD_BG);
        txtSearch.setForeground(Color.WHITE);
        txtSearch.setCaretColor(AMBER_GOLD);
        txtSearch.putClientProperty("JTextField.placeholderText", "Tìm kiếm tên, email hoặc mã độc giả...");
        txtSearch.putClientProperty("JTextField.leadingIcon", safeLoadIcon("icons/search.svg", 16, 16));
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(80, 80, 80), 1, true), new EmptyBorder(0, 15, 0, 15)
        ));

        searchTimer = new Timer(500, e -> refreshTable(txtSearch.getText()));
        searchTimer.setRepeats(false);
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { restart(); }
            public void removeUpdate(DocumentEvent e) { restart(); }
            public void changedUpdate(DocumentEvent e) { restart(); }
            private void restart() { if (searchTimer.isRunning()) searchTimer.stop(); searchTimer.start(); }
        });

        JPanel searchWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 20));
        searchWrapper.setOpaque(false);
        searchWrapper.add(txtSearch);

        // --- Table ---
        String[] columns = {"MÃ", "HỌ TÊN", "EMAIL", "SỐ ĐIỆN THOẠI", "NGÀY THAM GIA", "TRẠNG THÁI", "THAO TÁC"};
        table = new JTable(new DefaultTableModel(null, columns) {
            @Override public boolean isCellEditable(int r, int c) { return c == 6; }
        });
        styleMemberTable();

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(BG_COLOR);

        JPanel topPart = new JPanel();
        topPart.setLayout(new BoxLayout(topPart, BoxLayout.Y_AXIS));
        topPart.setOpaque(false);
        topPart.add(headerPanel);
        topPart.add(searchWrapper);

        add(topPart, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        refreshTable(null);
    }

    private Icon safeLoadIcon(String path, int w, int h) {
        try { return new FlatSVGIcon(path, w, h); } catch (Exception e) { return null; }
    }

    public void refreshTable(String query) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        List<Member> all = memberService.getAllMembers();

        if (query == null || query.trim().isEmpty()) {
            for (Member m : all) addRow(model, m);
            return;
        }

        String key = query.toLowerCase();
        List<Member> matches = new ArrayList<>();
        List<Member> others = new ArrayList<>();

        for (Member m : all) {
            boolean isMatch = m.getFullName().toLowerCase().contains(key) || 
                              (m.getEmail() != null && m.getEmail().toLowerCase().contains(key)) ||
                              String.valueOf(m.getId()).contains(key);
            if (isMatch) matches.add(m); else others.add(m);
        }

        for (Member m : matches) addRow(model, m);
        for (Member m : others) addRow(model, m);
    }

    private void addRow(DefaultTableModel model, Member m) {
        model.addRow(new Object[]{
            "DG-" + String.format("%05d", m.getId()),
            m.getFullName(),
            m.getEmail(),
            m.getPhone(),
            m.getJoinedDate() != null ? m.getJoinedDate().format(dtf) : "",
            m.getStatus(),
            ""
        });
    }

    private void showMemberDialog(Member member) {
        boolean isEdit = (member != null);
        JTextField fName = new JTextField(isEdit ? member.getFullName() : "");
        JTextField fEmail = new JTextField(isEdit ? member.getEmail() : "");
        JTextField fPhone = new JTextField(isEdit ? member.getPhone() : "");
        JComboBox<String> fStatus = new JComboBox<>(new String[]{"ACTIVE", "BANNED"});
        if (isEdit) fStatus.setSelectedItem(member.getStatus());

        Object[] message = {
            "Họ tên:", fName,
            "Email:", fEmail,
            "Số điện thoại:", fPhone,
            "Trạng thái:", fStatus
        };

        int option = JOptionPane.showConfirmDialog(this, message, isEdit ? "Sửa độc giả" : "Thêm độc giả", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            Member m = isEdit ? member : new Member();
            m.setFullName(fName.getText());
            m.setEmail(fEmail.getText());
            m.setPhone(fPhone.getText());
            m.setStatus((String) fStatus.getSelectedItem());
            if (!isEdit) m.setJoinedDate(LocalDate.now());

            memberService.saveOrUpdateMember(m);
            refreshTable(null);
        }
    }

    private void styleMemberTable() {
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

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int col) {
                JLabel c = (JLabel) super.getTableCellRendererComponent(t, v, s, f, r, col);
                c.setHorizontalAlignment(CENTER);
                if (!s) {
                    c.setBackground(r % 2 == 0 ? BG_COLOR : new Color(30, 30, 30));
                    if (col == 0) c.setForeground(AMBER_GOLD);
                    else if (col == 5 && "BANNED".equals(v)) c.setForeground(Color.RED);
                    else c.setForeground(Color.WHITE);
                }
                return c;
            }
        });

        table.getColumnModel().getColumn(6).setCellRenderer(new MemberActionRenderer());
        table.getColumnModel().getColumn(6).setCellEditor(new MemberActionEditor());
    }

    // --- Inner Classes cho Thao tác ---
    class ActionPanel extends JPanel {
        public JButton btnEdit = new JButton("✎");
        public JButton btnDelete = new JButton("🗑");
        public ActionPanel() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 10, 8)); setOpaque(false);
            btnEdit.setForeground(AMBER_GOLD); btnDelete.setForeground(new Color(255, 77, 77));
            btnEdit.setFont(new Font("SansSerif", Font.PLAIN, 18));
            btnDelete.setFont(new Font("SansSerif", Font.PLAIN, 18));
            btnEdit.setBorderPainted(false); btnEdit.setContentAreaFilled(false);
            btnDelete.setBorderPainted(false); btnDelete.setContentAreaFilled(false);
            btnEdit.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnDelete.setCursor(new Cursor(Cursor.HAND_CURSOR));
            add(btnEdit); add(btnDelete);
        }
    }

    class MemberActionRenderer implements TableCellRenderer {
        private final ActionPanel panel = new ActionPanel();
        @Override public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int c) {
            panel.setBackground(s ? t.getSelectionBackground() : (r % 2 == 0 ? BG_COLOR : new Color(30, 30, 30)));
            panel.setOpaque(true); return panel;
        }
    }

    class MemberActionEditor extends AbstractCellEditor implements TableCellEditor {
        private final ActionPanel panel = new ActionPanel();
        public MemberActionEditor() {
            panel.btnEdit.addActionListener(e -> {
                Long id = Long.parseLong(table.getValueAt(table.getSelectedRow(), 0).toString().replace("DG-", ""));
                fireEditingStopped();
                showMemberDialog(memberService.getMemberById(id));
            });
            panel.btnDelete.addActionListener(e -> {
                Long id = Long.parseLong(table.getValueAt(table.getSelectedRow(), 0).toString().replace("DG-", ""));
                if (JOptionPane.showConfirmDialog(null, "Xác nhận xóa độc giả này?") == JOptionPane.YES_OPTION) {
                    memberService.deleteMember(id); refreshTable(null);
                }
                fireEditingStopped();
            });
        }
        @Override public Component getTableCellEditorComponent(JTable t, Object v, boolean s, int r, int c) {
            panel.setBackground(t.getSelectionBackground()); panel.setOpaque(true); return panel;
        }
        @Override public Object getCellEditorValue() { return ""; }
    }
}