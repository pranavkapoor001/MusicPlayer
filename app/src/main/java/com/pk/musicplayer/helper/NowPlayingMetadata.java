package com.pk.musicplayer.helper;

/* This class returns the metadata about currently playing media */

import android.support.v4.media.MediaMetadataCompat;

public class NowPlayingMetadata {

    private static NowPlayingMetadata instance;
    private MediaMetadataCompat metadata;

    // Singleton
    public static NowPlayingMetadata getInstance() {
        if (instance == null)
            instance = new NowPlayingMetadata();

        return instance;
    }

    public void setMetadata(MediaMetadataCompat metadata) {
        this.metadata = metadata;
    }
}
