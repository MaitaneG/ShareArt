package com.example.shareart.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.shareart.R;
import com.example.shareart.providers.AuthProvider;
import com.example.shareart.providers.PostProvider;
import com.example.shareart.providers.UserProvider;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileActivity extends AppCompatActivity {

    private TextView erabiltzaileIzenaTextView;
    private TextView korreoaTextView;
    private TextView argitalpenKopurua;
    private CircleImageView perfilekoArgazkia;

    private AuthProvider authProvider;
    private UserProvider userProvider;
    private PostProvider postProvider;

    private String extraErabiltzaileId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        hasieratu();

        extraErabiltzaileId=getIntent().getStringExtra("erabiltzaileId");

        // Erabiltzailea hasieratu
        getErabiltzailearenInformazioa();
        getArgitalpenKopurua();
    }

    private void hasieratu() {
        // TextView
        erabiltzaileIzenaTextView = findViewById(R.id.textInputEditTextErabiltzaileIzenaBesteErabiltzaile);
        korreoaTextView = findViewById(R.id.textInputEditTextKorreoaBesteErabiltzaile);
        argitalpenKopurua = findViewById(R.id.argitarapenZenbakiaBesteErabiltzaile);
        // CircleImageView
        perfilekoArgazkia = findViewById(R.id.perfilaArgazkiaBesteErabiltzaile);
        // Providers
        authProvider = new AuthProvider();
        userProvider = new UserProvider();
        postProvider = new PostProvider();
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

                    if (documentSnapshot.contains("argazkiaProfila")) {
                        String argazkiaUrl = documentSnapshot.getString("argazkiaProfila");

                        if (argazkiaUrl != null) {
                            if (!argazkiaUrl.isEmpty()) {
                                Picasso.with(UserProfileActivity.this).load(argazkiaUrl).into(perfilekoArgazkia);
                            }
                        }
                    }
                }
            }
        });
    }

    private void getArgitalpenKopurua() {
        postProvider.getErabiltzaileBakoitzekoArgitalpenak(extraErabiltzaileId).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int zenbat = queryDocumentSnapshots.size();
                argitalpenKopurua.setText(zenbat+"");
            }
        });
    }
}