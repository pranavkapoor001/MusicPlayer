package com.pk.musicplayer.services;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.media.MediaBrowserServiceCompat;

import java.util.List;

public class MusicService extends MediaBrowserServiceCompat {

    // vars
    private static final String TAG = "MusicService";
    // Media Components
    private MediaSessionCompat mediaSession;
    private PlaybackStateCompat.Builder playbackStateBuilder;


    //-------------------------------------Lifecycle methods--------------------------------------//

    @Override
    public void onCreate() {
        super.onCreate();

        initMediaSession();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);

        // Stop service
        stopSelf();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Release resources held by media session
        mediaSession.release();
    }

    // --------------------- Necessary overridden methods --------------------- //

    @Nullable
    @Override
    public BrowserRoot onGetRoot(@NonNull String clientPackageName, int clientUid,
                                 @Nullable Bundle rootHints) {
        /*
         * https://developer.android.com/guide/topics/media-apps/audio-app/building-a-mediabrowserservice#client-connections
         * Must return non-null rootId so clients can connect to this service
         * Allow clients to connect to media session without browsing
         * Empty BrowserRoot (onLoadChildren returns nothing)
         *
         * THIS METHOD CONTROLS ACCESS TO THIS SERVICE
         */

        return new BrowserRoot("empty_root_id", null);

    }

    @Override
    public void onLoadChildren(@NonNull String parentId,
                               @NonNull Result<List<MediaBrowserCompat.MediaItem>> result) {

        /*
         * This method provides the ability for a client to build and display
         * A menu of the MediaBrowserService's content hierarchy
         */

        result.sendResult(null); // return all available media

    }

    //--------------------------------- Initialize Media Session ---------------------------------//

    private void initMediaSession() {

        // Create a MediaSessionCompat
        mediaSession = new MediaSessionCompat(this, TAG);

        // Enable callbacks from MediaButtons and TransportControls
        mediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player
        playbackStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);
        mediaSession.setPlaybackState(playbackStateBuilder.build());

        /* Assign instance of MediaSessionCallbacks to media session
         * MediaSessionCallbacks has methods that handle callbacks from a media controller
         */
        mediaSession.setCallback(new MediaSessionCallbacks());

        // A token that will be used to create a MediaController for this session
        setSessionToken(mediaSession.getSessionToken());

    }


    //---------------------------------- MediaSessionCallbacks -----------------------------------//

    /*
     * https://developer.android.com/guide/topics/media-apps/audio-app/building-a-mediabrowserservice
     *
     * https://developer.android.com/guide/topics/media-apps/working-with-a-media-session
     * Part of Initializing the media session
     * (This is where we control the player play(), pause())
     */

    private static class MediaSessionCallbacks extends MediaSessionCompat.Callback {
        @Override
        public void onPlayFromUri(Uri uri, Bundle extras) {
            super.onPlayFromUri(uri, extras);
        }

        @Override
        public void onPlay() {
            super.onPlay();
        }

        @Override
        public void onPause() {
            super.onPause();
        }
    }
}
