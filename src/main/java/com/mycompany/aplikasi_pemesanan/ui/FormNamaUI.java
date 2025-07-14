package com.mycompany.aplikasi_pemesanan.ui;

import com.mycompany.aplikasi_pemesanan.utils.DBConnection;
import com.mycompany.aplikasi_pemesanan.ui.MenuUI;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.Statement;

public class FormNamaUI extends JFrame {

    private JTextField txtNama, txtMeja;
    private JComboBox<String> cbLayanan;

    public FormNamaUI() {
        setTitle("Form Nama Pelanggan");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.decode("#f9f9f9"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblNama = new JLabel("Nama:");
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(lblNama, gbc);

        txtNama = new JTextField(20);
        gbc.gridx = 1;
        panel.add(txtNama, gbc);

        JLabel lblMeja = new JLabel("Nomor Meja:");
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(lblMeja, gbc);

        txtMeja = new JTextField(20);
        gbc.gridx = 1;
        panel.add(txtMeja, gbc);

        JLabel lblLayanan = new JLabel("Layanan:");
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(lblLayanan, gbc);

        cbLayanan = new JComboBox<>(new String[]{"-- Pilih --", "ditempat", "dibawa_pulang"});
        gbc.gridx = 1;
        panel.add(cbLayanan, gbc);

        cbLayanan.addActionListener(e -> {
            boolean isDitempat = cbLayanan.getSelectedItem().equals("ditempat");
            txtMeja.setEnabled(isDitempat);
            txtMeja.setText(isDitempat ? "" : "-");
        });

        JButton btnSimpan = new JButton("Simpan");
        btnSimpan.setBackground(Color.decode("#1976d2"));
        btnSimpan.setForeground(Color.white);
        gbc.gridwidth = 2; gbc.gridx = 0; gbc.gridy = 3;
        panel.add(btnSimpan, gbc);

        btnSimpan.addActionListener(e -> simpanKeDatabase());

        add(panel);
    }

    private void simpanKeDatabase() {
        String nama = txtNama.getText().trim();
        String meja = txtMeja.getText().trim();
        String layanan = cbLayanan.getSelectedItem().toString();

        if (nama.isEmpty() || layanan.equals("-- Pilih --") || (layanan.equals("ditempat") && meja.isEmpty())) {
            JOptionPane.showMessageDialog(this, "Isi data dengan benar.");
            return;
        }

        try (var conn = DBConnection.getConnection()) {
            // Simpan pelanggan
            var stmt = conn.prepareStatement("""
                INSERT INTO pelanggan (nama_pelanggan) VALUES (?)
            """, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, nama);
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            int idPelanggan = -1;
            if (rs.next()) idPelanggan = rs.getInt(1);

            // Simpan pesanan
            var stmtPesan = conn.prepareStatement("""
                INSERT INTO pesanan (id_pelanggan, jenis_pesan, nomor_meja) VALUES (?, ?, ?)
            """, Statement.RETURN_GENERATED_KEYS);
            stmtPesan.setInt(1, idPelanggan);
            stmtPesan.setString(2, layanan);
            stmtPesan.setString(3, layanan.equals("ditempat") ? meja : null);
            stmtPesan.executeUpdate();

            ResultSet rsPesanan = stmtPesan.getGeneratedKeys();
            int idPesanan = -1;
            if (rsPesanan.next()) idPesanan = rsPesanan.getInt(1);

            // Berhasil → Buka MenuUI
            JOptionPane.showMessageDialog(this, "Data berhasil disimpan!");
            dispose(); // Tutup form ini
            new MenuUI(nama, meja, layanan, idPelanggan, idPesanan).setVisible(true); // ← Ini dia!

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal menyimpan data.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
