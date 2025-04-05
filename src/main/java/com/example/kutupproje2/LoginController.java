package com.example.kutupproje2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginController {

    @FXML
    private TextField emailInput;

    @FXML
    private PasswordField passwordInput;

    @FXML
    private void onSignInClicked(ActionEvent event) {
        String email = emailInput.getText().trim();
        String password = passwordInput.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Uyarı", "Lütfen email ve şifre giriniz.");
            return;
        }

        String query = "SELECT * FROM login_users WHERE email = ? AND password = ? AND role = 'admin'";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                showAlert("Başarılı", "Giriş başarılı! Ana menüye yönlendiriliyorsunuz.");
                HelloApplication.zoomAndFadeTransition("/com/example/kutupproje2/main-menu.fxml");
            } else {
                showAlert("Hata", "Geçersiz kullanıcı adı veya şifre.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Hata", "Bir hata oluştu. Lütfen tekrar deneyin.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
