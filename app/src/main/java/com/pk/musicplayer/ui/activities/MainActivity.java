package com.pk.musicplayer.ui.activities;

import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pk.musicplayer.R;
import com.pk.musicplayer.helper.MediaBrowserClientHelper;
import com.pk.musicplayer.ui.fragments.BottomMediaControllerFragment;
import com.pk.musicplayer.ui.fragments.HomeFragment;
import com.pk.musicplayer.ui.fragments.NowPlayingFragment;
import com.pk.musicplayer.ui.fragments.SearchFragment;
import com.pk.musicplayer.viewmodels.NowPlayingViewModel;

public class MainActivity extends AppCompatActivity implements IMainActivity {

    private BottomNavigationView bottomNavigationView;
    private MediaBrowserClientHelper mediaBrowserClientHelper;
    private boolean mIsPlaying;
    private NowPlayingViewModel mNowPlayingViewModel;


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


    //-------------------------------------Lifecycle methods--------------------------------------//

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

        // Create MediaBrowserCompat
        mediaBrowserClientHelper = new MediaBrowserClientHelper(this);

        mNowPlayingViewModel = ViewModelProviders.of(this).get(NowPlayingViewModel.class);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Connect to MediaBrowser
        mediaBrowserClientHelper.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Disconnect from MediaBrowser
        mediaBrowserClientHelper.onStop();
    }

    @Override
    public void onMediaSelected(Uri mediaUri) {
        mediaBrowserClientHelper.getTransportControls().playFromUri(mediaUri, null);
        mIsPlaying = true;

        getBottomMediaController().setIsPlaying(true);
        getBottomMediaController().setMediaTitle("Playing Song");

        mNowPlayingViewModel.setIsPlaying(true);

    }


    //------------------------------- Bottom Media Controller ------------------------------------//

    private BottomMediaControllerFragment getBottomMediaController() {
        return (BottomMediaControllerFragment) getSupportFragmentManager()
                .findFragmentById(R.id.bottom_media_controller);
    }

    @Override
    public void playPause() {
        if (mIsPlaying) {
            // pause the playback
            mediaBrowserClientHelper.getTransportControls().pause();

            // show play button
            getBottomMediaController().setIsPlaying(false);

            // show play button in now playing
            mNowPlayingViewModel.setIsPlaying(false);

            mIsPlaying = false;
        } else {
            // resume playback
            mediaBrowserClientHelper.getTransportControls().play();

            // show pause button
            getBottomMediaController().setIsPlaying(true);

            // show pause button in now playing
            mNowPlayingViewModel.setIsPlaying(true);

            mIsPlaying = true;
        }
    }
}