package com.app.service;

import com.app.entity.Book;
import com.app.entity.Loan;
import com.app.repository.BookRepository;
import com.app.repository.LoanRepository;
import java.time.LocalDate;
import java.util.List;

public class LoanService {
    private LoanRepository loanRepo = new LoanRepository();
    private BookRepository bookRepo = new BookRepository();

    public List<Loan> getAllLoans() {
        return loanRepo.findAll();
    }

    // Logic xử lý mượn sách
    public String borrowBook(Long memberId, int bookId, Long userId) {
        Book book = bookRepo.findById(bookId);
        if (book == null) return "Không tìm thấy sách!";
        if (book.getAvailableQty() <= 0) return "Sách này hiện đã hết trên giá!";

        // Tạo phiếu mượn mới
        Loan loan = new Loan();
        loan.setMemberId(memberId);
        loan.setBookId((long) bookId);
        loan.setUserId(userId);
        loan.setBorrowDate(LocalDate.now());
        loan.setDueDate(LocalDate.now().plusDays(14)); // Mặc định hạn trả là 14 ngày
        loan.setStatus("BORROWED");

        // Trừ số lượng sách có sẵn
        book.setAvailableQty(book.getAvailableQty() - 1);

        loanRepo.saveOrUpdate(loan);
        bookRepo.saveOrUpdate(book);
        return "SUCCESS";
    }

    // Logic xử lý trả sách
    public void returnBook(Long loanId) {
        Loan loan = loanRepo.findById(loanId);
        if (loan != null && "BORROWED".equals(loan.getStatus())) {
            loan.setReturnDate(LocalDate.now());
            loan.setStatus("RETURNED");

            // Cộng lại số lượng sách vào kho
            Book book = bookRepo.findById(loan.getBookId().intValue());
            if (book != null) {
                book.setAvailableQty(book.getAvailableQty() + 1);
                bookRepo.saveOrUpdate(book);
            }
            loanRepo.saveOrUpdate(loan);
        }
    }
    public long getActiveLoansCount() {
        return getAllLoans().stream().filter(l -> "BORROWED".equals(l.getStatus())).count();
    }

    public long getOverdueCount() {
        return getAllLoans().stream()
                .filter(l -> "BORROWED".equals(l.getStatus()) && l.getDueDate().isBefore(java.time.LocalDate.now()))
                .count();
    }
}