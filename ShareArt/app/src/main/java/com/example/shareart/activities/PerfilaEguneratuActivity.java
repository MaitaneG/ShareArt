package com.example.shareart.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.shareart.R;

public class PerfilaEguneratuActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfila_eguneratu);

        // Tresna-barra
        toolbar = (Toolbar) findViewById(R.id.ToolBar);

        setSupportActionBar(toolbar);
        //toolbar.inflateMenu(R.menu.bilatzailea_eta_sesioa_itxi_menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout) {

            return true;
        }
        return false;
    }
}