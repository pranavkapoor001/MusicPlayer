package com.pk.musicplayer.models;

// Song item data class

import android.net.Uri;

public class Song {

    private String mSongTitle;
    private Uri mAlbumArtUri;

    public Song(String songTitle, Uri albumArtUri) {
        mSongTitle = songTitle;
        mAlbumArtUri = albumArtUri;
    }

    // Getters
    public String getSongTitle() {
        return mSongTitle;
    }

    // Setters
    public void setSongTitle(String songTitle) {
        mSongTitle = songTitle;
    }

    public Uri getAlbumArtUri() {
        return mAlbumArtUri;
    }
}
