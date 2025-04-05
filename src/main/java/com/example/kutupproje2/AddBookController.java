package com.example.kutupproje2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class AddBookController extends Book{

    @FXML
    private TextField textfieldBookName; // Kitap adı için metin alanı
    @FXML
    private TextField textfieldAuthor; // Yazar adı için metin alanı
    @FXML
    private TextField textfieldPublisher; // Yayıncı adı için metin alanı
    @FXML
    private TextField textfieldPublicationYear; // Yayın yılı için metin alanı
    @FXML
    private TextField textfieldPageCount; // Sayfa sayısı için metin alanı
    @FXML
    private TextField textfieldGenre; // Türü için metin alanı
    @FXML
    private Button addButton; // Kitap ekleme işlemini başlatan buton

    private static final int WINDOW_WIDTH = 800; // Pencere genişliği
    private static final int WINDOW_HEIGHT = 600; // Pencere yüksekliği

    @FXML
    private void onAddBookButtonClick() {
        // Kitap ekleme sorgusu
        String query = "INSERT INTO books (name, author, publisher, publicationYear, pageCount, genre) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.connect(); // Veritabanına bağlan
             PreparedStatement stmt = conn.prepareStatement(query)) { // Sorguyu hazırla

            // TextField değerlerini al ve sorguya ekle
            stmt.setString(1, textfieldBookName.getText());
            stmt.setString(2, textfieldAuthor.getText());
            stmt.setString(3, textfieldPublisher.getText());
            stmt.setInt(4, Integer.parseInt(textfieldPublicationYear.getText())); // Yayın yılı int olarak
            stmt.setInt(5, Integer.parseInt(textfieldPageCount.getText())); // Sayfa sayısı int olarak
            stmt.setString(6, textfieldGenre.getText());

            stmt.executeUpdate(); // Sorguyu çalıştır

            System.out.println("Kitap başarıyla eklendi!"); // Başarılı mesajı

            // Pencereyi kapat
            Stage stage = (Stage) addButton.getScene().getWindow();
            stage.close();

        } catch (Exception e) {
            e.printStackTrace(); // Hata durumunda detaylı bilgi yazdır
        }
    }

    public void openWindow(String fxmlPath, String title) {
        // Yeni bir pencere açmak için kullanılan genel metot
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath)); // FXML dosyasını yükle
            Parent root = loader.load(); // Parent kök elemanı al

            Stage stage = new Stage(); // Yeni bir sahne oluştur
            stage.setTitle(title); // Pencere başlığını ayarla

            Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT); // Pencere boyutlarını ayarla
            stage.setScene(scene); // Sahneyi pencereye ata
            stage.show(); // Pencereyi göster
        } catch (IOException e) {
            e.printStackTrace(); // Hata durumunda detaylı bilgi yazdır
        }
    }
}
