package com.example.shareart.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shareart.R;
import com.example.shareart.adapters.PostAdapter;
import com.example.shareart.models.Argitalpena;
import com.example.shareart.providers.PostProvider;
import com.example.shareart.providers.UserProvider;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class FiltratutakoaActivity extends AppCompatActivity {

    private TextView textViewArgitalpenKantitatea;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private PostAdapter postAdapter;

    private PostProvider postProvider;
    private UserProvider userProvider;

    private String extraKategoria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtratutakoa);

        hasieratu();
    }

    private void hasieratu() {
        // TextView
        textViewArgitalpenKantitatea = findViewById(R.id.textViewArgitalpenKopuruaKategoria);
        // Providers
        postProvider = new PostProvider();
        userProvider = new UserProvider();
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

    private void loadPosts() {
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

    private void bilatuPosts(String s) {
        userProvider.getErabiltzaileaByErabiltzaileIzena(s).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (int i = 0; i < queryDocumentSnapshots.size(); i++) {

                    Query query = postProvider.getArgitalpenByUserAndKategoria(queryDocumentSnapshots.getDocuments().get(i).getId(), extraKategoria);
                    FirestoreRecyclerOptions<Argitalpena> options =
                            new FirestoreRecyclerOptions.Builder<Argitalpena>()
                                    .setQuery(query, Argitalpena.class)
                                    .build();


                    // PostAdapter
                    postAdapter = new PostAdapter(options, FiltratutakoaActivity.this, textViewArgitalpenKantitatea);
                    textViewArgitalpenKantitatea.setText(String.valueOf(options.getSnapshots().size()));
                    recyclerView.setAdapter(postAdapter);
                    postAdapter.startListening();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bilatzailea_menu, menu);

        MenuItem item = menu.findItem(R.id.bilatu);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (!TextUtils.isEmpty(s)) {
                    bilatuPosts(s);
                } else {
                    loadPosts();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (!TextUtils.isEmpty(s)) {
                    bilatuPosts(s);
                } else {
                    loadPosts();
                }
                return false;
            }
        });

        return true;
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
        loadPosts();
    }

    @Override
    public void onStop() {
        super.onStop();
        postAdapter.stopListening();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (postAdapter != null) {
            if (postAdapter.getListenerRegistrationLike() != null && postAdapter.getListenerRegistrationKomentarioa() != null) {
                postAdapter.getListenerRegistrationLike().remove();
                postAdapter.getListenerRegistrationKomentarioa().remove();
            }
        }
    }
}