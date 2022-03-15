package com.example.shareart.providers;

import com.example.shareart.models.Like;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class LikeProvider {
    private final CollectionReference collectionReference;

    public LikeProvider() {
        collectionReference = FirebaseFirestore.getInstance().collection("Like");
    }

    public Task<Void> createLike(Like like) {
        DocumentReference documentReference = collectionReference.document();
        String id= documentReference.getId();
        like.setId(id);
        return documentReference.set(like);
    }

    public Query getLikesByArgitalpen(String idArgitalpen) {
        return collectionReference.whereEqualTo("id_argitalpen", idArgitalpen);
    }

    public Query getLikeByArgitalpenAndErabiltzaile(String idArgitalpen, String idErabiltzaile) {
        return collectionReference.whereEqualTo("id_argitalpen", idArgitalpen).whereEqualTo("id_erabiltzaile", idErabiltzaile);
    }

    public Task<Void> deleteLike(String id) {
        return collectionReference.document(id).delete();
    }
}
