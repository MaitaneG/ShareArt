package com.example.shareart.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.shareart.R;
import com.example.shareart.adapters.MyPostAdapter;
import com.example.shareart.models.Argitalpena;
import com.example.shareart.providers.AuthProvider;
import com.example.shareart.providers.PostProvider;
import com.example.shareart.providers.UserProvider;
import com.example.shareart.utils.RelativeTime;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileActivity extends AppCompatActivity {

    private TextView erabiltzaileIzenaTextView;
    private TextView korreoaTextView;
    private TextView argitalpenKopuruaTextView;
    private TextView dataTextView;
    private TextView argitalpenTexView;
    private CircleImageView perfilekoArgazkia;
    private RecyclerView recyclerView;
    private Toolbar toolbar;

    private AuthProvider authProvider;
    private UserProvider userProvider;
    private PostProvider postProvider;
    private MyPostAdapter postAdapter;

    private String extraErabiltzaileId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        hasieratu();

        extraErabiltzaileId = getIntent().getStringExtra("erabiltzaileId");

        // Erabiltzailea hasieratu
        getErabiltzailearenInformazioa();
        getArgitalpenKopurua();
        getExistitzenDenArgitalpena();
    }

    private void hasieratu() {
        // TextView
        erabiltzaileIzenaTextView = findViewById(R.id.textViewErabiltzaileIzenaBesteErabiltzaile);
        korreoaTextView = findViewById(R.id.textViewKorreoaBesteErabiltzaile);
        argitalpenKopuruaTextView = findViewById(R.id.argitarapenZenbakiaBesteErabiltzaile);
        dataTextView = findViewById(R.id.textViewDataBesteErabiltzaile);
        argitalpenTexView = findViewById(R.id.textViewArgitalpenBesteErabiltzaile);
        // CircleImageView
        perfilekoArgazkia = findViewById(R.id.perfilaArgazkiaBesteErabiltzaile);
        // Providers
        authProvider = new AuthProvider();
        userProvider = new UserProvider();
        postProvider = new PostProvider();
        // Toolbar
        toolbar = findViewById(R.id.toolbarProfila);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // RecyclerView
        recyclerView = findViewById(R.id.recyclerViewNireArgitarapenakBesteErabiltzaile);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(UserProfileActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void getErabiltzailearenInformazioa() {
        userProvider.getErabiltzailea(extraErabiltzaileId).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    if (documentSnapshot.contains("erabiltzaileIzena")) {
                        erabiltzaileIzenaTextView.setText(documentSnapshot.getString("erabiltzaileIzena"));
                    }

                    if (documentSnapshot.contains("email")) {
                        korreoaTextView.setText(documentSnapshot.getString("email"));
                    }

                    if (documentSnapshot.contains("argazkiaProfilaUrl")) {
                        String argazkiaUrl = documentSnapshot.getString("argazkiaProfilaUrl");

                        if (argazkiaUrl != null) {
                            if (!argazkiaUrl.isEmpty()) {
                                Picasso.with(UserProfileActivity.this).load(argazkiaUrl).into(perfilekoArgazkia);
                            }
                        }
                    }

                    if (documentSnapshot.contains("sortzeData")) {
                        String relativeTime = RelativeTime.timeFormatAMPM(documentSnapshot.getLong("sortzeData"));
                        dataTextView.setText(relativeTime + "-an sartu zen");
                    }

                }
            }
        });
    }

    private void getArgitalpenKopurua() {
        postProvider.getArgitalpenakByErabiltzailea(extraErabiltzaileId).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int zenbat = queryDocumentSnapshots.size();
                argitalpenKopuruaTextView.setText(zenbat + "");
            }
        });
    }

    private void getExistitzenDenArgitalpena() {
        postProvider.getArgitalpenGuztiak().get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.getDocuments().size() > 0) {
                    argitalpenTexView.setText("Argitalpenak");
                } else {
                    argitalpenTexView.setText("Ez daude argitalpeik");
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
        postAdapter = new MyPostAdapter(options, UserProfileActivity.this);
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
}