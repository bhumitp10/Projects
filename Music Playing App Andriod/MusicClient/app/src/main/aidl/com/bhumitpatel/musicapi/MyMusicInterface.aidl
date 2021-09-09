// MyMusicInterface.aidl
package com.bhumitpatel.musicapi;



// Declare any non-default types here with import statements

interface MyMusicInterface {
    
    Bundle getAllSongs();
    Bundle getSpecificSong(int index);
    String getSongURL(int index);
}