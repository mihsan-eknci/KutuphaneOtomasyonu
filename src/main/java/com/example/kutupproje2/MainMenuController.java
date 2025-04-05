package com.example.kutupproje2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

public class MainMenuController {

    @FXML
    private void handleTopLeftClick(MouseEvent event) {
        // Kitap İşlemleri ekranına geçiş
        HelloApplication.zoomAndFadeTransition("/com/example/kutupproje2/book-view.fxml");
    }

    @FXML
    private void handleTopRightClick(MouseEvent event) {
        // Kullanıcı İşlemleri ekranına geçiş
        HelloApplication.zoomAndFadeTransition("/com/example/kutupproje2/hello-view.fxml");
    }


    @FXML
    private void handleBottomLeftClick(MouseEvent event) {
        // Ödünç Ver ekranına geçiş (Henüz uygulanmayacak)
        HelloApplication.zoomAndFadeTransition("/com/example/kutupproje2/loan-view.fxml");
    }

    @FXML
    private void handleBottomRightClick(MouseEvent event) {
        // İade Al ekranına geçiş (Henüz uygulanmayacak)
        HelloApplication.zoomAndFadeTransition("/com/example/kutupproje2/return-view.fxml");
    }

    @FXML
    private void goToPage1(MouseEvent event) {
        // Ana ekrana geri dönme işlevi
        HelloApplication.zoomAndFadeTransition("/com/example/kutupproje2/login-view.fxml");
    }
}
