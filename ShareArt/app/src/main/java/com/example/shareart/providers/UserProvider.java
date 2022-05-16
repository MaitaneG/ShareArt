package com.example.shareart.providers;

import com.example.shareart.models.Erabiltzailea;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class UserProvider {

    private final CollectionReference collectionReference;

    /**
     * Konstruktorea
     */
    public UserProvider() {
        collectionReference = FirebaseFirestore.getInstance().collection("Erabiltzailea");
    }

    /**
     * Erabiltzen da lortzeko erabiltzailea sesioa hasten denean Google-rekin
     *
     * @param id
     * @return
     */
    public DocumentReference getErabiltzailea(String id) {
        return collectionReference.document(id);
    }

    public Task<QuerySnapshot> getErabiltzaileaByErabiltzaileIzena(String izena) {
        return collectionReference.orderBy("erabiltzaile_izena").startAt(izena.toUpperCase()).endAt(izena + "\uf8ff").get();

    }

    /**
     * Erabiltzailea erregistratzen denean
     * edo
     * Erabiltzailea bat sortzen du Googlerekin lehengo aldiz sesioa hasterakoan
     *
     * @return
     */
    public Task<Void> createErabiltzailea(Erabiltzailea erabiltzailea) {
        return collectionReference.document(erabiltzailea.getId()).set(erabiltzailea);
    }

    /**
     * Erabiltzailea eguneratzeko
     *
     * @param erabiltzailea
     * @return
     */
    public Task<Void> updateErabiltzailea(Erabiltzailea erabiltzailea) {
        Map<String, Object> map = new HashMap<>();
        map.put("erabiltzaile_izena", erabiltzailea.getErabiltzaile_izena());
        map.put("argazkia_profila_url", erabiltzailea.getArgazkia_profila_url());
        map.put("deskribapena", erabiltzailea.getDeskribapena());

        return collectionReference.document(erabiltzailea.getId()).update(map);
    }

    public Task<Void> setEgiaztatua(Erabiltzailea erabiltzailea) {
        Map<String, Object> map = new HashMap<>();
        map.put("egiaztatua",erabiltzailea.isEgiaztatua());
        return collectionReference.document(erabiltzailea.getId()).update(map);
    }
}
