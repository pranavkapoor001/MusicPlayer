package com.pk.musicplayer.db.repositories;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.media.MediaMetadataCompat;
import android.util.Log;
import android.util.Size;

import androidx.lifecycle.MutableLiveData;

import com.pk.musicplayer.application.MyApplication;
import com.pk.musicplayer.db.models.Song;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SongRepository {

    private static final String TAG = "SongRepository";
    private static SongRepository instance;
    MutableLiveData<List<Song>> mSongs = new MutableLiveData<>();
    private List<MediaMetadataCompat> metadataList = new ArrayList<>();

    // Media Item vars
    private String songTitle;
    private String songId;
    private String songArtist;
    private long songDuration;
    private Bitmap thumbnail;
    private Uri contentUri;

    /**
     * Singleton pattern:
     * To Make sure there is only one instance to our data source
     *
     * @return instance of SongRepository
     */
    public static SongRepository getInstance() {
        if (instance == null) {
            instance = new SongRepository();
        }
        return instance;
    }

    public MutableLiveData<List<Song>> getSongs() {
        // Fetch from content provider
        mSongs.setValue(getAllSongs());
        return mSongs;
    }

    /**
     * This method fetches music files on external storage
     * Using content resolver
     *
     * @return List<> of type Song data class
     */
    private List<Song> getAllSongs() {
        List<Song> mSongList = new ArrayList<>();

        // Get handle to ContentResolver
        ContentResolver contentResolver = MyApplication.getContext().getContentResolver();

        // Get External Media URI
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;


        /* SELECTION CRITERIA START */

        // Get Audio Track name
        String[] projection = new String[]{MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media._ID, MediaStore.Audio.Artists.ARTIST,
                MediaStore.Audio.Media.DURATION};

        // Select only music files
        String selectionClause = MediaStore.Audio.Media.IS_MUSIC + " = ?";

        // MediaStore.Audio.Media.IS_MUSIC returns 1 for music
        String[] selectionArgs = new String[]{"1"};

        // Order fetched data by display name
        String orderBy = MediaStore.Audio.Media.TITLE;

        /* SELECTION CRITERIA END */


        Cursor musicCursor = contentResolver.query(
                musicUri,
                projection, // The columns to return for each row
                selectionClause, // Selection criteria
                selectionArgs, // Selection criteria to match
                orderBy); // The sort order for the returned rows

        if (musicCursor != null && musicCursor.getCount() > 0) {
            while (musicCursor.moveToNext()) {

                // Get song name
                songTitle = musicCursor.getString(
                        musicCursor.getColumnIndex(MediaStore.Audio.Media.TITLE));

                // Get id to concatenate with alarmArt folder path
                songId = musicCursor.getString(
                        musicCursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));

                // Get song artist
                songArtist = musicCursor.getString(
                        musicCursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST));

                // Get song duration
                songDuration = musicCursor.getLong(
                        musicCursor.getColumnIndex(MediaStore.Audio.Media.DURATION));

                // Get path for actual media file
                contentUri = ContentUris.withAppendedId(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, Long.parseLong(songId));

                // Load Media thumbnail
                try {

                    /* Set null, otherwise if loadThumbnail() fails
                     * Thumbnail from previous iteration(song) may be used (globally initialized var)
                     */
                    thumbnail = null;

                    // load thumbnail
                    thumbnail = contentResolver.loadThumbnail(contentUri,
                            new Size(640, 480), null);
                } catch (IOException ignored) {
                    // No thumbnail found
                }

                // Add song to list
                mSongList.add(new Song(songTitle, thumbnail, contentUri));

                // Build Metadata
                MediaMetadataCompat metadata = getMetadata();

                // Add to Metadata list
                metadataList.add(musicCursor.getPosition(), metadata);

            }
        }

        if (musicCursor != null)
            musicCursor.close();

        Log.e(TAG, "getAllSongs: Triggered");

        return mSongList;
    } //TODO: Request access to internal storage


    //--------------------------- Get MetaData Object --------------------------------------------//

    private MediaMetadataCompat getMetadata() {

        MediaMetadataCompat metadata = new MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, songTitle)
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, songId)
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, songArtist)
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, songDuration)
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI, String.valueOf(contentUri))
                .putBitmap(MediaMetadataCompat.METADATA_KEY_ART, thumbnail)
                .build();

        return metadata;
    }

    public List<MediaMetadataCompat> getMediaItems() {
        return metadataList;
    }
}
