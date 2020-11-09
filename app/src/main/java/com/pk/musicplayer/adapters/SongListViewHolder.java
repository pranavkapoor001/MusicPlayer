package com.pk.musicplayer.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.pk.musicplayer.R;
import com.pk.musicplayer.models.Song;

import de.hdodenhof.circleimageview.CircleImageView;

public class SongListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    // UI Components
    private TextView tvSongTitle;
    private CircleImageView ivAlbumArt;

    // vars
    private IMediaSelector mIMediaSelector;

    public SongListViewHolder(@NonNull View itemView, IMediaSelector iMediaSelector) {
        super(itemView);

        // find views
        tvSongTitle = itemView.findViewById(R.id.item_song_title);
        ivAlbumArt = itemView.findViewById(R.id.item_song_img);

        // Set IMediaSelector listener
        mIMediaSelector = iMediaSelector;
    }

    public void bindTo(Song currentSong) {
        tvSongTitle.setText(currentSong.getSongTitle());

        Glide.with(itemView.getContext())
                .load(currentSong.getAlbumArtUri())
                .placeholder(R.drawable.ic_music)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(ivAlbumArt);
    }

    @Override
    public void onClick(View v) {
        mIMediaSelector.onMediaSelected(getAdapterPosition());
    }

    public interface IMediaSelector {
        void onMediaSelected(int position);
    }
}
