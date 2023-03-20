package com.example.musicapplication;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.musicapplication.Class.Song;

import java.io.File;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MusicPlayFragment#} factory method to
 * create an instance of this fragment.
 */
public class MusicPlayFragment extends AppCompatActivity {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String songName;
    private ArrayList<File> songs;
    private ArrayList<Song> songOn;

    Button btnPlay,btnNext,btnPrevious,btnFastForward,btnFastBackward;
    TextView txtSongName,txtSongStart,txtSongEnd;
    SeekBar musicSeekbar;
    ImageView songImage;
    int position;

    private MainActivity main;

    Thread updateSeekbar;
    static MediaPlayer mediaPlayer;
    public static final String NAME = "song_name";

    public MusicPlayFragment() {
        // Required empty public constructor
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_music_play);
        
        btnPlay = findViewById(R.id.btnPlay);
        btnPrevious = findViewById(R.id.btnPrevious);
        btnNext = findViewById(R.id.btnNext);
        btnFastForward = findViewById(R.id.btnForward);
        btnFastBackward = findViewById(R.id.btnRewind);

        txtSongName = findViewById(R.id.txtSong);
        txtSongStart = findViewById(R.id.txtSongStart);
        txtSongEnd = findViewById(R.id.txtSongEnd);
        musicSeekbar = findViewById(R.id.seekBar);

        songImage = findViewById(R.id.imgView);

        if (mediaPlayer != null)
        {
            mediaPlayer.start();
            mediaPlayer.release();
        }

//        // Tạo Uri từ đường dẫn trên
//        Uri uri = Uri.parse(firebaseMusicLink);
//
//// Đọc dữ liệu từ Uri và lưu vào InputStream
//        InputStream inputStream = getContentResolver().openInputStream(uri);
//
//// Đưa nội dung của InputStream vào MediaPlayer
//        mediaPlayer.setDataSource(inputStream.getFD());
//        mediaPlayer.prepare();
//        mediaPlayer.start();


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        int check = bundle.getInt("check");
        songs = (ArrayList) bundle.getParcelableArrayList("song");
        songOn = (ArrayList) bundle.getParcelableArrayList("listOn");
        String sName = intent.getStringExtra("songName");
        position = intent.getIntExtra("position",0);
        txtSongName.setSelected(true);
        Uri uri = null;
        txtSongName.setText(bundle.getString("songName"));
        if (check == 0) {
            uri = Uri.parse(songs.get(position).getPath());
        }
        else{
            uri =Uri.parse(songOn.get(position).getSongLink());
        }

        mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
        mediaPlayer.start();

//        if (songs != null) {
//            try {
//                mediaPlayer.setDataSource(url);
//                mediaPlayer.prepare();
//
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }

        updateSeekbar = new Thread(){
            @Override
            public void run() {
                int toltalDuration = mediaPlayer.getDuration();
                int currentDuration = 0;

                while (currentDuration < toltalDuration){
                    try {
                        sleep(500);
                        currentDuration = mediaPlayer.getCurrentPosition();
                        musicSeekbar.setProgress(currentDuration);
                    } catch (InterruptedException | IllegalStateException e){
                        e.printStackTrace();
                    }
                }
            }
        };
        musicSeekbar.setMax(mediaPlayer.getDuration());
        updateSeekbar.start();
        musicSeekbar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.purple_700), PorterDuff.Mode.MULTIPLY);
        musicSeekbar.getThumb().setColorFilter(getResources().getColor(R.color.purple_700), PorterDuff.Mode.SRC_IN);

        musicSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        final Handler handler = new Handler();
        final int delay = 0;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String currentTime = Time(mediaPlayer.getCurrentPosition());
                String EndTime = Time(mediaPlayer.getDuration());
                txtSongStart.setText(currentTime);
                txtSongEnd.setText(EndTime);
                handler.postDelayed(this,delay);
            }
        }, delay);

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){
                    btnPlay.setBackgroundResource(R.drawable.ic_play);
                    mediaPlayer.pause();
                }
                else {
                    btnPlay.setBackgroundResource(R.drawable.ic_pause);
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

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if (check == 0) {
                    position = ((position + 1) % songs.size());
                    Uri uri = Uri.parse(songs.get(position).toString());
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                    songName = songs.get(position).getName();
                }
                else{
                    position = ((position + 1) % songOn.size());
                    Uri uri = Uri.parse(songOn.get(position).getSongLink());
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                    songName = songOn.get(position).getSongTitle();
                }
                txtSongName.setText (songName);
                mediaPlayer.start();
            }
        });

        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if (check == 1){
                    position = ((position-1)<0)? (songs.size()-1): position-1;
                    Uri uri = Uri.parse(songs.get(position).toString());
                    mediaPlayer = MediaPlayer.create (getApplicationContext(), uri);
                    songName = songs.get(position).getName();
                }
                else{
                    position = ((position-1)<0)? (songOn.size()-1): position-1;
                    Uri uri = Uri.parse(songOn.get(position).getSongLink());
                    mediaPlayer = MediaPlayer.create (getApplicationContext(), uri);
                    songName = songOn.get(position).getSongTitle();
                }
                txtSongName.setText (songName);
                mediaPlayer.start();
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                btnNext.performClick();
            }
        });

        btnFastForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + 10000);
                }
            }
        });

        btnFastBackward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - 10000);
                }
            }
        });
    }

    public String Time(int durantion){
        String time ="";
        int min = durantion/1000/60;
        int sec = durantion/1000%60;

        time = time+min+":";
        if (sec < 10){
            time+="0";
        }
        time+=sec;
        return time;
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
//    public static MusicPlayFragment newInstance(String param1, LinkedList<File> param2) {
//        MusicPlayFragment fragment = new MusicPlayFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putSerializable(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }



//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_music_play, container, false);
//        return view;
//    }



//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        if (getArguments() != null) {
//            songName = getArguments().getString(ARG_PARAM1);
//            songs = (LinkedList<File>) getArguments().getSerializable(ARG_PARAM2);
//        }
//    }
}