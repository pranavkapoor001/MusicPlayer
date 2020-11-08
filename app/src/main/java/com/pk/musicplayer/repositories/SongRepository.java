package com.pk.musicplayer.repositories;

import androidx.lifecycle.MutableLiveData;

import com.pk.musicplayer.models.Song;

import java.util.List;

public class SongRepository {

    private static SongRepository instance;
    MutableLiveData<List<Song>> mSongs = new MutableLiveData<>();

    /**
     * Singleton pattern:
     * To Make sure there is only one instance to our data source
     *
     * @return instance of SongRepository
     */
    public static SongRepository getInstance() {
        if (instance == null) {
            instance = new SongRepository();
        }
        return instance;
    }

    public MutableLiveData<List<Song>> getSongs() {
        // Fetch from content provider

        return mSongs;
    }
}
