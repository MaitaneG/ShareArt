package com.example.shareart.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.shareart.R;
import com.example.shareart.models.Erabiltzailea;
import com.example.shareart.providers.AuthProvider;
import com.example.shareart.providers.UserProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;

public class ProfilaBeteActivity extends AppCompatActivity {

    private TextInputEditText editTextErabiltzaileIzena;
    private LinearLayout imageButtonOsatu;
    private ProgressBar progressBar;
    private Toolbar toolbar;

    private AuthProvider authProvider;
    private UserProvider userProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profila_bete);

        hasieratu();
    }

    private void hasieratu() {
        // EditText
        editTextErabiltzaileIzena = findViewById(R.id.textInputEditTextErabiltzaileaGoogle);
        // ImageButton
        imageButtonOsatu = findViewById(R.id.buttonErregistratuGoogle);
        // ProgressBar
        progressBar = findViewById(R.id.indeterminateBarGoogle);
        progressBar.setVisibility(View.INVISIBLE);
        // Providers
        authProvider = new AuthProvider();
        userProvider = new UserProvider();
        // Tresna-barra
        toolbar = findViewById(R.id.ToolBar);
        toolbar.setTitle("Osatu zure informazioa");
        setSupportActionBar(toolbar);
        // OnClickListener
        imageButtonOsatu.setOnClickListener(this::osatuInformazioa);
    }

    private void osatuInformazioa(View view) {
        String erabiltzaileIzena = editTextErabiltzaileIzena.getText().toString();

        if (erabiltzaileIzena.isEmpty()) {
            Toast.makeText(ProfilaBeteActivity.this, "Erabiltzaile izena bete behar duzu", Toast.LENGTH_SHORT).show();
        } else {
            eguneratuErabiltzailea(erabiltzaileIzena);
        }
    }

    private void eguneratuErabiltzailea(String erabiltzaileIzena) {
        String id = authProvider.getUid();
        Erabiltzailea erabiltzailea = new Erabiltzailea();
        erabiltzailea.setId(id.trim());
        erabiltzailea.setErabiltzaile_izena(erabiltzaileIzena.trim());

        progressBar.setVisibility(View.VISIBLE);

        userProvider.updateErabiltzailea(erabiltzailea).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressBar.setVisibility(View.INVISIBLE);
                if (task.isSuccessful()) {
                    Intent intent = new Intent(ProfilaBeteActivity.this, HomeActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(ProfilaBeteActivity.this, "Arazo bat egon da informazioa osatzerakoan", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {

    }
}