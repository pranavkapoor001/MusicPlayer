package com.pk.musicplayer.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.pk.musicplayer.R;
import com.pk.musicplayer.ui.activities.IMainActivity;

public class BottomMediaControllerFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "MediaControllerFragment";


    // UI Components
    private TextView mSongTitle;
    private ImageView mPlayPause;

    // vars
    private IMainActivity mIMainActivity;
    private boolean mIsPlaying;
    private Context mContext;


    //------------------------------- Lifecycle methods ------------------------------------------//

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_media_controller, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Find Views
        mSongTitle = view.findViewById(R.id.media_song_title);
        mPlayPause = view.findViewById(R.id.play_pause);

        // OnClick Listeners
        mPlayPause.setOnClickListener(this);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        mContext = context;
        mIMainActivity = (IMainActivity) getActivity();
    }


    //------------------------------- UI Setter methods ------------------------------------------//

    public void setMediaTitle(String title) {
        mSongTitle.setText(title);
    }

    public void setIsPlaying(boolean isPlaying) {
        if (isAdded())
            if (isPlaying) {
                Glide.with(mContext)
                        .load(R.drawable.ic_pause)
                        .into(mPlayPause);
            } else {
                Glide.with(mContext)
                        .load(R.drawable.ic_play)
                        .into(mPlayPause);
            }
        mIsPlaying = isPlaying;
    }


    //------------------------------- Misc methods -----------------------------------------------//

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.play_pause) {
            Log.e(TAG, "onClick: Called");
            mIMainActivity.playPause();
        }
    }
} //TODO: Hide this fragment when center fragment is opened
