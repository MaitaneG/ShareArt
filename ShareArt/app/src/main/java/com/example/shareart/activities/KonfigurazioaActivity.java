package com.example.shareart.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shareart.R;
import com.example.shareart.providers.AuthProvider;
import com.example.shareart.providers.TokenProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class KonfigurazioaActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView emailTextView;
    private TextView kontuBerriaSortuTextView;
    private TextView pasahitzaAldatuTextView;
    private TextView egiaztatuKorreoaLinkaTextView;
    private TextView egiaztatuKorreoaTextView;
    private ImageView saioaItxiImageView;
    private ImageView egiaztatuaImageView;

    private AuthProvider authProvider;
    private TokenProvider tokenProvider;

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
        pasahitzaAldatuTextView = findViewById(R.id.pasahitzaAldatuTextView);
        egiaztatuKorreoaLinkaTextView = findViewById(R.id.egiaztatuKorreoaLinkaTextView);
        egiaztatuKorreoaTextView = findViewById(R.id.egiaztatuKorreoaTextView);
        egiaztatuaImageView = findViewById(R.id.egiaztatuaImageView);
        // ImageView
        saioaItxiImageView = findViewById(R.id.itxiSaioaButtom);
        // Providers
        authProvider = new AuthProvider();
        tokenProvider = new TokenProvider();
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
        pasahitzaAldatuTextView.setOnClickListener(this::pasahitzaAldatu);
        egiaztatuKorreoaLinkaTextView.setOnClickListener(this::bidaliEgiaztatzekoMezua);
    }

    private void bidaliEgiaztatzekoMezua(View view) {
        authProvider.korreoaEgiaztatu().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    new AlertDialog.Builder(KonfigurazioaActivity.this)
                            .setMessage("Korreo bat helduko zaizu korreoa egiaztatzeko")
                            .setPositiveButton("Ongi", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    authProvider.saioaItxi();
                                    Intent intent = new Intent(KonfigurazioaActivity.this, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            }).show();
                }
            }
        });
    }

    private void pasahitzaAldatu(View view) {
        new AlertDialog.Builder(KonfigurazioaActivity.this)
                .setTitle("Pasahitza aldatu")
                .setMessage("Pasahitza aldatu nahi duzu?")
                .setPositiveButton("Bai", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        authProvider.changePasahitza(authProvider.getEmail()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                new AlertDialog.Builder(KonfigurazioaActivity.this)
                                        .setMessage("Korreo bat helduko zaizu pasahitza aldatzeko")
                                        .setPositiveButton("Ongi", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                finish();
                                            }
                                        }).show();
                            }
                        });
                    }
                })
                .setNegativeButton("Ez", null)
                .show();
    }

    private void erregistratuPantailaraJoan(View view) {
        new AlertDialog.Builder(KonfigurazioaActivity.this)
                .setTitle("Saioa ixten")
                .setMessage("Ziur zaude saioa itxi nahi duzula")
                .setPositiveButton("Bai", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        tokenProvider.deleteToken(authProvider.getUid());
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
                        tokenProvider.deleteToken(authProvider.getUid());
                        Log.d("token", tokenProvider.getToken(authProvider.getUid()).toString());
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

        if (authProvider.korreoaEgiaztatuta()) {
            egiaztatuKorreoaLinkaTextView.setVisibility(View.GONE);
            egiaztatuKorreoaTextView.setText("Egiaztatua");
            egiaztatuKorreoaTextView.setTextColor(getResources().getColor(R.color.green));
            egiaztatuaImageView.setImageResource(R.drawable.bai_egiaztatuta_ikonoa);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

}