package com.example.shareart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

    private TextView erregistroLinka;
    private TextInputEditText emailEditText;
    private TextInputEditText pasahitzaEditText;
    private ImageButton hasiSaioBotoia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hasieratu();
    }

    private void hasieratu() {
        erregistroLinka = findViewById(R.id.erregistratu_linka);
        emailEditText = findViewById(R.id.textInputEditTextEmail);
        pasahitzaEditText = findViewById(R.id.textInputEditTextPasahitza);
        hasiSaioBotoia = findViewById(R.id.ImageButtonHasiSaioa);

        erregistroLinka.setOnClickListener(this::erregistroraJoan);
        hasiSaioBotoia.setOnClickListener(this::hasiSaioa);
    }

    private void erregistroraJoan(View view) {
        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    private void hasiSaioa(View view) {
        String email = emailEditText.getText().toString();
        String pasahitza = pasahitzaEditText.getText().toString();

        if(email.trim().equals("")||pasahitza.trim().equals("")){
            Toast.makeText(MainActivity.this, "Gakoa guztiak bete behar dira", Toast.LENGTH_SHORT).show();
        }

    }


}