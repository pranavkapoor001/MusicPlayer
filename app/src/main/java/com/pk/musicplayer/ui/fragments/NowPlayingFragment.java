package com.pk.musicplayer.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.google.android.material.textview.MaterialTextView;
import com.pk.musicplayer.R;
import com.pk.musicplayer.ui.activities.IMainActivity;
import com.pk.musicplayer.ui.activities.MainActivity;
import com.pk.musicplayer.viewmodels.NowPlayingViewModel;

public class NowPlayingFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "NowPlayingFragment";

    // UI components
    private MaterialTextView tvSongTitle;
    private ImageView ivSongImage, ivPlayPauseDetail, ivSkipPrevious, ivSkipToNext;
    private SeekBar seekBar;

    //vars
    private IMainActivity mIMainActivity;
    private Context mContext;
    private boolean mIsPlaying;
    private Activity mActivity;


    //------------------------------- Lifecycle methods ------------------------------------------//

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_now_playing, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Find Views
        tvSongTitle = view.findViewById(R.id.tvSongTitle);

        ivSongImage = view.findViewById(R.id.ivSongImage);
        ivPlayPauseDetail = view.findViewById(R.id.ivPlayPauseDetail);
        ivSkipPrevious = view.findViewById(R.id.ivSkipPrevious);
        ivSkipToNext = view.findViewById(R.id.ivSkipToNext);

        seekBar = view.findViewById(R.id.seekBar);

        // OnClick Listeners
        ivPlayPauseDetail.setOnClickListener(this);
        ivSkipPrevious.setOnClickListener(this);
        ivSkipToNext.setOnClickListener(this);


        initNowPlayingViewModel();

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        mContext = context;
        mIMainActivity = (IMainActivity) getActivity();

        if (context instanceof Activity)
            mActivity = (MainActivity) context;
    }


    //------------------------------- UI Setter methods ------------------------------------------//

    public void setSongName(String name) {
        tvSongTitle.setText(name);
    }

    public void setIsPlaying(boolean isPlaying) {

        Log.e(TAG, "setIsPlaying Fragment: Called");
        if (isAdded())
            if (isPlaying) {
                Glide.with(mContext)
                        .load(R.drawable.ic_pause)
                        .into(ivPlayPauseDetail);
            } else {
                Glide.with(mContext)
                        .load(R.drawable.ic_play)
                        .into(ivPlayPauseDetail);
            }
        mIsPlaying = isPlaying;
    }


    //----------------------- ViewModel Observer methods -----------------------------------------//

    private void initNowPlayingViewModel() {

        /* init view model with Activity, we get same instance of view model
         * that main activity created
         * See: https://developer.android.com/topic/libraries/architecture/viewmodel.html#sharing
         */
        final NowPlayingViewModel mNowPlayingViewModel =
                ViewModelProviders.of((FragmentActivity) mActivity).get(NowPlayingViewModel.class);

        // Setup observer for play/pause state
        mNowPlayingViewModel.getIsPlaying().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                setIsPlaying(aBoolean);
            }
        });
    }


    //------------------------------ Misc methods ------------------------------------------------//

    @Override
    public void onClick(View v) {
        mIMainActivity.playPause();
    }
}