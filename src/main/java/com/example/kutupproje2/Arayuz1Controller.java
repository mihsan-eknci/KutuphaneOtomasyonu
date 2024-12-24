package com.example.kutupproje2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class Arayuz1Controller {


    // TableView ve TableColumns
    @FXML
    private TableView<Book> tableviewKitap;

    @FXML
    private TableColumn<Book, Integer> columnBookID;
    @FXML
    private TableColumn<Book, String> columnBookName;
    @FXML
    private TableColumn<Book, String> columnAuthor;
    @FXML
    private TableColumn<Book, String> columnPublisher;
    @FXML
    private TableColumn<Book, Integer> columnPageCount;
    @FXML
    private TableColumn<Book, Integer> columnPublicationYear;

    // TextField bileşenleri (Yan panel)
    @FXML
    private TextField textfieldBookID;
    @FXML
    private TextField textfieldBookName;
    @FXML
    private TextField textfieldAuthor;
    @FXML
    private TextField textfieldPublisher;
    @FXML
    private TextField textfieldPageCount;
    @FXML
    private TextField textfieldPublicationYear;

    // Arama çubuğu ve butonlar
    @FXML
    private TextField textfieldKitapArama;

    @FXML
    private Button buttonListele;
    @FXML
    private Button buttonKitaplariListele;
    @FXML
    private Button buttonKitapGuncelle;
    @FXML
    private Button buttonKitapSil;
    @FXML
    private Button buttonKitapEkle;
    @FXML
    private Button buttonAnaSayfa;
    @FXML
    private Button buttonGeri;

    @FXML
    private Label labelKitapBllgileri;

    // Başlangıç işlemleri
    @FXML
    public void initialize() {
        System.out.println("Arayuz1Controller başlatıldı!");
        configureTableColumns();
    }


    @FXML
    private void onSearchButtonClicked() {
        String keyword = textfieldKitapArama.getText().trim();

        if (keyword.isEmpty()) {
            loadBooksData();
            return;
        }

        ObservableList<Book> filteredBooks = FXCollections.observableArrayList();
        String query = "SELECT * FROM books WHERE name LIKE ? OR author LIKE ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, "%" + keyword + "%");
            stmt.setString(2, "%" + keyword + "%");

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                filteredBooks.add(new Book(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("author"),
                        rs.getString("publisher"),
                        rs.getInt("pageCount"),
                        rs.getInt("publicationYear")
                ));
            }

            tableviewKitap.setItems(filteredBooks);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onListAllBooksClicked(ActionEvent event) {
        loadBooksData(); // Tüm kitapları yeniden yükle
    }


    @FXML
    private void onUpdateBookClicked(ActionEvent event) {
        // Tablodan seçili kitabı al
        Book selectedBook = tableviewKitap.getSelectionModel().getSelectedItem();

        if (selectedBook == null) {
            showAlert("Uyarı", "Lütfen güncellemek için bir kitap seçin.");
            return;
        }

        // Kullanıcıdan alınan bilgileri doğrula
        try {
            String name = textfieldBookName.getText();
            String author = textfieldAuthor.getText();
            String publisher = textfieldPublisher.getText();
            int pageCount = Integer.parseInt(textfieldPageCount.getText());
            int publicationYear = Integer.parseInt(textfieldPublicationYear.getText());

            // SQL Güncelleme Sorgusu
            String query = "UPDATE books SET name = ?, author = ?, publisher = ?, pageCount = ?, publicationYear = ? WHERE id = ?";
            try (Connection conn = DatabaseConnection.connect();
                 PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setString(1, name);
                stmt.setString(2, author);
                stmt.setString(3, publisher);
                stmt.setInt(4, pageCount);
                stmt.setInt(5, publicationYear);
                stmt.setInt(6, selectedBook.getId());

                stmt.executeUpdate();
                loadBooksData(); // Tabloyu yeniden yükle
                showAlert("Başarılı", "Kitap başarıyla güncellendi!");

            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Hata", "Kitap güncellenirken bir hata oluştu.");
            }

        } catch (NumberFormatException e) {
            showAlert("Hata", "Sayfa sayısı ve basım yılı sayı olmalıdır.");
        }
    }



    @FXML
    private void onDeleteBookClicked(ActionEvent event) {
        // Tablodan seçili kitabı al
        Book selectedBook = tableviewKitap.getSelectionModel().getSelectedItem();

        if (selectedBook == null) {
            showAlert("Uyarı", "Lütfen silmek için bir kitap seçin.");
            return;
        }

        // Kullanıcı onayı iste
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Onay");
        confirmationAlert.setHeaderText("Kitabı silmek istediğinizden emin misiniz?");
        confirmationAlert.setContentText("Silinen kitap geri alınamaz.");
        if (confirmationAlert.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {
            return;
        }

        // SQL Silme Sorgusu
        String query = "DELETE FROM books WHERE id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, selectedBook.getId());
            stmt.executeUpdate();
            loadBooksData(); // Tabloyu yeniden yükle
            showAlert("Başarılı", "Kitap başarıyla silindi!");

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Hata", "Kitap silinirken bir hata oluştu.");
        }
    }

    @FXML
    private void onAddBookClicked(ActionEvent event) {
        try {
            // FXML dosyasını yükle
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/kutupproje2/arayuz_2.fxml"));
            Scene scene = new Scene(loader.load());

            // Yeni bir sahne oluştur
            Stage stage = new Stage();
            stage.setTitle("Yeni Kitap Ekle");
            stage.setScene(scene);
            stage.setResizable(false); // Kullanıcının boyut değiştirmesini engelle
            stage.showAndWait(); // Bu pencere açıkken ana ekran kilitlenir

            // Yeni kitap eklendikten sonra tabloyu güncelle
            loadBooksData();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }





    // Sütunları ayarlama
    private void configureTableColumns() {
        columnBookID.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnBookName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        columnPublisher.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        columnPageCount.setCellValueFactory(new PropertyValueFactory<>("pageCount"));
        columnPublicationYear.setCellValueFactory(new PropertyValueFactory<>("publicationYear"));
    }


    private void loadBooksData() {
        ObservableList<Book> books = FXCollections.observableArrayList();
        String query = "SELECT * FROM books";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                books.add(new Book(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("author"),
                        rs.getString("publisher"),
                        rs.getInt("pageCount"),
                        rs.getInt("publicationYear")
                ));
            }

            tableviewKitap.setItems(books);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}


