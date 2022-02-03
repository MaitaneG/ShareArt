package com.example.shareart.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

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
    private AuthProvider authProvider;
    private ProgressBar progressBar;

    private GoogleSignInClient mGoogleSignInClient;
    private SignInButton hasiSaioaGooglekinBotoia;
    private UserProvider userProvider;

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
        emailEditText = findViewById(R.id.textInputEditTextEmailLogin);
        pasahitzaEditText = findViewById(R.id.textInputEditTextPasahitzaLogin);
        // ImageButton
        hasiSaioBotoia = findViewById(R.id.ImageButtonHasiSaioa);
        hasiSaioaGooglekinBotoia = findViewById(R.id.ButtonHasiSaioaGoogle);
        //FireBase AuthProviderS
        authProvider = new AuthProvider();
        // ProgressBar
        progressBar = findViewById(R.id.indeterminateBarLogin);
        progressBar.setVisibility(View.INVISIBLE);
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
            progressBar.setVisibility(View.VISIBLE);
            authProvider.sesioaHasiEmailEtaPasahitzarekin(email, pasahitza).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
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
                    progressBar.setVisibility(View.INVISIBLE);
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                    //finish();
                    Toast.makeText(MainActivity.this, "Sesioa ondo hasi da", Toast.LENGTH_SHORT).show();
                } else {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(MainActivity.this, "Arazo bat egon da sesioa hastean", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void eremuakGarbitu() {
        emailEditText.setText("");
        pasahitzaEditText.setText("");
        pasahitzaEditText.clearFocus();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (authProvider.getUserSession() != null) {
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
}