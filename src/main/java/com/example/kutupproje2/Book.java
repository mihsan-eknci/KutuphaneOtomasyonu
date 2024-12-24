package com.example.kutupproje2;

public class Book {
    private int id;
    private String name;
    private String author;
    private String publisher;
    private int pageCount;
    private int publicationYear;

    public Book(int id, String name, String author, String publisher, int pageCount, int publicationYear) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.publisher = publisher;
        this.pageCount = pageCount;
        this.publicationYear = publicationYear;
    }

    // Getters ve Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }

    public int getPageCount() { return pageCount; }
    public void setPageCount(int pageCount) { this.pageCount = pageCount; }

    public int getPublicationYear() { return publicationYear; }
    public void setPublicationYear(int publicationYear) { this.publicationYear = publicationYear; }

}
