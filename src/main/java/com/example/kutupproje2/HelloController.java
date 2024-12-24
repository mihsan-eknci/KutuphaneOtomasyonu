package com.example.kutupproje2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HelloController {
    @FXML
    private TableView<User> tableView;

    @FXML
    private TableColumn<User, Integer> columnID;

    @FXML
    private TableColumn<User, String> columnFirstName;

    @FXML
    private TableColumn<User, String> columnLastName;

    @FXML
    private TableColumn<User, Integer> columnAge;

    @FXML
    private TextField firstNameField, lastNameField, ageField, idField;

    @FXML
    private Button updateButton, deleteButton, addButton;

    private ObservableList<User> userList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // TableView sütunlarını model sınıfı ile bağlama
        columnID.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        columnLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        columnAge.setCellValueFactory(new PropertyValueFactory<>("age"));

        // Veritabanından veri yükleme
        loadUserData();

        // TableView'deki seçili satırdaki verileri TextField'lere doldurma
        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                fillUserDetails(newValue);
            }
        });
    }

    private void loadUserData() {
        userList.clear();
        String query = "SELECT * FROM users";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                userList.add(new User(
                        rs.getInt("id"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getInt("age")
                ));
            }

            tableView.setItems(userList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void fillUserDetails(User user) {
        firstNameField.setText(user.getFirstName());
        lastNameField.setText(user.getLastName());
        ageField.setText(String.valueOf(user.getAge()));
        idField.setText(String.valueOf(user.getId()));
    }

    @FXML
    private void onUpdateButtonClicked() {
        User selectedUser = tableView.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            String query = "UPDATE users SET firstName = ?, lastName = ?, age = ? WHERE id = ?";

            try (Connection conn = DatabaseConnection.connect();
                 PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setString(1, firstNameField.getText());
                stmt.setString(2, lastNameField.getText());
                stmt.setInt(3, Integer.parseInt(ageField.getText()));
                stmt.setInt(4, selectedUser.getId());

                stmt.executeUpdate();
                loadUserData(); // Verileri yeniden yükle

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void onDeleteButtonClicked() {
        User selectedUser = tableView.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            String query = "DELETE FROM users WHERE id = ?";

            try (Connection conn = DatabaseConnection.connect();
                 PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setInt(1, selectedUser.getId());
                stmt.executeUpdate();
                loadUserData(); // Verileri yeniden yükle

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void onAddButtonClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/kutupproje2/add-user.fxml"));
            Scene scene = new Scene(loader.load(), 336, 317); // Genişlik: 500, Yükseklik: 400

            // CSS ekleme
            scene.getStylesheets().add(getClass().getResource("/com/example/kutupproje2/style.css").toExternalForm());

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Yeni Kullanıcı Ekle");
            stage.setResizable(false); // Kullanıcı tarafından boyut değiştirmeyi kapat
            stage.showAndWait();

            // Yeni kullanıcı eklendikten sonra tabloyu güncelle
            loadUserData();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}