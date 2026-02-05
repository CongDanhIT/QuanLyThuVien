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
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import com.app.entity.*;
import com.app.service.*;

public class DashboardPanel extends JPanel {
    private final Color AMBER_GOLD = Color.decode("#FFC845");
    private final Color CRIMSON_RED = Color.decode("#800000");
    private final Color CARD_BG = Color.decode("#2D2A1E");

    private BookService bookService = new BookService();
    private MemberService memberService = new MemberService();
    private LoanService loanService = new LoanService();
    
    private StatCard cardBooks, cardMembers, cardLoans, cardOverdue;
    private JTable overdueTable;
    private BarChartPanel barChart;
    private PieChartPanel pieChart;
    private JLabel lblInsightSummary;

    public DashboardPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.decode("#1A1A14"));
        setBorder(new EmptyBorder(30, 30, 30, 30));

        // --- 1. Header ---
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel title = new JLabel("Hệ Thống Thống Kê Thư Viện");
        title.setFont(new Font("Serif", Font.BOLD, 32));
        title.setForeground(Color.WHITE);
        header.add(title, BorderLayout.WEST);
        
        JLabel dateLabel = new JLabel(LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, dd/MM/yyyy")).toUpperCase() + " 🔔");
        dateLabel.setForeground(AMBER_GOLD);
        dateLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        header.add(dateLabel, BorderLayout.EAST);

        // --- 2. Stats Area ---
        JPanel cardsArea = new JPanel(new GridLayout(1, 4, 20, 0));
        cardsArea.setOpaque(false);
        cardsArea.setBorder(new EmptyBorder(25, 0, 25, 0));
        
        cardBooks = new StatCard("TỔNG SỐ SÁCH", "0", "...");
        cardMembers = new StatCard("ĐỘC GIẢ", "0", "...");
        cardLoans = new StatCard("ĐANG MƯỢN", "0", "...");
        cardOverdue = new StatCard("QUÁ HẠN", "0", "...");

        cardsArea.add(cardBooks); cardsArea.add(cardMembers); 
        cardsArea.add(cardLoans); cardsArea.add(cardOverdue);

        // --- 3. Charts Area (Bổ sung JScrollPane cho Legend) ---
        JPanel chartsContainer = new JPanel(new BorderLayout());
        chartsContainer.setOpaque(false);
        
        JPanel chartsArea = new JPanel(new GridLayout(1, 2, 20, 0));
        chartsArea.setOpaque(false);
        chartsArea.setPreferredSize(new Dimension(0, 320));

        barChart = new BarChartPanel("Hoạt động mượn sách (7 ngày qua)");
        pieChart = new PieChartPanel("Phân bổ sách theo thể loại");
        chartsArea.add(barChart);
        chartsArea.add(pieChart);

        lblInsightSummary = new JLabel("Đang xử lý dữ liệu...");
        lblInsightSummary.setForeground(new Color(180, 180, 180));
        lblInsightSummary.setFont(new Font("SansSerif", Font.ITALIC, 13));
        lblInsightSummary.setBorder(new EmptyBorder(10, 5, 20, 0));

        chartsContainer.add(lblInsightSummary, BorderLayout.NORTH);
        chartsContainer.add(chartsArea, BorderLayout.CENTER);

        // --- 4. Table Area ---
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setOpaque(false);
        JLabel tableTitle = new JLabel("Phiếu mượn cần thu hồi");
        tableTitle.setForeground(Color.WHITE);
        tableTitle.setFont(new Font("Serif", Font.BOLD, 22));
        tableTitle.setBorder(new EmptyBorder(0, 0, 10, 0));

        String[] cols = {"Mã phiếu", "Độc giả", "Tên sách", "Ngày mượn", "Hết hạn", "Trễ hạn"};
        overdueTable = new JTable(new DefaultTableModel(null, cols) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        });
        styleTable(overdueTable);
        
        JScrollPane scrollPane = new JScrollPane(overdueTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60)));
        scrollPane.getViewport().setBackground(Color.decode("#1A1A14"));
        
        tablePanel.add(tableTitle, BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setOpaque(false);
        mainContent.add(cardsArea);
        mainContent.add(chartsContainer);
        mainContent.add(tablePanel);

        add(header, BorderLayout.NORTH);
        add(mainContent, BorderLayout.CENTER);

        refreshData(); 
    }

    public void refreshData() {
        List<Book> books = bookService.getAllBooks();
        List<Member> members = memberService.getAllMembers();
        List<Loan> loans = loanService.getAllLoans();
        LocalDate today = LocalDate.now();

        // Cards logic
        long newBooks = books.stream().filter(b -> b.getCreatedAt() != null && b.getCreatedAt().getMonth() == today.getMonth()).count();
        cardBooks.setData(String.format("%,d", books.size()), "+" + newBooks + " cuốn mới");

        long newMembers = members.stream().filter(m -> m.getJoinedDate() != null && m.getJoinedDate().equals(today)).count();
        cardMembers.setData(String.format("%,d", members.size()), "+" + newMembers + " hôm nay");

        long activeLoansCount = loans.stream().filter(l -> "BORROWED".equals(l.getStatus())).count();
        int totalInv = books.stream().mapToInt(Book::getTotalQty).sum();
        double ratio = totalInv > 0 ? (double) activeLoansCount / totalInv * 100 : 0;
        cardLoans.setData(String.valueOf(activeLoansCount), String.format("%.1f%% năng suất kho", ratio));

        List<Loan> overdue = loans.stream().filter(l -> "BORROWED".equals(l.getStatus()) && l.getDueDate().isBefore(today)).collect(Collectors.toList());
        cardOverdue.setData(String.valueOf(overdue.size()), overdue.size() > 0 ? "⚠ CẦN XỬ LÝ" : "ỔN ĐỊNH");

        // Charts logic
        Map<LocalDate, Integer> trend = new LinkedHashMap<>();
        for(int i=6; i>=0; i--) trend.put(today.minusDays(i), 0);
        loans.forEach(l -> { if(trend.containsKey(l.getBorrowDate())) trend.put(l.getBorrowDate(), trend.get(l.getBorrowDate())+1); });
        barChart.updateChart(trend);

        Map<String, Integer> cats = new HashMap<>();
        books.forEach(b -> cats.put(b.getCategory(), cats.getOrDefault(b.getCategory(), 0) + 1));
        pieChart.updateChart(cats);

        lblInsightSummary.setText(String.format("✨ Hiệu suất kho đạt %.1f%%. Tổng số lượt mượn trong tuần: %d.", ratio, trend.values().stream().mapToInt(Integer::intValue).sum()));

        // Table logic
        DefaultTableModel model = (DefaultTableModel) overdueTable.getModel();
        model.setRowCount(0);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for (Loan l : overdue) {
            Member m = memberService.getMemberById(l.getMemberId());
            Book b = books.stream().filter(bk -> bk.getId() == l.getBookId().intValue()).findFirst().orElse(null);
            model.addRow(new Object[]{ "#PH"+String.format("%04d", l.getId()), m!=null?m.getFullName():"ID:"+l.getMemberId(), b!=null?b.getTitle():"N/A", l.getBorrowDate().format(dtf), l.getDueDate().format(dtf), ChronoUnit.DAYS.between(l.getDueDate(), today)+" ngày" });
        }
    }

    private void styleTable(JTable table) {
        JTableHeader header = table.getTableHeader();
        header.setBackground(CRIMSON_RED);
        header.setForeground(AMBER_GOLD);
        header.setFont(new Font("SansSerif", Font.BOLD, 14));
        header.setPreferredSize(new Dimension(0, 40));
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int col) {
                JLabel c = (JLabel) super.getTableCellRendererComponent(t, v, s, f, r, col);
                if (!s) c.setBackground(r % 2 == 0 ? Color.decode("#1A1A14") : Color.decode("#2A2A2A"));
                if (col == 5) c.setForeground(Color.decode("#FF4D4D")); else c.setForeground(Color.WHITE);
                c.setHorizontalAlignment(CENTER); return c;
            }
        });
        table.setRowHeight(38);
        table.setShowGrid(false);
    }
}

// --- BIỂU ĐỒ TRÒN CÓ THANH CUỘN (SCROLLABLE LEGEND) ---
class PieChartPanel extends JPanel {
    private Map<String, Integer> data = new HashMap<>();
    private JPanel legendContainer;
    private DrawPanel drawPanel;
    private String title;

    public PieChartPanel(String t) {
        this.title = t;
        setLayout(new BorderLayout());
        setOpaque(false);
        setBackground(Color.decode("#2D2A1E"));

        // Header tiêu đề
        JLabel lblTitle = new JLabel(title);
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblTitle.setBorder(new EmptyBorder(15, 20, 10, 0));
        add(lblTitle, BorderLayout.NORTH);

        // Panel vẽ hình tròn (Trái)
        drawPanel = new DrawPanel();
        drawPanel.setPreferredSize(new Dimension(180, 0));
        add(drawPanel, BorderLayout.WEST);

        // Danh sách chú thích có Scroll (Phải)
        legendContainer = new JPanel();
        legendContainer.setLayout(new BoxLayout(legendContainer, BoxLayout.Y_AXIS));
        legendContainer.setOpaque(false);

        JScrollPane scrollLegend = new JScrollPane(legendContainer);
        scrollLegend.setOpaque(false);
        scrollLegend.getViewport().setOpaque(false);
        scrollLegend.setBorder(null);
        scrollLegend.getVerticalScrollBar().setUnitIncrement(10);
        scrollLegend.setPreferredSize(new Dimension(200, 0));
        add(scrollLegend, BorderLayout.CENTER);
    }

    public void updateChart(Map<String, Integer> d) {
        this.data = d;
        legendContainer.removeAll();
        Color[] colors = {Color.decode("#FFC845"), Color.decode("#50C878"), Color.decode("#4DA6FF"), Color.decode("#FF4D4D"), Color.decode("#A0A0A0"), Color.CYAN, Color.MAGENTA};
        
        int total = data.values().stream().mapToInt(Integer::intValue).sum();
        int i = 0;
        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            double pct = total > 0 ? (double) entry.getValue() / total * 100 : 0;
            legendContainer.add(new LegendItem(entry.getKey(), String.format("%.1f%%", pct), colors[i % colors.length]));
            i++;
        }
        
        legendContainer.revalidate();
        legendContainer.repaint();
        drawPanel.repaint();
    }

    private class DrawPanel extends JPanel {
        public DrawPanel() { setOpaque(false); }
        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            int total = data.values().stream().mapToInt(Integer::intValue).sum();
            if (total == 0) return;

            int size = Math.min(getWidth(), getHeight()) - 40;
            int x = (getWidth() - size) / 2;
            int y = (getHeight() - size) / 2;
            int start = 0, i = 0;
            Color[] colors = {Color.decode("#FFC845"), Color.decode("#50C878"), Color.decode("#4DA6FF"), Color.decode("#FF4D4D"), Color.decode("#A0A0A0"), Color.CYAN, Color.MAGENTA};

            for (Integer val : data.values()) {
                int angle = (int)Math.round((double)val / total * 360);
                g2.setColor(colors[i % colors.length]);
                g2.fillArc(x, y, size, size, start, angle);
                start += angle; i++;
            }
            g2.dispose();
        }
    }

    private class LegendItem extends JPanel {
        public LegendItem(String label, String percent, Color color) {
            setOpaque(false);
            setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
            
            JPanel box = new JPanel() {
                @Override protected void paintComponent(Graphics g) {
                    g.setColor(color);
                    g.fillRoundRect(0, 0, 12, 12, 4, 4);
                }
            };
            box.setPreferredSize(new Dimension(12, 12));
            box.setOpaque(false);

            JLabel txt = new JLabel(label + " (" + percent + ")");
            txt.setForeground(new Color(220, 220, 220));
            txt.setFont(new Font("SansSerif", Font.PLAIN, 12));

            add(box);
            add(txt);
        }
    }

    @Override protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.decode("#2D2A1E"));
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
        g2.setColor(Color.decode("#D4AF37"));
        g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 20, 20);
        g2.dispose();
    }
}

// --- BAR CHART & STAT CARD GIỮ NGUYÊN NHƯ PHIÊN BẢN TRƯỚC ---
class BarChartPanel extends JPanel {
    private Map<LocalDate, Integer> data = new LinkedHashMap<>();
    private String title;
    public BarChartPanel(String t) { this.title = t; setOpaque(false); }
    public void updateChart(Map<LocalDate, Integer> d) { this.data = d; repaint(); }
    @Override protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.decode("#2D2A1E"));
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
        g2.setColor(Color.WHITE); g2.setFont(new Font("SansSerif", Font.BOLD, 14));
        g2.drawString(title, 20, 30);
        if(data.isEmpty()) return;
        int max = Math.max(5, data.values().stream().max(Integer::compare).orElse(1));
        int pad = 50, cH = getHeight()-100, xS = 50, bW = (getWidth()-100)/7;
        for(int j=0; j<=4; j++) {
            int yG = getHeight()-pad-(j*cH/4);
            g2.setColor(new Color(255,255,255,30)); g2.drawLine(xS, yG, getWidth()-30, yG);
        }
        int i=0; DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM");
        for(Map.Entry<LocalDate, Integer> e : data.entrySet()) {
            int h = (int)((double)e.getValue()/max*cH);
            int bx = xS + i*bW + 10, by = getHeight()-pad-h;
            g2.setColor(Color.decode("#FFC845")); g2.fillRoundRect(bx, by, bW-20, h, 8, 8);
            if(e.getValue()>0) { g2.setColor(Color.WHITE); g2.drawString(String.valueOf(e.getValue()), bx+(bW-20)/2-5, by-8); }
            g2.setColor(new Color(180, 180, 180)); g2.drawString(e.getKey().format(dtf), bx, getHeight()-25);
            i++;
        }
        g2.dispose();
    }
}

class StatCard extends JPanel {
    private JLabel lblValue, lblSub;
    public StatCard(String title, String val, String sub) {
        setOpaque(false); setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(22, 25, 22, 25));
        JLabel t = new JLabel(title); t.setForeground(new Color(180, 180, 180));
        lblValue = new JLabel(val); lblValue.setForeground(Color.decode("#FFD700"));
        lblValue.setFont(new Font("SansSerif", Font.BOLD, 36));
        lblSub = new JLabel(sub); lblSub.setForeground(Color.decode("#D4AF37"));
        add(t); add(Box.createVerticalStrut(10)); add(lblValue); add(Box.createVerticalStrut(5)); add(lblSub);
    }
    public void setData(String v, String s) { lblValue.setText(v); lblSub.setText(s); lblSub.setForeground(s.contains("⚠")?Color.decode("#FF4D4D"):Color.decode("#D4AF37")); }
    @Override protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create(); g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.decode("#2D2A1E")); g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
        g2.setColor(Color.decode("#D4AF37")); g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 20, 20); g2.dispose();
    }
}