package com.example.shareart.providers;

import androidx.annotation.NonNull;

import com.example.shareart.models.Token;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

public class TokenProvider {

    private CollectionReference collectionReference;

    public TokenProvider() {
        collectionReference = FirebaseFirestore.getInstance().collection("Tokens");
    }

    public void createToken(String idUser) {
        if (idUser == null) {
            return;
        }
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String s) {
                Token token = new Token(s);
                collectionReference.document(idUser).set(token);
            }
        });
    }

    public void deleteToken(String idUser) {
        if (idUser != null) {
            FirebaseMessaging.getInstance().deleteToken().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        collectionReference.document(idUser).delete();
                    }
                }
            });
        }
    }

    public Task<DocumentSnapshot> getToken(String idUser) {
        return collectionReference.document(idUser).get();
    }
}
