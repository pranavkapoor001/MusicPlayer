package com.pk.musicplayer.ui.activities;

public interface IMainActivity {

    void onMediaSelected();

    void playPause();

    void seekTo(long position);
}
