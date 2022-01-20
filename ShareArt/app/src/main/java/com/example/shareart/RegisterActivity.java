package com.example.shareart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

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
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

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
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
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
            Toast.makeText(RegisterActivity.this, "Gako guztiak bete behar dira.", Toast.LENGTH_SHORT);
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
        firebaseAuth.createUserWithEmailAndPassword(email, pasahitza).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String id = firebaseAuth.getCurrentUser().getUid();
                    Map<String, Object> map = new HashMap<>();
                    map.put("erabiltzailea", erabiltzailea);
                    map.put("email", email);
                    Toast.makeText(RegisterActivity.this, "Erabiltzailea ondo sortu da", Toast.LENGTH_SHORT).show();
                    firebaseFirestore.collection("Users").document(id).set(map);
                } else {
                    Toast.makeText(RegisterActivity.this, "Arazo bat egon da, erabiltzailea sortzean", Toast.LENGTH_SHORT).show();
                }
            }
        });

        finish();
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