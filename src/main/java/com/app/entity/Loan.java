package com.app.entity;

import jakarta.persistence.*;
import java.time.LocalDate; // Dùng cái này cho chuẩn mới

@Entity
@Table(name = "Loans") // Lưu ý: Trong ảnh pgAdmin tên bảng là "Loans" (có thể viết hoa chữ L)
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_loan")
    private Long id;

    @Column(name = "id_book")
    private Long bookId;

    @Column(name = "id_member")
    private Long memberId;

    @Column(name = "id_user")
    private Long userId; // Người cho mượn

    @Column(name = "borrow_date")
    private LocalDate borrowDate;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "return_date")
    private LocalDate returnDate;

    @Column(name = "status")
    private String status;

    public Loan() {}

    public Loan(Long bookId, Long memberId, Long userId, LocalDate borrowDate, LocalDate dueDate, String status) {
        this.bookId = bookId;
        this.memberId = memberId;
        this.userId = userId;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.status = status;
    }
    
    // Getter & Setter (Alt + Shift + S -> R)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getBookId() { return bookId; }
    public void setBookId(Long bookId) { this.bookId = bookId; }
    public Long getMemberId() { return memberId; }
    public void setMemberId(Long memberId) { this.memberId = memberId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public LocalDate getBorrowDate() { return borrowDate; }
    public void setBorrowDate(LocalDate borrowDate) { this.borrowDate = borrowDate; }
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}