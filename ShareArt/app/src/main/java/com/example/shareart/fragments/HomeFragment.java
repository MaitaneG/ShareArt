package com.example.shareart.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shareart.R;
import com.example.shareart.activities.ArgitalpenActivity;
import com.example.shareart.activities.MainActivity;
import com.example.shareart.adapters.PostAdapter;
import com.example.shareart.models.Argitalpena;
import com.example.shareart.providers.AuthProvider;
import com.example.shareart.providers.PostProvider;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.Query;

public class HomeFragment extends Fragment {

    private View view;
    private FloatingActionButton floatingActionButton;
    private Toolbar toolbar;
    private RecyclerView recyclerView;

    private AuthProvider authProvider;
    private PostProvider postProvider;
    private PostAdapter postAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Fragmentu honen bista kargatzen denean
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        // Argitalpen bat egiteko botoia
        floatingActionButton = view.findViewById(R.id.floatingButtonArgitalpen);
        // Tresna-barra
        toolbar = (Toolbar) view.findViewById(R.id.ToolBar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
        // Sesioak kudeatzeko
        authProvider = new AuthProvider();
        // PostProvider
        postProvider = new PostProvider();
        // RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewArgitarapenak);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        // OnClickListenerrak
        floatingActionButton.setOnClickListener(this::argitaratuArgazkiBat);
        return view;
    }

    private void argitaratuArgazkiBat(View view) {
        Intent intent = new Intent(getContext(), ArgitalpenActivity.class);
        startActivity(intent);

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.bilatzailea_eta_sesioa_itxi_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            saioaItxi();
            return true;
        }
        return false;
    }

    private void saioaItxi() {
        authProvider.saioaItxi();

        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        Query query = postProvider.getArgitalpenGuztiak();
        FirestoreRecyclerOptions<Argitalpena> options =
                new FirestoreRecyclerOptions.Builder<Argitalpena>()
                        .setQuery(query, Argitalpena.class)
                        .build();

        // PostAdapter
        postAdapter = new PostAdapter(options, getContext());
        recyclerView.setAdapter(postAdapter);
        postAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        postAdapter.stopListening();
    }
}