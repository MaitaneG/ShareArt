package com.example.shareart.providers;

import com.example.shareart.models.Argitalpena;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Collections;

public class PostProvider {

    private final CollectionReference collectionReference;

    public PostProvider() {
        collectionReference = FirebaseFirestore.getInstance().collection("Argitalpena");
    }

    public Task<Void> createArgitalpena(Argitalpena argitalpena) {

        DocumentReference documentReference = collectionReference.document();
        String id = documentReference.getId();
        argitalpena.setId(id);
        return documentReference.set(argitalpena);
    }

    public Query getArgitalpenGuztiak() {
        return collectionReference.orderBy("data", Query.Direction.DESCENDING);
    }

    public Query getArgitalpenakByErabiltzailea(String idErabiltzaile) {
        return collectionReference.whereEqualTo("id_user", idErabiltzaile).orderBy("data", Query.Direction.DESCENDING);
    }

    public Task<DocumentSnapshot> getArgitalpenaById(String id){
        return collectionReference.document(id).get();
    }

    public Query getArgitalpenaByIdQuery(String id){
        return collectionReference.whereEqualTo("id", id);
    }

    public Task<Void> deleteArgitalpena(String id) {
        return collectionReference.document(id).delete();
    }

    public Query getArgitalpenByKategoria(String kategoria) {
        return collectionReference.whereEqualTo("kategoria", kategoria).orderBy("data", Query.Direction.DESCENDING);
    }
    
    public Query getArgitalpenByUserAndKategoria(String idErabiltzaile,String kategoria){
        return collectionReference.whereEqualTo("kategoria",kategoria).whereEqualTo("id_user", idErabiltzaile);
    }
}
