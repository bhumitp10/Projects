package com.bhumitpatel.musicclient;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class MyAdapter extends RecyclerView.Adapter {

    // ViewHolder class to hold view data and manage view

    public class ViewHolder extends  RecyclerView.ViewHolder implements  View.OnClickListener{
        public TextView textView;
        public TextView textView2;
        public ImageView imageView;
        private RVClickListener listener;


        public ViewHolder(@NonNull View itemView, RVClickListener passedListener) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.textView);
            textView2 = (TextView) itemView.findViewById(R.id.textView2);
            imageView = (ImageView) itemView.findViewById(R.id.imageView2);
            this.listener = passedListener;

            itemView.setOnClickListener((View v)->{
                listener.onClick(v, getAdapterPosition()); // Call the onClick method and pass in appropriate parameters

            });

        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getAdapterPosition());
            Log.i("Main Activtiy", "clicked");

        }
    }



    private RVClickListener RVlistener;   // store listener
    private SongsData DataOfSongs;  // store data of songs

    MyAdapter(SongsData data, RVClickListener listener){

        this.RVlistener = listener;
        this.DataOfSongs = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View listview = inflater.inflate(R.layout.list_item,parent,  false); // inflate
        ViewHolder viewHolder  = new ViewHolder(listview, RVlistener);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ArrayList<String> songs = this.DataOfSongs.getSongs();   // get list of songs
        ArrayList<String> artists = this.DataOfSongs.getArtists();  // get list of artists
        ArrayList<Bitmap> images = this.DataOfSongs.getImages();   // get list of image ids

        ((ViewHolder)holder).textView.setText(songs.get(position));  // set song name
        ((ViewHolder)holder).textView2.setText(artists.get(position));  // set artist/band name
        ((ViewHolder)holder).imageView.setImageBitmap(images.get(position));  // set image
        ((ViewHolder)holder).imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);  // fit image
    }

    @Override
    public int getItemCount() {
        return DataOfSongs.getSongs().size();  // size of songs
    }
}

