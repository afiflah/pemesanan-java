package com.mycompany.aplikasi_pemesanan.model;

public class Produk {
    private int idProduk;
    private String namaProduk;
    private double harga;
    private String jenis;
    private int stok;
    private String foto;

    // Constructor lengkap
    public Produk(int idProduk, String namaProduk, double harga, String jenis, int stok, String foto) {
        this.idProduk = idProduk;
        this.namaProduk = namaProduk;
        this.harga = harga;
        this.jenis = jenis;
        this.stok = stok;
        this.foto = foto;
    }

    // Constructor tanpa ID & atribut lain (misal tambah produk baru)
    public Produk(String namaProduk, double harga, String jenis, int stok, String foto) {
        this.namaProduk = namaProduk;
        this.harga = harga;
        this.jenis = jenis;
        this.stok = stok;
        this.foto = foto;
    }

    // Getter & Setter
    public int getIdProduk() { return idProduk; }
    public void setIdProduk(int idProduk) { this.idProduk = idProduk; }

    public String getNamaProduk() { return namaProduk; }
    public void setNamaProduk(String namaProduk) { this.namaProduk = namaProduk; }

    public double getHarga() { return harga; }
    public void setHarga(double harga) { this.harga = harga; }

    public String getJenis() { return jenis; }
    public void setJenis(String jenis) { this.jenis = jenis; }

    public int getStok() { return stok; }
    public void setStok(int stok) { this.stok = stok; }

    public String getFoto() { return foto; }
    public void setFoto(String foto) { this.foto = foto; }

}
