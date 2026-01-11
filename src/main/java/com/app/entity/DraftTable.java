package com.app.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "draft_table") // Tên bảng trong Database sẽ là "draft_table"
public class DraftTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "test_name")
    private String testName;

    @Column(name = "random_note")
    private String randomNote;

    // Constructor rỗng (Bắt buộc)
    public DraftTable() {}

    // Constructor nhập nhanh
    public DraftTable(String testName, String randomNote) {
        this.testName = testName;
        this.randomNote = randomNote;
    }

    // Getter & Setter (Tạo nhanh bằng Alt + Shift + S -> R)
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTestName() { return testName; }
    public void setTestName(String testName) { this.testName = testName; }
    public String getRandomNote() { return randomNote; }
    public void setRandomNote(String randomNote) { this.randomNote = randomNote; }
}