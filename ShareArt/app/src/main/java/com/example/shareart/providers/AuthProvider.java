package com.example.shareart.providers;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class AuthProvider {
    private FirebaseAuth firebaseAuth;

    public AuthProvider() {
        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    public Task<AuthResult> sesioaHasiEmailEtaPasahitzarekin(String email, String pasahitza){
        return firebaseAuth.signInWithEmailAndPassword(email, pasahitza);
    }

    public Task<AuthResult> sesioaHasiGooglerekin(GoogleSignInAccount googleSignInAccount){
        AuthCredential credential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);
        return firebaseAuth.signInWithCredential(credential);
    }

    public String getUid(){
        if(firebaseAuth.getCurrentUser()!=null){
            return firebaseAuth.getCurrentUser().getUid();
        }else{
            return null;
        }

    }
}
