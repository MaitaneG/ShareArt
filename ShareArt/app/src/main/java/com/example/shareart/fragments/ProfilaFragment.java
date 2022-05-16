package com.example.shareart.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shareart.R;
import com.example.shareart.activities.KonfigurazioaActivity;
import com.example.shareart.activities.ProfilaEguneratuActivity;
import com.example.shareart.adapters.MyPostAdapter;
import com.example.shareart.models.Argitalpena;
import com.example.shareart.providers.AuthProvider;
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

public class ProfilaFragment extends Fragment {

    private View view;
    private ImageView perfilaEguneratuLinka;
    private ImageView konfigurazioLinka;
    private ImageView egiaztatuaImageView;
    private TextView erabiltzaileIzenaTextView;
    private TextView korreoaTextView;
    private TextView argitalpenKopuruaTextView;
    private TextView argitalpenTexView;
    private TextView deskribapenaTextView;
    private TextView dataTextView;
    private CircleImageView perfilekoArgazkia;
    private RecyclerView recyclerView;
    private Toolbar toolbar;

    private AuthProvider authProvider;
    private UserProvider userProvider;
    private PostProvider postProvider;
    private MyPostAdapter postAdapter;

    private ListenerRegistration listenerRegistration;

    public ProfilaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profila, container, false);
        // TextView
        erabiltzaileIzenaTextView = view.findViewById(R.id.textViewErabiltzaileIzena);
        korreoaTextView = view.findViewById(R.id.textViewKorreoa);
        argitalpenKopuruaTextView = view.findViewById(R.id.argitarapenZenbakia);
        dataTextView = view.findViewById(R.id.textViewData);
        argitalpenTexView = view.findViewById(R.id.textViewArgitalpen);
        deskribapenaTextView = view.findViewById(R.id.textViewDeskribapena);
        egiaztatuaImageView = view.findViewById(R.id.imageViewEgiaztatua);
        // ImageView
        perfilaEguneratuLinka = view.findViewById(R.id.perfilaEditatuLink);
        konfigurazioLinka = view.findViewById(R.id.konfigurazioauLink);
        // CircleImageView
        perfilekoArgazkia = view.findViewById(R.id.perfilaArgazkia);
        // OnClickListener
        perfilaEguneratuLinka.setOnClickListener(this::perfilaEditaturaJoan);
        konfigurazioLinka.setOnClickListener(this::konfigurazioraJoan);
        // Toolbar
        toolbar = view.findViewById(R.id.toolbarProfila);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("");
        // Providers
        authProvider = new AuthProvider();
        userProvider = new UserProvider();
        postProvider = new PostProvider();
        // Erabiltzailea hasieratu
        getErabiltzailearenInformazioa();
        getExistitzenDenArgitalpenaEtaKopurua();
        // RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewNireArgitarapenak);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        return view;
    }

    private void getErabiltzailearenInformazioa() {
        userProvider.getErabiltzailea(authProvider.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
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
                                deskribapenaTextView.setText("Ez daukazu deskribapenik, aldatu zure deskripzioa.");
                            }
                        } else {
                            deskribapenaTextView.setText("Ez daukazu deskribapenik, aldatu zure deskripzioa.");
                        }
                    }

                    if (documentSnapshot.contains("argazkia_profila_url")) {
                        String argazkiaUrl = documentSnapshot.getString("argazkia_profila_url");

                        if (argazkiaUrl != null) {
                            if (!argazkiaUrl.isEmpty()) {
                                Picasso.with(getContext()).load(argazkiaUrl).into(perfilekoArgazkia);
                            }
                        }
                    }

                    if (documentSnapshot.contains("sortze_data")) {
                        String relativeTime = RelativeTime.timeFormatAMPM(documentSnapshot.getLong("sortze_data"));
                        dataTextView.setText(relativeTime + "-an sartu zinen");
                    }

                    if (documentSnapshot.contains("egiaztatua")){
                        userProvider.getErabiltzailea(authProvider.getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
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
        listenerRegistration = postProvider.getArgitalpenakByErabiltzailea(authProvider.getUid()).addSnapshotListener(new EventListener<QuerySnapshot>() {
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

    private void perfilaEditaturaJoan(View view) {
        Intent intent = new Intent(getContext(), ProfilaEguneratuActivity.class);
        startActivity(intent);
    }

    private void konfigurazioraJoan(View view) {
        Intent intent = new Intent(getContext(), KonfigurazioaActivity.class);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (postAdapter != null) {
            listenerRegistration.remove();
        }
    }
}