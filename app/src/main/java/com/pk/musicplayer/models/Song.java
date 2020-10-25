package com.pk.musicplayer.models;

// Song item data class

public class Song {

    private String mSongTitle;

    public Song(String songTitle) {
        mSongTitle = songTitle;
    }

    // Getters
    public String getSongTitle() {
        return mSongTitle;
    }

    // Setters
    public void setSongTitle(String songTitle) {
        mSongTitle = songTitle;
    }
}
