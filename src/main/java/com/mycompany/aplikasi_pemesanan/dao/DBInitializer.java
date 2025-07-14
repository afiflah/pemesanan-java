package com.mycompany.aplikasi_pemesanan.dao;

import java.sql.*;

public class DBInitializer {

    private static final String DB_NAME = "aplikasi_pemesanan";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/";
    private static final String USER = "root";
    private static final String PASS = "";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement()) {

            // 1. Cek apakah database sudah ada
            ResultSet rs = stmt.executeQuery("SHOW DATABASES LIKE '" + DB_NAME + "'");
            if (!rs.next()) {
                System.out.println("Database belum ada, membuat...");
                stmt.executeUpdate("CREATE DATABASE " + DB_NAME);
            } else {
                System.out.println("Database sudah ada.");
            }

            // 2. Gunakan database
            stmt.execute("USE " + DB_NAME);

            // 3. Buat tabel-tabel jika belum ada
            createTables(stmt);

            System.out.println("Database dan tabel siap!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createTables(Statement stmt) throws SQLException {
        stmt.executeUpdate("""
            CREATE TABLE IF NOT EXISTS login (
                id_login INT AUTO_INCREMENT PRIMARY KEY,
                username VARCHAR(50) UNIQUE NOT NULL,
                password VARCHAR(100) NOT NULL
            )
        """);

        stmt.executeUpdate("""
            CREATE TABLE IF NOT EXISTS pelanggan (
                id_pelanggan INT AUTO_INCREMENT PRIMARY KEY,
                nama_pelanggan VARCHAR(100) NOT NULL
            )
        """);

        stmt.executeUpdate("""
            CREATE TABLE IF NOT EXISTS produk (
                id_produk INT AUTO_INCREMENT PRIMARY KEY,
                nama_produk VARCHAR(100) NOT NULL,
                harga DECIMAL(10,2) NOT NULL,
                jenis ENUM('makanan', 'minuman') NOT NULL,
                stok INT DEFAULT 0,
                foto VARCHAR(255)
            )
        """);

        stmt.executeUpdate("""
            CREATE TABLE IF NOT EXISTS pesanan (
                id_pesanan INT AUTO_INCREMENT PRIMARY KEY,
                id_pelanggan INT,
                jenis_pesan ENUM('ditempat', 'dibawa_pulang') NOT NULL,
                nomor_meja VARCHAR(10),
                waktu_pesan DATETIME DEFAULT CURRENT_TIMESTAMP,
                status ENUM('pending', 'diproses', 'selesai', 'dibatalkan') DEFAULT 'pending',
                status_bayar ENUM ('belum', 'sudah') DEFAULT 'belum',
                metode_bayar VARCHAR(20),
                catatan TEXT,
                FOREIGN KEY (id_pelanggan) REFERENCES pelanggan(id_pelanggan)
            )
        """);

        stmt.executeUpdate("""
            CREATE TABLE IF NOT EXISTS detail_pesanan (
                id_detail INT AUTO_INCREMENT PRIMARY KEY,
                id_pesanan INT,
                id_produk INT,
                quantity INT NOT NULL,
                harga_satuan DECIMAL(10,2) NOT NULL,
                FOREIGN KEY (id_pesanan) REFERENCES pesanan(id_pesanan),
                FOREIGN KEY (id_produk) REFERENCES produk(id_produk)
            )
        """);

        System.out.println("Semua tabel sudah dicek/dibuat.");
    }
}
