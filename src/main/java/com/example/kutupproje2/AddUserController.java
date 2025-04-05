package com.example.kutupproje2;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddUserController extends User{

    @FXML
    private TextField firstNameField, lastNameField, ageField; // Kullanıcı bilgileri için metin alanları

    @FXML
    private Button saveButton, backButton; // Kaydet ve geri butonları

    // Kaydet butonuna tıklanınca çalışır
    @FXML
    private void onSaveButtonClicked() {
        // Kullanıcıdan alınan verileri metin alanlarından al
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String ageText = ageField.getText();

        // Alanların boş olup olmadığını kontrol et
        if (firstName.isEmpty() || lastName.isEmpty() || ageText.isEmpty()) {
            showErrorAlert("Lütfen tüm alanları doldurun!");
            return;
        }

        int age;
        try {
            // Yaş alanını sayıya çevir ve pozitif bir değer olup olmadığını kontrol et
            age = Integer.parseInt(ageText);
            if (age <= 0) {
                showErrorAlert("Yaş 0 veya negatif olamaz!");
                return;
            }
        } catch (NumberFormatException e) {
            // Sayıya çevrilemezse hata mesajı göster
            showErrorAlert("Yaş bir sayı olmalıdır!");
            return;
        }

        // Kullanıcı bilgilerini veritabanına eklemek için SQL sorgusu
        String query = "INSERT INTO users (firstName, lastName, age) VALUES (?, ?, ?)";
        try (var conn = DatabaseConnection.connect(); // Veritabanı bağlantısını başlat
             var stmt = conn.prepareStatement(query)) { // Sorguyu hazırla

            // Sorguya kullanıcı bilgilerini ekle
            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setInt(3, age);

            stmt.executeUpdate(); // Sorguyu çalıştır
            showSuccessAlert("Kullanıcı başarıyla eklendi!"); // Başarı mesajı göster

            // Pencereyi kapat
            Stage stage = (Stage) saveButton.getScene().getWindow();
            stage.close();

        } catch (Exception e) {
            // Bir hata oluşursa hata mesajı göster
            showErrorAlert("Kullanıcı eklenirken bir hata oluştu!");
            e.printStackTrace();
        }
    }

    // Geri butonuna tıklanınca çalışır
    @FXML
    private void onBackButtonClicked() {
        // Mevcut pencereyi kapat
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
    }

    // Başarı mesajlarını göstermek için yardımcı metot
    private void showSuccessAlert(String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION); // Bilgilendirme türünde uyarı
        alert.setTitle("Başarılı"); // Başlık
        alert.setHeaderText(null); // Başlık detayı yok
        alert.setContentText(content); // Mesaj içeriği
        alert.showAndWait(); // Mesajı göster ve bekle
    }

    // Hata mesajlarını göstermek için yardımcı metot
    private void showErrorAlert(String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR); // Hata türünde uyarı
        alert.setTitle("Hata"); // Başlık
        alert.setHeaderText(null); // Başlık detayı yok
        alert.setContentText(content); // Mesaj içeriği
        alert.showAndWait(); // Mesajı göster ve bekle
    }
}
