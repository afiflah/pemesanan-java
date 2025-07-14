package com.mycompany.aplikasi_pemesanan.model;

public class KeranjangItem {
    private int idProduk;
    private String namaProduk;
    private double harga;
    private int jumlah;
    private String catatan;

    public KeranjangItem(Produk produk, int jumlah, String catatan) {
     this.idProduk = produk.getIdProduk();
     this.namaProduk = produk.getNamaProduk();
     this.harga = produk.getHarga();
     this.jumlah = jumlah;
     this.catatan = catatan;
 }


    public int getIdProduk() {
        return idProduk;
    }

    public String getNamaProduk() {
        return namaProduk;
    }

    public double getHarga() {
        return harga;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public String getCatatan() {
        return catatan;
    }

    public void setCatatan(String catatan) {
        this.catatan = catatan;
    }

    public double getSubtotal() {
        return harga * jumlah;
    }
}
