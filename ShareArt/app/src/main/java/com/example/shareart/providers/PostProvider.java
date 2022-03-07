package com.example.shareart.providers;

import com.example.shareart.models.Argitalpena;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class PostProvider {

    private CollectionReference collectionReference;

    public PostProvider() {
        collectionReference= FirebaseFirestore.getInstance().collection("Argitalpena");
    }

    public Task<Void> createArgitalpena(Argitalpena argitalpena){

        DocumentReference documentReference = collectionReference.document();
        String id= documentReference.getId();
        argitalpena.setId(id);
        return documentReference.set(argitalpena);
    }

    public Query getArgitalpenGuztiak(){
        return collectionReference.orderBy("data", Query.Direction.DESCENDING);
    }

    public Query getArgitalpenakByErabiltzailea(String id){
        return collectionReference.whereEqualTo("id_user",id);
    }

    public Task<Void> deleteArgitalpena(String id) {
        return collectionReference.document(id).delete();
    }
}
