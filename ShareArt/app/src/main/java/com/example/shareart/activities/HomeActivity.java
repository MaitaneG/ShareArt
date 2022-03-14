package com.example.shareart.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.example.shareart.fragments.FiltroakFragment;
import com.example.shareart.fragments.HomeFragment;
import com.example.shareart.fragments.ProfilaFragment;
import com.example.shareart.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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
        SwipeListener swipeListener = new SwipeListener(coordinatorLayout);
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

    /**
     * Klase honek irristatzeaz arduratzen da
     */
    private class SwipeListener implements View.OnTouchListener {
        GestureDetector gestureDetector;

        /**
         * Klasearen konstruktorea
         *
         * @param view
         */
        SwipeListener(View view) {
            int threshold = 100;
            int velocity_threshold = 100;

            /**
             * Irristapenaren listenerra
             */
            GestureDetector.SimpleOnGestureListener listener = new GestureDetector.SimpleOnGestureListener() {
                /**
                 * Klikatzerakoan
                 * @param e
                 * @return
                 */
                @Override
                public boolean onDown(MotionEvent e) {
                    return true;
                }

                /**
                 * Irristatzerakoan
                 * @param e1
                 * @param e2
                 * @param velocityX
                 * @param velocityY
                 * @return
                 */
                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    // X eta Y-ren desberdintasuna
                    float xDiff = e2.getX() - e1.getX();
                    float yDiff = e2.getY() - e1.getY();

                    try {
                        // X-ren diferentzia handiago denean y-rena baino
                        if (Math.abs(xDiff) > Math.abs(yDiff)) {
                            // X-ren diferentzia handiagoa denean guk zehaztutakoa baino
                            // Abiadura handiagoa bada guk zehaztutakoa baino
                            if (Math.abs(xDiff) > threshold && Math.abs(velocityX) > velocity_threshold) {
                                if (xDiff > 0) {
                                    openFragment(new HomeFragment());
                                } else {
                                    openFragment(new FiltroakFragment());
                                }
                                return true;
                            }
                        }
                    } catch (Exception ex) {
                        ex.getStackTrace();
                    }
                    return false;
                }
            };
            gestureDetector = new GestureDetector(listener);
            view.setOnTouchListener(this);
        }

        /**
         * Ikutzerakoan
         *
         * @param view
         * @param motionEvent
         * @return
         */
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            return gestureDetector.onTouchEvent(motionEvent);
        }
    }
}