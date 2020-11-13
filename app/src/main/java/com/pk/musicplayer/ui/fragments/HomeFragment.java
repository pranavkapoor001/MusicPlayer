package com.pk.musicplayer.ui.fragments;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
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
import com.pk.musicplayer.adapters.ui.SongListAdapter;
import com.pk.musicplayer.adapters.ui.SongListViewHolder;
import com.pk.musicplayer.db.models.Song;
import com.pk.musicplayer.helper.PermissionHelper;
import com.pk.musicplayer.ui.activities.IMainActivity;
import com.pk.musicplayer.ui.viewmodels.SongListViewModel;

import java.util.List;

public class HomeFragment extends Fragment implements SongListViewHolder.IMediaSelector {

    private RecyclerView mSongListRecyclerView;
    private SongListViewModel mSongListViewModel;
    private SongListAdapter mSongListAdapter;
    private IMainActivity iMainActivity;


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

        /* Check permissions
         * Init views and data only if permission is granted
         */
        int permissionGranted = PermissionHelper.checkPermission(view, this);
        if (permissionGranted == PackageManager.PERMISSION_GRANTED) {
            // Init recycler view layout
            initSongListRecyclerView();

            // Init view model
            initSongListViewModel();
        }

    }


    //----------------------------------- Init methods -------------------------------------------//

    public void initSongListViewModel() {
        mSongListViewModel = ViewModelProviders.of(requireActivity()).get(SongListViewModel.class);

        mSongListViewModel.getSongs().observe(this, new Observer<List<Song>>() {
            @Override
            public void onChanged(List<Song> songs) {

                // Pass songs list to SongsListAdapter
                mSongListAdapter.setSongs(songs);

                // Update recycler view when Song class data changes
                mSongListAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initSongListRecyclerView() {
        mSongListAdapter = new SongListAdapter(this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mSongListRecyclerView.setLayoutManager(layoutManager);
        mSongListRecyclerView.setAdapter(mSongListAdapter);
    }

    @Override
    public void onMediaSelected() {
        // Notify MainActivity that a media item is selected
        iMainActivity.onMediaSelected();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        iMainActivity = (IMainActivity) getActivity();
    }

    // Init songs list RecyclerView and ViewModel when permission is granted
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        Log.e("HomeFragment", "onRequestPermissionsResult Called");

        if (requestCode == PermissionHelper.READ_EXTERNAL_PERMISSION_CODE) {
            // init recycler view and song view model
            initSongListRecyclerView();
            initSongListViewModel();
        }
    } //TODO: Handle case when permission is denied even after SnackBar - Rationale
}