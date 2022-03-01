package com.example.shareart.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.shareart.R;

public class PerfilaEguneratuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfila_eguneratu);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Perfila editatzeko pantailatik irteten")
                .setMessage("Ziur zaude irten nahi zarela pantaila honetatik perfila editatu gabe?")
                .setPositiveButton("Bai", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setNegativeButton("Ez", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}