package com.app.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "title")
    private String title;

    @Column(name = "author")
    private String author;

    @Column(name = "category")
    private String category;

    @Column(name = "total_qty")
    private int totalQty;

    @Column(name = "available_qty")
    private int availableQty;

    public Book() { }

    public Book(String title, String author, String category, int totalQty, int availableQty) {
        this.title = title;
        this.author = author;
        this.category = category;
        this.totalQty = totalQty;
        this.availableQty = availableQty;
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getTotalQty() {
		return totalQty;
	}

	public void setTotalQty(int totalQty) {
		this.totalQty = totalQty;
	}

	public int getAvailableQty() {
		return availableQty;
	}

	public void setAvailableQty(int availableQty) {
		this.availableQty = availableQty;
	}
    
    // Nếu code đỏ chữ id, title... bạn nhớ tự tạo Getter/Setter nhé
    // (Source -> Generate Getters and Setters -> Select All -> Generate)
}