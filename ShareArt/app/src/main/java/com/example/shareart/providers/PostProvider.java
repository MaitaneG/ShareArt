package com.example.shareart.providers;

import com.example.shareart.models.Argitalpena;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class PostProvider {

    private CollectionReference collectionReference;

    public PostProvider() {
        collectionReference= FirebaseFirestore.getInstance().collection("Argitalpena");
    }

    public Task<Void> gordeArgitalpenarenInformazioa(Argitalpena argitalpena){
        return collectionReference.document().set(argitalpena);
    }

    public Query getArgitalpenGuztiak(){
        return collectionReference.orderBy("deskribapena", Query.Direction.DESCENDING);
    }

    public Query getErabiltzaileBakoitzekoArgitalpenak(String id){
        return collectionReference.whereEqualTo("id_user",id);
    }
}
