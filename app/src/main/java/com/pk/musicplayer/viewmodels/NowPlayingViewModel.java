package com.pk.musicplayer.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import com.pk.musicplayer.ui.activities.MainActivity;

public class NowPlayingViewModel extends ViewModel {

    private static NowPlayingViewModel instance;

    // vars
    private final MutableLiveData<String> mSongName = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mIsPlaying = new MutableLiveData<>();

    /* Singleton: Return the same instance of NowPlayingViewModel
     * Since this instance's data needs to be shared among all observers
     */

    public static NowPlayingViewModel getInstance(MainActivity mainActivity) {
        if (instance == null)
            return ViewModelProviders.of(mainActivity).get(NowPlayingViewModel.class);

        else
            return instance;
    }


    //----------------------------  Getters ------------------------------------------------------//

    public LiveData<String> getSongName() {
        return mSongName;
    }

    public void setSongName(String songName) {
        mSongName.setValue(songName);
    }


    //----------------------------  Getters ------------------------------------------------------//

    public LiveData<Boolean> getIsPlaying() {
        return mIsPlaying;
    }

    public void setIsPlaying(boolean isPlaying) {
        mIsPlaying.setValue(isPlaying);
    }
}
