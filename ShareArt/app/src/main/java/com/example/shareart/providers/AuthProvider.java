package com.example.shareart.providers;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

/**
 * Firebase-eko autentifikazioa egiteko
 */
public class AuthProvider {
    private final FirebaseAuth firebaseAuth;

    /**
     * Kontruktorea
     */
    public AuthProvider() {
        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    public Task<AuthResult> erregistratu(String email, String pasahitza) {
        return firebaseAuth.createUserWithEmailAndPassword(email, pasahitza);
    }

    /**
     * Sesioa hasteko email eta pasahitzarekin
     *
     * @param email
     * @param pasahitza
     * @return
     */
    public Task<AuthResult> sesioaHasiEmailEtaPasahitzarekin(String email, String pasahitza) {
        return firebaseAuth.signInWithEmailAndPassword(email, pasahitza);
    }

    /**
     * Momentu horretako erabiltzailearen id-a lortzeko
     *
     * @return
     */
    public String getUid() {
        if (firebaseAuth.getCurrentUser() != null) {
            return firebaseAuth.getCurrentUser().getUid();
        } else {
            return null;
        }
    }

    /**
     * Momentu horretako erabiltzailea lortzeko
     *
     * @return
     */
    public FirebaseUser getUserSession() {
        if (firebaseAuth.getCurrentUser() != null) {
            return firebaseAuth.getCurrentUser();
        } else {
            return null;
        }
    }

    /**
     * Momentu horretako erabiltzailea itxi
     */
    public void saioaItxi() {
        if (firebaseAuth != null) {
            firebaseAuth.signOut();
        }
    }

    /**
     * Sesioa hasteko googlerekin
     *
     * @param googleSignInAccount
     * @return
     */
    public Task<AuthResult> sesioaHasiGooglerekin(GoogleSignInAccount googleSignInAccount) {
        AuthCredential credential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);
        return firebaseAuth.signInWithCredential(credential);
    }

    public String getEmail() {
        if (firebaseAuth.getCurrentUser() != null) {
            return firebaseAuth.getCurrentUser().getEmail();
        } else {
            return null;
        }
    }

    public Task<Void> changePasahitza(String email){
        return firebaseAuth.sendPasswordResetEmail(email);
    }
}
