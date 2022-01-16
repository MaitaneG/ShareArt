package com.example.shareart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView erregistroLinka;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hasieratu();
    }

    private void hasieratu() {
        erregistroLinka = findViewById(R.id.erregistratu_linka);

        erregistroLinka.setOnClickListener(this::erregistroraJoan);
    }

    private void erregistroraJoan(View view) {
        Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
        startActivity(intent);
    }


}