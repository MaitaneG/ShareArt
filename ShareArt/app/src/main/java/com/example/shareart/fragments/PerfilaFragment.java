package com.example.shareart.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.shareart.R;
import com.example.shareart.activities.HomeActivity;
import com.example.shareart.activities.PerfilaEguneratuActivity;
import com.example.shareart.providers.AuthProvider;
import com.example.shareart.providers.UserProvider;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class PerfilaFragment extends Fragment {

    private View view;
    private LinearLayout linearLayout;
    private TextView erabiltzaileIzenaTextView;
    private TextView korreoaTextView;
    private CircleImageView perfilekoArgazkia;

    private AuthProvider authProvider;
    private UserProvider userProvider;

    public PerfilaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_perfila, container, false);
        // TextView
        erabiltzaileIzenaTextView = view.findViewById(R.id.textInputEditTextErabiltzaileIzena);
        korreoaTextView = view.findViewById(R.id.textInputEditTextKorreoa);
        // LinearLayout
        linearLayout = view.findViewById(R.id.perfilaEditatuLink);
        // CircleImageView
        perfilekoArgazkia= view.findViewById(R.id.perfilaArgazkia);
        // OnClickListener
        linearLayout.setOnClickListener(this::perfilaEditaturaJoan);
        // Providers
        authProvider = new AuthProvider();
        userProvider = new UserProvider();
        // Erabiltzailea hasieratu
        hasieratu();

        return view;
    }

    private void hasieratu() {
        userProvider.getErabiltzailea(authProvider.getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    erabiltzaileIzenaTextView.setText(documentSnapshot.getString("erabiltzaileIzena"));
                    korreoaTextView.setText(documentSnapshot.getString("email"));

                    String argazkiaUrl = documentSnapshot.getString("argazkiaProfila");
                    Picasso.with(getContext()).load(argazkiaUrl).into(perfilekoArgazkia);
                }
            }
        });
    }


    private void perfilaEditaturaJoan(View view) {
        Intent intent = new Intent(getContext(), PerfilaEguneratuActivity.class);
        startActivity(intent);
    }
}