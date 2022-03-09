package com.example.shareart.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.shareart.R;
import com.example.shareart.activities.FiltratutakoaActivity;
import com.example.shareart.activities.HomeActivity;
import com.example.shareart.activities.MainActivity;
import com.example.shareart.providers.AuthProvider;

public class FiltroakFragment extends Fragment {

    private View view;
    private ImageView imageViewNatura;
    private ImageView imageViewHiperrealismo;
    private ImageView imageViewErretratoak;
    private ImageView imageViewGrafitiak;
    private ImageView imageViewKomikia;
    private ImageView imageViewIlustrazioa;
    private ImageView imageViewKarikatura;
    private ImageView imageViewNaturaHila;
    private Toolbar toolbar;

    private AuthProvider authProvider;

    public FiltroakFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_filtroak, container, false);
        // ImageView
        imageViewNatura = view.findViewById(R.id.imageViewNaturaFiltratu);
        imageViewHiperrealismo = view.findViewById(R.id.imageViewHiperrealismoFiltratu);
        imageViewErretratoak = view.findViewById(R.id.imageViewErretratoakFiltratu);
        imageViewGrafitiak = view.findViewById(R.id.imageViewGraffitiFiltratu);
        imageViewKomikia = view.findViewById(R.id.imageViewKomikiaFiltratu);
        imageViewIlustrazioa = view.findViewById(R.id.imageViewIlustrazioaFiltratu);
        imageViewKarikatura = view.findViewById(R.id.imageViewKarikaturaFiltratu);
        imageViewNaturaHila = view.findViewById(R.id.imageViewNaturaHilaFiltratu);
        // Tresna-barra
        toolbar = (Toolbar) view.findViewById(R.id.ToolBar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
        // OnClickListener
        imageViewNatura.setOnClickListener(this::filtratu);
        imageViewHiperrealismo.setOnClickListener(this::filtratu);
        imageViewErretratoak.setOnClickListener(this::filtratu);
        imageViewGrafitiak.setOnClickListener(this::filtratu);
        imageViewKomikia.setOnClickListener(this::filtratu);
        imageViewIlustrazioa.setOnClickListener(this::filtratu);
        imageViewKarikatura.setOnClickListener(this::filtratu);
        imageViewNaturaHila.setOnClickListener(this::filtratu);
        // Sesioak kudeatzeko
        authProvider = new AuthProvider();
        return view;
    }

    private void filtratu(View view) {
        String kategoria = "";
        switch (view.getId()) {
            case R.id.imageViewNaturaFiltratu:
                kategoria = "Natura";
                break;
            case R.id.imageViewHiperrealismoFiltratu:
                kategoria = "Hiperrealismo";
                break;
            case R.id.imageViewErretratoakFiltratu:
                kategoria = "Erretratoak";
                break;
            case R.id.imageViewGraffitiFiltratu:
                kategoria = "Graffiti";
                break;
            case R.id.imageViewKomikiaFiltratu:
                kategoria = "Komikiak";
                break;
            case R.id.imageViewIlustrazioaFiltratu:
                kategoria = "Ilustrazioa";
                break;
            case R.id.imageViewKarikaturaFiltratu:
                kategoria = "Karikaturak";
                break;
            case R.id.imageViewNaturaHilaFiltratu:
                kategoria = "Natura hila";
                break;
        }

        Intent intent = new Intent(getContext(), FiltratutakoaActivity.class);
        intent.putExtra("kategoria", kategoria);
        startActivity(intent);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.sesioa_itxi_menu, menu);
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
}