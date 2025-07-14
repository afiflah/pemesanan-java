package com.mycompany.aplikasi_pemesanan.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/aplikasi_pemesanan";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Pastikan driver tersedia
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver tidak ditemukan!", e);
        }

        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }

    // Tambahkan ini untuk test koneksi
    public static void main(String[] args) {
        try (Connection conn = getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("Koneksi ke database BERHASIL!");
            } else {
                System.out.println("Koneksi GAGAL.");
            }
        } catch (SQLException e) {
            System.out.println("Terjadi error koneksi: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
