package com.example.shareart.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

import com.example.shareart.R;

public class FiltratutakoaActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView textViewKategoria;

    private String extraKategoria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtratutakoa);

        hasieratu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bilatzailea_menu, menu);
        return true;
    }

    private void hasieratu() {
        textViewKategoria = findViewById(R.id.textViewKategoria);

        extraKategoria = getIntent().getStringExtra("kategoria");

        // Tresna-barra
        toolbar = (Toolbar) findViewById(R.id.ToolBar);
        toolbar.setTitle(extraKategoria);
        ((AppCompatActivity) FiltratutakoaActivity.this).setSupportActionBar(toolbar);
    }
}