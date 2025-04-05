package com.example.kutupproje2;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Loan {
    private int id;
    private StringProperty userName;
    private StringProperty bookName;
    private StringProperty loanDate;
    public Loan(){};
    public Loan(int id, String userName, String bookName, String loanDate) {
        this.id = id;
        this.userName = new SimpleStringProperty(userName);
        this.bookName = new SimpleStringProperty(bookName);
        this.loanDate = new SimpleStringProperty(loanDate);
    }

    public int getId() {
        return id;
    }

    public StringProperty userNameProperty() {
        return userName;
    }

    public StringProperty bookNameProperty() {
        return bookName;
    }

    public StringProperty loanDateProperty() {
        return loanDate;
    }

    // Yeni getBookName metodu
    public String getBookName() {
        return bookName.get();
    }
}
