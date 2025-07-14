//package com.mycompany.aplikasi_pemesanan.ui;
//
//public class KeranjangUI extends JFrame {
//
//    public KeranjangUI(int idPelanggan) {
//        setTitle("Keranjang Pemesanan");
//        setSize(400, 300);
//        setLocationRelativeTo(null);
//        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
//
//        JPanel panel = new JPanel(new BorderLayout());
//        DefaultListModel<String> model = new DefaultListModel<>();
//        int total = 0;
//
//        for (KeranjangItem item : Keranjang.getItems()) {
//            String row = item.getProduk().getNama() + " x" + item.getJumlah() + " = Rp" + item.getTotalHarga();
//            model.addElement(row);
//            total += item.getTotalHarga();
//        }
//
//        JList<String> list = new JList<>(model);
//        panel.add(new JScrollPane(list), BorderLayout.CENTER);
//
//        JLabel totalLabel = new JLabel("Total: Rp" + total);
//        panel.add(totalLabel, BorderLayout.SOUTH);
//
//        JButton btnSimpan = new JButton("Simpan & Checkout");
//        btnSimpan.addActionListener(e -> {
//            // Simpan ke DB di sini
//            // INSERT INTO detail_pesanan
//            JOptionPane.showMessageDialog(this, "Pesanan disimpan ke database!");
//            Keranjang.clear();
//            dispose();
//        });
//
//        panel.add(btnSimpan, BorderLayout.NORTH);
//
//        add(panel);
//        setVisible(true);
//    }
//}
