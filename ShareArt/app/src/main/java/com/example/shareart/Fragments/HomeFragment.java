package com.example.shareart.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shareart.R;
import com.example.shareart.activities.PostActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeFragment extends Fragment {

    private View view;
    private FloatingActionButton floatingActionButton;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        floatingActionButton=view.findViewById(R.id.floatingButtonArgitalpen);
        floatingActionButton.setOnClickListener(this::argitaratuArgazkiBat);
        return view;
    }

    private void argitaratuArgazkiBat(View view) {
        Intent intent = new Intent(getContext(), PostActivity.class);
        startActivity(intent);

    }
}