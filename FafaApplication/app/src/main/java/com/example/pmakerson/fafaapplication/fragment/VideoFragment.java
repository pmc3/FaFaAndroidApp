package com.example.pmakerson.fafaapplication.fragment;

import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.pmakerson.fafaapplication.R;
import com.example.pmakerson.fafaapplication.util.Const;

/**
 * Created by P Makerson on 31/03/2016.
 */
public class VideoFragment extends Fragment {

    private VideoView myVideoView;
    private MediaController mediaControls;
    private ProgressDialog progressDialog;
    private int position;


    public VideoFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set the media controller buttons
        if (mediaControls == null) {
            mediaControls = new MediaController(getActivity());
        }

        position = 0;


        // create a progress bar while the video file is loading
        progressDialog = new ProgressDialog(getActivity());
        // set a title for the progress bar
        progressDialog.setTitle("Android Video View Example");
        // set a message for the progress bar
        progressDialog.setMessage("Loading...");
        //set the progress bar not cancelable on users' touch
        progressDialog.setCancelable(false);
        // show the progress bar
        progressDialog.show();


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View videoView = inflater.inflate(R.layout.item_video_view, container, false);
        return videoView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //initialize the VideoView
        myVideoView = (VideoView) view.findViewById(R.id.videoView);

        try {

            //set the media controller in the VideoView
            myVideoView.setMediaController(mediaControls);

            //set the uri of the video to be played
            Uri uriPath = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.playme);
            //Uri uriPath = Uri.parse(Const.STREAMING_VIDEO_LINK);

            myVideoView.setVideoURI(uriPath);


        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }


        myVideoView.requestFocus();
        //we also set an setOnPreparedListener in order to know when the video file is ready for playback
        myVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            public void onPrepared(MediaPlayer mediaPlayer) {
                // close the progress bar and play the video
                progressDialog.dismiss();
                //if we have a position on savedInstanceState, the video playback should start from here
                myVideoView.seekTo(position);

                if (position == 0) {
                    myVideoView.start();
                } else {
                    //if we come from a resumed activity, video playback will be paused
                    myVideoView.pause();
                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //we use onSaveInstanceState in order to store the video playback position for orientation change
        outState.putInt("Position", myVideoView.getCurrentPosition());
        myVideoView.pause();
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            position = savedInstanceState.getInt("Position");
            myVideoView.seekTo(position);
        }
    }


}
