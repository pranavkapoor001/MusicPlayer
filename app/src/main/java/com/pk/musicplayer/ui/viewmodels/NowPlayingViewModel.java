package com.pk.musicplayer.ui.viewmodels;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.media.MediaMetadataCompat;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import com.pk.musicplayer.adapters.player.ExoPlayerAdapter;
import com.pk.musicplayer.db.repositories.NowPlayingMetadataRepo;

public class NowPlayingViewModel extends ViewModel {

    private static final String TAG = "NowPlayingViewModel";
    private static NowPlayingViewModel instance;
    // vars
    private final MutableLiveData<Boolean> mIsPlaying = new MutableLiveData<>();
    private final MutableLiveData<MediaMetadataCompat> mSongMetadata = new MutableLiveData<>();
    private MutableLiveData<Integer> seekBarStatus = new MutableLiveData<>();
    private Handler seekBarHandler;
    private Runnable seekBarRunnable;
    private boolean shouldUpdateSeekBar = true;

    /* Singleton: Return the same instance of NowPlayingViewModel
     * Since this instance's data needs to be shared among all observers
     */

    public static NowPlayingViewModel getInstance(Activity Activity) {
        if (instance == null)
            return ViewModelProviders.of((FragmentActivity) Activity).get(NowPlayingViewModel.class);

        else
            return instance;
    }


    //----------------------------  Getters / Setters  -------------------------------------------//

    public LiveData<Boolean> getIsPlaying() {
        return mIsPlaying;
    }

    public void setIsPlaying(boolean isPlaying) {
        mIsPlaying.setValue(isPlaying);
    }

    public LiveData<MediaMetadataCompat> getSongMetadata() {
        return mSongMetadata;
    }

    public void setSongMetadata(MediaMetadataCompat songMetadata) {
        mSongMetadata.setValue(songMetadata);
    }

    // Update SeekBar value every second
    public void updateSeekBar() {

        if (seekBarHandler != null) {
            seekBarHandler.removeCallbacks(seekBarRunnable);
            Log.e(TAG, "updateSeekBar: Removing old callback");
        }

        // Get song duration
        final long duration = NowPlayingMetadataRepo.getInstance().getMetadata()
                .getLong(MediaMetadataCompat.METADATA_KEY_DURATION) / 1000;

        Log.e("MetadataRepo", "updateSeekBar: DURATION: " + duration);

        // Paused
        if (!ExoPlayerAdapter.getExoplayer().getPlayWhenReady())
            return;

        seekBarHandler = new Handler(Looper.myLooper());
        seekBarRunnable = new Runnable() {
            int seekBarValue = 0;

            @Override
            public void run() {
                // Get current song position
                final long currentContentPos = ExoPlayerAdapter.getExoplayer().getContentPosition();
                // Get current song total duration
                final long totalDuration = ExoPlayerAdapter.getExoplayer().getDuration();

                // Don't update SeekBar
                if (!shouldUpdateSeekBar) {
                    seekBarHandler.removeCallbacks(this);
                    Log.e(TAG, "run: Stop SeekBar updates");
                    return;
                } else
                    Log.e(TAG, "run: SeekBar updating");

                if (currentContentPos < totalDuration) {

                    seekBarValue = (int) currentContentPos;
                    seekBarStatus.setValue(seekBarValue);
                    seekBarHandler.postDelayed(this, 1000); // Update every sec
                }
            }
        };
        seekBarHandler.post(seekBarRunnable);
    }

    public LiveData<Integer> getSeekBarStatus() {
        return seekBarStatus;
    }

    // Don't update SeekBar if playback is paused
    public void seekBarStatus(boolean shouldUpdateSeekBar) {
        this.shouldUpdateSeekBar = shouldUpdateSeekBar;

        if (shouldUpdateSeekBar)
            updateSeekBar();
    }

} //TODO: Try for a better SeekBar update logic
