package com.pk.musicplayer.repositories;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.pk.musicplayer.application.MyApplication;
import com.pk.musicplayer.models.Song;

import java.util.ArrayList;
import java.util.List;

public class SongRepository {

    private static final String TAG = "SongRepository";
    private static SongRepository instance;
    MutableLiveData<List<Song>> mSongs = new MutableLiveData<>();

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
                MediaStore.Audio.Media._ID};

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

                String songTitle = musicCursor.getString(
                        musicCursor.getColumnIndex(MediaStore.Audio.Media.TITLE));

                // Get id to concatenate with alarmArt folder path
                String id = musicCursor.getString(
                        musicCursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));

                // Get path for albumArt
                Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
                Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, Long.parseLong(id));

                // Get path for actual media file
                Uri contentUri = ContentUris.withAppendedId(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, Long.parseLong(id));

                // Add song to list
                mSongList.add(new Song(songTitle, albumArtUri, contentUri));

            }
        }

        if (musicCursor != null)
            musicCursor.close();

        Log.e(TAG, "getAllSongs: Triggered");

        return mSongList;
    } //TODO: Request access to internal storage
}
