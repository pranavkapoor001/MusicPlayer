package com.pk.musicplayer.models;

// Song item data class

import android.net.Uri;

public class Song {

    private String mSongTitle;
    private Uri mAlbumArtUri;
    private Uri mSongUri;

    public Song(String songTitle, Uri albumArtUri, Uri songUri) {
        mSongTitle = songTitle;
        mAlbumArtUri = albumArtUri;
        mSongUri = songUri;
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

    public Uri getSongUri() {
        return mSongUri;
    }
}
