package com.example.kutupproje2;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.Connection;

public class HelloApplication extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        // Veritabanı bağlantısını kontrol et
        Connection connection = DatabaseConnection.connect();
        if (connection != null) {
            System.out.println("Veritabanı bağlantısı başarılı!");
        } else {
            System.out.println("Veritabanı bağlantısı başarısız!");
        }

        // Ana sahneyi yükle
        primaryStage = stage;
        loadScene("login-view.fxml", 1100, 700); // Başlangıçta giriş ekranı
        primaryStage.setTitle("Kütüphane Otomasyonu");
        primaryStage.setResizable(false);
        primaryStage.show();
    }
    public static void loadScene(String fxmlFile, double width, double height) {
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource(fxmlFile));
            Parent root = loader.load();
            Scene scene = new Scene(root, width, height);

            // Pencere boyutlarını sabitleyin
            primaryStage.setScene(scene);
            primaryStage.setWidth(width);
            primaryStage.setHeight(height);
            primaryStage.setResizable(false); // Kullanıcı yeniden boyutlandırmayı engellemek için
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void zoomAndFadeTransition(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource(fxmlFile));
            Parent newRoot = loader.load();

            double width = 1100; // Sabit genişlik
            double height = 700; // Sabit yükseklik

            Scene newScene = new Scene(newRoot, width, height);

            // Animasyonlar
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(800), newRoot);
            scaleTransition.setFromX(0.5);
            scaleTransition.setFromY(0.5);
            scaleTransition.setToX(1);
            scaleTransition.setToY(1);

            FadeTransition fadeTransition = new FadeTransition(Duration.millis(800), newRoot);
            fadeTransition.setFromValue(0);
            fadeTransition.setToValue(1);

            ParallelTransition parallelTransition = new ParallelTransition(scaleTransition, fadeTransition);

            // Yeni sahneyi uygula ve pencere boyutlarını sabitle
            primaryStage.setScene(newScene);
            primaryStage.setWidth(width);
            primaryStage.setHeight(height);
            primaryStage.setResizable(false); // Kullanıcı yeniden boyutlandırmayı engellemek için
            parallelTransition.play();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
