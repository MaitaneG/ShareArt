package com.example.shareart.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.shareart.R;
import com.example.shareart.models.User;
import com.example.shareart.providers.AuthProvider;
import com.example.shareart.providers.UserProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private ImageButton atzeraBotoia;
    private ImageButton erregistratuBotoia;
    private TextInputEditText erabiltzaileaEditText;
    private TextInputEditText emailEditText;
    private TextInputEditText pasahitzaEditText;
    private TextInputEditText pasahitzaBaieztatuEditText;
    private AuthProvider authProvider;
    private UserProvider userProvider;
    private ProgressBar progressBar;

    /**
     * Activity-a sortzen denean
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        hasieratu();
    }

    /**
     * Konponenteak hasieratzen dira
     */
    private void hasieratu() {
        // Botoiak
        atzeraBotoia = findViewById(R.id.atzera_botoia);
        erregistratuBotoia = findViewById(R.id.ImageButtonErregistratu);
        // EditText
        erabiltzaileaEditText = findViewById(R.id.TextInputEditTextErabiltzaileaErregistratu);
        emailEditText = findViewById(R.id.TextInputEditTextEmailErregistratu);
        pasahitzaEditText = findViewById(R.id.TextInputEditTextPasahitzaErregistratu);
        pasahitzaBaieztatuEditText = findViewById(R.id.TextInputEditTextBaieztatuPasahitzaErregistratu);
        // FireBase
        authProvider = new AuthProvider();
        userProvider = new UserProvider();
        // ProgressBar
        progressBar = (ProgressBar) findViewById(R.id.indeterminateBarRegister);
        progressBar.setVisibility(View.INVISIBLE);
        // OnClickListener
        atzeraBotoia.setOnClickListener(this::atzeraJoan);
        erregistratuBotoia.setOnClickListener(this::erregistratu);
    }

    /**
     * Errgistratzen da
     *
     * @param view
     */
    private void erregistratu(View view) {
        String erabiltzailea = erabiltzaileaEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String pasahitza = pasahitzaEditText.getText().toString().trim();
        String pasahitzaBaeieztatu = pasahitzaBaieztatuEditText.getText().toString().trim();

        if (erabiltzailea.isEmpty() || email.isEmpty() || pasahitza.isEmpty() || pasahitzaBaeieztatu.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Gako guztiak bete behar dira.", Toast.LENGTH_SHORT).show();
        } else if (!emailaKonprobatu(email)) {
            Toast.makeText(RegisterActivity.this, "Emailaren formatua gaizki dago.", Toast.LENGTH_SHORT).show();
        } else {
            if (!pasahitza.equals(pasahitzaBaeieztatu)) {
                Toast.makeText(RegisterActivity.this, "Pasahitzak ez dira berdinak.", Toast.LENGTH_SHORT).show();
            } else {
                if (pasahitza.length() < 8) {
                    Toast.makeText(RegisterActivity.this, "Pasahitza oso motza da. Gutxienez 8 karaketere izan behar ditu.", Toast.LENGTH_SHORT).show();
                } else {
                    sortuErabiltzailea(erabiltzailea, email, pasahitza);
                }
            }
        }
    }

    /**
     * Erabiltzailea sortzen du FireBase Authentification-en eta Firebaseko datubasean
     *
     * @param erabiltzailea
     * @param email
     * @param pasahitza
     */
    private void sortuErabiltzailea(String erabiltzailea, String email, String pasahitza) {
        progressBar.setVisibility(View.VISIBLE);
        authProvider.erregistratu(email, pasahitza).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    String id = authProvider.getUid();
                    Map<String, Object> map = new HashMap<>();
                    map.put("erabiltzailea", erabiltzailea);
                    map.put("email", email);
                    User user = new User(id,erabiltzailea,email);

                    userProvider.create(user);
                    Toast.makeText(RegisterActivity.this, "Erabiltzailea ondo sortu da", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(RegisterActivity.this,HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    Toast.makeText(RegisterActivity.this, "Arazo bat egon da, erabiltzailea sortzean", Toast.LENGTH_SHORT).show();
                }

                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    /**
     * Hasierako menura joateko
     *
     * @param view
     */
    private void atzeraJoan(View view) {
        finish();
    }

    /**
     * Email-a ondo idatzita dagoen konprobatzen du
     *
     * @param email
     * @return
     */
    private boolean emailaKonprobatu(String email) {
        String expression = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}