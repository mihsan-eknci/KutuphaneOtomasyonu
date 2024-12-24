package com.example.kutupproje2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class Arayuz2Controller {

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

    @FXML
    private Button buttonAddBook;
    @FXML
    private Button buttonHome;
    @FXML
    private Button buttonBack;

    private void onAddBookClicked(ActionEvent event) {
        try {
            String name = textfieldBookName.getText();
            String author = textfieldAuthor.getText();
            String publisher = textfieldPublisher.getText();
            int pageCount = Integer.parseInt(textfieldPageCount.getText());
            int publicationYear = Integer.parseInt(textfieldPublicationYear.getText());

            // SQL sorgusu
            String query = "INSERT INTO books (name, author, publisher, pageCount, publicationYear) VALUES (?, ?, ?, ?, ?)";
            try (Connection conn = DatabaseConnection.connect();
                 PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setString(1, name);
                stmt.setString(2, author);
                stmt.setString(3, publisher);
                stmt.setInt(4, pageCount);
                stmt.setInt(5, publicationYear);

                stmt.executeUpdate();
                System.out.println("Kitap başarıyla eklendi!");

                // Bu pencereyi kapat
                buttonAddBook.getScene().getWindow().hide();

            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (NumberFormatException e) {
            System.out.println("Sayfa sayısı ve basım yılı için geçerli bir sayı girin.");
        }
    }

    @FXML
    private void onHomeClicked(ActionEvent event) {
        // Ana sayfaya dönme işlemleri (örneğin, başka bir ekran yükleme)
        System.out.println("Ana sayfaya dönülüyor...");
    }

    @FXML
    private void onBackClicked(ActionEvent event) {
        // Bu ekranı kapat
        buttonBack.getScene().getWindow().hide();
    }


}
