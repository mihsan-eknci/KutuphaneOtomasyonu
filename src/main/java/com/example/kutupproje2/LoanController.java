package com.example.kutupproje2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoanController extends Loan {

    @FXML
    private TableView<User> userTable;
    @FXML
    private TableColumn<User, Integer> userIdColumn;
    @FXML
    private TableColumn<User, String> userNameColumn;

    @FXML
    private TableView<Book> bookTable;
    @FXML
    private TableColumn<Book, Integer> bookIdColumn;
    @FXML
    private TableColumn<Book, String> bookNameColumn;
    @FXML
    private TableColumn<Book, Integer> bookStockColumn;

    @FXML
    private DatePicker loanDatePicker;
    @FXML
    private Button loanButton;
    @FXML
    private Button returnButton;
    @FXML
    private Button backButton;

    private ObservableList<User> users = FXCollections.observableArrayList();
    private ObservableList<Book> books = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        configureUserTable();
        configureBookTable();
        loadUsers();
        loadBooks();
    }

    private void configureUserTable() {
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        userNameColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getFirstName() + " " + cellData.getValue().getLastName()));
    }

    private void configureBookTable() {
        bookIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        bookNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        bookStockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
    }

    private void loadUsers() {
        users.clear();
        String query = "SELECT id, firstName, lastName FROM users";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                users.add(new User(
                        rs.getInt("id"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        0
                ));
            }
            userTable.setItems(users);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadBooks() {
        books.clear();
        String query = "SELECT id, name, stock FROM books";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                books.add(new Book(
                        rs.getInt("id"),
                        rs.getString("name"),
                        null, null, 0, 0, null,
                        rs.getInt("stock")
                ));
            }
            bookTable.setItems(books);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onLoanButtonClick() {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        Book selectedBook = bookTable.getSelectionModel().getSelectedItem();

        if (selectedUser == null || selectedBook == null || loanDatePicker.getValue() == null) {
            showAlert("Hata", "Lütfen bir kullanıcı, bir kitap ve bir tarih seçiniz.");
            return;
        }

        if (selectedBook.getStock() <= 0) {
            showAlert("Hata", "Bu kitap stokta yok!");
            return;
        }

        if (isBookAlreadyLoaned(selectedUser.getId(), selectedBook.getId())) {
            showAlert("Hata", "Bu kitap zaten bu kullanıcıya verilmiş!");
            return;
        }

        String query = "INSERT INTO loans (user_id, book_id, loan_date) VALUES (?, ?, ?)";
        String updateStockQuery = "UPDATE books SET stock = stock - 1 WHERE id = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement loanStmt = conn.prepareStatement(query);
             PreparedStatement stockStmt = conn.prepareStatement(updateStockQuery)) {

            loanStmt.setInt(1, selectedUser.getId());
            loanStmt.setInt(2, selectedBook.getId());
            loanStmt.setDate(3, Date.valueOf(loanDatePicker.getValue()));
            loanStmt.executeUpdate();

            stockStmt.setInt(1, selectedBook.getId());
            stockStmt.executeUpdate();

            selectedBook.setStock(selectedBook.getStock() - 1);
            bookTable.refresh();

            showAlert("Başarılı", "Kitap başarıyla ödünç verildi!");

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Hata", "Ödünç verme sırasında bir hata oluştu.");
        }
    }

    private boolean isBookAlreadyLoaned(int userId, int bookId) {
        String query = "SELECT COUNT(*) FROM loans WHERE user_id = ? AND book_id = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, bookId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return true;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @FXML
    private void onReturnButtonClick() {
        Book selectedBook = bookTable.getSelectionModel().getSelectedItem();

        if (selectedBook == null) {
            showAlert("Hata", "İade edilecek bir kitap seçiniz.");
            return;
        }

        String updateStockQuery = "UPDATE books SET stock = stock + 1 WHERE id = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stockStmt = conn.prepareStatement(updateStockQuery)) {

            stockStmt.setInt(1, selectedBook.getId());
            stockStmt.executeUpdate();

            selectedBook.setStock(selectedBook.getStock() + 1);
            bookTable.refresh();

            showAlert("Başarılı", "Kitap başarıyla iade edildi ve stok güncellendi!");

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Hata", "Kitap iadesi sırasında bir hata oluştu.");
        }
    }

    @FXML
    private void onBackButtonClick() {
        HelloApplication.zoomAndFadeTransition("/com/example/kutupproje2/main-menu.fxml");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
