package com.pk.musicplayer.adapters.player;

import android.content.Context;
import android.net.Uri;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;

public class ExoPlayerAdapter {

    // ExoPlayer Components
    private static SimpleExoPlayer exoPlayer;

    // vars
    private Context mContext;

    public ExoPlayerAdapter(Context context) {
        mContext = context;
        initExoPlayer();
    }

    public static SimpleExoPlayer getExoplayer() {
        if (exoPlayer == null)
            throw new IllegalStateException("ExoPlayer should not be null!");

        return exoPlayer;
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

    public void seekTo(long position) {
        exoPlayer.seekTo(position);
    }

    public void stop() {
        exoPlayer.release();
    }

}
