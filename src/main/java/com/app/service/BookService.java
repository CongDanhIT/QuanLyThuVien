package com.app.service;

import com.app.entity.Book;
import com.app.repository.BookRepository;
import java.util.List;

public class BookService {
    private BookRepository bookRepo = new BookRepository();

    public List<Book> getAllBooks() {
        return bookRepo.findAll();
    }

    public void addBook(String title, String author, String category, int qty) {
        Book book = new Book(title, author, category, qty, qty); // Ban đầu qty sẵn dùng = tổng qty
        bookRepo.saveOrUpdate(book);
    }

    public void deleteBook(int id) {
        bookRepo.deleteById(id);
    }
    public void updateBook(int id, String title, String author, String category, int totalQty) {
        Book book = bookRepo.findById(id);
        if (book != null) {
            // Tính toán lại available_qty nếu cần (ví dụ: giữ nguyên độ chênh lệch khi tăng kho)
            int diff = totalQty - book.getTotalQty();
            book.setTitle(title);
            book.setAuthor(author);
            book.setCategory(category);
            book.setTotalQty(totalQty);
            book.setAvailableQty(book.getAvailableQty() + diff);
            
            bookRepo.saveOrUpdate(book);
        }
    }
    public long countNewBooksThisMonth() {
        java.time.LocalDate startOfMonth = java.time.LocalDate.now().withDayOfMonth(1);
        return getAllBooks().stream()
                .filter(b -> b.getCreatedAt() != null && !b.getCreatedAt().isBefore(startOfMonth))
                .count();
    }

    public int getTotalInventoryQty() {
        return getAllBooks().stream().mapToInt(Book::getTotalQty).sum();
    }
}