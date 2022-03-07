package com.example.shareart.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.shareart.R;
import com.example.shareart.activities.PerfilaEguneratuActivity;
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

public class PerfilaFragment extends Fragment {

    private View view;
    private LinearLayout linearLayout;
    private TextView erabiltzaileIzenaTextView;
    private TextView korreoaTextView;
    private TextView argitalpenKopurua;
    private TextView dataTextView;
    private CircleImageView perfilekoArgazkia;
    private RecyclerView recyclerView;

    private AuthProvider authProvider;
    private UserProvider userProvider;
    private PostProvider postProvider;
    private MyPostAdapter postAdapter;

    public PerfilaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_perfila, container, false);
        // TextView
        erabiltzaileIzenaTextView = view.findViewById(R.id.textViewErabiltzaileIzena);
        korreoaTextView = view.findViewById(R.id.textViewKorreoa);
        argitalpenKopurua = view.findViewById(R.id.argitarapenZenbakia);
        dataTextView = view.findViewById(R.id.textViewData);
        // LinearLayout
        linearLayout = view.findViewById(R.id.perfilaEditatuLink);
        // CircleImageView
        perfilekoArgazkia = view.findViewById(R.id.perfilaArgazkia);
        // OnClickListener
        linearLayout.setOnClickListener(this::perfilaEditaturaJoan);
        // Providers
        authProvider = new AuthProvider();
        userProvider = new UserProvider();
        postProvider = new PostProvider();
        // Erabiltzailea hasieratu
        getErabiltzailearenInformazioa();
        getArgitalpenKopurua();
        // RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewNireArgitarapenak);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        return view;
    }

    private void getErabiltzailearenInformazioa() {
        userProvider.getErabiltzailea(authProvider.getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
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
                                Picasso.with(getContext()).load(argazkiaUrl).into(perfilekoArgazkia);
                            }
                        }
                    }

                    if (documentSnapshot.contains("sortzeData")) {
                        String relativeTime = RelativeTime.timeFormatAMPM(documentSnapshot.getLong("sortzeData"));
                        dataTextView.setText(relativeTime + "-an sartu zinen");
                    }
                }
            }
        });
    }

    private void getArgitalpenKopurua() {
        postProvider.getArgitalpenakByErabiltzailea(authProvider.getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int zenbat = queryDocumentSnapshots.size();
                argitalpenKopurua.setText(zenbat + "");
            }
        });
    }

    private void perfilaEditaturaJoan(View view) {
        Intent intent = new Intent(getContext(), PerfilaEguneratuActivity.class);
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        Query query = postProvider.getArgitalpenakByErabiltzailea(authProvider.getUid());
        FirestoreRecyclerOptions<Argitalpena> options =
                new FirestoreRecyclerOptions.Builder<Argitalpena>()
                        .setQuery(query, Argitalpena.class)
                        .build();

        // PostAdapter
        postAdapter = new MyPostAdapter(options, getContext());
        recyclerView.setAdapter(postAdapter);
        postAdapter.startListening();
    }
}