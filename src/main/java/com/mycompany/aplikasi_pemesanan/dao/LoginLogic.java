package com.mycompany.aplikasi_pemesanan.dao;

public class LoginLogic {
    public static String checkLogin(String username, String password) {
        if (username.equals("admin") && password.equals("omarhandi")) {
            return "admin";
        } else if (username.equals("pelanggan") && password.equals("lahomarhandilagi")) {
            return "pelanggan";
        } else {
            return null;
        }
    }
}
