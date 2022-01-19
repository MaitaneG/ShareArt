package com.example.shareart;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private ImageButton atzeraBotoia;
    private ImageButton erregistratuBotoia;
    private TextInputEditText erabiltzaileaEditText;
    private TextInputEditText emailEditText;
    private TextInputEditText pasahitzaEditText;
    private TextInputEditText pasahitzaBaieztatuEditText;

    /**
     * Activity-a sortzen denean
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
        emailEditText = findViewById(R.id.textInputEditTextEmail);
        pasahitzaEditText = findViewById(R.id.TextInputEditTextPasahitzaErregistratu);
        pasahitzaBaieztatuEditText = findViewById(R.id.TextInputEditTextBaieztatuPasahitzaErregistratu);
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

        if (erabiltzailea.equals("") || email.equals("") || pasahitza.equals("") || pasahitzaBaeieztatu.equals("")) {
            Toast.makeText(RegisterActivity.this, "Gako guztiak bete behar dira", Toast.LENGTH_SHORT);
        } else if (!emailaKonprobatu(email)) {
            Toast.makeText(RegisterActivity.this, "Emailaren formatua gaizki dago", Toast.LENGTH_SHORT).show();
        } else {

        }
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