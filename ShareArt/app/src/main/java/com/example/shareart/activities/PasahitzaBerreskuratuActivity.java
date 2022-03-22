package com.example.shareart.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shareart.R;
import com.example.shareart.providers.AuthProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;

public class PasahitzaBerreskuratuActivity extends AppCompatActivity {

    private TextInputEditText editTextEmail;
    private ImageButton imageButtonPasahitzaBerreskuratu;
    private ImageButton atzeraBotoia;
    private ProgressBar progressBar;

    private AuthProvider authProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pasahitza_berreskuratu);

        hasieratu();
    }

    private void hasieratu() {
        // EditText
        editTextEmail = findViewById(R.id.textInputEditTextEmailBerreskuratu);
        // ImageButton
        imageButtonPasahitzaBerreskuratu = findViewById(R.id.imageButtonBerreskuratu);
        atzeraBotoia = findViewById(R.id.atzera_botoiaBerreskuratu);
        // ProgressBar
        progressBar = findViewById(R.id.indeterminateBarBerreskuratu);
        progressBar.setVisibility(View.INVISIBLE);
        // Providers
        authProvider = new AuthProvider();
        // OnClickListener
        imageButtonPasahitzaBerreskuratu.setOnClickListener(this::berreskuratuPasahitza);
        atzeraBotoia.setOnClickListener(this::atzeraJoan);
    }

    private void berreskuratuPasahitza(View view) {
        String email = editTextEmail.getText().toString();

        if (email.isEmpty()) {
            Toast.makeText(PasahitzaBerreskuratuActivity.this, "Korreoa sartu behar duzu", Toast.LENGTH_SHORT).show();
        } else {
            bidaliMezua(email);
        }
    }

    private void bidaliMezua(String email) {
        progressBar.setVisibility(View.VISIBLE);
        authProvider.changePasahitza(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressBar.setVisibility(View.INVISIBLE);
                if (task.isSuccessful()) {
                    Toast.makeText(PasahitzaBerreskuratuActivity.this, "Mezu bat bidali da zure korreora", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PasahitzaBerreskuratuActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(PasahitzaBerreskuratuActivity.this, "Errore bat egon da mezua bidaltzean", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void atzeraJoan(View view) {
        finish();
    }
}