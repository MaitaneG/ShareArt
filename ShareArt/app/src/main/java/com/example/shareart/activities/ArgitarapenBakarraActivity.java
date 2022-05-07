package com.example.shareart.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.shareart.R;
import com.example.shareart.adapters.PostAdapter;
import com.example.shareart.models.Argitalpena;
import com.example.shareart.providers.AuthProvider;
import com.example.shareart.providers.PostProvider;
import com.example.shareart.providers.UserProvider;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ArgitarapenBakarraActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;

    private PostProvider postProvider;
    private UserProvider userProvider;

    private String post_id;
    private String erabiltzaile_izena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_argitarapen_bakarra);

        post_id = getIntent().getStringExtra("postId");

        // Tresna barra
        toolbar = findViewById(R.id.ToolBar);
        toolbar.setTitle("Aukeratutako marrazkia");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Providerrak
        postProvider = new PostProvider();
        userProvider = new UserProvider();
        // RecyclerView
        recyclerView = findViewById(R.id.recyclerViewArgitarapenBakarra);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        kargatuInformazioa();
    }

    private void kargatuInformazioa() {
        postProvider.getArgitalpenaById(post_id).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                userProvider.getErabiltzailea(documentSnapshot.getString("id_user")).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        erabiltzaile_izena = documentSnapshot.getString("erabiltzaile_izena");
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.partekatu_menu, menu);

        MenuItem item = menu.findItem(R.id.partekatu);

        item.setOnMenuItemClickListener(this::partekatu);
        return true;
    }

    private boolean partekatu(MenuItem menuItem) {
        postProvider.getArgitalpenaById(post_id).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Picasso.with(ArgitarapenBakarraActivity.this).load(documentSnapshot.getString("url_argazkia")).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        Bitmap argazkia = bitmap;

                        if (argazkia != null) {
                            String shareBody = documentSnapshot.getString("deskribapena") + "\n" + "Egilea: @" + erabiltzaile_izena + "\n" + "ShareArt-etik partekatuta";

                            Uri uri = gordeArgazkiaPartekatzeko(argazkia);

                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.putExtra(Intent.EXTRA_STREAM, uri);
                            intent.putExtra(Intent.EXTRA_SUBJECT, "Gaia hemen");
                            intent.putExtra(Intent.EXTRA_TEXT, shareBody);
                            intent.setType("image/png");
                            startActivity(Intent.createChooser(intent, "Share Via"));
                        }
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                        Toast.makeText(ArgitarapenBakarraActivity.this, "Errore bat egon da partekatzerakoan", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
            }
        });
        return false;
    }

    private Uri gordeArgazkiaPartekatzeko(Bitmap argazkia) {
        File imageFolder = new File(getApplicationContext().getCacheDir(), "images");

        Uri uri = null;

        try {
            imageFolder.mkdirs();
            File file = new File(imageFolder, "shared_image.png");

            FileOutputStream stream = new FileOutputStream(file);

            argazkia.compress(Bitmap.CompressFormat.PNG, 90, stream);
            stream.flush();
            stream.close();
            uri = FileProvider.getUriForFile(getApplicationContext(), "com.example.shareart.fileprovider", file);
        } catch (Exception e) {
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return uri;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Query query = postProvider.getArgitalpenaByIdQuery(post_id);

        FirestoreRecyclerOptions<Argitalpena> options =
                new FirestoreRecyclerOptions.Builder<Argitalpena>()
                        .setQuery(query, Argitalpena.class)
                        .build();

        // PostAdapter
        postAdapter = new PostAdapter(options, this);
        recyclerView.setAdapter(postAdapter);
        postAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        postAdapter.stopListening();
    }
}