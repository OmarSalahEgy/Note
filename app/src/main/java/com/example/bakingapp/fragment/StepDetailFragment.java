package com.example.bakingapp.fragment;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.bakingapp.R;
import com.example.bakingapp.model.Steps;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class StepDetailFragment extends Fragment implements ExoPlayer.EventListener{

    private static final String TAG = StepDetailFragment.class.getSimpleName();
    private Steps step;
    private ArrayList<Steps> steps;
    private TextView shortDescriptionTextView;
    private TextView descriptionTextView;
    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;
    private static MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    private Context context;
    private ImageView thumbnailView;
    private final String STATE_PLAYER_FULLSCREEN = "playerFullscreen";
    View rootView;



    public StepDetailFragment(){}
    public void setStepDetailFragment(ArrayList<Steps> steps,Steps step,Context context){
        this.step=step;
        this.steps=steps;
        this.context=context;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (savedInstanceState != null) {

            step = savedInstanceState.getParcelable(STATE_PLAYER_FULLSCREEN);
        }

         rootView = inflater.inflate(R.layout.fragment_setp_detail, container, false);
        shortDescriptionTextView=rootView.findViewById(R.id.short_description);
        descriptionTextView=rootView.findViewById(R.id.description);
        mPlayerView=rootView.findViewById(R.id.playerView);
        thumbnailView=rootView.findViewById(R.id.thumbnailView);



        initializeMediaSession();
       if (!step.getVideoURL().isEmpty()){
           mPlayerView.setVisibility(View.VISIBLE);
           initializePlayer(Uri.parse(step.getVideoURL()));}
        if (!step.getThumbnailURL().isEmpty()) {
            // if Thumbnail exists but no video exists (General case 2)
            mPlayerView.setVisibility(View.INVISIBLE);
            thumbnailView.setVisibility(View.VISIBLE);
            Picasso.get()
                    .load(step.getThumbnailURL())
                    .into(thumbnailView);
        }
        if (step.getVideoURL().isEmpty()&&step.getThumbnailURL().isEmpty())
        {
            mPlayerView.setVisibility(View.INVISIBLE);
            thumbnailView.setVisibility(View.INVISIBLE);
        }

        shortDescriptionTextView.setText(step.getShortDescription());
        descriptionTextView.setText(step.getDescription());

        return rootView;
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putParcelable(STATE_PLAYER_FULLSCREEN, step);

        super.onSaveInstanceState(outState);
    }
    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {

           // initFullscreenDialog();
           // initFullscreenButton();
            TrackSelector trackSelector=new DefaultTrackSelector();
            LoadControl loadControl=new DefaultLoadControl();
            mExoPlayer= ExoPlayerFactory.newSimpleInstance(context,trackSelector,loadControl);
            mPlayerView.setPlayer(mExoPlayer);

            mExoPlayer.addListener(this);

            String userAgent= Util.getUserAgent(context," Classicl Music Quiz ");
            MediaSource mediaSource=new ExtractorMediaSource(mediaUri,new DefaultDataSourceFactory(context,userAgent)
                    ,new DefaultExtractorsFactory(),null,null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }



    }

    private void initializeMediaSession() {

        // Create a MediaSessionCompat.
        mMediaSession = new MediaSessionCompat(context, TAG);

        // Enable callbacks from MediaButtons and TransportControls.
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Do not let MediaButtons restart the player when the app is not visible.
        mMediaSession.setMediaButtonReceiver(null);

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player.
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE|
                                PlaybackStateCompat.ACTION_SKIP_TO_NEXT);

        mMediaSession.setPlaybackState(mStateBuilder.build());


        // MySessionCallback has methods that handle callbacks from a media controller.
        mMediaSession.setCallback(new MySessionCallback());

        // Start the Media Session since the activity is active.
        mMediaSession.setActive(true);

    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

        if((playbackState == ExoPlayer.STATE_READY) && playWhenReady){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(), 1f);
        } else if((playbackState == ExoPlayer.STATE_READY)){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(), 1f);
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());

    }


    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }



    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            mExoPlayer.seekTo(0);
        }

        @Override
        public void onSkipToNext() {
            mExoPlayer.seekTo(0);        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mExoPlayer!=null){
        releasePlayer();
        mMediaSession.setActive(false);
        }
    }
    private void releasePlayer() {
        if(mExoPlayer!=null){
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);


        if (getActivity().getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_LANDSCAPE) {
            LinearLayout.LayoutParams params ;
            params = (LinearLayout.LayoutParams)
                    mPlayerView.getLayoutParams();
            params.width = params.MATCH_PARENT;
            params.height = params.MATCH_PARENT;
            mPlayerView.setLayoutParams(params);
            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        }else if (getActivity().getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_PORTRAIT){
            mPlayerView=rootView.findViewById(R.id.playerView);
        }
    }
}



