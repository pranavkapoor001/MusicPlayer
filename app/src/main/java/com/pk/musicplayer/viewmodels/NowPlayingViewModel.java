package com.pk.musicplayer.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NowPlayingViewModel extends ViewModel {

    // vars
    private final MutableLiveData<String> mSongName = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mIsPlaying = new MutableLiveData<>();

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
