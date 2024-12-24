package com.example.kutupproje2;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddUserController {
    @FXML
    private TextField firstNameField, lastNameField, ageField;

    @FXML
    private Button saveButton, backButton;

    @FXML
    private void onSaveButtonClicked() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String ageText = ageField.getText();

        // Form doğrulama
        if (firstName.isEmpty() || lastName.isEmpty() || ageText.isEmpty()) {
            showErrorAlert("Lütfen tüm alanları doldurun!");
            return;
        }

        int age;
        try {
            age = Integer.parseInt(ageText);
            if (age <= 0) {
                showErrorAlert("Yaş 0 veya negatif olamaz!");
                return;
            }
        } catch (NumberFormatException e) {
            showErrorAlert("Yaş bir sayı olmalıdır!");
            return;
        }

        // Kullanıcıyı veritabanına ekle
        String query = "INSERT INTO users (firstName, lastName, age) VALUES (?, ?, ?)";
        try (var conn = DatabaseConnection.connect();
             var stmt = conn.prepareStatement(query)) {

            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setInt(3, age);

            stmt.executeUpdate();
            showSuccessAlert("Kullanıcı başarıyla eklendi!");

            // Sahneyi kapat
            Stage stage = (Stage) saveButton.getScene().getWindow();
            stage.close();

        } catch (Exception e) {
            showErrorAlert("Kullanıcı eklenirken bir hata oluştu!");
            e.printStackTrace();
        }
    }

    @FXML
    private void onBackButtonClicked() {
        // Sahneyi kapat
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
    }

    private void showSuccessAlert(String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Başarılı");
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showErrorAlert(String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Hata");
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
