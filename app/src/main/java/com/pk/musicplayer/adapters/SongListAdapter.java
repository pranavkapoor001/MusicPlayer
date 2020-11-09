package com.pk.musicplayer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pk.musicplayer.R;
import com.pk.musicplayer.models.Song;

import java.util.List;

public class SongListAdapter extends RecyclerView.Adapter<SongListViewHolder> {

    List<Song> mSongs;
    private Context mContext;
    private SongListViewHolder.IMediaSelector mIMediaSelector;

    public SongListAdapter(SongListViewHolder.IMediaSelector iMediaSelector) {
        mIMediaSelector = iMediaSelector;
    }

    public void setSongs(List<Song> songs) {
        mSongs = songs;
    }

    @NonNull
    @Override
    public SongListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Set context
        mContext = parent.getContext();

        View itemView = LayoutInflater.from(mContext).inflate(R.layout.song_item, parent, false);
        return new SongListViewHolder(itemView, mIMediaSelector);
    }

    @Override
    public void onBindViewHolder(@NonNull SongListViewHolder holder, int position) {
        Song currentSong = mSongs.get(position);

        holder.bindTo(currentSong);
    }

    @Override
    public int getItemCount() {
        return mSongs.size();
    }
}
