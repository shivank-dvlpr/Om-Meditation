package com.shivank.om;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class MediaBroadcastReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {


        String action = intent.getAction();


        if (action.equals("com.shivank.om.PAUSE_MUSIC_ACTION")) {
       /*     if (MusicActivity.mediaPlayer.isPlaying()){
                Toast.makeText(context, "Called", Toast.LENGTH_SHORT).show();
                MusicActivity.mediaPlayer.pause();
                MusicActivity.mediaPlayer.seekTo(MusicActivity.serviceMediaLength);
            }*/

        }
        else if (action.equals("com.shivank.om.STOP_MUSIC_ACTION")){
           // MusicActivity.mediaPlayer.stop();
            Toast.makeText(context, "stop", Toast.LENGTH_SHORT).show();
            //MusicBackgroundService.notificationManager.cancelAll();
            MusicBackgroundService musicBackgroundService = new MusicBackgroundService();
            musicBackgroundService.startService(new Intent(context,MusicBackgroundService.class).setAction("STOP"));


        }


    }
}
