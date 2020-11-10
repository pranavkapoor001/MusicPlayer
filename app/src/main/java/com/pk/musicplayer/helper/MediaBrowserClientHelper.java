package com.pk.musicplayer.helper;

import android.app.Activity;
import android.content.ComponentName;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.widget.ImageView;

import com.pk.musicplayer.R;
import com.pk.musicplayer.services.MusicService;

/* There are 4 Client side components
 *
 * 1. MediaBrowserCompat: Browse media content offered by MediaBrowserService
 * 2. MediaBrowserCompat.ConnectionCallback: Here get MediaSession Token and create
 *                                           MediaControllerCompat
 * 3. MediaControllerCompat: Connects UI to media controller (Actual UI component states are set here)
 * 4. MediaControllerCompat.Callback: Callbacks when stuff like metadata, playback state changes
 */

public class MediaBrowserClientHelper {

    // vars
    private static final String TAG = "MediaBrowserClientHelpe";
    // Media Components
    private MediaBrowserCompat mediaBrowser;
    private MediaControllerCompat mediaController;
    private Activity mActivity;


    //-------------------------------- MediaControllerCallback -----------------------------------//

    private MediaControllerCompat.Callback mediaControllerCallback =
            new MediaControllerCompat.Callback() {
                @Override
                public void onMetadataChanged(MediaMetadataCompat metadata) {
                    super.onMetadataChanged(metadata);
                }

                @Override
                public void onPlaybackStateChanged(PlaybackStateCompat state) {
                    super.onPlaybackStateChanged(state);
                }
            };


    //-------------------------------- MediaBrowserCallback --------------------------------------//

    private final MediaBrowserCompat.ConnectionCallback mediaBrowserCallbacks =
            new MediaBrowserCompat.ConnectionCallback() {
                @Override
                public void onConnected() {
                    super.onConnected();

                    Log.e(TAG, "onConnected: Connected");

                    // Get sessionToken for the connected MediaSession
                    MediaSessionCompat.Token sessionToken = mediaBrowser.getSessionToken();

                    // Create a MediaControllerCompat
                    mediaController = new MediaControllerCompat(mActivity,
                            sessionToken);

                    // Set the controller, this enables handling of media buttons
                    MediaControllerCompat.setMediaController(mActivity, mediaController);

                    // Finish building the UI
                    buildTransportControls();
                }

                @Override
                public void onConnectionSuspended() {
                    super.onConnectionSuspended();
                }

                @Override
                public void onConnectionFailed() {
                    super.onConnectionFailed();
                }
            };


    // Called from MainActivity onCreate()
    public MediaBrowserClientHelper(Activity activity) {

        mActivity = activity;

        // Create MediaBrowserCompat
        mediaBrowser = new MediaBrowserCompat(mActivity,
                new ComponentName(mActivity, MusicService.class),
                mediaBrowserCallbacks,
                null);

    }

    //------------------- MediaController - TransportControls ------------------------------------//

    // Set UI state here (Play/Pause Button)
    private void buildTransportControls() {

        // Register Media Controller Callback to keep UI in sync with media session
        mediaController = MediaControllerCompat.getMediaController(mActivity);

        // Set UI state

        ImageView ivPlayPause = mActivity.findViewById(R.id.play_pause);

        int currentState = mediaController.getPlaybackState().getState();

        // Set state to play/pause button
        if (currentState == PlaybackStateCompat.STATE_PLAYING) {
            Log.e(TAG, "buildTransportControls: Playing");
            ivPlayPause.setImageResource(R.drawable.ic_pause);
        } else {
            Log.e(TAG, "buildTransportControls: Paused");
            ivPlayPause.setImageResource(R.drawable.ic_play);
        }

        mediaController.registerCallback(mediaControllerCallback);
    }


    //------------------------------------ Lifecycle methods -------------------------------------//


    /* Called from MainActivity onStart()
     * This method connects the UI component
     * to MusicService (MediaBrowserService)
     */
    public void onStart() {
        mediaBrowser.connect();
    }

    /* Called from MainActivity onStp[()
     * This method disconnects MediaBrowser
     */
    public void onStop() {
        if (mediaController != null) {
            MediaControllerCompat.getMediaController(mActivity)
                    .unregisterCallback(mediaControllerCallback);
        }
        mediaBrowser.disconnect();
    }

    public MediaControllerCompat.TransportControls getTransportControls() {
        if (mediaController == null)
            throw new IllegalStateException("MediaController is null!");

        return mediaController.getTransportControls();
    }
}
