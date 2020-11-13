package com.pk.musicplayer.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.media.MediaMetadataCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.bumptech.glide.Glide;
import com.google.android.material.textview.MaterialTextView;
import com.pk.musicplayer.R;
import com.pk.musicplayer.adapters.player.ExoPlayerAdapter;
import com.pk.musicplayer.ui.activities.IMainActivity;
import com.pk.musicplayer.ui.viewmodels.NowPlayingViewModel;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class NowPlayingFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "NowPlayingFragment";

    // UI components
    private MaterialTextView tvSongTitle;
    private ImageView ivSongImage, ivPlayPauseDetail, ivSkipPrevious, ivSkipToNext;
    private SeekBar seekBar;
    private TextView tvSongDuration, tvCurTime;
    private NowPlayingViewModel mNowPlayingViewModel;

    //vars
    private IMainActivity mIMainActivity;
    private Context mContext;
    private boolean mIsPlaying;


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
        tvSongDuration = view.findViewById(R.id.tvSongDuration);
        tvCurTime = view.findViewById(R.id.tvCurTime);

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

    }

    @Override
    public void onAttachFragment(@NonNull Fragment childFragment) {
        super.onAttachFragment(childFragment);

        // Start updating SeekBar
        if (mIsPlaying)
            shouldUpdateSeekBar(true);
    }

    @Override
    public void onStop() {
        super.onStop();

        // Stop updating SeekBar
        shouldUpdateSeekBar(false);
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // Stop updating SeekBar
        shouldUpdateSeekBar(false);
    }


    //------------------------------- UI Setter methods ------------------------------------------//

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
        mNowPlayingViewModel = NowPlayingViewModel.getInstance(requireActivity());

        // Setup observer for play/pause state
        mNowPlayingViewModel.getIsPlaying().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                setIsPlaying(aBoolean);

                // Start/Stop updating SeekBar
                shouldUpdateSeekBar(aBoolean);
            }
        });

        mNowPlayingViewModel.getSongMetadata().observe(this, new Observer<MediaMetadataCompat>() {
            @Override
            public void onChanged(MediaMetadataCompat metadata) {
                updateNowPlayingUI(metadata);

                // Start updating SeekBar
                shouldUpdateSeekBar(true);
            }
        });

        final SimpleDateFormat sdf = new SimpleDateFormat("mm:ss", Locale.getDefault());
        mNowPlayingViewModel.getSeekBarStatus().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer progress) {
                seekBar.setProgress(progress);
                tvCurTime.setText(sdf.format(ExoPlayerAdapter.getExoplayer().getContentPosition()));
            }
        });
    }


    //------------------------------ Misc methods ------------------------------------------------//

    @Override
    public void onClick(View v) {
        mIMainActivity.playPause();
    }

    private void updateNowPlayingUI(MediaMetadataCompat metadata) {
        ivSongImage.setImageBitmap(metadata.getDescription().getIconBitmap());
        tvSongTitle.setText(metadata.getDescription().getTitle());

        // Get song duration (millis)
        long songDuration = ExoPlayerAdapter.getExoplayer().getDuration();
        Log.e(TAG, "updateNowPlayingUI: songDuration: " + songDuration);

        // Set song duration text
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss", Locale.getDefault());
        tvSongDuration.setText(sdf.format(songDuration)); //TODO: Fix 3x:xx (why 3)
        Log.e(TAG, "updateNowPlayingUI: DurationText: " + sdf.format(songDuration));

        // Set max length of SeekBar
        seekBar.setMax((int) songDuration);
    }

    private void shouldUpdateSeekBar(boolean shouldUpdate) {
        // Start updating SeekBar
        mNowPlayingViewModel.seekBarStatus(shouldUpdate);
    }
}