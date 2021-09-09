package com.bhumitpatel.project4;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;


public class ImageAdapter extends BaseAdapter {

    private static final int  SIZE = 9;

    private Context mContext;

    ImageAdapter(Context context){
        mContext = context;
    }

    @Override
    public int getCount() {
        return SIZE;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView imageView;

        //System.out.println(position);

        if(convertView == null){

            LayoutInflater inflater = LayoutInflater.from(mContext);
            imageView = (ImageView)inflater.inflate(R.layout.grid_item,parent,  false); // inflate


        }else{
            imageView =  convertView.findViewById(R.id.imageView);
        }

        imageView.setImageResource(R.drawable.tile);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

        return imageView;
    }
}
