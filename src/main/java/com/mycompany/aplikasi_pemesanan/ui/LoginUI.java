package com.mycompany.aplikasi_pemesanan.ui;

import com.mycompany.aplikasi_pemesanan.dao.LoginLogic;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginUI extends JFrame {

    public LoginUI() {
        setTitle("Login");
        setSize(400, 250);
        setLocationRelativeTo(null); // Center di layar
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        // Panel utama
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(Color.decode("#f0f0f0")); // warna latar

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // padding

        JLabel lblTitle = new JLabel("Login");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setForeground(Color.decode("#333"));
        gbc.gridwidth = 2;
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(lblTitle, gbc);

        JLabel lblUsername = new JLabel("Username:");
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(lblUsername, gbc);

        JTextField txtUsername = new JTextField(15);
        gbc.gridx = 1;
        panel.add(txtUsername, gbc);

        JLabel lblPassword = new JLabel("Password:");
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(lblPassword, gbc);

        JPasswordField txtPassword = new JPasswordField(15);
        gbc.gridx = 1;
        panel.add(txtPassword, gbc);

        JButton btnLogin = new JButton("Login");
        btnLogin.setBackground(Color.decode("#1976d2"));
        btnLogin.setForeground(Color.white);
        gbc.gridwidth = 2;
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(btnLogin, gbc);

        // Event klik tombol
        btnLogin.addActionListener(e -> {
            String username = txtUsername.getText();
            String password = new String(txtPassword.getPassword());

            String result = LoginLogic.checkLogin(username, password);

            if (result != null) {
                JOptionPane.showMessageDialog(this, "Login Berhasil sebagai " + result + "!");

                if (result.equals("admin")) {
                    new AdminMenuUI().setVisible(true);
                    this.dispose(); // menutup jendela login
                } else if (result.equals("pelanggan")) {
                    new FormNamaUI().setVisible(true);
                    this.dispose(); // menutup jendela login
                }

            } else {
                JOptionPane.showMessageDialog(this, "Login Gagal. Coba lagi.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        });

        add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginUI().setVisible(true);
        });
    }
}
