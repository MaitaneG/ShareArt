package com.example.shareart;

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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {

    private TextView erregistroLinka;
    private TextInputEditText emailEditText;
    private TextInputEditText pasahitzaEditText;
    private ImageButton hasiSaioBotoia;
    private FirebaseAuth firebaseAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private SignInButton hasiSaioaGooglekinBotoia;

    /**
     * Activity-a sortzen denean
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
        emailEditText = findViewById(R.id.textInputEditTextEmail);
        pasahitzaEditText = findViewById(R.id.textInputEditTextPasahitza);
        // ImageButton
        hasiSaioBotoia = findViewById(R.id.ImageButtonHasiSaioa);
        hasiSaioaGooglekinBotoia = findViewById(R.id.ButtonHasiSaioaGoogle);
        //FireBase
        firebaseAuth = FirebaseAuth.getInstance();
        // Login Google-ekin
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("701358991762-doqqgkfo09u14ki3vteqovgb2j7lvl7b.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(MainActivity.this, googleSignInOptions);

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
        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
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

        if (email.isEmpty() || pasahitza.isEmpty()) {
            Toast.makeText(MainActivity.this, "Gakoa guztiak bete behar dira", Toast.LENGTH_SHORT).show();
        } else {
            firebaseAuth.signInWithEmailAndPassword(email, pasahitza).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(MainActivity.this, "Emaila edo pasahitza gaizki daude", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


    private ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent intent = mGoogleSignInClient.getSignInIntent();

                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(intent);

                try {
                    GoogleSignInAccount account = task.getResult(ApiException.class);

                    assert account != null;

                    firebaseAuthWithGoogle(account.getIdToken());
                } catch (ApiException e) {

                }
            }
        }
    });

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                    //finish();
                    Toast.makeText(MainActivity.this, "Sesioa ondo hasi da", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Arazo bat egon da sesioa hastean", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}