package com.example.shareart.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shareart.R;
import com.example.shareart.adapters.CommentAdapter;
import com.example.shareart.models.FCMBody;
import com.example.shareart.models.FCMResponse;
import com.example.shareart.models.Komentarioa;
import com.example.shareart.providers.AuthProvider;
import com.example.shareart.providers.CommentProvider;
import com.example.shareart.providers.NotificationProvider;
import com.example.shareart.providers.PostProvider;
import com.example.shareart.providers.TokenProvider;
import com.example.shareart.providers.UserProvider;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KomentarioakActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FloatingActionButton komentatuBotoia;
    private CommentAdapter commentAdapter;
    private Toolbar toolbar;

    private CommentProvider commentProvider;
    private PostProvider postProvider;
    private AuthProvider authProvider;
    private NotificationProvider notificationProvider;
    private TokenProvider tokenProvider;
    private UserProvider userProvider;

    private String extraPostId;
    private String komentatzailea;

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
        // Toolbar
        toolbar = findViewById(R.id.ToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Providers
        commentProvider = new CommentProvider();
        postProvider = new PostProvider();
        authProvider = new AuthProvider();
        notificationProvider = new NotificationProvider();
        tokenProvider = new TokenProvider();
        userProvider = new UserProvider();
        // OnClickListener
        komentatuBotoia.setOnClickListener(this::komentatu);

        userProvider.getErabiltzailea(authProvider.getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                komentatzailea = documentSnapshot.getString("erabiltzaile_izena");
            }
        });
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
        komentarioa.setMezua(mezua.trim());
        komentarioa.setId_erabiltzailea(authProvider.getUid());
        komentarioa.setData(new Date().getTime());
        komentarioa.setId_argitalpen(extraPostId.trim());

        commentProvider.createKomentarioa(komentarioa).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    abisuaBidali(mezua);
                    Toast.makeText(KomentarioakActivity.this, "Komentarioa ondo gorde da", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(KomentarioakActivity.this, "Errore bat egon da komentarioa gordetzean", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void abisuaBidali(String mezua) {
        postProvider.getArgitalpenaById(extraPostId).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getString("id_user") != null) {
                    tokenProvider.getToken(documentSnapshot.getString("id_user")).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.contains("token")) {
                                String token = documentSnapshot.getString("token");
                                Map<String, String> data = new HashMap<>();
                                data.put("title", "Komentario berria (" + komentatzailea + ")");
                                data.put("body", mezua);
                                FCMBody body = new FCMBody(token, "high", "4500s", data);
                                notificationProvider.sendNotification(body).enqueue(new Callback<FCMResponse>() {
                                    @Override
                                    public void onResponse(Call<FCMResponse> call, Response<FCMResponse> response) {
                                        if (response.body() != null) {
                                            if (response.body().getSuccess() == 1) {
                                                Toast.makeText(KomentarioakActivity.this, "SE HA ENVIADO EL MENSAJE", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<FCMResponse> call, Throwable t) {

                                    }
                                });
                            } else {
                                Toast.makeText(KomentarioakActivity.this, "NO HAY TOKEN", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
}