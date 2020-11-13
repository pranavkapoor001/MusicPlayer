package com.pk.musicplayer.ui.activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.MediaMetadataCompat;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pk.musicplayer.R;
import com.pk.musicplayer.db.repositories.NowPlayingMetadataRepo;
import com.pk.musicplayer.helper.MediaBrowserClientHelper;
import com.pk.musicplayer.ui.fragments.BottomMediaControllerFragment;
import com.pk.musicplayer.ui.fragments.HomeFragment;
import com.pk.musicplayer.ui.fragments.NowPlayingFragment;
import com.pk.musicplayer.ui.fragments.SearchFragment;
import com.pk.musicplayer.ui.viewmodels.NowPlayingViewModel;

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
                    showHideBottomMediaFragment(true);
                    break;
                case R.id.nav_nowPlaying:
                    selectedFragment = new NowPlayingFragment();

                    /* Hide the Bottom Media Fragment when NowPlaying(Fav)
                     * Fragment is open
                     */
                    showHideBottomMediaFragment(false);
                    break;
                case R.id.nav_search:
                    selectedFragment = new SearchFragment();
                    showHideBottomMediaFragment(true);
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

        mNowPlayingViewModel = NowPlayingViewModel.getInstance(this);

        // Add Bottom Media Fragment dynamically
        getSupportFragmentManager().beginTransaction()
                .add(R.id.bottom_media_controller_container, new BottomMediaControllerFragment())
                .commit();
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
    public void onMediaSelected() {
        mIsPlaying = true;

        // Update NowPlayingFragment & BottomMediaControllerFragment UI
        updateNowPlayingUI();
    }


    //------------------------------- Bottom Media Controller ------------------------------------//

    private BottomMediaControllerFragment getBottomMediaController() {
        return (BottomMediaControllerFragment) getSupportFragmentManager()
                .findFragmentById(R.id.bottom_media_controller_container);
    }

    @Override
    public void playPause() {
        // Nothing to play
        if (NowPlayingMetadataRepo.getInstance().getMetadata() == null) {
            Toast.makeText(this, "Nothing to play", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mIsPlaying) {
            // pause the playback
            mediaBrowserClientHelper.getTransportControls().pause();

            // show play button in bottom controller
            getBottomMediaController().setIsPlaying(false);

            // show play button in now playing
            mNowPlayingViewModel.setIsPlaying(false);

            mIsPlaying = false;
        } else {
            // resume playback
            mediaBrowserClientHelper.getTransportControls().play();

            // show pause button in bottom controller
            getBottomMediaController().setIsPlaying(true);

            // show pause button in now playing
            mNowPlayingViewModel.setIsPlaying(true);

            mIsPlaying = true;
        }
    }

    //-------------------------------- UI Update Methods -----------------------------------------//

    private void showHideBottomMediaFragment(boolean showFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        // Check if fragment is present
        BottomMediaControllerFragment bottomMediaFragment =
                (BottomMediaControllerFragment) fragmentManager
                        .findFragmentById(R.id.bottom_media_controller_container);

        if (bottomMediaFragment != null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            if (showFragment)
                fragmentTransaction.show(bottomMediaFragment);
            else
                fragmentTransaction.hide(bottomMediaFragment);
            fragmentTransaction.commit();
        }
    }

    /* This method updates the NowPlayingFragment
     * and BottomMediaController with current songs metadata and state
     */
    private void updateNowPlayingUI() {

        // Get song Uri from NowPlayingMetadata Helper
        MediaMetadataCompat metadata = NowPlayingMetadataRepo.getInstance().getMetadata();
        String songUri = metadata.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI);

        // Set MediaItem in view model
        mNowPlayingViewModel.setSongMetadata(metadata);

        // Play from Uri
        mediaBrowserClientHelper.getTransportControls()
                .playFromUri(Uri.parse(songUri), null);

        // Update bottom media player
        getBottomMediaController().setIsPlaying(true);
        getBottomMediaController()
                .setMediaTitle(metadata.getString(MediaMetadataCompat.METADATA_KEY_TITLE));

        // Update Play state in NowPlayingFragment
        mNowPlayingViewModel.setIsPlaying(true);

    }
}