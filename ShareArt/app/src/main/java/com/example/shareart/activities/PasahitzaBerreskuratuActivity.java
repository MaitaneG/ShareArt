package com.example.shareart.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;

import com.example.shareart.R;
import com.example.shareart.providers.AuthProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;

public class PasahitzaBerreskuratuActivity extends AppCompatActivity {

    private TextInputEditText editTextEmail;
    private LinearLayout imageButtonPasahitzaBerreskuratu;
    private ProgressBar progressBar;
    private Toolbar toolbar;

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
        //LinearLayout
        imageButtonPasahitzaBerreskuratu = findViewById(R.id.buttonBerreskuratu);
        // ProgressBar
        progressBar = findViewById(R.id.indeterminateBarBerreskuratu);
        progressBar.setVisibility(View.INVISIBLE);
        // Providers
        authProvider = new AuthProvider();
        // Tresna-barra
        toolbar = findViewById(R.id.ToolBar);
        toolbar.setTitle("Pasahitza berreskuratu");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // OnClickListener
        imageButtonPasahitzaBerreskuratu.setOnClickListener(this::berreskuratuPasahitza);
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
        authProvider.changePasahitza(email.trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
}