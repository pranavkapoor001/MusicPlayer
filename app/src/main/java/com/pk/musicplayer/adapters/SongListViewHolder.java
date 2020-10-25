package com.pk.musicplayer.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.pk.musicplayer.R;
import com.pk.musicplayer.models.Song;

public class SongListViewHolder extends RecyclerView.ViewHolder {

    TextView tvSongTitle;
    ImageView ivAlbumArt;

    public SongListViewHolder(@NonNull View itemView) {
        super(itemView);

        // find views
        tvSongTitle = itemView.findViewById(R.id.item_song_title);
        ivAlbumArt = itemView.findViewById(R.id.item_song_img);
    }

    public void bindTo(Song currentSong) {
        tvSongTitle.setText(currentSong.getSongTitle());

        Glide.with(itemView.getContext())
                .load(currentSong.getAlbumArtUri())
                .placeholder(R.drawable.ic_music)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(ivAlbumArt);
    }
}
