package com.bhumitpatel.musiccentral;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.os.RemoteException;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.bhumitpatel.musicapi.MyMusicInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyMusicService extends Service {

    private final String TAG = "MyMusicService";

    private static final int NOTIFICATION_ID = 1;

    private Notification notification;

    // store data in objects

    private ArrayList<String> songNames;
    private ArrayList<String> artists;
    private String[] songUrls;
    private List<Integer> images;

    private String URL_PARSE_STRING = "https://drive.google.com/uc?export=download&id="; // "the front part of the url", since putting the whole url was not parsing properly

    private static String CHANNEL_ID = "Music player style" ;


    private final MyMusicInterface.Stub mBinder = new MyMusicInterface.Stub() {

        // returns all the info of all songs

        @Override
        public Bundle getAllSongs() throws RemoteException {
           Bundle b = new Bundle();


            synchronized (songNames) {  // lock object
                // store all data in a bundle of all songs

                b.putStringArray("songs", getResources().getStringArray(R.array.songNameList));
                b.putStringArray("artists", getResources().getStringArray(R.array.artistList));
                b.putStringArray("songUrls", songUrls);
                b.putParcelable("0", BitmapFactory.decodeResource(getResources(), images.get(0)));
                b.putParcelable("1", BitmapFactory.decodeResource(getResources(), images.get(1)));
                b.putParcelable("2", BitmapFactory.decodeResource(getResources(), images.get(2)));
                b.putParcelable("3", BitmapFactory.decodeResource(getResources(), images.get(3)));
                b.putParcelable("4", BitmapFactory.decodeResource(getResources(), images.get(4)));
                b.putParcelable("5", BitmapFactory.decodeResource(getResources(), images.get(5)));

            }

            return b;
        }

        // returns info of a song given a song number

        @Override
        public Bundle getSpecificSong(int index) throws RemoteException {
            Bundle b = new Bundle();

            synchronized (songNames) { // lock object
                // store data of a song in bundle

                b.putString("SongName", songNames.get(index));
                b.putString("ArtistName", artists.get(index));
                b.putParcelable("Image", BitmapFactory.decodeResource(getResources(), images.get(index)));

            }

            return b;
        }

        // returns url of a song

        @Override
        public String getSongURL(int index) throws RemoteException {
            return songUrls[index];  // return url
        }
    };


    @Override
    public void onCreate() {
        super.onCreate();

        // UB: Starting in Oreo notifications have a notification channel
        //     The channel defines basic properties of
        this.createNotificationChannel();

        notification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_media_play)
                .setOngoing(true).setContentTitle("Music Playing")
                .setContentText("Enjoy Listening!!")
                .build();

        songNames = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.songNameList)));
        artists = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.artistList)));
        images = Arrays.asList(R.drawable.images1, R.drawable.images2,
                R.drawable.images3, R.drawable.images4, R.drawable.images5, R.drawable.images6);
        songUrls = getSongUrls();

        startForeground(NOTIFICATION_ID, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    // UB 11-12-2018:  Now Oreo wants communication channels...
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        CharSequence name = "Music player notification";
        String description = "The channel for music player notifications";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = null;
            channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    // This methods returns array of strings which are urls , it parses each string since storing the whole url was not working ,
    // for some reason, parsing was required

    public String[] getSongUrls(){
        String[] endUrl = getResources().getStringArray(R.array.songUrls);
        String[] songUrlsList = new String[endUrl.length];


        for(int i = 0; i < endUrl.length; i++){
            String s = URL_PARSE_STRING + endUrl[i];
            songUrlsList[i] = s;

        }

        return songUrlsList;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        stopSelf();
        return super.onUnbind(intent);
    }



}