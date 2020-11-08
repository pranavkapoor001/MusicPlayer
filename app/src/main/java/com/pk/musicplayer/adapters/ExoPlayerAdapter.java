package com.pk.musicplayer.adapters;

import android.content.Context;
import android.net.Uri;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;

public class ExoPlayerAdapter {

    // ExoPlayer Components
    private SimpleExoPlayer exoPlayer;

    // vars
    private Context mContext;

    public ExoPlayerAdapter(Context context) {
        mContext = context;
        initExoPlayer();
    }


    private void initExoPlayer() {
        if (exoPlayer == null)
            exoPlayer = new SimpleExoPlayer.Builder(mContext).build();
    }

    public void playFromUri(Uri uri) {

        MediaItem mediaItem = MediaItem.fromUri(uri);

        exoPlayer.setMediaItem(mediaItem);

        exoPlayer.prepare();
    }

    public void play() {
        exoPlayer.setPlayWhenReady(true);
    }

    public void pause() {
        exoPlayer.setPlayWhenReady(false);
    }

    public void stop() {
        exoPlayer.release();
    }

}
