package com.example.shareart.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.shareart.R;
import com.example.shareart.adapters.CommentAdapter;
import com.example.shareart.models.Komentarioa;
import com.example.shareart.providers.AuthProvider;
import com.example.shareart.providers.CommentProvider;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.util.Date;

public class KomentarioakActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FloatingActionButton komentatuBotoia;
    private CommentAdapter commentAdapter;

    private CommentProvider commentProvider;
    private AuthProvider authProvider;

    private String extraPostId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_komentarioak);

        hasieratu();
        alertKomentarioa();
    }


    private void hasieratu() {
        // FloatingButton
        komentatuBotoia = findViewById(R.id.floatingButtonKomentarioa);
        // PostId
        extraPostId = getIntent().getStringExtra("postId");
        // RecyclerView
        recyclerView = findViewById(R.id.recyclerViewKomentarioak);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(KomentarioakActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        // Providers
        commentProvider = new CommentProvider();
        authProvider = new AuthProvider();
        // OnClickListener
        komentatuBotoia.setOnClickListener(this::komentatu);
    }

    private void alertKomentarioa() {
        EditText editText = new EditText(KomentarioakActivity.this);
        editText.setHint("Jarri komentario bat");
        editText.setPadding(20, 35, 25, 35);

        // EditText-en marginak zehazteko
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(36, 36, 36, 0);

        editText.setLayoutParams(layoutParams);
        RelativeLayout container = new RelativeLayout(KomentarioakActivity.this);
        RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        container.setLayoutParams(relativeParams);

        container.addView(editText);
        new AlertDialog.Builder(KomentarioakActivity.this)
                .setView(container)
                .setPositiveButton("Komentatu", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String mezua = editText.getText().toString();

                        if (mezua.isEmpty()) {
                            Toast.makeText(KomentarioakActivity.this, "Zerbait idatzi behar duzu", Toast.LENGTH_SHORT).show();
                        } else {
                            komentarioaSortu(mezua);
                        }
                    }
                })
                .setNegativeButton("Itxi", null)
                .show();
    }

    private void komentarioaSortu(String mezua) {
        Komentarioa komentarioa = new Komentarioa();
        komentarioa.setMezua(mezua);
        komentarioa.setIdErabiltzailea(authProvider.getUid());
        komentarioa.setData(new Date().getTime());
        komentarioa.setIdArgitalpen(extraPostId);

        commentProvider.createKomentarioa(komentarioa).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(KomentarioakActivity.this, "Komentarioa ondo gorde da", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(KomentarioakActivity.this, "Errore bat egon da komentarioa gordetzean", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void komentatu(View view) {
        alertKomentarioa();
    }

    @Override
    public void onStart() {
        super.onStart();
        Query query = commentProvider.getKomentarioakByArgitalpen(extraPostId);
        FirestoreRecyclerOptions<Komentarioa> options =
                new FirestoreRecyclerOptions.Builder<Komentarioa>()
                        .setQuery(query, Komentarioa.class)
                        .build();

        // PostAdapter
        commentAdapter = new CommentAdapter(options, KomentarioakActivity.this);
        recyclerView.setAdapter(commentAdapter);
        commentAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        commentAdapter.stopListening();
    }
}