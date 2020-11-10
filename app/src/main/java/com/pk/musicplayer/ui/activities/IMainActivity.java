package com.pk.musicplayer.ui.activities;

import android.support.v4.media.MediaBrowserCompat;

public interface IMainActivity {

    void onMediaSelected(MediaBrowserCompat.MediaItem mediaItem);

    void playPause();
}
