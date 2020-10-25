package com.pk.musicplayer.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pk.musicplayer.R;
import com.pk.musicplayer.models.Song;

public class SongListViewHolder extends RecyclerView.ViewHolder {

    TextView tvSongTitle;

    public SongListViewHolder(@NonNull View itemView) {
        super(itemView);

        // find views
        tvSongTitle = itemView.findViewById(R.id.item_song_title);
    }

    public void bindTo(Song currentSong) {
        tvSongTitle.setText(currentSong.getSongTitle());
    }
}
