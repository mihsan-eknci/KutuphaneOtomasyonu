package com.example.kutupproje2;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

// Book sınıfı, kitap bilgilerini temsil eden bir model sınıfıdır
public class Book {

    // Kitap özelliklerini temsil eden property değişkenleri
    private IntegerProperty id; // Kitap ID'si
    private StringProperty name; // Kitap adı
    private StringProperty author; // Yazar adı
    private StringProperty publisher; // Yayıncı
    private IntegerProperty publicationYear; // Yayın yılı
    private IntegerProperty pageCount; // Sayfa sayısı
    private StringProperty genre; // Tür
    private IntegerProperty stock; // Stok miktarı

    public Book(){};
    // Yapıcı metot: Yeni bir Book nesnesi oluşturur ve tüm özellikleri alır
    public Book(int id, String name, String author, String publisher, int publicationYear, int pageCount, String genre, int stock) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.author = new SimpleStringProperty(author);
        this.publisher = new SimpleStringProperty(publisher);
        this.publicationYear = new SimpleIntegerProperty(publicationYear);
        this.pageCount = new SimpleIntegerProperty(pageCount);
        this.genre = new SimpleStringProperty(genre);
        this.stock = new SimpleIntegerProperty(stock);
    }

    // ID özelliği için getter, setter ve property metotları
    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    // Kitap adı için getter, setter ve property metotları
    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty nameProperty() {
        return name;
    }

    // Yazar adı için getter, setter ve property metotları
    public String getAuthor() {
        return author.get();
    }

    public void setAuthor(String author) {
        this.author.set(author);
    }

    public StringProperty authorProperty() {
        return author;
    }

    // Yayıncı için getter, setter ve property metotları
    public String getPublisher() {
        return publisher.get();
    }

    public void setPublisher(String publisher) {
        this.publisher.set(publisher);
    }

    public StringProperty publisherProperty() {
        return publisher;
    }

    // Yayın yılı için getter, setter ve property metotları
    public int getPublicationYear() {
        return publicationYear.get();
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear.set(publicationYear);
    }

    public IntegerProperty publicationYearProperty() {
        return publicationYear;
    }

    // Sayfa sayısı için getter, setter ve property metotları
    public int getPageCount() {
        return pageCount.get();
    }

    public void setPageCount(int pageCount) {
        this.pageCount.set(pageCount);
    }

    public IntegerProperty pageCountProperty() {
        return pageCount;
    }

    // Tür (genre) için getter, setter ve property metotları
    public String getGenre() {
        return genre.get();
    }

    public void setGenre(String genre) {
        this.genre.set(genre);
    }

    public StringProperty genreProperty() {
        return genre;
    }

    // Stok miktarı için getter, setter ve property metotları
    public int getStock() {
        return stock.get();
    }

    public void setStock(int stock) {
        this.stock.set(stock);
    }

    public IntegerProperty stockProperty() {
        return stock;
    }
}
