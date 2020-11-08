package com.pk.musicplayer.ui.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pk.musicplayer.R;
import com.pk.musicplayer.ui.fragments.HomeFragment;
import com.pk.musicplayer.ui.fragments.NowPlayingFragment;
import com.pk.musicplayer.ui.fragments.SearchFragment;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    private BottomNavigationView.OnNavigationItemSelectedListener navListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment selectedFragment;

            switch (item.getItemId()) {
                case R.id.nav_home:
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.nav_nowPlaying:
                    selectedFragment = new NowPlayingFragment();
                    break;
                case R.id.nav_search:
                    selectedFragment = new SearchFragment();
                    break;

                default:
                    // Nothing selected
                    return false;
            }

            // Launch the selected fragment
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    selectedFragment).commit();

            // Highlight the selected fragment in UI (BottomNavBar)
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find views
        bottomNavigationView = findViewById(R.id.bottom_nav); // FrameLayout

        // On selected listeners
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        // Launch Home fragment by default
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new HomeFragment()).commit();
    }
}