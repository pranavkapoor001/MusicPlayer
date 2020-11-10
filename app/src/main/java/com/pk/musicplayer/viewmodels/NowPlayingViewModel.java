package com.pk.musicplayer.viewmodels;

import android.app.Activity;
import android.support.v4.media.MediaBrowserCompat;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

public class NowPlayingViewModel extends ViewModel {

    private static NowPlayingViewModel instance;

    // vars
    private final MutableLiveData<String> mSongName = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mIsPlaying = new MutableLiveData<>();
    private final MutableLiveData<MediaBrowserCompat.MediaItem> mSongItem = new MutableLiveData<>();

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

    public LiveData<String> getSongName() {
        return mSongName;
    }

    public void setSongName(String songName) {
        mSongName.setValue(songName);
    }


    public LiveData<Boolean> getIsPlaying() {
        return mIsPlaying;
    }

    public void setIsPlaying(boolean isPlaying) {
        mIsPlaying.setValue(isPlaying);
    }

    public LiveData<MediaBrowserCompat.MediaItem> getSongItem() {
        return mSongItem;
    }

    public void setSongItem(MediaBrowserCompat.MediaItem songItem) {
        mSongItem.setValue(songItem);
    }
}
