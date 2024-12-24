package com.example.kutupproje2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;


public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Connection connection = DatabaseConnection.connect();
        if (connection != null) {
            System.out.println("Veritabanı bağlantısı başarılı!");
        } else {
            System.out.println("Veritabanı bağlantısı başarısız!");
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        Scene scene = new Scene(loader.load(), 600, 400); // Genişlik: 800, Yükseklik: 600
        stage.setScene(scene);
        stage.setTitle("Kütüphane Otomasyonu");
        stage.setResizable(false); // Kullanıcı tarafından boyut değiştirme kapalı
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}