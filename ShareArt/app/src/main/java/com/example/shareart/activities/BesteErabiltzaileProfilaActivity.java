package com.example.shareart.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shareart.R;
import com.example.shareart.adapters.MyPostAdapter;
import com.example.shareart.models.Argitalpena;
import com.example.shareart.providers.PostProvider;
import com.example.shareart.providers.UserProvider;
import com.example.shareart.utils.RelativeTime;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class BesteErabiltzaileProfilaActivity extends AppCompatActivity {

    private TextView erabiltzaileIzenaTextView;
    private TextView korreoaTextView;
    private TextView argitalpenKopuruaTextView;
    private TextView dataTextView;
    private TextView deskribapenaTextView;
    private TextView argitalpenTexView;
    private ImageView egiaztatuaImageView;
    private CircleImageView perfilekoArgazkia;
    private RecyclerView recyclerView;
    private Toolbar toolbar;

    private UserProvider userProvider;
    private PostProvider postProvider;
    private MyPostAdapter postAdapter;

    private ListenerRegistration listenerRegistration;

    private String extraErabiltzaileId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beste_erabiltzaile_profila);

        hasieratu();

        extraErabiltzaileId = getIntent().getStringExtra("erabiltzaileId");

        // Erabiltzailea hasieratu
        getErabiltzailearenInformazioa();
        getExistitzenDenArgitalpenaEtaKopurua();
    }

    private void hasieratu() {
        // TextView
        erabiltzaileIzenaTextView = findViewById(R.id.textViewErabiltzaileIzenaBesteErabiltzaile);
        korreoaTextView = findViewById(R.id.textViewKorreoaBesteErabiltzaile);
        argitalpenKopuruaTextView = findViewById(R.id.argitarapenZenbakiaBesteErabiltzaile);
        dataTextView = findViewById(R.id.textViewDataBesteErabiltzaile);
        argitalpenTexView = findViewById(R.id.textViewArgitalpenBesteErabiltzaile);
        deskribapenaTextView = findViewById(R.id.textViewDeskribapenaBesteErabiltzaile);
        // ImageView
        egiaztatuaImageView = findViewById(R.id.imageViewEgiaztatuaBesteErabiltzaile);
        // CircleImageView
        perfilekoArgazkia = findViewById(R.id.perfilaArgazkiaBesteErabiltzaile);
        // Providers
        userProvider = new UserProvider();
        postProvider = new PostProvider();
        // Toolbar
        toolbar = findViewById(R.id.toolbarProfila);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // RecyclerView
        recyclerView = findViewById(R.id.recyclerViewNireArgitarapenakBesteErabiltzaile);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BesteErabiltzaileProfilaActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void getErabiltzailearenInformazioa() {
        userProvider.getErabiltzailea(extraErabiltzaileId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    if (documentSnapshot.contains("erabiltzaile_izena")) {
                        erabiltzaileIzenaTextView.setText(documentSnapshot.getString("erabiltzaile_izena"));
                    }

                    if (documentSnapshot.contains("email")) {
                        korreoaTextView.setText(documentSnapshot.getString("email"));
                    }

                    if (documentSnapshot.contains("deskribapena")) {
                        String deskribapena = documentSnapshot.getString("deskribapena");
                        if (deskribapena != null) {
                            if (!deskribapena.isEmpty()) {
                                deskribapenaTextView.setText(deskribapena);
                            } else {
                                deskribapenaTextView.setText("Erabiltzaile honek ez dauka deskripziorik");
                            }
                        } else {
                            deskribapenaTextView.setText("Erabiltzaile honek ez dauka deskripziorik");
                        }

                    }

                    if (documentSnapshot.contains("argazkia_profila_url")) {
                        String argazkiaUrl = documentSnapshot.getString("argazkia_profila_url");

                        if (argazkiaUrl != null) {
                            if (!argazkiaUrl.isEmpty()) {
                                Picasso.with(BesteErabiltzaileProfilaActivity.this).load(argazkiaUrl).into(perfilekoArgazkia);
                            }
                        }
                    }

                    if (documentSnapshot.contains("sortze_data")) {
                        String relativeTime = RelativeTime.timeFormatAMPM(documentSnapshot.getLong("sortze_data"));
                        dataTextView.setText(relativeTime + "-an sartu zen");
                    }

                    if (documentSnapshot.contains("egiaztatua")){
                        userProvider.getErabiltzailea(extraErabiltzaileId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                if (value != null) {
                                    if (value.contains("egiaztatua") && value.getBoolean("egiaztatua") != null && value.getBoolean("egiaztatua")) {
                                        egiaztatuaImageView.setVisibility(View.VISIBLE);
                                    }else{
                                        egiaztatuaImageView.setVisibility(View.GONE);
                                    }
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    private void getExistitzenDenArgitalpenaEtaKopurua() {
        listenerRegistration = postProvider.getArgitalpenakByErabiltzailea(extraErabiltzaileId).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null) {
                    if (value.size() > 0) {
                        argitalpenTexView.setText("Argitalpenak");
                        argitalpenKopuruaTextView.setText(value.size() + "");
                    } else {
                        argitalpenTexView.setText("Ez daude argitalpenik");
                    }
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Query query = postProvider.getArgitalpenakByErabiltzailea(extraErabiltzaileId);
        FirestoreRecyclerOptions<Argitalpena> options =
                new FirestoreRecyclerOptions.Builder<Argitalpena>()
                        .setQuery(query, Argitalpena.class)
                        .build();

        // PostAdapter
        postAdapter = new MyPostAdapter(options, BesteErabiltzaileProfilaActivity.this);
        recyclerView.setAdapter(postAdapter);
        postAdapter.startListening();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
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
            listenerRegistration.remove();
        }
    }
}