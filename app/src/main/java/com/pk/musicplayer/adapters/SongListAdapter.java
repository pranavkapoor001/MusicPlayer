package com.pk.musicplayer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pk.musicplayer.R;

public class SongListAdapter extends RecyclerView.Adapter<SongListViewHolder> {

    private Context mContext;

    public SongListAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public SongListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.song_item, parent, false);
        return new SongListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SongListViewHolder holder, int position) {
        holder.bindTo();
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
