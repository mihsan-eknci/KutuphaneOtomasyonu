module com.example.kutupproje2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.kutupproje2 to javafx.fxml;
    exports com.example.kutupproje2;
}