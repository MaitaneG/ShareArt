package com.example.shareart.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
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
import com.example.shareart.providers.UserProvider;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class HomeFragment extends Fragment {

    private View view;
    private FloatingActionButton floatingActionButton;
    private Toolbar toolbar;
    private RecyclerView recyclerView;

    private AuthProvider authProvider;
    private PostProvider postProvider;
    private PostAdapter postAdapter;
    private UserProvider userProvider;

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
        // Providerrak
        authProvider = new AuthProvider();
        postProvider = new PostProvider();
        userProvider = new UserProvider();
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

    private void loadPosts() {
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

    private void bilatuPosts(String s) {
        userProvider.getErabiltzaileaByErabiltzaileIzena(s).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (int i=0;i<queryDocumentSnapshots.size();i++){

                    Query query = postProvider.getArgitalpenakByErabiltzailea(queryDocumentSnapshots.getDocuments().get(i).getId());
                    FirestoreRecyclerOptions<Argitalpena> options =
                            new FirestoreRecyclerOptions.Builder<Argitalpena>()
                                    .setQuery(query, Argitalpena.class)
                                    .build();

                    // PostAdapter
                    postAdapter = new PostAdapter(options, getContext());
                    recyclerView.setAdapter(postAdapter);
                    postAdapter.startListening();
                }
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.bilatzailea_eta_sesioa_itxi_menu, menu);

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
        loadPosts();
    }

    @Override
    public void onStop() {
        super.onStop();
        postAdapter.stopListening();
    }
}