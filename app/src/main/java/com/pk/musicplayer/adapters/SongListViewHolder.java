package com.pk.musicplayer.adapters;

import android.net.Uri;
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
    private Song currentSong;

    public SongListViewHolder(@NonNull View itemView, IMediaSelector iMediaSelector) {
        super(itemView);

        // find views
        tvSongTitle = itemView.findViewById(R.id.item_song_title);
        ivAlbumArt = itemView.findViewById(R.id.item_song_img);

        // Set IMediaSelector listener
        mIMediaSelector = iMediaSelector;

        // Set OnClick listeners
        tvSongTitle.setOnClickListener(this);
        ivAlbumArt.setOnClickListener(this);
    }

    public void bindTo(Song currentSong) {
        this.currentSong = currentSong;

        tvSongTitle.setText(currentSong.getSongTitle());

        Glide.with(itemView.getContext())
                .load(currentSong.getSongThumbnail())
                .placeholder(R.drawable.ic_music)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(ivAlbumArt);
    }

    @Override
    public void onClick(View v) {
        mIMediaSelector.onMediaSelected(currentSong.getSongUri());
    }

    public interface IMediaSelector {
        void onMediaSelected(Uri mediaUri);
    }
}
