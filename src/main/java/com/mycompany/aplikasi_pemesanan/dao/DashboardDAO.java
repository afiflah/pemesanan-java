package com.mycompany.aplikasi_pemesanan.dao;

import com.mycompany.aplikasi_pemesanan.utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DashboardDAO {

    public static int getTotalPelanggan() {
        try (Connection conn = DBConnection.getConnection()) {
            var stmt = conn.prepareStatement("""
                SELECT COUNT(DISTINCT id_pelanggan) AS total FROM pesanan
            """);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("total");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int getTotalPesanan() {
        try (Connection conn = DBConnection.getConnection()) {
            var stmt = conn.prepareStatement("""
                SELECT COUNT(*) AS total FROM pesanan
            """);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("total");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int getTotalBelumBayar() {
        try (Connection conn = DBConnection.getConnection()) {
            var stmt = conn.prepareStatement("""
                SELECT COUNT(*) AS total FROM pesanan WHERE status_bayar = 'belum'
            """);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("total");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static double getPendapatanHariIni() {
        try (Connection conn = DBConnection.getConnection()) {
            var stmt = conn.prepareStatement("""
                SELECT COALESCE(SUM(dp.quantity * dp.harga_satuan), 0) AS total
                FROM pesanan p
                JOIN detail_pesanan dp ON p.id_pesanan = dp.id_pesanan
                WHERE DATE(p.waktu_pesan) = CURDATE() AND p.status_bayar = 'sudah'
            """);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getDouble("total");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    public static List<Object[]> getPembeliTerakhir() {
        List<Object[]> list = new ArrayList<>();
        String sql = """
                SELECT p.id_pesanan, pl.nama_pelanggan, 
                       GROUP_CONCAT(pr.nama_produk SEPARATOR ', ') AS items, 
                       SUM(dp.quantity * dp.harga_satuan) AS total_harga
                FROM pesanan p
                JOIN pelanggan pl ON p.id_pelanggan = pl.id_pelanggan
                JOIN detail_pesanan dp ON p.id_pesanan = dp.id_pesanan
                JOIN produk pr ON dp.id_produk = pr.id_produk
                GROUP BY p.id_pesanan
                ORDER BY p.waktu_pesan DESC
                LIMIT 5
                """;

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            int no = 1;
            while (rs.next()) {
                Object[] row = {
                    no++,
                    rs.getString("nama_pelanggan"),
                    rs.getString("items"),
                    "Rp " + rs.getDouble("total_harga")
                };
                list.add(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

}
