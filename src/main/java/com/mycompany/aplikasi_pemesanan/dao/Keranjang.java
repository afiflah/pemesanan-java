package com.mycompany.aplikasi_pemesanan.dao;

import com.mycompany.aplikasi_pemesanan.model.KeranjangItem;
import java.util.ArrayList;
import java.util.List;

public class Keranjang {
    private static final List<KeranjangItem> itemList = new ArrayList<>();

    public static void tambahItem(KeranjangItem item) {
        itemList.add(item);
    }

    public static List<KeranjangItem> getItems() {
        return new ArrayList<>(itemList);
    }

    public static void clear() {
        itemList.clear();
    }
}
