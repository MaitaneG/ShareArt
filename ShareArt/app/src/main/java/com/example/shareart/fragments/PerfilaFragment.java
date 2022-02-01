package com.example.shareart.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.shareart.R;
import com.example.shareart.activities.PerfilaEguneratuActivity;

public class PerfilaFragment extends Fragment {

    private View view;
    private LinearLayout linearLayout;

    public PerfilaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_perfila, container, false);
        // TextView
        linearLayout = view.findViewById(R.id.perfilaEditatuLink);
        // OnClickListener
        linearLayout.setOnClickListener(this::perfilaEditaturaJoan);

        return view;
    }

    private void perfilaEditaturaJoan(View view) {
        Intent intent = new Intent(getContext(), PerfilaEguneratuActivity.class);
        startActivity(intent);
    }
}