package com.example.shareart.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.shareart.R;

public class KomentarioakActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_komentarioak);

        hasieratu();
        alertKomentarioa();
    }


    private void hasieratu() {

    }

    private void alertKomentarioa() {
        EditText editText = new EditText(KomentarioakActivity.this);
        editText.setHint("Jarri komentario bat");
        editText.setPadding(20 ,35,25,35);

        // EditText-en marginak zehazteko
        LinearLayout.LayoutParams layoutParams= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(36,36,36,0);

        editText.setLayoutParams(layoutParams);
        RelativeLayout container =new RelativeLayout(KomentarioakActivity.this);
        RelativeLayout.LayoutParams relativeParams= new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);

        container.setLayoutParams(relativeParams);

        container.addView(editText);
        new AlertDialog.Builder(KomentarioakActivity.this)
                .setView(container)
                .setPositiveButton("Komentatu", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String komentarioa = editText.getText().toString();
                    }
                })
                .setNegativeButton("Itxi", null)
                .show();
    }

}