package com.example.shareart.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shareart.R;
import com.example.shareart.adapters.PostAdapter;
import com.example.shareart.models.Argitalpena;
import com.example.shareart.providers.PostProvider;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

public class FiltratutakoaActivity extends AppCompatActivity {

    private TextView textViewArgitalpenKantitatea;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private PostAdapter postAdapter;

    private PostProvider postProvider;

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
        // TextView
        textViewArgitalpenKantitatea = findViewById(R.id.textViewArgitalpenKopuruaKategoria);
        // Providers
        postProvider = new PostProvider();
        // Kategoria
        extraKategoria = getIntent().getStringExtra("kategoria");
        // Tresna-barra
        toolbar = findViewById(R.id.ToolBar);
        toolbar.setTitle(extraKategoria);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // RecyclerView
        recyclerView = findViewById(R.id.recyclerViewArgitarapenakKategoria);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(FiltratutakoaActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        Query query = postProvider.getArgitalpenByKategoria(extraKategoria);
        FirestoreRecyclerOptions<Argitalpena> options =
                new FirestoreRecyclerOptions.Builder<Argitalpena>()
                        .setQuery(query, Argitalpena.class)
                        .build();

        // PostAdapter
        postAdapter = new PostAdapter(options, FiltratutakoaActivity.this, textViewArgitalpenKantitatea);
        recyclerView.setAdapter(postAdapter);
        postAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        postAdapter.stopListening();
    }

}