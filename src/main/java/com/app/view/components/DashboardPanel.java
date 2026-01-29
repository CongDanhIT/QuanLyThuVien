package com.app.view.components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import com.app.entity.*;
import com.app.service.*;

public class DashboardPanel extends JPanel {
    private final Color AMBER_GOLD = Color.decode("#FFC845");
    private final Color CRIMSON_RED = Color.decode("#800000");

    private BookService bookService = new BookService();
    private MemberService memberService = new MemberService();
    private LoanService loanService = new LoanService();
    
    private JPanel cardsPanel;
    private JTable overdueTable;

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
        
        JLabel dateLabel = new JLabel(LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, dd/MM/yyyy")).toUpperCase() + " 🔔");
        dateLabel.setForeground(AMBER_GOLD);
        header.add(dateLabel, BorderLayout.EAST);

        // --- Container cho Cards và Table ---
        JPanel centerContent = new JPanel();
        centerContent.setLayout(new BoxLayout(centerContent, BoxLayout.Y_AXIS));
        centerContent.setOpaque(false);

        cardsPanel = new JPanel(new GridLayout(1, 4, 25, 0));
        cardsPanel.setOpaque(false);
        cardsPanel.setBorder(new EmptyBorder(30, 0, 40, 0));
        
        // --- Table Section ---
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setOpaque(false);
        JLabel tableTitle = new JLabel("Danh sách mượn quá hạn");
        tableTitle.setForeground(Color.WHITE);
        tableTitle.setFont(new Font("Serif", Font.BOLD, 22));
        tableTitle.setBorder(new EmptyBorder(0, 0, 15, 0));

        String[] cols = {"Mã phiếu", "Độc giả", "Tên sách", "Ngày mượn", "Hết hạn", "Trễ hạn"};
        DefaultTableModel tableModel = new DefaultTableModel(null, cols) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        
        overdueTable = new JTable(tableModel);
        styleTable(overdueTable);
        
        JScrollPane scrollPane = new JScrollPane(overdueTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60)));
        scrollPane.getViewport().setBackground(Color.decode("#1A1A14"));
        
        tablePanel.add(tableTitle, BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        centerContent.add(cardsPanel);
        centerContent.add(tablePanel);

        add(header, BorderLayout.NORTH);
        add(centerContent, BorderLayout.CENTER);

        refreshData(); // Nạp dữ liệu thật
    }

    public void refreshData() {
        // 1. Lấy dữ liệu từ Database
        List<Book> allBooks = bookService.getAllBooks();
        List<Member> allMembers = memberService.getAllMembers();
        List<Loan> allLoans = loanService.getAllLoans();
        LocalDate today = LocalDate.now();

        // --- Tính toán Card 1: Tổng sách ---
        long totalBooks = allBooks.size();
        long newBooksMonth = allBooks.stream()
                .filter(b -> b.getCreatedAt() != null && b.getCreatedAt().getMonth() == today.getMonth())
                .count();

        // --- Tính toán Card 2: Độc giả ---
        long totalMembers = allMembers.size();
        long newMembersToday = allMembers.stream()
                .filter(m -> m.getJoinedDate() != null && m.getJoinedDate().equals(today))
                .count();

        // --- Tính toán Card 3: Đang mượn & % Kho ---
        long activeLoans = allLoans.stream().filter(l -> "BORROWED".equals(l.getStatus())).count();
        int totalInventory = bookService.getTotalInventoryQty();
        double percentage = totalInventory > 0 ? (double) activeLoans / totalInventory * 100 : 0;

        // --- Tính toán Card 4 & Table: Quá hạn ---
        List<Loan> overdueLoans = allLoans.stream()
                .filter(l -> "BORROWED".equals(l.getStatus()) && l.getDueDate().isBefore(today))
                .collect(Collectors.toList());

        // 2. Cập nhật Stat Cards
        cardsPanel.removeAll();
        cardsPanel.add(new StatCard("Tổng số sách", String.format("%,d", totalBooks), "+" + newBooksMonth + " cuốn tháng này"));
        cardsPanel.add(new StatCard("Tổng độc giả", String.format("%,d", totalMembers), "+" + newMembersToday + " độc giả hôm nay"));
        cardsPanel.add(new StatCard("Sách đang mượn", String.format("%,d", activeLoans), String.format("%.1f%% trên tổng kho", percentage)));
        cardsPanel.add(new StatCard("Sách quá hạn", String.valueOf(overdueLoans.size()), "CẦN XỬ LÝ NGAY"));
        cardsPanel.revalidate();

        // 3. Cập nhật Bảng quá hạn
        DefaultTableModel model = (DefaultTableModel) overdueTable.getModel();
        model.setRowCount(0);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (Loan l : overdueLoans) {
            Member m = memberService.getMemberById(l.getMemberId());
            Book b = allBooks.stream().filter(book -> book.getId() == l.getBookId().intValue()).findFirst().orElse(null);
            
            long daysLate = ChronoUnit.DAYS.between(l.getDueDate(), today);
            
            model.addRow(new Object[]{
                "#PH" + String.format("%04d", l.getId()),
                m != null ? m.getFullName() : "N/A",
                b != null ? b.getTitle() : "N/A",
                l.getBorrowDate().format(dtf),
                l.getDueDate().format(dtf),
                daysLate + " ngày"
            });
        }
    }

    private void styleTable(JTable table) {
        JTableHeader header = table.getTableHeader();
        header.setBackground(CRIMSON_RED);
        header.setForeground(AMBER_GOLD);
        header.setFont(new Font("SansSerif", Font.BOLD, 14));
        header.setPreferredSize(new Dimension(0, 40));

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, 
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.decode("#1A1A14") : Color.decode("#2A2A2A"));
                    c.setForeground(Color.WHITE);
                }
                if (column == 5 && value != null) c.setForeground(Color.decode("#FF4D4D"));
                ((JLabel)c).setHorizontalAlignment(SwingConstants.CENTER);
                return c;
            }
        });
        table.setRowHeight(40);
        table.setShowGrid(false);
    }
}

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

        add(t); add(Box.createVerticalStrut(10));
        add(v); add(Box.createVerticalStrut(5));
        add(s);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(BG_CARD);
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
        g2.setColor(BORDER_GOLD);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
        g2.dispose();
    }
}