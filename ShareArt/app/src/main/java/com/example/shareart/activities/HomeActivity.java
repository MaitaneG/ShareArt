package com.example.shareart.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.shareart.Fragments.FiltroakFragment;
import com.example.shareart.Fragments.HomeFragment;
import com.example.shareart.Fragments.PerfilaFragment;
import com.example.shareart.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        hasieratu();
    }

    private void hasieratu() {
        bottomNavigation = findViewById(R.id.bottom_navigation);

        bottomNavigation.setOnItemSelectedListener(this::nabigatu);
        // Fragmentu lehenetsia
        openFragment(new HomeFragment());
    }

    private boolean nabigatu(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            // Home fragmentua
            case R.id.navigation_home:
                openFragment(new HomeFragment());
                return true;
            // Filtroak fragmentua
            case R.id.navigation_filtroak:
                openFragment(new FiltroakFragment());
                return true;
            // Perfila fragmentua
            case R.id.navigation_perfila:
                openFragment(new PerfilaFragment());
                return true;
        }
        return false;
    }
    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}