package com.example.shareart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class RegisterActivity extends AppCompatActivity {

    private ImageButton atzeraBotoia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        hasieratu();
    }

    private void hasieratu() {
        atzeraBotoia= findViewById(R.id.atzera_botoia);
        atzeraBotoia.setOnClickListener(this::atzeraJoan);
    }

    private void atzeraJoan(View view) {
        finish();
    }
}