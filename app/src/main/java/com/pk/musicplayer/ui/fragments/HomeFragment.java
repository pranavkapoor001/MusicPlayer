package com.pk.musicplayer.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pk.musicplayer.R;
import com.pk.musicplayer.adapters.SongListAdapter;

public class HomeFragment extends Fragment {

    private RecyclerView mSongListRecyclerView;


    //-------------------------------------Lifecycle methods--------------------------------------//

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Find views
        mSongListRecyclerView = view.findViewById(R.id.song_list_recycler);

        // Init recycler view layout
        initSongListRecyclerView();

    }


    //----------------------------------- Init RecyclerView --------------------------------------//

    private void initSongListRecyclerView() {
        SongListAdapter mSongListAdapter = new SongListAdapter(getContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mSongListRecyclerView.setLayoutManager(layoutManager);
        mSongListRecyclerView.setAdapter(mSongListAdapter);
    }
}