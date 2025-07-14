//package com.mycompany.aplikasi_pemesanan.ui;
//
//import com.mycompany.aplikasi_pemesanan.dao.ProdukDAO;
//import com.mycompany.aplikasi_pemesanan.model.Produk;
//
//import javax.swing.*;
//import java.awt.*;
//import java.util.List;
//
//public class ProdukUI extends JFrame {
//
//    public ProdukUI() {
//        setTitle("Manajemen Produk");
//        setSize(1000, 600);
//        setLocationRelativeTo(null);
//        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        setResizable(false);
//
//        JPanel mainPanel = new JPanel(new BorderLayout());
//
//        // Sidebar
//        JPanel sidebar = new JPanel(new GridLayout(0, 1, 0, 10));
//        sidebar.setBackground(new Color(240, 240, 240));
//        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
//
//        JButton btnHome = new JButton("🏠 Home");
//        JButton btnProduk = new JButton("📦 Produk");
//        JButton btnPesanan = new JButton("📋 Pesanan");
//        JButton btnPembayaran = new JButton("💳 Pembayaran");
//        JButton btnLogout = new JButton("🚪 Logout");
//
//        sidebar.add(btnHome);
//        sidebar.add(btnProduk);
//        sidebar.add(btnPesanan);
//        sidebar.add(btnPembayaran);
//        sidebar.add(Box.createVerticalStrut(30));
//        sidebar.add(btnLogout);
//
//        mainPanel.add(sidebar, BorderLayout.WEST);
//
//        // Konten Tabel Produk
//        JPanel contentPanel = new JPanel(new BorderLayout());
//
//        List<Produk> produkList = ProdukDAO.getAllProduk();
//        String[] kolom = {"ID", "Nama Produk", "Harga", "Jenis", "Stok", "Foto"};
//        Object[][] data = new Object[produkList.size()][6];
//        for (int i = 0; i < produkList.size(); i++) {
//            Produk p = produkList.get(i);
//            data[i][0] = p.getId();
//            data[i][1] = p.getNama();
//            data[i][2] = p.getHarga();
//            data[i][3] = p.getJenis();
//            data[i][4] = p.getStok();
//            data[i][5] = p.getFoto();
//        }
//
//        JTable table = new JTable(data, kolom);
//        JScrollPane scrollPane = new JScrollPane(table);
//        contentPanel.add(scrollPane, BorderLayout.CENTER);
//
//        mainPanel.add(contentPanel, BorderLayout.CENTER);
//
//        // Event Sidebar (Sesuai Kebutuhan)
//        btnHome.addActionListener(e -> {
//            dispose();
//            new AdminMenuUI().setVisible(true);
//        });
//
//        btnLogout.addActionListener(e -> System.exit(0));
//
//        add(mainPanel);
//    }
//
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> new ProdukUI().setVisible(true));
//    }
//}
