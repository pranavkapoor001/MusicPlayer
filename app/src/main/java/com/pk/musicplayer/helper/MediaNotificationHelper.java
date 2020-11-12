package com.pk.musicplayer.helper;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import androidx.core.app.NotificationCompat;
import androidx.media.session.MediaButtonReceiver;

import com.pk.musicplayer.BuildConfig;
import com.pk.musicplayer.R;
import com.pk.musicplayer.db.repositories.NowPlayingMetadataRepo;
import com.pk.musicplayer.services.MusicService;

public class MediaNotificationHelper {

    private static final String CHANNEL_ID = BuildConfig.APPLICATION_ID + "MediaNotification";
    // Notification Components
    private NotificationManager mNotificationManager;
    // vars
    private MusicService mMusicService;


    public MediaNotificationHelper(MusicService musicService) {
        mMusicService = musicService;

        mNotificationManager =
                (NotificationManager) musicService.getSystemService(Context.NOTIFICATION_SERVICE);

        // Create notification channel
        createNotificationChannel();
    }


    //---------------------------------- Notification Channel ------------------------------------//

    private void createNotificationChannel() {

        // Notification Channel exists, reuse it
        if (mNotificationManager.getNotificationChannel(CHANNEL_ID) != null)
            return;


        // Notification Channel does not exist, create new one
        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,
                "MusicPlayer", NotificationManager.IMPORTANCE_DEFAULT);

        // Set notification channel behaviors
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(Color.GREEN);
        notificationChannel.setDescription("Notifications from MusicPlayer");

        // Create notification channel
        mNotificationManager.createNotificationChannel(notificationChannel);

    }


    //---------------------------------- Notifications -------------------------------------------//

    public Notification buildNotification(MediaControllerCompat controller, boolean isPlaying) {


        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                mMusicService, CHANNEL_ID);

        // Get metadata description
        MediaDescriptionCompat description =
                NowPlayingMetadataRepo.getInstance().getMetadata().getDescription();


        // Add metadata
        builder.setContentTitle(description.getTitle())
                .setContentText(description.getSubtitle())
                .setLargeIcon(description.getIconBitmap());

        // Launch activity when notification is clicked
        builder.setContentIntent(controller.getSessionActivity());

        // Stop service when notification is swiped away
        builder.setDeleteIntent(MediaButtonReceiver.buildMediaButtonPendingIntent(mMusicService,
                PlaybackStateCompat.ACTION_STOP));

        // Make the transport controls visible on the lock screen
        builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

        // Add an app icon and set its color
        builder.setSmallIcon(R.drawable.ic_music)
                .setColor(mMusicService.getColor(R.color.colorAccent));

        // Add a pause button
        if (isPlaying) {
            builder.addAction(new NotificationCompat.Action(R.drawable.ic_pause,
                    "Pause", MediaButtonReceiver.buildMediaButtonPendingIntent(mMusicService,
                    PlaybackStateCompat.ACTION_PLAY_PAUSE)));
        } else {
            // Add a play button
            builder.addAction(new NotificationCompat.Action(R.drawable.ic_play,
                    "Play", MediaButtonReceiver.buildMediaButtonPendingIntent(mMusicService,
                    PlaybackStateCompat.ACTION_PLAY_PAUSE)));
        }

        /* Use Media Style notification
         * Associate with media session, add pause button to compat view
         * Add a cancel button with action as STOP
         */
        builder.setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                //.setMediaSession(mMusicService.getSessionToken())
                .setShowActionsInCompactView(0)
                .setShowCancelButton(true)
                .setCancelButtonIntent(MediaButtonReceiver.buildMediaButtonPendingIntent(mMusicService,
                        PlaybackStateCompat.ACTION_STOP)));

        // Build notification and return
        return builder.build();
    }
}
