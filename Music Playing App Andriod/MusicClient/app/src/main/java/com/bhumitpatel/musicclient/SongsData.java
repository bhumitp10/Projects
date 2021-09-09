package com.bhumitpatel.musicclient;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class SongsData {

    private ArrayList<Bitmap> images;
    private ArrayList<String> artists;
    private ArrayList<String> songs;
    private ArrayList<String> imagePaths;
    private ArrayList<String> songUrls;



    public SongsData(ArrayList<Bitmap> images, ArrayList<String> artists, ArrayList<String> songs, ArrayList<String> imagePaths, ArrayList<String> songUrls) {
        this.images = images;
        this.artists = artists;
        this.songs = songs;
        this.imagePaths = imagePaths;
        this.songUrls = songUrls;
    }


    public ArrayList<Bitmap> getImages() {
        return images;
    }


    public ArrayList<String> getArtists() {
        return artists;
    }


    public ArrayList<String> getSongs() {
        return songs;
    }

    public ArrayList<String> getimagePaths() {return imagePaths;}

    public ArrayList<String> getSongUrls(){return  songUrls;}

}
