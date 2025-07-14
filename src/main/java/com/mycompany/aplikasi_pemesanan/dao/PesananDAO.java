package com.mycompany.aplikasi_pemesanan.dao;

import com.mycompany.aplikasi_pemesanan.utils.DBConnection;
import java.sql.*;
import java.util.*;

public class PesananDAO {
    
    public static List<Object[]> getAllPesanan() {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT p.id_pesanan, pl.nama_pelanggan, p.jenis_pesan, p.nomor_meja, " +
                     "p.waktu_pesan, p.status, p.status_bayar, SUM(d.quantity * d.harga_satuan) AS total " +
                     "FROM pesanan p JOIN pelanggan pl ON p.id_pelanggan = pl.id_pelanggan " +
                     "LEFT JOIN detail_pesanan d ON p.id_pesanan = d.id_pesanan " +
                     "GROUP BY p.id_pesanan";
        
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Object[] row = new Object[8];
                row[0] = rs.getInt("id_pesanan");
                row[1] = rs.getString("nama_pelanggan");
                row[2] = rs.getString("jenis_pesan");
                row[3] = rs.getString("nomor_meja");
                row[4] = rs.getTimestamp("waktu_pesan");
                row[5] = rs.getString("status");
                row[6] = rs.getString("status_bayar");
                row[7] = rs.getDouble("total");
                list.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<Object[]> getDetailPesanan(int idPesanan) {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT pr.nama_produk, d.quantity, d.harga_satuan " +
                     "FROM detail_pesanan d JOIN produk pr ON d.id_produk = pr.id_produk " +
                     "WHERE d.id_pesanan = ?";
        
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPesanan);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Object[] row = new Object[3];
                row[0] = rs.getString("nama_produk");
                row[1] = rs.getInt("quantity");
                row[2] = rs.getDouble("harga_satuan");
                list.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static boolean updateStatusPesanan(int idPesanan, String status) {
        String sql = "UPDATE pesanan SET status = ? WHERE id_pesanan = ?";
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, idPesanan);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean hapusPesanan(int idPesanan) {
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                // Hapus detail pesanan
                PreparedStatement stmtDetail = conn.prepareStatement("DELETE FROM detail_pesanan WHERE id_pesanan = ?");
                stmtDetail.setInt(1, idPesanan);
                stmtDetail.executeUpdate();
                
                // Hapus pesanan
                PreparedStatement stmtPesanan = conn.prepareStatement("DELETE FROM pesanan WHERE id_pesanan = ?");
                stmtPesanan.setInt(1, idPesanan);
                stmtPesanan.executeUpdate();

                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // 1. Simpan pesanan awal & kembalikan id_pesanan baru
    public static int simpanPesananAwal(int idPelanggan, String jenisPesan, String nomorMeja, String metodeBayar, String catatan) {
        String sql = "INSERT INTO pesanan (id_pelanggan, jenis_pesan, nomor_meja, metode_bayar, catatan) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, idPelanggan);
            stmt.setString(2, jenisPesan);
            stmt.setString(3, nomorMeja);
            stmt.setString(4, metodeBayar);
            stmt.setString(5, catatan);
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);  // id_pesanan yang baru dibuat
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;  // gagal
    }

    // 2. Simpan detail pesanan (produk-produk yang dipesan)
    public static boolean simpanDetailPesanan(int idPesanan, int idProduk, int quantity, double hargaSatuan) {
        String sql = "INSERT INTO detail_pesanan (id_pesanan, id_produk, quantity, harga_satuan) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPesanan);
            stmt.setInt(2, idProduk);
            stmt.setInt(3, quantity);
            stmt.setDouble(4, hargaSatuan);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean updateStatusBayar(int idPesanan, String statusBayar) {
        String sql = "UPDATE pesanan SET status_bayar = ? WHERE id_pesanan = ?";
        try (var conn = DBConnection.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, statusBayar);
            stmt.setInt(2, idPesanan);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}