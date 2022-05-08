package com.example.shareart.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shareart.R;
import com.example.shareart.providers.AuthProvider;

public class KonfigurazioaActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView emailTextView;
    private TextView kontuBerriaSortuTextView;
    private ImageView saioaItxiImageView;

    private AuthProvider authProvider;

    private String emaila;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konfigurazioa);

        hasieratu();
    }

    private void hasieratu() {
        // TextView
        emailTextView = findViewById(R.id.emailTextView);
        kontuBerriaSortuTextView=findViewById(R.id.kontuBerriaSortuTextView);
        // ImageView
        saioaItxiImageView = findViewById(R.id.itxiSaioaButtom);
        // Providers
        authProvider = new AuthProvider();
        // Tresna-barra
        toolbar = findViewById(R.id.ToolBar);
        toolbar.setTitle("Konfigurazioa");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Erabiltzailearen informazioa kargatu
        kargatuInformazioa();
        // OnClickListener
        saioaItxiImageView.setOnClickListener(this::saioaItxi);
        kontuBerriaSortuTextView.setOnClickListener(this::erregistratuPantailaraJoan);
    }

    private void erregistratuPantailaraJoan(View view) {
        authProvider.saioaItxi();
        Intent intent = new Intent(KonfigurazioaActivity.this, ErregistroActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void saioaItxi(View view) {
        authProvider.saioaItxi();
        Intent intent = new Intent(KonfigurazioaActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void kargatuInformazioa() {
        emaila = authProvider.getEmail();
        emailTextView.setText(emaila);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

}