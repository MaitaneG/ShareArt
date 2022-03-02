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
import com.example.shareart.activities.PerfilaEguneratuActivity;
import com.example.shareart.providers.AuthProvider;
import com.example.shareart.providers.PostProvider;
import com.example.shareart.providers.UserProvider;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
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

    private AuthProvider authProvider;
    private UserProvider userProvider;
    private PostProvider postProvider;

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
        dataTextView=view.findViewById(R.id.textViewData);
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

                    if (documentSnapshot.contains("sortzeData")){
                        dataTextView.setText(documentSnapshot.getString("sortzeData")+"-an sartu zen");
                    }
                }
            }
        });
    }

    private void getArgitalpenKopurua() {
        postProvider.getErabiltzaileBakoitzekoArgitalpenak(authProvider.getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int zenbat = queryDocumentSnapshots.size();
                argitalpenKopurua.setText(zenbat+"");
            }
        });
    }

    private void perfilaEditaturaJoan(View view) {
        Intent intent = new Intent(getContext(), PerfilaEguneratuActivity.class);
        startActivity(intent);
    }
}