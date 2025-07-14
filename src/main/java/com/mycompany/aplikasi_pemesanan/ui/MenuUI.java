package com.mycompany.aplikasi_pemesanan.ui;

import com.mycompany.aplikasi_pemesanan.dao.PesananDAO;
import com.mycompany.aplikasi_pemesanan.dao.ProdukDAO;
import com.mycompany.aplikasi_pemesanan.model.KeranjangItem;
import com.mycompany.aplikasi_pemesanan.model.Produk;
import com.mycompany.aplikasi_pemesanan.utils.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MenuUI extends JFrame {

    private JPanel mainPanel;
    private JPanel panelAtas;
    private JPanel panelBawah;
    private String namaPelanggan, nomorMeja, jenisLayanan;
    private int idPelanggan, idPesanan;
    private List<KeranjangItem> keranjang = new ArrayList<>();

    public MenuUI(String namaPelanggan, String meja, String layanan, int idPelanggan, int idPesanan) {
        this.namaPelanggan = namaPelanggan;
        this.nomorMeja = meja;
        this.jenisLayanan = layanan;
        this.idPelanggan = idPelanggan;
        this.idPesanan = idPesanan;

        setTitle("Menu Pemesanan");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.white);

        buatPanelAtas();
        buatPanelBawah();

        tampilkanBeranda();

        add(mainPanel);
    }

    // Panel Atas (Header)
    private void buatPanelAtas() {
        panelAtas = new JPanel();
        panelAtas.setLayout(new BoxLayout(panelAtas, BoxLayout.Y_AXIS));
        panelAtas.setBackground(Color.white);

        JPanel barisAtas = new JPanel(new BorderLayout());
        barisAtas.setBackground(Color.white);

        JLabel lblLogo = new JLabel("🍽️");
        lblLogo.setFont(new Font("Arial", Font.PLAIN, 28));
        barisAtas.add(lblLogo, BorderLayout.WEST);

        JPanel panelTombol = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelTombol.setBackground(Color.white);

        JButton btnRiwayat = new JButton("📜 Riwayat");
        btnRiwayat.addActionListener(e -> tampilkanRiwayat());
        panelTombol.add(btnRiwayat);

        JButton btnBayar = new JButton("💰 Bayar Sekarang");
        btnBayar.addActionListener(e -> prosesPembayaran());
        panelTombol.add(btnBayar);

        JButton btnKeranjang = new JButton("🛒 Keranjang");
        btnKeranjang.addActionListener(e -> tampilkanKeranjang());
        panelTombol.add(btnKeranjang);

        barisAtas.add(panelTombol, BorderLayout.EAST);

        JLabel welcomeLabel = new JLabel("Selamat Datang, " + namaPelanggan, JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        panelAtas.add(barisAtas);
        panelAtas.add(welcomeLabel);
    }

    // Panel Bawah (Logout)
    private void buatPanelBawah() {
        panelBawah = new JPanel(new BorderLayout());
        panelBawah.setBackground(Color.white);

        JButton btnLogout = new JButton("⎋ Logout");
        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin keluar?", "Konfirmasi Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        JPanel kiri = new JPanel(new FlowLayout(FlowLayout.LEFT));
        kiri.setBackground(Color.white);
        kiri.add(btnLogout);

        panelBawah.add(kiri, BorderLayout.WEST);
    }

    // Ganti Konten Tengah
    private void gantiKontenTengah(JComponent komponen) {
        mainPanel.removeAll();
        mainPanel.add(panelAtas, BorderLayout.NORTH);
        mainPanel.add(panelBawah, BorderLayout.SOUTH);
        mainPanel.add(komponen, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    // ==================== Tampilan ====================

    private void tampilkanBeranda() {
        JPanel contentPanel = new JPanel(new GridLayout(2, 1, 20, 20));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPanel.setBackground(Color.white);

        contentPanel.add(createProdukPanel("makanan"));
        contentPanel.add(createProdukPanel("minuman"));

        gantiKontenTengah(contentPanel);
    }

    private JPanel createProdukPanel(String jenis) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.white);

        JLabel title = new JLabel(jenis.toUpperCase());
        title.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(title, BorderLayout.NORTH);

        JPanel listPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        listPanel.setBackground(Color.white);

        List<Produk> produkList = ProdukDAO.getProdukTerbaru(jenis, 3);
        for (Produk p : produkList) {
            JPanel card = createProdukCard(p);
            listPanel.add(card);
        }

        JButton btnLihatSemua = new JButton("➤");
        btnLihatSemua.setToolTipText("Lihat semua " + jenis);
        btnLihatSemua.addActionListener(e -> tampilkanSemuaProduk(jenis));

        panel.add(listPanel, BorderLayout.CENTER);
        panel.add(btnLihatSemua, BorderLayout.EAST);

        return panel;
    }

    private JPanel createProdukCard(Produk produk) {
        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(180, 100));
        card.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        card.setLayout(new BorderLayout());

        JLabel lblNama = new JLabel(produk.getNamaProduk(), JLabel.CENTER);
        lblNama.setFont(new Font("Arial", Font.BOLD, 14));
        JLabel lblHarga = new JLabel("Rp " + produk.getHarga(), JLabel.CENTER);

        JButton btnPesan = new JButton("Pesan");
        btnPesan.addActionListener(e -> tampilkanPopupPesan(produk));

        card.add(lblNama, BorderLayout.NORTH);
        card.add(lblHarga, BorderLayout.CENTER);
        card.add(btnPesan, BorderLayout.SOUTH);

        return card;
    }

    private void tampilkanPopupPesan(Produk produk) {
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        JTextField txtCatatan = new JTextField();
        JSpinner spinnerJumlah = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));

        panel.add(new JLabel("Produk:"));
        panel.add(new JLabel(produk.getNamaProduk()));
        panel.add(new JLabel("Catatan:"));
        panel.add(txtCatatan);
        panel.add(new JLabel("Jumlah:"));
        panel.add(spinnerJumlah);

        int result = JOptionPane.showConfirmDialog(this, panel, "Tambah ke Keranjang", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            keranjang.add(new KeranjangItem(produk, (Integer) spinnerJumlah.getValue(), txtCatatan.getText()));
            JOptionPane.showMessageDialog(this, "Produk '" + produk.getNamaProduk() + "' ditambahkan ke keranjang.");
        }
    }

    private void tampilkanSemuaProduk(String jenis) {
        List<Produk> semuaProduk = ProdukDAO.getProdukTerbaru(jenis, 100);
        JPanel listPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        listPanel.setBackground(Color.white);

        for (Produk p : semuaProduk) {
            listPanel.add(createProdukCard(p));
        }

        JPanel panelAtas = new JPanel(new BorderLayout());
        panelAtas.setBackground(Color.white);

        JButton btnKembali = new JButton("← Kembali");
        btnKembali.addActionListener(e -> tampilkanBeranda());
        JLabel title = new JLabel("Semua " + jenis.toUpperCase(), JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));

        panelAtas.add(btnKembali, BorderLayout.WEST);
        panelAtas.add(title, BorderLayout.CENTER);

        JPanel panelScroll = new JPanel(new BorderLayout());
        panelScroll.add(panelAtas, BorderLayout.NORTH);
        panelScroll.add(new JScrollPane(listPanel), BorderLayout.CENTER);

        gantiKontenTengah(panelScroll);
    }

    // ==================== Fitur Keranjang & Riwayat ====================

    private void tampilkanKeranjang() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] column = {"Produk", "Jumlah", "Harga", "Catatan", "Subtotal"};
        Object[][] data = new Object[keranjang.size()][5];

        double total = 0;
        for (int i = 0; i < keranjang.size(); i++) {
            KeranjangItem item = keranjang.get(i);
            data[i][0] = item.getNamaProduk();
            data[i][1] = item.getJumlah();
            data[i][2] = item.getHarga();
            data[i][3] = item.getCatatan();
            data[i][4] = item.getSubtotal();
            total += item.getSubtotal();
        }

        JTable table = new JTable(data, column);
        JScrollPane scroll = new JScrollPane(table);

        JLabel lblTotal = new JLabel(String.format("Total Harga: Rp %.2f", total));
        lblTotal.setFont(new Font("Arial", Font.BOLD, 16));
        lblTotal.setHorizontalAlignment(SwingConstants.RIGHT);

        JButton btnHapus = new JButton("🗑 Hapus Item");
        btnHapus.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                keranjang.remove(selectedRow);
                tampilkanKeranjang();
            }
        });

        JButton btnSimpan = new JButton("Simpan & Checkout");
        btnSimpan.addActionListener(e -> simpanKeDatabase());

        JButton btnKembali = new JButton("← Kembali");
        btnKembali.addActionListener(e -> tampilkanBeranda());

        JPanel bawah = new JPanel(new BorderLayout());
        bawah.add(lblTotal, BorderLayout.NORTH);
        bawah.add(btnKembali, BorderLayout.WEST);
        bawah.add(btnHapus, BorderLayout.CENTER);
        bawah.add(btnSimpan, BorderLayout.EAST);

        panel.add(new JLabel("Keranjang Sementara", JLabel.CENTER), BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(bawah, BorderLayout.SOUTH);

        gantiKontenTengah(panel);
    }

    private void tampilkanRiwayat() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] column = {"Tanggal", "Jenis", "Meja", "Produk", "Jumlah", "Catatan"};
        List<Object[]> dataList = new ArrayList<>();

        try (var conn = DBConnection.getConnection()) {
            var stmt = conn.prepareStatement("""
                SELECT p.waktu_pesan, p.jenis_pesan, p.nomor_meja,
                       pr.nama_produk, dp.quantity, '' AS catatan
                FROM pesanan p
                JOIN detail_pesanan dp ON p.id_pesanan = dp.id_pesanan
                JOIN produk pr ON dp.id_produk = pr.id_produk
                WHERE p.id_pelanggan = ?
                ORDER BY p.waktu_pesan DESC
            """);
            stmt.setInt(1, idPelanggan);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                dataList.add(new Object[]{
                    rs.getTimestamp("waktu_pesan"),
                    rs.getString("jenis_pesan"),
                    rs.getString("nomor_meja"),
                    rs.getString("nama_produk"),
                    rs.getInt("quantity"),
                    rs.getString("catatan")
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal mengambil riwayat.");
            return;
        }

        JTable table = new JTable(dataList.toArray(new Object[0][]), column);
        JScrollPane scroll = new JScrollPane(table);

        JButton btnKembali = new JButton("← Kembali");
        btnKembali.addActionListener(e -> tampilkanBeranda());

        JPanel atas = new JPanel(new BorderLayout());
        atas.add(btnKembali, BorderLayout.WEST);
        atas.add(new JLabel("Riwayat Pesanan", JLabel.CENTER), BorderLayout.CENTER);

        panel.add(atas, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

        gantiKontenTengah(panel);
    }

    private void simpanKeDatabase() {
        if (keranjang.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Keranjang masih kosong!");
            return;
        }

        try (var conn = DBConnection.getConnection()) {
            int idPesanan = -1;

            // Cek apakah ada pesanan aktif yang belum dibayar
            var stmtCek = conn.prepareStatement("""
                SELECT id_pesanan FROM pesanan
                WHERE id_pelanggan = ? AND status_bayar = 'belum'
                ORDER BY id_pesanan DESC LIMIT 1
            """);
            stmtCek.setInt(1, idPelanggan);
            ResultSet rsCek = stmtCek.executeQuery();

            if (rsCek.next()) {
                idPesanan = rsCek.getInt("id_pesanan");
            } else {
                // Kalau tidak ada, buat pesanan baru
                idPesanan = PesananDAO.simpanPesananAwal(
                    idPelanggan,
                    jenisLayanan,
                    nomorMeja,
                    "tunai",
                    ""
                );
            }

            if (idPesanan != -1) {
                for (KeranjangItem item : keranjang) {
                    PesananDAO.simpanDetailPesanan(
                        idPesanan,
                        item.getIdProduk(),
                        item.getJumlah(),
                        item.getHarga()
                    );
                }

                JOptionPane.showMessageDialog(this, "Pesanan berhasil disimpan!");
                keranjang.clear();
                tampilkanBeranda();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menyimpan pesanan.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal menyimpan pesanan.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    private void prosesPembayaran() {
        try (var conn = DBConnection.getConnection()) {
            // Ambil ID pesanan aktif
            var stmtId = conn.prepareStatement("""
                SELECT id_pesanan FROM pesanan 
                WHERE id_pelanggan = ? ORDER BY id_pesanan DESC LIMIT 1
            """);
            stmtId.setInt(1, idPelanggan);
            ResultSet rs = stmtId.executeQuery();

            int idPesanan = -1;
            if (rs.next()) idPesanan = rs.getInt("id_pesanan");
            if (idPesanan == -1) {
                JOptionPane.showMessageDialog(this, "Tidak ada pesanan yang bisa dibayar.");
                return;
            }

            // Hitung total dari detail_pesanan
            var stmtTotal = conn.prepareStatement("""
                SELECT SUM(dp.quantity * p.harga) AS total 
                FROM detail_pesanan dp 
                JOIN produk p ON dp.id_produk = p.id_produk 
                WHERE dp.id_pesanan = ?
            """);
            stmtTotal.setInt(1, idPesanan);
            ResultSet rsTotal = stmtTotal.executeQuery();
            int total = 0;
            if (rsTotal.next()) total = rsTotal.getInt("total");

            // Panel konfirmasi pembayaran
            String[] metode = {"Tunai", "QRIS", "Transfer"};
            JComboBox<String> cbMetode = new JComboBox<>(metode);

            JPanel panel = new JPanel(new GridLayout(3, 1));
            panel.add(new JLabel("Total Tagihan: Rp " + total));
            panel.add(new JLabel("Metode Pembayaran:"));
            panel.add(cbMetode);

            int result = JOptionPane.showConfirmDialog(this, panel, "Pilih Metode Pembayaran", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String metodeBayar = cbMetode.getSelectedItem().toString();

                // ✅ HANYA simpan metode bayar, status bayar tetap "belum"
                var stmtUpdate = conn.prepareStatement("""
                    UPDATE pesanan SET metode_bayar = ? WHERE id_pesanan = ?
                """);
                stmtUpdate.setString(1, metodeBayar);
                stmtUpdate.setInt(2, idPesanan);
                stmtUpdate.executeUpdate();

                JOptionPane.showMessageDialog(this, "Metode pembayaran disimpan.\nSilakan lakukan pembayaran di kasir.");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal memproses pembayaran.");
        }
    }
}
