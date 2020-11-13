package com.pk.musicplayer.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.media.MediaBrowserServiceCompat;
import androidx.media.session.MediaButtonReceiver;

import com.pk.musicplayer.adapters.player.ExoPlayerAdapter;
import com.pk.musicplayer.db.repositories.NowPlayingMetadataRepo;
import com.pk.musicplayer.helper.MediaNotificationHelper;

import java.util.List;

public class MusicService extends MediaBrowserServiceCompat {

    // vars
    private static final String TAG = "MusicService";
    private static final int NOTIFICATION_ID = 101;
    private MediaNotificationHelper mediaNotificationHelper;
    private boolean mIsServiceStarted;

    // Media Components
    private MediaSessionCompat mediaSession;
    private PlaybackStateCompat.Builder playbackStateBuilder;
    private ExoPlayerAdapter exoPlayerAdapter;


    //-------------------------------------Lifecycle methods--------------------------------------//

    @Override
    public void onCreate() {
        super.onCreate();

        // Create Media Session
        initMediaSession();

        // Create notification channel
        mediaNotificationHelper = new MediaNotificationHelper(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        MediaButtonReceiver.handleIntent(mediaSession, intent);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);

        // Stop service
        stopMusicService();
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

        // Set media button receiver intent
        Intent mediaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
        mediaButtonIntent.setClass(this, MediaButtonReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0,
                mediaButtonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mediaSession.setMediaButtonReceiver(pendingIntent);

        // A token that will be used to create a MediaController for this session
        setSessionToken(mediaSession.getSessionToken());

        // Init ExoPlayer
        exoPlayerAdapter = new ExoPlayerAdapter(this);

    }


    //---------------------------------- Misc Methods --------------------------------------------//

    /* This method shows media notification
     * And starts foreground service if not already started
     */
    private void showNotification(boolean isPlaying) {
        Notification notification =
                mediaNotificationHelper.buildNotification(mediaSession.getController(), isPlaying);

        // Start service if not already started
        if (!mIsServiceStarted) {
            ContextCompat.startForegroundService(MusicService.this,
                    new Intent(MusicService.this, MusicService.class));

            mIsServiceStarted = true;
        }

        startForeground(NOTIFICATION_ID, notification);
    }

    // This method completely stops the service
    private void stopMusicService() {
        // Stop foreground service and remove notification
        stopForeground(true);

        // Stop playback and release exoplayer
        exoPlayerAdapter.stop();

        stopSelf();

        mIsServiceStarted = false;
    }


    //---------------------------------- Misc Methods --------------------------------------------//

    private void setMediaPlaybackState(int state) {
        PlaybackStateCompat.Builder playbackStateBuilder = new PlaybackStateCompat.Builder();
        if (state == PlaybackStateCompat.STATE_PLAYING) {
            playbackStateBuilder.setActions(PlaybackStateCompat.ACTION_PLAY_PAUSE
                    | PlaybackStateCompat.ACTION_PAUSE);
        } else {
            playbackStateBuilder.setActions(PlaybackStateCompat.ACTION_PLAY_PAUSE
                    | PlaybackStateCompat.ACTION_PLAY);
        }
        playbackStateBuilder.setState(state, PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN, 0);
        mediaSession.setPlaybackState(playbackStateBuilder.build());
    } //TODO: Update notification to reflect state (notification play/pause works)


    //---------------------------------- MediaSessionCallbacks -----------------------------------//

    /*
     * https://developer.android.com/guide/topics/media-apps/audio-app/building-a-mediabrowserservice
     *
     * https://developer.android.com/guide/topics/media-apps/working-with-a-media-session
     * Part of Initializing the media session
     * (This is where we control the player play(), pause())
     */

    private class MediaSessionCallbacks extends MediaSessionCompat.Callback {
        @Override
        public void onPlayFromUri(Uri uri, Bundle extras) {
            super.onPlayFromUri(uri, extras);

            exoPlayerAdapter.playFromUri(uri);

            // Start playback
            onPlay();
        }

        @Override
        public void onPlay() {
            super.onPlay();

            // Set the session active
            mediaSession.setActive(true);

            // Start playback
            exoPlayerAdapter.play();

            mediaSession.setMetadata(NowPlayingMetadataRepo.getInstance().getMetadata());

            // Set playback state
            setMediaPlaybackState(PlaybackStateCompat.STATE_PLAYING);

            // Start foreground service, show notification
            showNotification(true /* Playing */);

        }

        @Override
        public void onPause() {
            super.onPause();

            exoPlayerAdapter.pause();

            // Show paused notification
            showNotification(false /* Paused */);

            // Allow notification to be swiped away
            stopForeground(false);
            mIsServiceStarted = false;

            // Set playback state
            setMediaPlaybackState(PlaybackStateCompat.STATE_PAUSED);
        }

        @Override
        public void onStop() {
            super.onStop();

            // Set the session inactive
            mediaSession.setActive(false);

            stopMusicService();
        }
    }
} //TODO: Move code from media callbacks to exoplayer adapter
