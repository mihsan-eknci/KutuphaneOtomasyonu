package com.example.kutupproje2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ReturnController extends Loan implements Initialize {

    @FXML
    private TableView<Loan> loansTable;
    @FXML
    private TableColumn<Loan, String> userColumn;
    @FXML
    private TableColumn<Loan, String> bookColumn;
    @FXML
    private TableColumn<Loan, String> loanDateColumn;
    @FXML
    private DatePicker returnDatePicker;
    @FXML
    private Button returnButton;

    private ObservableList<Loan> loansList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        configureTableColumns();
        loadLoansData();
    }

    private void configureTableColumns() {
        userColumn.setCellValueFactory(cellData -> cellData.getValue().userNameProperty());
        bookColumn.setCellValueFactory(cellData -> cellData.getValue().bookNameProperty());
        loanDateColumn.setCellValueFactory(cellData -> cellData.getValue().loanDateProperty());
    }

    private void loadLoansData() {
        loansList.clear();
        String query = "SELECT loans.id, users.firstName, users.lastName, books.name, loans.loan_date " +
                "FROM loans " +
                "JOIN users ON loans.user_id = users.id " +
                "JOIN books ON loans.book_id = books.id";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String userName = rs.getString("firstName") + " " + rs.getString("lastName");
                String bookName = rs.getString("name");
                String loanDate = rs.getDate("loan_date").toString();

                loansList.add(new Loan(rs.getInt("id"), userName, bookName, loanDate));
            }
            loansTable.setItems(loansList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onReturnButtonClick() {
        Loan selectedLoan = loansTable.getSelectionModel().getSelectedItem();
        if (selectedLoan == null || returnDatePicker.getValue() == null) {
            showAlert("Hata", "Lütfen iade edilecek öğeyi ve tarihi seçiniz.");
            return;
        }

        // Ödünç alınan tarihi Loan nesnesinden çek
        LocalDate loanDate = LocalDate.parse(selectedLoan.loanDateProperty().get());
        LocalDate returnDate = returnDatePicker.getValue();

        // Gecikme günlerini hesapla
        long daysBetween = ChronoUnit.DAYS.between(loanDate, returnDate);
        if (daysBetween > 10) {
            long lateDays = daysBetween - 10; // Geciktirilen günler
            double fine = lateDays * 0.25; // Ceza hesaplama

            // Kullanıcıya bildirim göster
            showAlert("Gecikme Uyarısı", "Kitabı " + lateDays + " gün geciktirdiğiniz için " + fine + " TL borç ödemeniz gerekmektedir.");
        }

        // Veritabanından ödünç kaydını sil ve stok artır
        String deleteQuery = "DELETE FROM loans WHERE id = ?";
        String updateStockQuery = "UPDATE books SET stock = stock + 1 WHERE name = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery);
             PreparedStatement stockStmt = conn.prepareStatement(updateStockQuery)) {

            // Ödünç kaydını sil
            deleteStmt.setInt(1, selectedLoan.getId());
            deleteStmt.executeUpdate();

            // Stok bilgisini güncelle
            stockStmt.setString(1, selectedLoan.getBookName());
            stockStmt.executeUpdate();

            loansTable.getItems().remove(selectedLoan);
            showAlert("Başarılı", "Kitap başarıyla iade edildi ve stok güncellendi!");

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Hata", "İade işlemi sırasında bir hata oluştu.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void onBackButtonClick() {
        HelloApplication.zoomAndFadeTransition("/com/example/kutupproje2/main-menu.fxml");
    }
}
