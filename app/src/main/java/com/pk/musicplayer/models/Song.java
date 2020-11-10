package com.pk.musicplayer.models;

// Song item data class

import android.graphics.Bitmap;
import android.net.Uri;

public class Song {

    private String mSongTitle;
    private Bitmap mSongThumbnail;
    private Uri mSongUri;

    public Song(String songTitle, Bitmap songThumbnail, Uri songUri) {
        mSongTitle = songTitle;
        mSongThumbnail = songThumbnail;
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

    public Bitmap getSongThumbnail() {
        return mSongThumbnail;
    }

    public Uri getSongUri() {
        return mSongUri;
    }
}
