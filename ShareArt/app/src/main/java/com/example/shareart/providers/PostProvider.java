package com.example.shareart.providers;

import com.example.shareart.models.Argitarapena;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;

public class PostProvider {

    private CollectionReference collectionReference;

    public PostProvider() {
        collectionReference= FirebaseFirestore.getInstance().collection("Argitarapena");
    }

    public Task<Void> gordeArgitarapenarenInformazioa(Argitarapena argitarapena){
        return collectionReference.document().set(argitarapena);
    }

    public Query getArgitarapenGuztiak(){
        return collectionReference.orderBy("deskripzioa", Query.Direction.DESCENDING);
    }
}
