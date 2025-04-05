package com.example.kutupproje2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

// BookController sınıfı kitap işlemleri için gerekli metotları içerir
public class BookController extends Book {

    // FXML bileşenleri, kitaplarla ilgili tablo ve sütunlar
    @FXML
    private TableView<Book> tableviewKitap; // Kitapların listelendiği tablo
    @FXML
    private TableColumn<Book, Integer> columnBookID; // Kitap ID'sini temsil eden sütun
    @FXML
    private TableColumn<Book, String> columnBookName; // Kitap adını temsil eden sütun
    @FXML
    private TableColumn<Book, String> columnAuthor; // Yazar adını temsil eden sütun
    @FXML
    private TableColumn<Book, String> columnPublisher; // Yayıncıyı temsil eden sütun
    @FXML
    private TableColumn<Book, Integer> columnPageCount; // Sayfa sayısını temsil eden sütun
    @FXML
    private TableColumn<Book, Integer> columnPublicationYear; // Yayın yılını temsil eden sütun
    @FXML
    private TableColumn<Book, String> columnGenre; // Türü temsil eden sütun
    @FXML
    private TableColumn<Book, Integer> columnStock; // Stok miktarını temsil eden sütun

    // Kullanıcı giriş alanları
    @FXML
    private TextField textfieldBookName; // Kitap adı
    @FXML
    private TextField textfieldAuthor; // Yazar adı
    @FXML
    private TextField textfieldPublisher; // Yayıncı
    @FXML
    private TextField textfieldPageCount; // Sayfa sayısı
    @FXML
    private TextField textfieldPublicationYear; // Yayın yılı
    @FXML
    private TextField textfieldGenre; // Tür
    @FXML
    private TextField textfieldStock; // Stok miktarı

    @FXML
    private Button backButton; // Geri butonu

    private ObservableList<Book> bookList = FXCollections.observableArrayList(); // Kitapların listesi

    @FXML
    public void initialize() {
        configureTableColumns(); // Tablo sütunlarını yapılandırır
        loadBooksData(); // Veritabanından kitapları yükler

        // Tablo satırına tıklandığında, seçilen kitabın detaylarını gösterir
        tableviewKitap.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                showBookDetails(newValue);
            }
        });
    }

    // Tablo sütunlarını ilgili model özelliklerine bağlar
    private void configureTableColumns() {
        columnBookID.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnBookName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        columnPublisher.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        columnPageCount.setCellValueFactory(new PropertyValueFactory<>("pageCount"));
        columnPublicationYear.setCellValueFactory(new PropertyValueFactory<>("publicationYear"));
        columnGenre.setCellValueFactory(new PropertyValueFactory<>("genre"));
        columnStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
    }

    // Veritabanından kitapları yükler ve tabloya ekler
    private void loadBooksData() {
        bookList.clear();
        String query = "SELECT * FROM books";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                bookList.add(new Book(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("author"),
                        rs.getString("publisher"),
                        rs.getInt("publicationYear"),
                        rs.getInt("pageCount"),
                        rs.getString("genre"),
                        rs.getInt("stock")
                ));
            }

            tableviewKitap.setItems(bookList); // Tabloya verileri bağlar

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Seçilen kitabın detaylarını giriş alanlarına doldurur
    private void showBookDetails(Book book) {
        textfieldBookName.setText(book.getName());
        textfieldAuthor.setText(book.getAuthor());
        textfieldPublisher.setText(book.getPublisher());
        textfieldPageCount.setText(String.valueOf(book.getPageCount()));
        textfieldPublicationYear.setText(String.valueOf(book.getPublicationYear()));
        textfieldGenre.setText(book.getGenre());
        textfieldStock.setText(String.valueOf(book.getStock()));
    }

    // Yeni bir kitap eklemek için kullanılan metot
    @FXML
    private void onAddButtonClick() {
        String query = "INSERT INTO books (name, author, publisher, publicationYear, pageCount, genre, stock) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, textfieldBookName.getText());
            stmt.setString(2, textfieldAuthor.getText());
            stmt.setString(3, textfieldPublisher.getText());
            stmt.setInt(4, Integer.parseInt(textfieldPublicationYear.getText()));
            stmt.setInt(5, Integer.parseInt(textfieldPageCount.getText()));
            stmt.setString(6, textfieldGenre.getText());
            stmt.setInt(7, Integer.parseInt(textfieldStock.getText()));

            stmt.executeUpdate();
            loadBooksData(); // Kitap listesini günceller

            showAlert("Başarılı", "Kitap başarıyla eklendi!"); // Başarı mesajı göster

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Hata", "Kitap eklenirken bir hata oluştu."); // Hata mesajı göster
        }
    }

    // Mevcut bir kitabı güncellemek için kullanılan metot
    @FXML
    private void onUpdateButtonClick() {
        Book selectedBook = tableviewKitap.getSelectionModel().getSelectedItem();

        if (selectedBook != null) {
            String query = "UPDATE books SET name = ?, author = ?, publisher = ?, publicationYear = ?, pageCount = ?, genre = ?, stock = ? WHERE id = ?";

            try (Connection conn = DatabaseConnection.connect();
                 PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setString(1, textfieldBookName.getText());
                stmt.setString(2, textfieldAuthor.getText());
                stmt.setString(3, textfieldPublisher.getText());
                stmt.setInt(4, Integer.parseInt(textfieldPublicationYear.getText()));
                stmt.setInt(5, Integer.parseInt(textfieldPageCount.getText()));
                stmt.setString(6, textfieldGenre.getText());
                stmt.setInt(7, Integer.parseInt(textfieldStock.getText()));
                stmt.setInt(8, selectedBook.getId());

                stmt.executeUpdate();
                loadBooksData(); // Kitap listesini günceller

                showAlert("Başarılı", "Kitap başarıyla güncellendi!"); // Başarı mesajı göster

            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Hata", "Kitap güncellenirken bir hata oluştu."); // Hata mesajı göster
            }
        }
    }

    // Seçilen kitabı silmek için kullanılan metot
    @FXML
    private void onDeleteButtonClick() {
        Book selectedBook = tableviewKitap.getSelectionModel().getSelectedItem();

        if (selectedBook != null) {
            String query = "DELETE FROM books WHERE id = ?";

            try (Connection conn = DatabaseConnection.connect();
                 PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setInt(1, selectedBook.getId());
                stmt.executeUpdate();
                loadBooksData(); // Kitap listesini günceller

                showAlert("Başarılı", "Kitap başarıyla silindi!"); // Başarı mesajı göster

            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Hata", "Kitap silinirken bir hata oluştu."); // Hata mesajı göster
            }
        }
    }

    // Giriş alanlarını temizler
    @FXML
    private void onClearButtonClick() {
        textfieldBookName.clear();
        textfieldAuthor.clear();
        textfieldPublisher.clear();
        textfieldPageCount.clear();
        textfieldPublicationYear.clear();
        textfieldGenre.clear();
        textfieldStock.clear();
    }

    // Ana menüye geri döner
    @FXML
    private void onBackButtonClick() {
        HelloApplication.zoomAndFadeTransition("/com/example/kutupproje2/main-menu.fxml");
    }

    // Bilgilendirme veya hata mesajı göstermek için kullanılan yardımcı metot
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
