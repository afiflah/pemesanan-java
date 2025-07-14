package com.mycompany.aplikasi_pemesanan.dao;

import com.mycompany.aplikasi_pemesanan.model.Produk;
import com.mycompany.aplikasi_pemesanan.utils.DBConnection;
import java.math.BigDecimal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdukDAO {

    // Ambil produk terbaru berdasarkan jenis (limit)
    public static List<Produk> getProdukTerbaru(String jenis, int limit) {
        List<Produk> list = new ArrayList<>();
        String sql = "SELECT * FROM produk WHERE jenis = ? ORDER BY id_produk DESC LIMIT ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, jenis);
            stmt.setInt(2, limit);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Produk p = new Produk(
                    rs.getInt("id_produk"),
                    rs.getString("nama_produk"),
                    rs.getDouble("harga"),
                    rs.getString("jenis"),
                    rs.getInt("stok"),
                    rs.getString("foto")
                );
                list.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // Ambil semua produk berdasarkan jenis
    public static List<Produk> getAllProdukByJenis(String jenis) {
        List<Produk> list = new ArrayList<>();
        String sql = "SELECT * FROM produk WHERE jenis = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, jenis);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Produk p = new Produk(
                    rs.getInt("id_produk"),
                    rs.getString("nama_produk"),
                    rs.getDouble("harga"),
                    rs.getString("jenis"),
                    rs.getInt("stok"),
                    rs.getString("foto")
                );
                list.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public static List<Produk> getAllProduk() {
        List<Produk> list = new ArrayList<>();
        String sql = "SELECT * FROM produk ORDER BY id_produk DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Produk p = new Produk(
                    rs.getInt("id_produk"),
                    rs.getString("nama_produk"),
                    rs.getDouble("harga"),
                    rs.getString("jenis"),
                    rs.getInt("stok"),
                    rs.getString("foto")
                );
                list.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    
    public static boolean tambahProduk(Produk produk) {
        String sql = "INSERT INTO produk (nama_produk, harga, jenis, stok, foto) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setString(1, produk.getNamaProduk());
            preparedStatement.setBigDecimal(2, BigDecimal.valueOf(produk.getHarga()));
            preparedStatement.setString(3, produk.getJenis());
            preparedStatement.setInt(4, produk.getStok());
            preparedStatement.setString(5, produk.getFoto());

            preparedStatement.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean hapusProduk(int idProduk) {
        String sql = "DELETE FROM produk WHERE id_produk = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idProduk);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static Produk getProdukById(int id) {
        try (var conn = DBConnection.getConnection()) {
            var stmt = conn.prepareStatement("SELECT * FROM produk WHERE id_produk = ?");
            stmt.setInt(1, id);
            var rs = stmt.executeQuery();
            if (rs.next()) {
                return new Produk(
                    rs.getInt("id_produk"),
                    rs.getString("nama_produk"),
                    rs.getDouble("harga"),
                    rs.getString("jenis"),
                    rs.getInt("stok"),
                    rs.getString("foto")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static boolean updateProduk(Produk produk) {
        try (var conn = DBConnection.getConnection()) {
            var stmt = conn.prepareStatement("""
                UPDATE produk 
                SET nama_produk = ?, harga = ?, jenis = ?, stok = ?, foto = ? 
                WHERE id_produk = ?
            """);
            stmt.setString(1, produk.getNamaProduk());
            stmt.setDouble(2, produk.getHarga());
            stmt.setString(3, produk.getJenis());
            stmt.setInt(4, produk.getStok());
            stmt.setString(5, produk.getFoto());
            stmt.setInt(6, produk.getIdProduk());
                        return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}
