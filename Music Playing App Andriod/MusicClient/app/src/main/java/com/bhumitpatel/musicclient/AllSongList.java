package com.bhumitpatel.musicclient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.bhumitpatel.musicapi.MyMusicInterface;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class AllSongList extends AppCompatActivity {

    // Store data of songs, artists, images , and urls

    private ArrayList<String> songNames;
    private ArrayList<String> artists;
    private ArrayList<Bitmap> images;
    private ArrayList<String> imagePaths;
    private ArrayList<String> songUrls;
    private SongsData data;

    // ** Clicking on a Recycler View item will immediately start playing the song no need to click on the "playPauseBtn" **//

    private RecyclerView recyclerView;  // recycler view to display all data
    private Button stopBtn;   // stop the music
    private Button playPauseBtn;  // play or pause the music,
    private TextView currentSongView;
    private int curIndex;

    private  enum State {
        PLAYING, PAUSED, STOPPED, NOT_STARTED
    }

    private State stateOfPlayer;
    private MediaPlayer mediaPlayer;


    // Listener when a Recycle View Item is clicked , when item is clicked song starts playing, the song can be controlled by the pause/play button
    private RVClickListener listener = (view, position)->{

        if(stateOfPlayer == State.PAUSED || stateOfPlayer == State.PLAYING){  // stop the player and reset
            mediaPlayer.stop();
            mediaPlayer.reset();
           // System.out.println("released");
        }

        // prepare the player to start playing

        try {
            mediaPlayer.setDataSource(songUrls.get(position));
            mediaPlayer.prepareAsync();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        // update Current Song view

        String curSongStr = "Current Song: " + songNames.get(position);
        currentSongView.setText(curSongStr);
        stateOfPlayer = State.PLAYING;
        curIndex = position;
        playPauseBtn.setEnabled(true);
        stopBtn.setEnabled(true);
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_song_list);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        stopBtn = (Button) findViewById(R.id.stopBtn2);
        playPauseBtn = (Button) findViewById(R.id.playPauseButton);
        currentSongView = (TextView) findViewById(R.id.textIndex);


        if(getIntent() != null){

            // pack data into intent extra

           songNames = getIntent().getStringArrayListExtra("songs");
           artists = getIntent().getStringArrayListExtra("artists");
           songUrls = getIntent().getStringArrayListExtra("songUrls");
            imagePaths = getIntent().getStringArrayListExtra("imagePaths");

            images  = new ArrayList<Bitmap>();

            // decode bitmap from the passed file name that stores the image, since parceable was too large a file was passed

            for(int i = 0; i < imagePaths.size(); i++){
               try {
                   Bitmap bitmap = BitmapFactory.decodeStream(openFileInput("myImage" + Integer.toString(i)));  // get bitmap from file
                   images.add(bitmap);
               } catch (FileNotFoundException e) {
                   e.printStackTrace();
               }
           }

        }

        data = new SongsData(images, artists, songNames, imagePaths, songUrls);  // store data

        MyAdapter adapter = new MyAdapter(data, listener);  // create adapter for recycler view

        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        stateOfPlayer = State.NOT_STARTED;
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

        mediaPlayer.setOnCompletionListener(e->{
            mediaPlayer.start();
        });


        // playPause button will play song if it is not playing and pause if it is playing.

        playPauseBtn.setOnClickListener(e->{
           if(stateOfPlayer == State.PAUSED) {
                mediaPlayer.start();
                stateOfPlayer = State.PLAYING;
            }else if(stateOfPlayer == State.PLAYING){
                mediaPlayer.pause();
                stateOfPlayer = State.PAUSED;
            }else if(stateOfPlayer == State.STOPPED){
               try {
                   mediaPlayer.setDataSource(songUrls.get(curIndex));
                   mediaPlayer.prepareAsync();
               } catch (IOException ioException) {
                   ioException.printStackTrace();
               }
               stopBtn.setEnabled(true);
               stateOfPlayer = State.PLAYING;
           }
        });

        // stopBtn when clicked will stop the song and reset player if a song was playing or paused

        stopBtn.setOnClickListener(e->{
            if((stateOfPlayer == State.PLAYING) || (stateOfPlayer == State.PAUSED)){
                mediaPlayer.stop();
                mediaPlayer.reset();
                stateOfPlayer = State.STOPPED;
                stopBtn.setEnabled(false);
            }
        });

        curIndex = -1;
        playPauseBtn.setEnabled(false);
        stopBtn.setEnabled(false);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(stateOfPlayer == State.PLAYING || stateOfPlayer == State.PAUSED){
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
        mediaPlayer.release();
    }



}