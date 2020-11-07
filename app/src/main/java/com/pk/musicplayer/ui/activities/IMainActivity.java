package com.pk.musicplayer.ui.activities;

import android.net.Uri;

public interface IMainActivity {

    void onMediaSelected(Uri mediaUri);

    void playPause();
}
