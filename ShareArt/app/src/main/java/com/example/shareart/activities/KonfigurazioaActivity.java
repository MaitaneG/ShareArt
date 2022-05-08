package com.example.shareart.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shareart.R;
import com.example.shareart.providers.AuthProvider;

public class KonfigurazioaActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView emailTextView;
    private TextView kontuBerriaSortuTextView;
    private TextView kontuaEzabatuTextView;
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
        kontuBerriaSortuTextView = findViewById(R.id.kontuBerriaSortuTextView);
        kontuaEzabatuTextView = findViewById(R.id.kontuaEzabatuTextView);
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
        kontuaEzabatuTextView.setOnClickListener(this::kontuaEzabatu);
    }

    private void kontuaEzabatu(View view) {
        Toast.makeText(this, "Oraindik ezin da ezabatu", Toast.LENGTH_SHORT).show();
    }

    private void erregistratuPantailaraJoan(View view) {
        new AlertDialog.Builder(KonfigurazioaActivity.this)
                .setTitle("Saioa ixten")
                .setMessage("Ziur zaude saioa itxi nahi duzula")
                .setPositiveButton("Bai", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        authProvider.saioaItxi();
                        Intent intent = new Intent(KonfigurazioaActivity.this, ErregistroActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Ez", null)
                .show();
    }

    private void saioaItxi(View view) {
        new AlertDialog.Builder(KonfigurazioaActivity.this)
                .setTitle("Saioa ixten")
                .setMessage("Ziur zaude saioa itxi nahi duzula")
                .setPositiveButton("Bai", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        authProvider.saioaItxi();
                        Intent intent = new Intent(KonfigurazioaActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Ez", null)
                .show();
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