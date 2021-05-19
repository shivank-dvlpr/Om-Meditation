package com.shivank.om;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadata;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;

import java.sql.Time;


public class MusicBackgroundService extends Service implements MediaPlayer.OnPreparedListener {
    public static NotificationManager notificationManager, notificationManager1;
    public static NotificationChannel notificationChannel;
    public static Notification notification, notification1;
    Bitmap bitmap;
    MediaSessionCompat mediaSessionCompat, mediaSessionCompat1;
    MusicActivity musicActivity;
    static int aa, start, loop, seek, playPid,getImage, startMp3;
    boolean at;
    MediaBroadcastReceiver mediaBroadcastReceiver = new MediaBroadcastReceiver();
    IntentFilter intentFilter1;
    Intent playIntent, pauseIntent, musicIntent;
    PendingIntent playPending, pausePending,musicI, stopPending;
    static boolean servicePlaying;
    static MediaPlayer mediaPlayer ;
    static MediaPlayer mediaPlayer22 = new MediaPlayer();
    MusicActivity musicActivity1;
    MediaPlayer mm;
    boolean open;
    int loopMp3, length, length2;
    static boolean counter;
    String notificationTitle, notificationContent;
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            try{
                if (intent.getAction().equals("com.shivank.om.PAUSE_MUSIC_ACTION")) {
                    mediaPlayer = musicActivity1.mediaPlayer;
                    //mediaPlayer22 = new MediaPlayer();
                    //musicActivity1.mediaPlayer.pause();
                    //MusicActivity.mediaPlayer.pause();
                    notification.actions[0] = new Notification.Action(R.drawable.noti_play, "PLAY", playPending);
                    MusicActivity.imgPlay.setVisibility(View.VISIBLE);
                    MusicActivity.imgPause.setVisibility(View.INVISIBLE);
                    mediaPlayer22.pause();
                    seek = mediaPlayer22.getCurrentPosition();

                   if (MusicActivity.countDownRunning){
                       MusicActivity.btn10.setText(MusicActivity.btn10.getText().toString());
                       MusicActivity.countDownTimer.cancel();
                   }

                    //MusicActivity.mediaPlayer.seekTo(seek);
                    mediaPlayer22.seekTo(seek);

                    startForeground(1, notification);
                } else if (intent.getAction().equals("com.shivank.om.STOP_MUSIC_ACTION")) {
                    // MusicActivity.mediaPlayer.stop();
                    mediaPlayer = musicActivity1.mediaPlayer;
                    Toast.makeText(context, "exit", Toast.LENGTH_SHORT).show();
                    //MusicActivity.mediaPlayer.stop();
                    //musicActivity1.mediaPlayer.stop();
                    stopForeground(true);
                    mediaPlayer22.stop();// NEW
                    MusicActivity.ac.finish();
                    stopService(new Intent(context, MusicBackgroundService.class));
                    MusicActivity.reset.callOnClick();
                    unregisterReceiver(receiver);
                    //MusicBackgroundService.notificationManager.cancelAll();
                    // MusicBackgroundService musicBackgroundService = new MusicBackgroundService();
                    //musicBackgroundService.startService(new Intent(context,MusicBackgroundService.class).setAction("STOP"));

                } else if (intent.getAction().equals("com.shivank.om.PLAY_MUSIC_ACTION")) {
                    //MusicActivity.mediaPlayer.start();
                    mediaPlayer = musicActivity1.mediaPlayer;
                    //musicActivity1.mediaPlayer.start();
                    mediaPlayer22.start();
                    MusicActivity.imgPlay.setVisibility(View.INVISIBLE);
                    MusicActivity.imgPause.setVisibility(View.VISIBLE);
                    notification.actions[0] = new Notification.Action(R.drawable.noti_pause, "PAUSE", pausePending);
                    //MusicActivity.mediaPlayer.seekTo(seek);
//                    musicActivity1.mediaPlayer.seekTo(seek);


                    if (MusicActivity.btns == 10){
                        musicActivity1.buttonTimer(MusicActivity.btn10, MusicActivity.timeLeft, "10MIN");
                    }else if (MusicActivity.btns == 15){
                        musicActivity1.buttonTimer(MusicActivity.btn15, MusicActivity.timeLeft, "15MIN");
                    }else if (MusicActivity.btns == 30){
                        musicActivity1.buttonTimer(MusicActivity.btn30, MusicActivity.timeLeft, "30MIN");
                    }

                    mediaPlayer22.seekTo(seek);
                    startForeground(1, notification);
                }
            }catch (NullPointerException nullPointerException){
                Log.i("OnRecieve", nullPointerException.getLocalizedMessage());
            }


        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
//        MusicActivity.open = true;
        musicActivity = new MusicActivity();
        musicActivity1 = new MusicActivity();
       // open = musicActivity1.serviceOpen;

        //at = MusicActivity.mediaPlayer.isPlaying();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.shivank.om.PAUSE_MUSIC_ACTION");
        intentFilter.addAction("com.shivank.om.STOP_MUSIC_ACTION");
        intentFilter.addAction("com.shivank.om.PLAY_MUSIC_ACTION");
        registerReceiver(receiver, intentFilter);
       /* intentFilter1 = new IntentFilter();
        intentFilter1.addAction("com.shivank.om.PAUSE_MUSIC_ACTION");
        intentFilter1.addAction("com.shivank.om.STOP_MUSIC_ACTION");
        registerReceiver(mediaBroadcastReceiver, intentFilter1);*/
        mediaSessionCompat = new MediaSessionCompat(this, "TAG");
        mediaSessionCompat.setMetadata(
                new MediaMetadataCompat.Builder()
                        .putString(MediaMetadata.METADATA_KEY_TITLE, notificationTitle)
                        .putString(MediaMetadata.METADATA_KEY_ARTIST, notificationContent)
                        .build()
        );

        mediaSessionCompat1 = new MediaSessionCompat(this,"TAG");
        mediaSessionCompat1.setMetadata(
                new MediaMetadataCompat.Builder()
                        .putString(MediaMetadata.METADATA_KEY_TITLE, "OM")
                        .putString(MediaMetadata.METADATA_KEY_ARTIST, "OM")
                        .build()
        );

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        musicActivity1 = new MusicActivity();
        mediaPlayer = musicActivity1.mediaPlayer;
        musicActivity = new MusicActivity();

        try {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {

                aa = intent.getExtras().getInt("imgValue");
                start = intent.getExtras().getInt("startMp3");
                loop = intent.getExtras().getInt("loopMp3");
                playPid = intent.getExtras().getInt("pp");
                String pp= intent.getExtras().getString("stop");
                notificationTitle = intent.getStringExtra("notificationTitle");
                notificationContent = intent.getStringExtra("notificationContent");
                getImage = intent.getExtras().getInt("notImage");


                // mediaPlayer = MediaPlayer.create(MusicBackgroundService.this, loop);
                if (musicActivity1.mediaPlayer.isPlaying()){
                    musicActivity1.mediaPlayer.stop();

                    //musicActivity1.mediaPlayerLoop(start,loop, getApplicationContext());
                    mediaPlayerLoop1(start, loop);
                   /* mediaPlayer.seekTo(musicActivity1.length);
                    mediaPlayer.start();*/

                    //mediaPlayer.start();
                }

                //musicActivity.mediaPlayerLoop(start, loop, MusicBackgroundService.this);
                //mediaPlayer.seekTo(MusicActivity.serviceMediaLength);
                //mediaPlayer.start();

                //musicActivity.mediaPlayerLoop(start, loop, MusicBackgroundService.this);




            }
        }catch (NullPointerException nullPointerException){
            Log.i("Bundle",nullPointerException.getLocalizedMessage());
        }



      /*  if (MusicActivity.btns == 10 ){
            //Toast.makeText(getApplicationContext(), "App Automatically Closes After Time Expired.", Toast.LENGTH_LONG).show();
            //musicActivity1.buttonTimer(MusicActivity.btn10, 600000, "10MIN");
        } else if (MusicActivity.btns == 15 ){
            //Toast.makeText(getApplicationContext(), "App Automatically Closes After Time Expired.", Toast.LENGTH_LONG).show();
            //musicActivity1.buttonTimer(MusicActivity.btn10, 600000, "10MIN");
        }*/
        //musicActivity1.serviceOpen = true;
        //open = musicActivity1.serviceOpen;
        //MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.om_start);
        //Intent intent1 = new Intent(this, MusicActivity.class);
        // PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent1, 0);
/*
         if (intent.getAction()!=null && intent.getAction().equals("STOP")){
            stopForeground(true);
        }*/
        servicePlaying = true;
      /*  if (MusicActivity.mediaPlayer.isPlaying()){
            MusicActivity.imgPlay.setVisibility(View.GONE);
            MusicActivity.imgPause.setVisibility(View.VISIBLE);
        }*/

        pauseIntent = new Intent("com.shivank.om.PAUSE_MUSIC_ACTION");
        pausePending = PendingIntent.getBroadcast(MusicBackgroundService.this, 1, pauseIntent, 0);

        Intent exitIntent = new Intent("com.shivank.om.STOP_MUSIC_ACTION");
        stopPending = PendingIntent.getBroadcast(MusicBackgroundService.this, 2, exitIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        playIntent = new Intent("com.shivank.om.PLAY_MUSIC_ACTION");
        playPending = PendingIntent.getBroadcast(MusicBackgroundService.this, 3, playIntent, 0);


        musicIntent = new Intent(this, MusicActivity.class);
        //musicActivity.openImagesAndGif(getApplicationContext());
        musicIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        musicIntent.putExtra("notiImageValue", MainActivity.imgValues);
        musicIntent.putExtra("mp3", start);
        musicIntent.putExtra("start", true);
        musicI = PendingIntent.getActivity(getApplicationContext(),4, musicIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager1 = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationChannel = new NotificationChannel("MYCHANNEL", "Meditation", NotificationManager.IMPORTANCE_LOW);
        notificationManager.createNotificationChannel(notificationChannel);






        bitmap = BitmapFactory.decodeResource(getResources(), aa);


        //at = MusicActivity.mediaPlayer.isPlaying();


        startNotification();


        // MusicActivity.mediaPlayer.start();
        //mediaPlayer.start();

        return START_STICKY;
    }

    public void startNotification(){
        notification = new NotificationCompat.Builder(this, "MYCHANNEL")
                .setSmallIcon(R.drawable.noti_play)
                .setContentTitle(notificationTitle)
                .setContentText(notificationContent)
                .setLargeIcon(bitmap)
                .addAction(R.drawable.noti_pause, "PAUSE", pausePending)
                .addAction(R.drawable.exit, "EXIT", stopPending)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(0, 1)
                        .setMediaSession(mediaSessionCompat.getSessionToken()))
                .setContentIntent(musicI)
                .setColorized(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();


        //notificationManager.notify(1, notification);
        startForeground(1, notification);
    }

    public void startNotification2(Context context, MediaSessionCompat mediaSessionCompat2 ){
        notification1 = new NotificationCompat.Builder(context, "MYCHANNEL")
                .setSmallIcon(R.drawable.noti_play)
                .setContentTitle("Music")
                .setContentText("OM")
                .setLargeIcon(bitmap)
                .addAction(R.drawable.noti_play, "PAUSE", playPending)
                .addAction(R.drawable.exit, "EXIT", stopPending)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(1, 3)
                        .setMediaSession(mediaSessionCompat2.getSessionToken()))
                .setContentIntent(musicI)
                .setColorized(true)
                .setColorized(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();


        //notificationManager.notify(0, notification);
        notificationManager.notify(1, notification);
        //startForeground(1, notification1);
    }

    public void mediaPlayerLoop1(int startMp3 , int loopMp3 ){


        this.loopMp3 = loopMp3;
        /*start = startMp3;
        loop = loopMp3;*/




        if (MusicActivity.serviceOpen){
            mediaPlayer22 = MediaPlayer.create(getApplicationContext(), startMp3);
        }else  {
            mediaPlayer22 = MediaPlayer.create(getApplicationContext(), loopMp3);
        }


        mediaPlayer22.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {

                if (MusicActivity.serviceOpen){
                    mediaPlayer22.seekTo(musicActivity1.serviceMediaLength);
                    //Toast.makeText(getApplicationContext(), musicActivity1.serviceOpen + "", Toast.LENGTH_SHORT).show();
                    mediaPlayer22.start();
                } else {
                    mediaPlayer22.seekTo(musicActivity1.serviceMediaLength2);
                    //Toast.makeText(getApplicationContext(), musicActivity1.serviceOpen + "", Toast.LENGTH_SHORT).show();
                    mediaPlayer22.start();
                }




            }
        });
        mm= MediaPlayer.create(getApplicationContext(), loopMp3);
        mediaPlayer22.setNextMediaPlayer(mm);
        mediaPlayer22.setOnCompletionListener(onCompletionListener);

    }
    private final MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            mp.release();
            mediaPlayer22 = mm;
            //open = false;
            musicActivity1.serviceOpen = false;

            mm= MediaPlayer.create(getApplicationContext(), loopMp3);
            mediaPlayer22.setNextMediaPlayer(mm);
            mediaPlayer22.setOnCompletionListener(onCompletionListener);

        }
    };


    @Override
    public void onDestroy() {
        super.onDestroy();
        //seek = MusicActivity.mediaPlayer.getCurrentPosition();


        //mediaPlayer.stop();
        //musicActivity.mediaPlayerLoop(1,1,MusicBackgroundService.this);
        seek = mediaPlayer22.getCurrentPosition();
        servicePlaying = false;
        stopForeground(true);
        counter = false;
        //Toast.makeText(MusicBackgroundService.this, "Service Destroyed", Toast.LENGTH_SHORT).show();

        //MusicActivity.mediaPlayer.stop();

    }

    @Override
    public void onPrepared(MediaPlayer mp) {

    }
}
