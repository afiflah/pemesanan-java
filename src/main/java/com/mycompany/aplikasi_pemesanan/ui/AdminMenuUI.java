package com.mycompany.aplikasi_pemesanan.ui;

import com.mycompany.aplikasi_pemesanan.dao.DashboardDAO;
import com.mycompany.aplikasi_pemesanan.dao.PesananDAO;
import com.mycompany.aplikasi_pemesanan.dao.ProdukDAO;
import com.mycompany.aplikasi_pemesanan.model.Produk;
import javax.swing.*;
import java.awt.*;
import java.util.Vector;
import java.util.List;

public class AdminMenuUI extends JFrame {
    private JPanel mainPanel;
    private JPanel contentPanel;
    private JTable table; 
    private JTable tableProduk;  // Tambahkan ini
    private JButton btnTambah, btnHapus, btnEdit;
 
            
    public AdminMenuUI() {
        setTitle("Dashboard Admin - ");
        setSize(1000, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        mainPanel = new JPanel(new BorderLayout());

        // Sidebar
        JPanel sidebar = new JPanel(new GridLayout(0, 1, 0, 10));
        sidebar.setBackground(new Color(240, 240, 240));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JButton btnHome = new JButton("🏠 Home");
        JButton btnProduk = new JButton("📦 Produk");
        JButton btnPesanan = new JButton("📋 Pesanan");
        JButton btnLogout = new JButton("🚪 Logout");

        sidebar.add(btnHome);
        sidebar.add(btnProduk);
        sidebar.add(btnPesanan);
        sidebar.add(Box.createVerticalStrut(30));
        sidebar.add(btnLogout);

        mainPanel.add(sidebar, BorderLayout.WEST);

        // Panel tengah untuk konten halaman
        contentPanel = new JPanel(new BorderLayout());
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        // Event tombol sidebar
        btnHome.addActionListener(e -> tampilkanDashboard());
        btnProduk.addActionListener(e -> tampilkanProduk());
        btnPesanan.addActionListener(e -> tampilkanPesanan());
        btnLogout.addActionListener(e -> System.exit(0));

        add(mainPanel);
        tampilkanDashboard(); // Tampilkan dashboard pertama kali
    }

    private void tampilkanDashboard() {
        contentPanel.removeAll();

        JPanel dashboard = new JPanel(new BorderLayout(10, 10));

        int totalPelanggan = DashboardDAO.getTotalPelanggan();
        int totalPesanan = DashboardDAO.getTotalPesanan();
        int totalBelumBayar = DashboardDAO.getTotalBelumBayar();
        double pendapatanHariIni = DashboardDAO.getPendapatanHariIni();

        // Search Bar
        JPanel searchPanel = new JPanel(new BorderLayout());
        JTextField txtSearch = new JTextField();
        JButton btnSearch = new JButton("Cari");
        searchPanel.add(txtSearch, BorderLayout.CENTER);
        searchPanel.add(btnSearch, BorderLayout.EAST);
        dashboard.add(searchPanel, BorderLayout.NORTH);

        // Card Ringkasan
        JPanel cardsPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        cardsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        cardsPanel.add(createCard("👥 Total Pengunjung", String.valueOf(totalPelanggan)));
        cardsPanel.add(createCard("📦 Total Pesanan", String.valueOf(totalPesanan)));
        cardsPanel.add(createCard("⏳ Pesanan Belum Dibuat", String.valueOf(totalBelumBayar)));
        cardsPanel.add(createCard("💰 Pendapatan Hari Ini", "Rp " + pendapatanHariIni));
        dashboard.add(cardsPanel, BorderLayout.CENTER);
               
        // Panel Tengah (Isi Cards + Chart)
        JPanel middlePanel = new JPanel(new BorderLayout());
        middlePanel.add(cardsPanel, BorderLayout.NORTH);
        dashboard.add(middlePanel, BorderLayout.CENTER);

        // Tabel Pembeli Terakhir
        List<Object[]> pembeli = DashboardDAO.getPembeliTerakhir();
        Object[][] data = new Object[pembeli.size()][4];
        for (int i = 0; i < pembeli.size(); i++) {
            data[i] = pembeli.get(i);
        }

        String[] columns = {"No", "Nama", "Item Dibeli", "Total Harga"};
        JTable table = new JTable(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("5 Pembeli Terakhir"));
        dashboard.add(scrollPane, BorderLayout.SOUTH);

        contentPanel.add(dashboard, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // Method untuk bikin Card Statistik
    private JPanel createCard(String title, String value) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        card.setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel(title, JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel lblValue = new JLabel(value, JLabel.CENTER);
        lblValue.setFont(new Font("Arial", Font.BOLD, 20));
        lblValue.setForeground(Color.decode("#1976d2"));

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(lblValue, BorderLayout.CENTER);
        return card;
    }

    private void tampilkanProduk() {
        contentPanel.removeAll();

        JPanel produkPanel = new JPanel(new BorderLayout());

        // Cek tombol, kalau belum ada baru buat
        if (btnTambah == null) {
            btnTambah = new JButton("Tambah Produk");
            btnHapus = new JButton("Hapus Produk Terpilih");
            btnEdit = new JButton("Edit Produk");

            btnTambah.addActionListener(e -> tampilkanDialogTambahProduk());
            btnHapus.addActionListener(e -> hapusProduk());
            btnEdit.addActionListener(e -> editProduk());
        }

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(btnTambah);
        topPanel.add(btnHapus);
        topPanel.add(btnEdit);
        produkPanel.add(topPanel, BorderLayout.NORTH);

        List<Produk> produkList = ProdukDAO.getAllProduk();
        String[] kolom = {"ID", "Nama Produk", "Harga", "Jenis", "Stok", "Foto"};
        Object[][] data = new Object[produkList.size()][6];
        for (int i = 0; i < produkList.size(); i++) {
            Produk p = produkList.get(i);
            data[i][0] = p.getIdProduk();
            data[i][1] = p.getNamaProduk();
            data[i][2] = p.getHarga();
            data[i][3] = p.getJenis();
            data[i][4] = p.getStok();
            data[i][5] = p.getFoto();
        }

        tableProduk = new JTable(data, kolom);
        JScrollPane scrollPane = new JScrollPane(tableProduk);
        produkPanel.add(scrollPane, BorderLayout.CENTER);

        contentPanel.add(produkPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // Method Hapus & Edit dipisah biar rapi
    private void hapusProduk() {
        int selectedRow = tableProduk.getSelectedRow();
        if (selectedRow >= 0) {
            int idProduk = (int) tableProduk.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Hapus produk ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (ProdukDAO.hapusProduk(idProduk)) {
                    JOptionPane.showMessageDialog(this, "Produk berhasil dihapus!");
                    tampilkanProduk();
                } else {
                    JOptionPane.showMessageDialog(this, "Gagal menghapus produk.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Pilih produk dulu!");
        }
    }

    private void editProduk() {
        int selectedRow = tableProduk.getSelectedRow();
        if (selectedRow >= 0) {
            int idProduk = (int) tableProduk.getValueAt(selectedRow, 0);
            Produk produk = ProdukDAO.getProdukById(idProduk);
            tampilkanDialogEditProduk(produk);
        } else {
            JOptionPane.showMessageDialog(this, "Pilih produk yang mau diedit!");
        }
    }


    private void tampilkanPesanan() {
        contentPanel.removeAll();

        JPanel pesananPanel = new JPanel(new BorderLayout());

        List<Object[]> pesananList = PesananDAO.getAllPesanan();
        String[] kolom = {"ID", "Nama", "Jenis", "Meja", "Waktu", "Status", "Bayar", "Total"};
        Object[][] data = new Object[pesananList.size()][8];
        for (int i = 0; i < pesananList.size(); i++) {
            data[i] = pesananList.get(i);
        }

        JTable tablePesanan = new JTable(data, kolom);
        JScrollPane scrollPane = new JScrollPane(tablePesanan);
        pesananPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel panelAksi = new JPanel();
        JButton btnProses = new JButton("Proses");
        JButton btnLihatDetail = new JButton("Lihat Detail");
        JButton btnEdit = new JButton("Edit");
        JButton btnHapus = new JButton("Hapus");
        JButton btnKonfirmasiBayar = new JButton("Konfirmasi Bayar");

        // Tombol Proses
        btnProses.addActionListener(e -> {
            int row = tablePesanan.getSelectedRow();
            if (row >= 0) {
                int id = (int) tablePesanan.getValueAt(row, 0);
                PesananDAO.updateStatusPesanan(id, "diproses");
                tampilkanPesanan();
            }
        });

        // Tombol Lihat Detail + Cetak Struk
        btnLihatDetail.addActionListener(e -> {
            int row = tablePesanan.getSelectedRow();
            if (row >= 0) {
                int id = (int) tablePesanan.getValueAt(row, 0);
                List<Object[]> detail = PesananDAO.getDetailPesanan(id);
                StringBuilder sb = new StringBuilder();
                sb.append("===== STRUK PEMESANAN =====\n");
                sb.append("ID Pesanan: ").append(id).append("\n");
                sb.append("Nama: ").append(tablePesanan.getValueAt(row, 1)).append("\n");
                sb.append("Jenis: ").append(tablePesanan.getValueAt(row, 2)).append("\n");
                sb.append("---------------------------\n");
                for (Object[] d : detail) {
                    sb.append("Produk: ").append(d[0])
                      .append("\nQty: ").append(d[1])
                      .append("\nHarga: ").append(d[2])
                      .append("\n---------------------------\n");
                }
                sb.append("Total: ").append(tablePesanan.getValueAt(row, 7)).append("\n");
                sb.append("===========================\n");

                JTextArea textArea = new JTextArea(sb.toString());
                textArea.setEditable(false);
                JScrollPane scroll = new JScrollPane(textArea);

                JButton btnPrint = new JButton("🖨️ Print Struk");
                btnPrint.addActionListener(ev -> {
                    try {
                        textArea.print();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });

                JPanel panel = new JPanel(new BorderLayout());
                panel.add(scroll, BorderLayout.CENTER);
                panel.add(btnPrint, BorderLayout.SOUTH);

                JDialog dialog = new JDialog(this, "Struk Pesanan", true);
                dialog.setSize(400, 500);
                dialog.setLocationRelativeTo(this);
                dialog.add(panel);
                dialog.setVisible(true);
            }
        });

        // Tombol Edit Status & Bayar
        btnEdit.addActionListener(e -> {
            int row = tablePesanan.getSelectedRow();
            if (row >= 0) {
                int id = (int) tablePesanan.getValueAt(row, 0);
                String status = (String) tablePesanan.getValueAt(row, 5);
                String bayar = (String) tablePesanan.getValueAt(row, 6);

                JComboBox<String> cbStatus = new JComboBox<>(new String[]{"pending", "diproses", "selesai"});
                cbStatus.setSelectedItem(status);

                JComboBox<String> cbBayar = new JComboBox<>(new String[]{"belum", "sudah"});
                cbBayar.setSelectedItem(bayar);

                JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
                panel.add(new JLabel("Status Pesanan:"));
                panel.add(cbStatus);
                panel.add(new JLabel("Status Bayar:"));
                panel.add(cbBayar);

                int result = JOptionPane.showConfirmDialog(this, panel, "Edit Pesanan", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    PesananDAO.updateStatusPesanan(id, cbStatus.getSelectedItem().toString());
                    PesananDAO.updateStatusBayar(id, cbBayar.getSelectedItem().toString());
                    tampilkanPesanan();
                }
            }
        });

        // Tombol Hapus Pesanan
        btnHapus.addActionListener(e -> {
            int row = tablePesanan.getSelectedRow();
            if (row >= 0) {
                int id = (int) tablePesanan.getValueAt(row, 0);
                int confirm = JOptionPane.showConfirmDialog(this, "Hapus pesanan ini?");
                if (confirm == JOptionPane.YES_OPTION) {
                    PesananDAO.hapusPesanan(id);
                    tampilkanPesanan();
                }
            }
        });
        
        btnKonfirmasiBayar.addActionListener(e -> {
        int row = tablePesanan.getSelectedRow();
        if (row >= 0) {
            int id = (int) tablePesanan.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Konfirmasi pembayaran untuk pesanan ini?");
            if (confirm == JOptionPane.YES_OPTION) {
                if (PesananDAO.updateStatusBayar(id, "sudah")) {
                    JOptionPane.showMessageDialog(this, "Pembayaran dikonfirmasi.");
                    tampilkanPesanan();
                } else {
                    JOptionPane.showMessageDialog(this, "Gagal mengupdate status bayar.");
                }
            }
        }
    });

        panelAksi.add(btnProses);
        panelAksi.add(btnLihatDetail);
        panelAksi.add(btnEdit);
        panelAksi.add(btnHapus);
        panelAksi.add(btnKonfirmasiBayar);

        pesananPanel.add(panelAksi, BorderLayout.SOUTH);

        contentPanel.add(pesananPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminMenuUI().setVisible(true));
    }

    private void tampilkanDialogTambahProduk() {
        JDialog dialog = new JDialog(this, "Tambah Produk Baru", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblNama = new JLabel("Nama Produk:");
        JLabel lblHarga = new JLabel("Harga:");
        JLabel lblJenis = new JLabel("Jenis:");
        JLabel lblStok = new JLabel("Stok:");
        JLabel lblFoto = new JLabel("Link Foto:");

        JTextField txtNama = new JTextField(15);
        JTextField txtHarga = new JTextField(10);
        JComboBox<String> cbJenis = new JComboBox<>(new String[]{"makanan", "minuman"});
        JTextField txtStok = new JTextField(5);
        JTextField txtFoto = new JTextField(15);
        JButton btnBrowse = new JButton("Pilih File");

        btnBrowse.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int option = fileChooser.showOpenDialog(dialog);
            if (option == JFileChooser.APPROVE_OPTION) {
                txtFoto.setText(fileChooser.getSelectedFile().getAbsolutePath());
            }
        });

        // Letakkan tombol Browse di samping text field
        JPanel panelFoto = new JPanel(new BorderLayout());
        panelFoto.add(txtFoto, BorderLayout.CENTER);
        panelFoto.add(btnBrowse, BorderLayout.EAST);

        JButton btnSimpan = new JButton("Simpan");
        JButton btnBatal = new JButton("Batal");

        gbc.gridx = 0; gbc.gridy = 0; dialog.add(lblNama, gbc);
        gbc.gridx = 1; dialog.add(txtNama, gbc);
        gbc.gridx = 0; gbc.gridy = 1; dialog.add(lblHarga, gbc);
        gbc.gridx = 1; dialog.add(txtHarga, gbc);
        gbc.gridx = 0; gbc.gridy = 2; dialog.add(lblJenis, gbc);
        gbc.gridx = 1; dialog.add(cbJenis, gbc);
        gbc.gridx = 0; gbc.gridy = 3; dialog.add(lblStok, gbc);
        gbc.gridx = 1; dialog.add(txtStok, gbc);
        gbc.gridx = 0; gbc.gridy = 4; dialog.add(lblFoto, gbc);
        gbc.gridx = 1; dialog.add(panelFoto, gbc);
        gbc.gridx = 0; gbc.gridy = 5; dialog.add(btnSimpan, gbc);
        gbc.gridx = 1; dialog.add(btnBatal, gbc);

        btnSimpan.addActionListener(e -> {
            try {
                String nama = txtNama.getText();
                double harga = Double.parseDouble(txtHarga.getText());
                String jenis = (String) cbJenis.getSelectedItem();
                int stok = Integer.parseInt(txtStok.getText());
                String foto = txtFoto.getText();

                if (nama.isEmpty() || foto.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Isi semua data!");
                    return;
                }

                Produk produkBaru = new Produk(0, nama, harga, jenis, stok, foto);
                if (ProdukDAO.tambahProduk(produkBaru)) {
                    JOptionPane.showMessageDialog(dialog, "Produk berhasil ditambahkan!");
                    dialog.dispose();
                    tampilkanProduk(); // Refresh table
                } else {
                    JOptionPane.showMessageDialog(dialog, "Gagal menambahkan produk.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Harga & stok harus angka.");
            }
        });

        btnBatal.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }
    
    private void tampilkanDialogEditProduk(Produk produk) {
        JDialog dialog = new JDialog(this, "Edit Produk", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblNama = new JLabel("Nama Produk:");
        JLabel lblHarga = new JLabel("Harga:");
        JLabel lblJenis = new JLabel("Jenis:");
        JLabel lblStok = new JLabel("Stok:");
        JLabel lblFoto = new JLabel("Link Foto:");

        JTextField txtNama = new JTextField(produk.getNamaProduk(), 15);
        JTextField txtHarga = new JTextField(String.valueOf(produk.getHarga()), 10);
        JComboBox<String> cbJenis = new JComboBox<>(new String[]{"makanan", "minuman"});
        cbJenis.setSelectedItem(produk.getJenis());
        JTextField txtStok = new JTextField(String.valueOf(produk.getStok()), 5);
        JTextField txtFoto = new JTextField(produk.getFoto(), 15);
        JButton btnBrowse = new JButton("Pilih File");

        btnBrowse.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int option = fileChooser.showOpenDialog(dialog);
            if (option == JFileChooser.APPROVE_OPTION) {
                txtFoto.setText(fileChooser.getSelectedFile().getAbsolutePath());
            }
        });

        JPanel panelFoto = new JPanel(new BorderLayout());
        panelFoto.add(txtFoto, BorderLayout.CENTER);
        panelFoto.add(btnBrowse, BorderLayout.EAST);

        gbc.gridx = 0; gbc.gridy = 0; dialog.add(lblNama, gbc);
        gbc.gridx = 1; dialog.add(txtNama, gbc);
        gbc.gridx = 0; gbc.gridy = 1; dialog.add(lblHarga, gbc);
        gbc.gridx = 1; dialog.add(txtHarga, gbc);
        gbc.gridx = 0; gbc.gridy = 2; dialog.add(lblJenis, gbc);
        gbc.gridx = 1; dialog.add(cbJenis, gbc);
        gbc.gridx = 0; gbc.gridy = 3; dialog.add(lblStok, gbc);
        gbc.gridx = 1; dialog.add(txtStok, gbc);
        gbc.gridx = 0; gbc.gridy = 4; dialog.add(lblFoto, gbc);
        gbc.gridx = 1; dialog.add(panelFoto, gbc);

        JButton btnSimpan = new JButton("Simpan");
        JButton btnBatal = new JButton("Batal");
        gbc.gridx = 0; gbc.gridy = 5; dialog.add(btnSimpan, gbc);
        gbc.gridx = 1; dialog.add(btnBatal, gbc);

        btnSimpan.addActionListener(e -> {
            try {
                produk.setNamaProduk(txtNama.getText());
                produk.setHarga(Double.parseDouble(txtHarga.getText()));
                produk.setJenis((String) cbJenis.getSelectedItem());
                produk.setStok(Integer.parseInt(txtStok.getText()));
                produk.setFoto(txtFoto.getText());

                if (ProdukDAO.updateProduk(produk)) {
                    JOptionPane.showMessageDialog(dialog, "Produk berhasil diperbarui!");
                    dialog.dispose();
                    tampilkanProduk(); // Refresh tabel
                } else {
                    JOptionPane.showMessageDialog(dialog, "Gagal memperbarui produk.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Harga & stok harus angka.");
            }
        });

        btnBatal.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }


}

