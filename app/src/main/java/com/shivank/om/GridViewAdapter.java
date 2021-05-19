package com.shivank.om;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;

public class GridViewAdapter extends BaseAdapter {

    Context context;
    ImageView imgView;
    int[] imgIds = {R.drawable.om, R.drawable.ganeshji, R.drawable.shivji, R.drawable.hanumanji, R.drawable.matarani,
    R.drawable.krishnaji, R.drawable.vishnuji, R.drawable.malala};

    public GridViewAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return imgIds.length;
    }

    @Override
    public Object getItem(int position) {
        return imgIds[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        float density = context.getResources().getDisplayMetrics().density;

        imgView = new ImageView(context);
        if (density > 2.0){
            imgView.setLayoutParams(new ViewGroup.LayoutParams(500, 500));
        }else if (density <= 2.0){
            imgView.setLayoutParams(new ViewGroup.LayoutParams(350, 350));
        }

        imgView.setPadding(2, 2, 2, 2);
        Glide.with(context)
                .load(imgIds[position])
                .circleCrop()
                .into(imgView);
        Intent ip = new Intent(context,MusicBackgroundService.class);
        if (MusicBackgroundService.servicePlaying){
            //MusicBackgroundService.mediaPlayer.start();
            //Toast.makeText(context, "Grid Main", Toast.LENGTH_SHORT).show();
            //ContextCompat.startForegroundService(context,ip);
            //MusicActivity.mediaPlayer.start();
        }
        return imgView;
    }

    public ImageView getImgView() {
        return imgView;
    }

    public void setImgView(ImageView imgView) {
        this.imgView = imgView;
    }
}
