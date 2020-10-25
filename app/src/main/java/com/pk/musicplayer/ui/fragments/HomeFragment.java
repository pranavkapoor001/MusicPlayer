package com.pk.musicplayer.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pk.musicplayer.R;
import com.pk.musicplayer.adapters.SongListAdapter;
import com.pk.musicplayer.models.Song;
import com.pk.musicplayer.viewmodels.SongListViewModel;

import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView mSongListRecyclerView;
    private SongListViewModel mSongListViewModel;
    private SongListAdapter mSongListAdapter;


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

        // Init view model
        initSongListViewModel();

        // Init recycler view layout
        initSongListRecyclerView();

    }


    //----------------------------------- Init methods -------------------------------------------//

    public void initSongListViewModel() {
        mSongListViewModel = ViewModelProviders.of(this).get(SongListViewModel.class);

        mSongListViewModel.getSongs().observe(this, new Observer<List<Song>>() {
            @Override
            public void onChanged(List<Song> songs) {

                // Update recycler view when Song class data changes
                mSongListAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initSongListRecyclerView() {
        // Pass context and List<Song> to recycler view
        mSongListAdapter = new SongListAdapter(getContext(),
                mSongListViewModel.getSongs().getValue());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mSongListRecyclerView.setLayoutManager(layoutManager);
        mSongListRecyclerView.setAdapter(mSongListAdapter);
    }
}