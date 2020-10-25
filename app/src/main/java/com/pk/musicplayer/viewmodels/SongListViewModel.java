package com.pk.musicplayer.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.pk.musicplayer.models.Song;

import java.util.List;

public class SongListViewModel extends ViewModel {

    // MutableLiveData: Since it may be used to update any data
    private MutableLiveData<List<Song>> mSongs;

    // Used by observer
    public LiveData<List<Song>> getSongs() {
        return mSongs;
    }
}
