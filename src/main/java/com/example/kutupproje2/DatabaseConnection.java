package com.example.kutupproje2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/library_system"; // Veritabanı URL'si
    private static final String USER = "root"; // MySQL kullanıcı adı
    private static final String PASSWORD = "44700"; // MySQL şifresi (boş bırakılmıştır)

    public static Connection connect() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            return null; // Bağlantı kurulamazsa null döndür
        }
    }
}
