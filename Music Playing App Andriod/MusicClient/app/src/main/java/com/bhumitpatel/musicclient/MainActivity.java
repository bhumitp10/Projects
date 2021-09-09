package com.bhumitpatel.musicclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.os.RemoteException;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bhumitpatel.musicapi.MyMusicInterface;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {



    private Button unbindButton;  // unbind from service
    private Button bindButton;   // bind  to service
    private Button requestButton;  // request to get all data of songs and open an it in activity with recyclerview

    private TextView statusView; // display status of connection to the service
    private ListView listView;   // list view of song numbers, when a song number is clicked a new activity opens with the song's info and allows user to play the song

    private MyMusicInterface mMusicInterface; // interface used to get data from the service


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        bindButton = (Button) findViewById(R.id.bindButton);
        unbindButton = (Button) findViewById(R.id.unBindButton);
        requestButton = (Button) findViewById(R.id.requestAll);


        statusView = (TextView) findViewById(R.id.statusView);
        listView = (ListView) findViewById(R.id.listView);

        // disable functionality that cannot happen without binding to service

        listView.setEnabled(false);
        unbindButton.setEnabled(false);
        requestButton.setEnabled(false);

        bindButton.setOnClickListener(e->{
            bindToService();
        });

        // unbind from service when clicked, also disable functionality that relies on the service

        unbindButton.setOnClickListener(e->{
            bindButton.setEnabled(true);
            listView.setEnabled(false);
            unbindButton.setEnabled(false);
            requestButton.setEnabled(false);
            statusView.setText("Not Bounded");
            unbindService(mConnection);
        });

        // when clicked, get all data from service, display all data of songs in recycler view, when recyclerview item is clicked song starts playing immediately
        requestButton.setOnClickListener(e->{
           SongsData data = getAllSongs();  // helper method to get data of all songs

            if(data == null){
                return;
            }

           Intent i = new Intent(this, AllSongList.class);  // create explicit intent

            // pass data to the other activity with extras

            i.putStringArrayListExtra("songs", data.getSongs());
            i.putStringArrayListExtra("artists", data.getArtists());
            i.putStringArrayListExtra("imagePaths", data.getimagePaths()); // note since images are large in size, a filename corresponding to a file storing bitmap is passed
            i.putStringArrayListExtra("songUrls", data.getSongUrls());
            startActivity(i);

        });

        Integer[] songNumberList = new Integer[]{1, 2, 3, 4, 5 , 6};

        listView.setAdapter(new ArrayAdapter<Integer>(this,R.layout.songnum_item,songNumberList));

        listView.setOnItemClickListener((parent, view, position, it) -> {
          //  System.out.println("Position : " + Integer.toString(position));
            startSpecificSongActivity(position);
        });



    }

    // starts an activity for a specific song and display that song's info and allows user to play that song

    void startSpecificSongActivity(int position){

        try {
            Bundle b = mMusicInterface.getSpecificSong(position);  // get song data
            String url = mMusicInterface.getSongURL(position);  // get song url

            String imagePath = createImageFromBitmap(b.getParcelable("Image"), 13); // store song's image bitmap in a file

            Intent i = new Intent(this, SpecificSong.class);

           // System.out.println(b.getString("ArtistName"));
            //System.out.println(url);


            i.putExtra("songName", b.getString("SongName"));
            i.putExtra("artist", b.getString("ArtistName"));
            i.putExtra("imagePath", imagePath);
            i.putExtra("urlSong", url);
            startActivity(i);
        } catch (RemoteException e) {
            e.printStackTrace();
        }


    }

    // Binds to the service and if successful then enables the relevant functionalities

    void bindToService(){

        Intent i = new Intent();
        i.setComponent(new ComponentName("com.bhumitpatel.musiccentral","com.bhumitpatel.musiccentral.MyMusicService" ));

        startForegroundService(i);
        boolean b = bindService(i, this.mConnection, Context.BIND_AUTO_CREATE);

        if(b){
            statusView.setText("Bounded");
            bindButton.setEnabled(false);
            unbindButton.setEnabled(true);
            listView.setEnabled(true);
            requestButton.setEnabled(true);
        }


    }

    private final ServiceConnection mConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className, IBinder iservice) {

            mMusicInterface = MyMusicInterface.Stub.asInterface(iservice);

            }

        public void onServiceDisconnected(ComponentName className) {

            mMusicInterface = null;

        }
    };


    // Gets data from the
    SongsData getAllSongs(){

        SongsData data = null;

        try {
            Bundle bundleList = mMusicInterface.getAllSongs(); // get data of all songs from the service in a bundle

            // unpack all the data send in bundle

            ArrayList<String> songNames = new ArrayList<String>(Arrays.asList(bundleList.getStringArray("songs")));
            ArrayList<String> artists = new ArrayList<String>(Arrays.asList(bundleList.getStringArray("artists")));
            ArrayList<String> songUrls = new ArrayList<String>(Arrays.asList(bundleList.getStringArray("songUrls")));
            ArrayList<Bitmap> images2 = new ArrayList<Bitmap>();
            images2.add(bundleList.getParcelable("0"));
            images2.add(bundleList.getParcelable("1"));
            images2.add(bundleList.getParcelable("2"));
            images2.add(bundleList.getParcelable("3"));
            images2.add(bundleList.getParcelable("4"));
            images2.add(bundleList.getParcelable("5"));

            // since images are too large to be passed into parceable, a file for each image is created

            ArrayList<String> imagePaths = new ArrayList<String>();  // store the filenames of each file containing a bitmap
            for(int i = 0; i < images2.size(); i++){
                String str = createImageFromBitmap(images2.get(i),i);
                //System.out.println(str);
                imagePaths.add(str);
            }

            data = new SongsData(images2,artists, songNames, imagePaths, songUrls);  // store all data in a SongsData object


        } catch (RemoteException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return data;

    }

    // This method creates a file that store a bitmap and returns the filename of the file that was created

    public String createImageFromBitmap(Bitmap bitmap, int i) {
        String fileName = "myImage" + Integer.toString(i);//no .png or .jpg needed
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            FileOutputStream fo = openFileOutput(fileName, Context.MODE_PRIVATE);
            fo.write(bytes.toByteArray());
            // remember close file output
            fo.close();
        } catch (Exception e) {
            e.printStackTrace();
            fileName = null;
        }
        return fileName;
    }







}