package com.example.musicapplication;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MusicPlayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MusicPlayFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Button btnPlay,btnNext,btnPrevious,btnFastForward,btnFastBackward;
    TextView txtSongName,txtSongStart,txtSongEnd;
    SeekBar musicSeekbar;
    ImageView songImage;

    String songName;
    int position;
    LinkedList<File> songs;
    static MediaPlayer mediaPlayer;
    public static final String NAME = "song_name";

    public MusicPlayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MusicPlayFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MusicPlayFragment newInstance(String param1, String param2) {
        MusicPlayFragment fragment = new MusicPlayFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_music_play, container, false);
        btnPlay = view.findViewById(R.id.btnPlay);
        btnPrevious = view.findViewById(R.id.btnPrevious);
        btnNext = view.findViewById(R.id.btnNext);
        btnFastForward = view.findViewById(R.id.btnForward);
        btnFastBackward = view.findViewById(R.id.btnRewind);

        txtSongName = view.findViewById(R.id.txtSong);
        txtSongStart = view.findViewById(R.id.txtSongStart);
        txtSongEnd = view.findViewById(R.id.txtSongEnd);
        musicSeekbar = view.findViewById(R.id.seekBar);

        songImage = view.findViewById(R.id.imgView);

        if (mediaPlayer != null)
        {
            mediaPlayer.start();
            mediaPlayer.release();
        }

        Intent intent = new Intent();
        Activity activity = getActivity();
        if (activity != null){
            intent = activity.getIntent();
        }

        Bundle bundle = intent.getExtras();

        songs = (LinkedList<File>)bundle.getSerializable("songs");
        String sName = intent.getStringExtra("songName");
        position = intent.getIntExtra("pos", 0);
        txtSongName.setSelected(true);
        Uri uri = Uri.parse(songs.get(position).getName());
        songName = songs.get(position).getName();

        mediaPlayer = MediaPlayer.create(getContext(), uri);
        mediaPlayer.start();

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){
                    btnPlay.setBackgroundResource(R.drawable.ic_play);
                    mediaPlayer.pause();
                }
                else {
                    btnPlay.setBackgroundResource(R.drawable.icon_play);
                    mediaPlayer.start();

                    TranslateAnimation moveAni = new TranslateAnimation(-25,25,-25,25);
                    moveAni.setInterpolator(new AccelerateInterpolator());
                    moveAni.setDuration(600);
                    moveAni.setFillEnabled(true);
                    moveAni.setFillAfter(true);
                    moveAni.setRepeatMode(Animation.REVERSE);
                    moveAni.setRepeatCount(1);
                    songImage.setAnimation(moveAni);
                }
            }
        });
        return view;
    }
}