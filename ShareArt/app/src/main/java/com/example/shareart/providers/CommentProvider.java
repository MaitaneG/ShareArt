package com.example.shareart.providers;

import com.example.shareart.models.Argitalpena;
import com.example.shareart.models.Komentarioa;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class CommentProvider {

    private final CollectionReference collectionReference;

    public CommentProvider() {
        collectionReference= FirebaseFirestore.getInstance().collection("Komentarioa");
    }

    public Task<Void> createKomentarioa(Komentarioa komentarioa){
        DocumentReference documentReference = collectionReference.document();
        String id = documentReference.getId();
        komentarioa.setId(id);
        return documentReference.set(komentarioa);
    }

    public Query getKomentarioakByArgitalpen(String idArgitalpen) {
        return collectionReference.whereEqualTo("id_argitalpen", idArgitalpen);
    }

    public Task<Void> deleteKomentarioa(String id) {
        return collectionReference.document(id).delete();
    }
}
