package com.app.view.components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.app.entity.Book;
import com.app.service.BookService;
import com.formdev.flatlaf.extras.FlatSVGIcon;

public class BookInventoryPanel extends JPanel {
    private final Color BG_COLOR = Color.decode("#1A1A14");
    private final Color CARD_BG = Color.decode("#2D2A1E");
    private final Color AMBER_GOLD = Color.decode("#FFC845");
    private final Color CRIMSON_RED = Color.decode("#800000");
    private final Color TEXT_GRAY = new Color(160, 160, 160);
    
    private BookService bookService = new BookService();
    private JTable table; 
    private Timer searchTimer;

    public BookInventoryPanel() {
        setLayout(new BorderLayout());
        setBackground(BG_COLOR);
        setBorder(new EmptyBorder(30, 30, 30, 30));

        // --- 1. Header Section ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JPanel titleGroup = new JPanel(new GridLayout(2, 1));
        titleGroup.setOpaque(false);
        JLabel title = new JLabel("Quản lý kho sách");
        title.setFont(new Font("Serif", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        JLabel subTitle = new JLabel("Hệ thống quản lý tài liệu học thuật SLMS");
        subTitle.setForeground(TEXT_GRAY);
        titleGroup.add(title);
        titleGroup.add(subTitle);

        JButton btnAdd = new JButton("+ Thêm Sách Mới");
        btnAdd.setBackground(AMBER_GOLD);
        btnAdd.setForeground(Color.BLACK);
        btnAdd.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnAdd.setBorder(new EmptyBorder(14, 24, 14, 24)); 
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAdd.addActionListener(e -> showAddBookDialog());

        headerPanel.add(titleGroup, BorderLayout.WEST);
        headerPanel.add(btnAdd, BorderLayout.EAST);

        // --- 2. Search Bar Section ---
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 20));
        searchPanel.setOpaque(false);
        
        JTextField txtSearch = new JTextField();
        txtSearch.setPreferredSize(new Dimension(550, 48));
        txtSearch.setBackground(CARD_BG);
        txtSearch.setForeground(Color.WHITE);
        txtSearch.setCaretColor(AMBER_GOLD);
        
        txtSearch.putClientProperty("JTextField.leadingIcon", new FlatSVGIcon("icons/search.svg", 16, 16));
        txtSearch.putClientProperty("JTextField.placeholderText", "Tìm kiếm theo mã sách, tên sách hoặc tác giả...");
        txtSearch.putClientProperty("JTextField.showClearButton", true);
        
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(80, 80, 80), 1, true),
            new EmptyBorder(0, 10, 0, 10)
        ));

        searchTimer = new Timer(500, e -> refreshTable(txtSearch.getText()));
        searchTimer.setRepeats(false);

        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { restartSearchTimer(); }
            public void removeUpdate(DocumentEvent e) { restartSearchTimer(); }
            public void changedUpdate(DocumentEvent e) { restartSearchTimer(); }

            private void restartSearchTimer() {
                if (searchTimer.isRunning()) searchTimer.stop();
                searchTimer.start();
            }
        });

        searchPanel.add(txtSearch);

        // --- 3. Table Section (Cập nhật cột SẴN CÓ) ---
        String[] columns = {"MÃ SÁCH", "TÊN SÁCH", "TÁC GIẢ", "THỂ LOẠI", "SỐ LƯỢNG", "SẴN CÓ", "TRẠNG THÁI", "THAO TÁC"};
        DefaultTableModel model = new DefaultTableModel(null, columns) {
            @Override
            public boolean isCellEditable(int row, int column) { return column == 7; } // Thao tác chuyển sang index 7
        };

        table = new JTable(model);
        styleTable(table);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(BG_COLOR);

        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setOpaque(false);
        footerPanel.setBorder(new EmptyBorder(20, 0, 0, 0));
        JLabel lblEntries = new JLabel("SLMS DATABASE CONNECTED");
        lblEntries.setForeground(TEXT_GRAY);
        footerPanel.add(lblEntries, BorderLayout.WEST);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(searchPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.add(footerPanel, BorderLayout.SOUTH);

        add(headerPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);

        refreshTable(); 
    }

    private void showAddBookDialog() {
        JTextField fTitle = new JTextField();
        JTextField fAuthor = new JTextField();
        JTextField fCategory = new JTextField();
        JTextField fQty = new JTextField();

        Object[] message = {
            "Tên sách:", fTitle,
            "Tác giả:", fAuthor,
            "Thể loại:", fCategory,
            "Số lượng tổng:", fQty
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Thêm sách mới", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                bookService.addBook(fTitle.getText(), fAuthor.getText(), fCategory.getText(), Integer.parseInt(fQty.getText()));
                refreshTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Dữ liệu không hợp lệ!");
            }
        }
    }

    // --- Logic Chỉnh sửa (Cập nhật logic Total/Available) ---
    private void showEditBookDialog(int id) {
        Book book = bookService.getAllBooks().stream()
                    .filter(b -> b.getId() == id).findFirst().orElse(null);

        if (book == null) return;

        JTextField fTitle = new JTextField(book.getTitle());
        JTextField fAuthor = new JTextField(book.getAuthor());
        JTextField fCategory = new JTextField(book.getCategory());
        JTextField fTotalQty = new JTextField(String.valueOf(book.getTotalQty()));
        JLabel lblCurrentAvail = new JLabel("Hiện đang trên giá: " + book.getAvailableQty());
        lblCurrentAvail.setForeground(AMBER_GOLD);

        Object[] message = {
            "Tên sách:", fTitle,
            "Tác giả:", fAuthor,
            "Thể loại:", fCategory,
            "Tổng số lượng kho:", fTotalQty,
            lblCurrentAvail
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Chỉnh sửa sách: BK-" + id, JOptionPane.OK_CANCEL_OPTION);
        
        if (option == JOptionPane.OK_OPTION) {
            try {
                int newTotal = Integer.parseInt(fTotalQty.getText());
                // Logic: Available mới = Available cũ + (Total mới - Total cũ)
                int diff = newTotal - book.getTotalQty();
                int newAvailable = book.getAvailableQty() + diff;

                if (newAvailable < 0) {
                    JOptionPane.showMessageDialog(this, "Lỗi: Không thể giảm tổng số lượng thấp hơn số sách đang cho mượn!");
                    return;
                }

                // Giả định bạn đã cập nhật hàm updateBook trong BookService nhận thêm tham số availableQty
                bookService.updateBook(
                    id, 
                    fTitle.getText(), 
                    fAuthor.getText(), 
                    fCategory.getText(), 
                    newTotal
                );
                
                refreshTable();
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
            }
        }
    }

    public void refreshTable() {
        refreshTable(null);
    }

    public void refreshTable(String query) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); 
        List<Book> allBooks = bookService.getAllBooks();
        
        if (query == null || query.trim().isEmpty()) {
            for (Book b : allBooks) addBookToModel(model, b);
            return;
        }

        String lowerKey = query.toLowerCase();
        List<Book> matches = new ArrayList<>();
        List<Book> others = new ArrayList<>();

        for (Book b : allBooks) {
            boolean isMatch = b.getTitle().toLowerCase().contains(lowerKey) || 
                              b.getAuthor().toLowerCase().contains(lowerKey) ||
                              b.getCategory().toLowerCase().contains(lowerKey) ||
                              String.valueOf(b.getId()).contains(lowerKey);
            if (isMatch) matches.add(b); else others.add(b);
        }

        for (Book b : matches) addBookToModel(model, b);
        for (Book b : others) addBookToModel(model, b);
    }

    private void addBookToModel(DefaultTableModel model, Book b) {
        String status = b.getAvailableQty() > 0 ? "SẴN SÀNG" : "HẾT SÁCH";
        model.addRow(new Object[]{
            "BK-" + String.format("%05d", b.getId()), 
            b.getTitle(),
            b.getAuthor(),
            b.getCategory(),
            String.format("%02d", b.getTotalQty()),
            String.format("%02d", b.getAvailableQty()), // Thêm giá trị Sẵn có
            status,
            "" 
        });
    }

    private void styleTable(JTable table) {
        table.setRowHeight(50);
        table.setBackground(BG_COLOR);
        table.setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(255, 200, 69, 30));
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(50, 50, 50));

        JTableHeader header = table.getTableHeader();
        header.setBackground(CRIMSON_RED);
        header.setForeground(AMBER_GOLD);
        header.setFont(new Font("SansSerif", Font.BOLD, 13));
        header.setPreferredSize(new Dimension(0, 45));

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel c = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setHorizontalAlignment(CENTER);
                c.setBorder(new EmptyBorder(0, 10, 0, 10));
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? BG_COLOR : new Color(30, 30, 30));
                }
                // Cột trạng thái giờ là index 6
                if (column == 6 && value != null) {
                    String status = value.toString();
                    if (status.contains("SẴN SÀNG")) c.setForeground(AMBER_GOLD);
                    else c.setForeground(new Color(255, 77, 77));
                } else if (!isSelected) {
                    c.setForeground(Color.WHITE);
                }
                return c;
            }
        });

        // Áp dụng Editor và Renderer cho cột Thao tác (Index 7)
        table.getColumnModel().getColumn(7).setCellRenderer(new ActionButtonRenderer());
        table.getColumnModel().getColumn(7).setCellEditor(new ActionButtonEditor(table));
    }

    class ActionPanel extends JPanel {
        public JButton btnEdit = new JButton("✎");
        public JButton btnDelete = new JButton("🗑");
        public ActionPanel() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 10, 8));
            setOpaque(false);
            styleActionButton(btnEdit, AMBER_GOLD);
            styleActionButton(btnDelete, new Color(255, 77, 77));
            add(btnEdit); add(btnDelete);
        }
        private void styleActionButton(JButton btn, Color color) {
            btn.setFont(new Font("SansSerif", Font.PLAIN, 18));
            btn.setForeground(color);
            btn.setBorderPainted(false);
            btn.setContentAreaFilled(false);
            btn.setFocusPainted(false);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
    }

    class ActionButtonRenderer implements TableCellRenderer {
        private final ActionPanel panel = new ActionPanel();
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            panel.setBackground(isSelected ? table.getSelectionBackground() : (row % 2 == 0 ? BG_COLOR : new Color(30, 30, 30)));
            panel.setOpaque(true);
            return panel;
        }
    }

    class ActionButtonEditor extends AbstractCellEditor implements TableCellEditor {
        private final ActionPanel panel = new ActionPanel();
        public ActionButtonEditor(JTable table) {
            panel.btnEdit.addActionListener(e -> {
                int row = table.getSelectedRow();
                if (row != -1) {
                    int id = Integer.parseInt(table.getValueAt(row, 0).toString().replace("BK-", ""));
                    fireEditingStopped();
                    showEditBookDialog(id);
                }
            });
            panel.btnDelete.addActionListener(e -> {
                int row = table.getSelectedRow();
                if (row != -1) {
                    int id = Integer.parseInt(table.getValueAt(row, 0).toString().replace("BK-", ""));
                    String title = table.getValueAt(row, 1).toString();
                    int confirm = JOptionPane.showConfirmDialog(null, "Xóa sách: " + title + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        bookService.deleteBook(id);
                        refreshTable(); 
                    }
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