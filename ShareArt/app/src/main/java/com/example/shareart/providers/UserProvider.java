package com.example.shareart.providers;

import com.example.shareart.models.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserProvider {

    private CollectionReference collectionReference;

    public UserProvider() {
        collectionReference = FirebaseFirestore.getInstance().collection("Users");
    }

    /**
     * Erabiltzen da lortzeko erabiltzailea sesioa hasten denean Google-rekin
     *
     * @param id
     * @return
     */
    public Task<DocumentSnapshot> getUser(String id) {
        return collectionReference.document(id).get();
    }

    /**
     * Erabiltzailea erregistratzen denean
     * edo
     * Erabiltzailea bat sortzen du Googlerekin lehengo aldiz sesioa hasterakoan
     *
     * @return
     */
    public Task<Void> create(User user) {
        return collectionReference.document(user.getId()).set(user);
    }
}
