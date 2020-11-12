package com.pk.musicplayer.db.repositories;

/* This class returns the metadata about currently playing media */

import android.support.v4.media.MediaMetadataCompat;

public class NowPlayingMetadataRepo {

    private static NowPlayingMetadataRepo instance;
    private MediaMetadataCompat mMetadata;

    // Singleton
    public static NowPlayingMetadataRepo getInstance() {
        if (instance == null)
            instance = new NowPlayingMetadataRepo();

        return instance;
    }

    public MediaMetadataCompat getMetadata() {
        return mMetadata;
    }

    public void setMetadata(MediaMetadataCompat metadata) {
        mMetadata = metadata;
    }
}
