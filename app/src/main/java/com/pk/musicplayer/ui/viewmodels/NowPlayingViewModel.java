package com.pk.musicplayer.ui.viewmodels;

import android.app.Activity;
import android.support.v4.media.MediaMetadataCompat;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

public class NowPlayingViewModel extends ViewModel {

    private static NowPlayingViewModel instance;

    // vars
    private final MutableLiveData<Boolean> mIsPlaying = new MutableLiveData<>();
    private final MutableLiveData<MediaMetadataCompat> mSongMetadata = new MutableLiveData<>();

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
}
