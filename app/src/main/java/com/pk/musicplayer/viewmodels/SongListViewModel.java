package com.pk.musicplayer.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.pk.musicplayer.models.Song;
import com.pk.musicplayer.repositories.SongRepository;

import java.util.List;

public class SongListViewModel extends ViewModel {

    /* MutableLiveData: Since it may be used to update any data
     * Used by NowPlaying Fragment / Songs List
     */
    private MutableLiveData<List<Song>> mSongs;

    private SongRepository mSongRepo;

    // Used by observer
    public LiveData<List<Song>> getSongs() {
        init();
        return mSongs;
    }

    public void init() {

        // Already populated
        if (mSongRepo != null)
            return;

        mSongRepo = SongRepository.getInstance();
        mSongs = mSongRepo.getSongs();
    }
}
