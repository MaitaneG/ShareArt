package com.example.shareart.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shareart.R;
import com.example.shareart.providers.AuthProvider;
import com.example.shareart.providers.UserProvider;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.DocumentSnapshot;
import com.example.shareart.models.Erabiltzailea;

import java.util.Date;

/**
 * Login pantailaren Activity-a
 */
public class MainActivity extends AppCompatActivity {

    private TextView erregistroLinka;
    private TextInputEditText emailEditText;
    private TextInputEditText pasahitzaEditText;
    private ImageButton hasiSaioBotoia;
    private ProgressBar progressBar;

    private GoogleSignInClient mGoogleSignInClient;
    private SignInButton hasiSaioaGooglekinBotoia;

    private UserProvider userProvider;
    private AuthProvider authProvider;

    /**
     * MainActivity-a sortzen denean
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hasieratu();
    }

    /**
     * Konponenteak hasieratzen dira
     */
    private void hasieratu() {
        // TextView
        erregistroLinka = findViewById(R.id.erregistratu_linka);
        // EditText
        emailEditText = findViewById(R.id.textInputEditTextEmailLogin);
        pasahitzaEditText = findViewById(R.id.textInputEditTextPasahitzaLogin);
        // ImageButton
        hasiSaioBotoia = findViewById(R.id.ImageButtonHasiSaioa);
        hasiSaioaGooglekinBotoia = findViewById(R.id.ButtonHasiSaioaGoogle);
        // Login Google-ekin
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("701358991762-2ip5fobsactab0fa5cl4gjrejpcj39j6.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(MainActivity.this, googleSignInOptions);
        //Providers
        authProvider = new AuthProvider();
        userProvider=new UserProvider();
        // ProgressBar
        progressBar = findViewById(R.id.indeterminateBarLogin);
        progressBar.setVisibility(View.INVISIBLE);
        // OnClickListener
        erregistroLinka.setOnClickListener(this::erregistroraJoan);
        hasiSaioBotoia.setOnClickListener(this::hasiSaioaKorreoaEtaPasahitzarekin);
        hasiSaioaGooglekinBotoia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resultLauncher.launch(new Intent(mGoogleSignInClient.getSignInIntent()));
            }
        });
    }

    /**
     * Erregistratzeko pantailara doa
     *
     * @param view
     */
    private void erregistroraJoan(View view) {
        Intent intent = new Intent(MainActivity.this, ErregistroActivity.class);
        startActivity(intent);
    }

    /**
     * Saioa hasteko
     *
     * @param view
     */
    private void hasiSaioaKorreoaEtaPasahitzarekin(View view) {
        String email = emailEditText.getText().toString().trim();
        String pasahitza = pasahitzaEditText.getText().toString().trim();

        // Gakoren bat hutsik badago
        if (email.isEmpty() || pasahitza.isEmpty()) {
            Toast.makeText(MainActivity.this, "Gakoa guztiak bete behar dira", Toast.LENGTH_SHORT).show();
        } else {
            progressBar.setVisibility(View.VISIBLE);
            authProvider.sesioaHasiEmailEtaPasahitzarekin(email, pasahitza).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    // Login-a ondo egin bada
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        eremuakGarbitu();
                    } else {

                        Toast.makeText(MainActivity.this, "Emaila edo pasahitza gaizki daude", Toast.LENGTH_SHORT).show();
                    }
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });
        }
    }

    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent intent = result.getData();

                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(intent);

                try {
                    GoogleSignInAccount account = task.getResult(ApiException.class);

                    //assert account != null;

                    firebaseAuthWithGoogle(account);
                } catch (ApiException e) {

                }
            }
        }
    });

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        progressBar.setVisibility(View.VISIBLE);
        authProvider.sesioaHasiGooglerekin(account).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String id = authProvider.getUid();
                    konprobatuErabiltzaileaExistitzenDen(id);
                } else {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(MainActivity.this, "Arazo bat egon da sesioa hastean", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void konprobatuErabiltzaileaExistitzenDen(String id) {
        userProvider.getErabiltzailea(id).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    progressBar.setVisibility(View.INVISIBLE);
                    
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                }else{
                    String email = authProvider.getEmail();
                    Erabiltzailea erabiltzailea = new Erabiltzailea();
                    erabiltzailea.setEmail(email);
                    erabiltzailea.setId(id);
                    erabiltzailea.setSortze_data(new Date().getTime());

                    userProvider.createErabiltzailea(erabiltzailea).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressBar.setVisibility(View.INVISIBLE);

                            if(task.isSuccessful()){
                                Intent intent = new Intent(MainActivity.this, ProfilaBeteActivity.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(MainActivity.this, "Errore bat egon da erabiltzailea gordetzean", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    /**
     * Login eremuak garbitzeko
     */
    private void eremuakGarbitu() {
        emailEditText.setText("");
        pasahitzaEditText.setText("");
        pasahitzaEditText.clearFocus();
    }

    /**
     * Aplikazioa hasterakoan, erabiltzailea hasita badago,
     * HomeActivity-ra doa
     */
    @Override
    protected void onStart() {
        super.onStart();
        // Konprobatu erabiltzailea logeatuta dagoen
        if (authProvider.getUserSession() != null) {
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
}