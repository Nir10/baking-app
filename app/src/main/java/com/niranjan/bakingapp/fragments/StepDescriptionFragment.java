package com.niranjan.bakingapp.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;

import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;

import com.google.android.exoplayer2.util.Util;
import com.niranjan.bakingapp.DescriptionActivity;
import com.niranjan.bakingapp.DetailsActivity;
import com.niranjan.bakingapp.R;
import com.niranjan.bakingapp.models.Step;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepDescriptionFragment extends Fragment {

    public static final String STEP_LIST_KEY = "step";
    public static final String LIST_INDEX_KEY = "list_index";
    private static final String VIDEO_POSITION_KEY = "video_position";
    private static final String PLAY_WHEN_READY_KEY = "play_when_ready";
    private long videoPosition = 0;

    // Tag for logging
    private static final String TAG = StepDescriptionFragment.class.getSimpleName();


    private ArrayList<Step> mStepArrayList;
    private int mListIndex;

    private OnFragmentInteractionListener mListener;

    @BindView(R.id.tv_step_description)
    TextView mDescriptionTextView;

    @BindView(R.id.fab_prev)
    public FloatingActionButton mFabPrev;
    @BindView(R.id.fab_next)
    public FloatingActionButton mFabNext;
    @BindView(R.id.video_view)
    SimpleExoPlayerView mVideoView;
    @BindView(R.id.iv_step_thumbnail)
    ImageView mStepThumbnailImageView;

    public static SimpleExoPlayer mExoPlayer;

    public boolean playWhenReady = true;

    Toast mToast;

    public StepDescriptionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Load the saved state (the list of stepsList and list index) if there is one
        if(savedInstanceState != null) {
            mStepArrayList = savedInstanceState.getParcelableArrayList(STEP_LIST_KEY);
            mListIndex = savedInstanceState.getInt(LIST_INDEX_KEY);
            playWhenReady = savedInstanceState.getBoolean(PLAY_WHEN_READY_KEY);
            if(!(mStepArrayList.get(mListIndex).mVideoUrl.equals(""))) {
                videoPosition = savedInstanceState.getLong(VIDEO_POSITION_KEY);
            }
        }

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_step_description, container, false);

        ButterKnife.bind(this,rootView);

        if(mStepArrayList != null){
            if(mStepArrayList.get(mListIndex).mVideoUrl.equals("")){
                //step does not contain video
                mVideoView.setVisibility(View.GONE);
                if (!(mStepArrayList.get(mListIndex).mThumbnailUrl.equals(""))) {
                    //step contains thumbnail image
                    mStepThumbnailImageView.setVisibility(View.VISIBLE);
                    showImage(mStepArrayList.get(mListIndex).mThumbnailUrl);
                }
            } else {
                //step contains video
                mStepThumbnailImageView.setVisibility(View.GONE);
                initializeExoPlayer(Uri.parse(mStepArrayList.get(mListIndex).mVideoUrl));
            }

            mDescriptionTextView.setText(mStepArrayList.get(mListIndex).mDescription);


            if(DetailsActivity.IS_TABLET) {
                mFabPrev.setVisibility(View.GONE);
                mFabNext.setVisibility(View.GONE);
            } else {
                //goes to next step
                mFabNext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (mListIndex == (mStepArrayList.size() - 1)) {
                            if(mToast != null){
                                mToast.cancel();
                            }
                            mToast = Toast.makeText(getActivity(),
                                    "This is Last Step", Toast.LENGTH_SHORT);
                            mToast.show();

                        } else {
                            mListIndex++;
                        }
                        mListener.onFragmentInteraction(mListIndex);
                    }
                });

                //goes to previous step
                mFabPrev.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListIndex == 0) {
                            if(mToast != null){
                                mToast.cancel();
                            }
                            mToast = Toast.makeText(getActivity(),
                                    "This is First Step", Toast.LENGTH_SHORT);
                            mToast.show();

                        } else {
                            mListIndex--;
                        }
                        mListener.onFragmentInteraction(mListIndex);
                    }
                });
            }
        }

        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;

    }


    public void setStepArrayList(ArrayList<Step> stepArrayList) {
        mStepArrayList = stepArrayList;
    }

    public void setListIndex(int index) {
        mListIndex = index;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(int position);
    }

    /**
     * Save the current state of this fragment
     */
    @Override
    public void onSaveInstanceState(Bundle currentState) {

        if(!(mStepArrayList.get(mListIndex).mVideoUrl.equals(""))) {

            videoPosition = mExoPlayer.getCurrentPosition();
            currentState.putLong(VIDEO_POSITION_KEY, videoPosition);
        }
        currentState.putParcelableArrayList(STEP_LIST_KEY, mStepArrayList);
        currentState.putInt(LIST_INDEX_KEY, mListIndex);
        currentState.putBoolean(PLAY_WHEN_READY_KEY,playWhenReady);
    }


    /**
     * This method initializes exoplayer
     * @Param video url to be streamed
     */
    private void initializeExoPlayer(Uri videoUrlString) {
        if (videoUrlString == null) return;
        TrackSelector trackSelector = new DefaultTrackSelector();
        LoadControl loadControl = new DefaultLoadControl();
        mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
        mVideoView.setPlayer(mExoPlayer);
        mExoPlayer.setRepeatMode(Player.REPEAT_MODE_ONE);

        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        DataSource.Factory dataSourceFactory =
                new DefaultDataSourceFactory(getContext(),
                        Util.getUserAgent(getContext(),
                                "BakingApp"), bandwidthMeter);
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        MediaSource videoSource = new ExtractorMediaSource(videoUrlString,
                dataSourceFactory,
                extractorsFactory,
                null,
                null);
        mExoPlayer.prepare(videoSource);
        if (videoPosition > 0) {
            mExoPlayer.seekTo(videoPosition);
            mExoPlayer.setPlayWhenReady(playWhenReady);
        }

        //if device is not a tablet, then make full screen
        if (!DetailsActivity.IS_TABLET) {
            initializeExoPlayerView(getResources().getConfiguration().orientation);
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        if (mExoPlayer != null) {
            //mExoPlayer.seekTo(videoPosition);
            mExoPlayer.setPlayWhenReady(playWhenReady);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mExoPlayer != null) {
            videoPosition = mExoPlayer.getCurrentPosition();
            playWhenReady = mExoPlayer.getPlayWhenReady();
            mExoPlayer.setPlayWhenReady(false);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    /**
     *This method makes player full screen when device is in landscape mode
     * @Param device orientation
     */
    private void initializeExoPlayerView(int orientation) {
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            mVideoView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mFabNext.setVisibility(View.GONE);
            mFabPrev.setVisibility(View.GONE);
            mVideoView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
            if(((DescriptionActivity) getActivity()) != null) {
                ((DescriptionActivity) getActivity()).getSupportActionBar().hide();
                ((DescriptionActivity) getActivity()).getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            }
        }
    }

    /**
     *This method displays steps thumbnail image
     * @Param step thumbnail URL
     */
    private void showImage(String thumbnailURL) {
        if ( ! TextUtils.isEmpty(thumbnailURL)) {
            Picasso.with(getContext())
                    .load(thumbnailURL)
                    .placeholder(R.drawable.ic_image)
                    .error(R.drawable.ic_broken_image)
                    .into(mStepThumbnailImageView);
        }
    }

}
