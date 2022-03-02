package com.example.shareart.providers;

import com.example.shareart.models.Erabiltzailea;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UserProvider {

    private CollectionReference collectionReference;

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
    public Task<DocumentSnapshot> getErabiltzailea(String id) {
        return collectionReference.document(id).get();
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
        map.put("erabiltzaileIzena", erabiltzailea.getErabiltzaileIzena());
        map.put("argazkiaProfilaUrl", erabiltzailea.getArgazkiaProfilaUrl());

        return collectionReference.document(erabiltzailea.getId()).update(map);
    }
}
