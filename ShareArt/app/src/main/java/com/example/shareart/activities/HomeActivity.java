package com.example.shareart.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.example.shareart.fragments.FiltroakFragment;
import com.example.shareart.fragments.HomeFragment;
import com.example.shareart.fragments.ProfilaFragment;
import com.example.shareart.R;
import com.example.shareart.providers.AuthProvider;
import com.example.shareart.providers.UserProvider;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;

/**
 * Fragmentuak dituen activity
 */
public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;
    private CoordinatorLayout coordinatorLayout;

    /**
     * HomeActivity-a sortzen denean
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        hasieratu();
    }

    /**
     * Konponenteak hasieratzen dira
     */
    private void hasieratu() {
        // NavigationMenu
        bottomNavigation = findViewById(R.id.bottom_navigation);
        // OnItemSelectedListener
        bottomNavigation.setOnItemSelectedListener(this::nabigatu);
        // Fragmentu lehenetsia
        openFragment(new HomeFragment());
        coordinatorLayout = findViewById(R.id.coordinator);
        // Provider
        AuthProvider authProvider = new AuthProvider();
        UserProvider userProvider = new UserProvider();
        userProvider.getErabiltzailea(authProvider.getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getString("erabiltzaile_izena").equals("")) {
                    Intent intent = new Intent(HomeActivity.this, ProfilaBeteActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * Fragmentuen arteko nabigazioa egiteko
     *
     * @param menuItem
     * @return
     */
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
                openFragment(new ProfilaFragment());
                return true;
        }
        return false;
    }

    /**
     * Fragmentua zabaltzeko
     *
     * @param fragment
     */
    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * Atzerako botoia klikatzean
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}