package com.bhumitpatel.musicclient;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.IOException;

public class SpecificSong extends AppCompatActivity {

    // UI objects

    private ImageView imageView;
    private TextView songNameView;
    private TextView artistNameView;
    private Button playPauseBtn;
    private Button stopBtn;

    // song info objects

    private String songName;
    private String artistName;
    private Bitmap bitmap;
    private String urlSong;

    private MediaPlayer mediaPlayer; // player to play music

    // represents the state of music
    private enum State {
        PLAYING, PAUSED, STOPPED, NOT_STARTED
    }

    private State stateOfPlayer; // store state of music

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_song);

        imageView = (ImageView) findViewById(R.id.songImageView);
        songNameView = (TextView) findViewById(R.id.songNameView);
        artistNameView = (TextView) findViewById(R.id.artistNameView);
        playPauseBtn = (Button) findViewById(R.id.playPauseButton);
        stopBtn = (Button) findViewById(R.id.stopBtn);

        // set mediaplayer attributes

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioAttributes(
                new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                        .build()
        );

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer player) {
                player.start();
            }

        });

        if(getIntent() != null){

            // get extras from intent to get relevant data

            songName = getIntent().getStringExtra("songName");
            artistName = getIntent().getStringExtra("artist");

            String imagePath = getIntent().getStringExtra("imagePath");
            try {
                bitmap = BitmapFactory.decodeStream(openFileInput(imagePath));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            urlSong = getIntent().getStringExtra("urlSong");

            imageView.setImageBitmap(bitmap);
            songNameView.setText(songName);
            artistNameView.setText(artistName);

        }

        stateOfPlayer = State.NOT_STARTED;

        // If music is not playing play it, if it is playing then pause it

        playPauseBtn.setOnClickListener(e->{

            if(stateOfPlayer == State.NOT_STARTED || stateOfPlayer == State.STOPPED){
                try {
                    mediaPlayer.setDataSource(urlSong);
                    mediaPlayer.prepareAsync();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

                stateOfPlayer = State.PLAYING;
            }else if(stateOfPlayer == State.PAUSED) {
                mediaPlayer.start();
                stateOfPlayer = State.PLAYING;
            }else if(stateOfPlayer == State.PLAYING){
                mediaPlayer.pause();
                stateOfPlayer = State.PAUSED;
            }

            stopBtn.setEnabled(true);
        });

        // If music is playing , then stop it

        stopBtn.setOnClickListener(e->{
            if((stateOfPlayer == State.PLAYING) || (stateOfPlayer == State.PAUSED)){
                mediaPlayer.stop();
                mediaPlayer.reset();
                stateOfPlayer = State.STOPPED;
                stopBtn.setEnabled(false);
            }

        });

        mediaPlayer.setOnCompletionListener(e->{
            mediaPlayer.start();
        });

        stopBtn.setEnabled(false);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(stateOfPlayer == State.PLAYING || stateOfPlayer == State.PAUSED){
            mediaPlayer.stop();
        }
        mediaPlayer.reset();
        mediaPlayer.release();
    }

}