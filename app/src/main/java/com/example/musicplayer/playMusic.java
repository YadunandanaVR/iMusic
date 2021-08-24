
package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class playMusic extends AppCompatActivity {

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.pause();
        mediaPlayer.release();
        updateSeekbar.interrupt();
    }

    TextView textView, timeStart, timeEnd;
    SeekBar seekBar;
    ImageView previous, pause, next;
    MediaPlayer mediaPlayer;
    ArrayList<File> songs;
    String currentSong;
    int position;
    Thread updateSeekbar, updateSeekbar2, updateSeekbar3;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);

        textView = findViewById(R.id.textView);
        seekBar = findViewById(R.id.seekBar);
        previous = findViewById(R.id.previous);
        pause = findViewById(R.id.pause);
        next = findViewById(R.id.next);
        timeStart = findViewById(R.id.timeStart);
        timeEnd = findViewById(R.id.timeEnd);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        songs = (ArrayList) bundle.getParcelableArrayList("songList");
        currentSong = intent.getStringExtra("currentSong");
        textView.setText(currentSong);
        textView.setSelected(true);
        position = intent.getIntExtra("position", 0);

        Uri uri = Uri.parse(songs.get(position).toString());
        mediaPlayer = MediaPlayer.create(this, uri);
        mediaPlayer.start();

        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {   //Notification that the user has finished a touch gesture.
                //mediaPlayer.start();
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        updateSeekbar = new Thread(){
            @Override
            public void run() {
                int currentPosition =  0;
                //seekBar.setProgress(currentPosition);
                try {
                    while (currentPosition < mediaPlayer.getDuration()) {
                        currentPosition = mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(currentPosition);
                        sleep(800);
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        updateSeekbar.start();

        String  endTime = createTime(mediaPlayer.getDuration());
        timeEnd .setText(endTime);

        final Handler handler = new Handler();
        final int delay = 1000;

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try{
                    String currentTime = createTime(mediaPlayer.getCurrentPosition());
                    //Log.d("time", currentTime);
                    timeStart.setText(currentTime);
                    handler.postDelayed(this, delay);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }, delay);

//        updateTime = new Thread(){
//            @Override
//            public void run() {
//                String currentTime = "";
//                int time = 0;
//
//                try {
//                    while (time < mediaPlayer.getDuration()){
//                        currentTime = createTime(mediaPlayer.getCurrentPosition());
//                        Log.d("currentTime", currentTime);
//                        timeStart.setText(currentTime);
//                        time = mediaPlayer.getCurrentPosition();
//                        sleep(1000);
//                    }
//                }catch(Exception e){
//                    e.printStackTrace();
//                }
//            }
//        };
//        updateTime.start();

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){
                    pause.setImageResource(R.drawable.play);
                    mediaPlayer.pause();
                }
                else{
                    pause.setImageResource(R.drawable.pause);
                    mediaPlayer.start();
                }
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.pause();
                mediaPlayer.release();
                updateSeekbar.interrupt();
                if(position!=0){
                    position = position - 1;
                }
                else{
                    position = songs.size() - 1;
                }

                updateSeekbar2 = new Thread(){
                    @Override
                    public void run() {
                        int currentPosition =  0;
                        //seekBar.setProgress(currentPosition);
                        try {
                            while (currentPosition < mediaPlayer.getDuration()) {
                                currentPosition = mediaPlayer.getCurrentPosition();
                                seekBar.setProgress(currentPosition);
                                sleep(800);
                            }
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };

                Uri uri = Uri.parse(songs.get(position).toString());
                mediaPlayer = MediaPlayer.create(playMusic.this, uri);

                int currentPosition = mediaPlayer.getCurrentPosition();
                seekBar.setMax(mediaPlayer.getDuration());
                seekBar.setProgress(currentPosition);
                mediaPlayer.start();
                mediaPlayer.seekTo(seekBar.getProgress());
                updateSeekbar2.start();
                pause.setImageResource(R.drawable.pause);
                currentSong = songs.get(position).getName();
                textView.setText(currentSong);
                String  endTime = createTime(mediaPlayer.getDuration());
                timeEnd .setText(endTime);

                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        next.performClick();
                    }
                });
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.pause();
                mediaPlayer.release();

                Log.d("count------------------", String.valueOf(count));
                if(count==0)
                    updateSeekbar.interrupt();

                else
                    updateSeekbar3.interrupt();

                count++;

                if(position != songs.size()-1){
                    position = position + 1;
                }
                else{
                    position = 0;
                }

                updateSeekbar3 = new Thread(){
                    @Override
                    public void run() {
                        int currentPosition =  0;
                        //seekBar.setProgress(currentPosition);
                        try {
                            while (currentPosition < mediaPlayer.getDuration()) {
                                currentPosition = mediaPlayer.getCurrentPosition();
                                seekBar.setProgress(currentPosition);
                                sleep(800);
                            }
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };

                Uri uri = Uri.parse(songs.get(position).toString());
                mediaPlayer = MediaPlayer.create(playMusic.this, uri);
                int currentPosition = mediaPlayer.getCurrentPosition();
                seekBar.setMax(mediaPlayer.getDuration());
                seekBar.setProgress(currentPosition);
                mediaPlayer.start();
                mediaPlayer.seekTo(seekBar.getProgress());
                updateSeekbar3.start();
                pause.setImageResource(R.drawable.pause);
                seekBar.setMax(mediaPlayer.getDuration());
                currentSong = songs.get(position).getName();
                textView.setText(currentSong);
                String  endTime = createTime(mediaPlayer.getDuration());
                timeEnd .setText(endTime);

                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        next.performClick();
                    }
                });

            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                next.performClick();
            }
        });
    }

    public String createTime(int duration){
        String time = "";

        int min = duration/1000/60;
        int sec = duration/1000%60;

        time += min + ":";

        if(sec<10){
            time+="0";
        }
        time += sec;

        return time;
    }

}