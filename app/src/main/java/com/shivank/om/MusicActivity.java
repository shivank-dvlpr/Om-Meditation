package com.shivank.om;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaMetadata;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class MusicActivity extends AppCompatActivity implements View.OnClickListener {

    public static ImageView musicImg,musicGif, controlsBg, imgPlay, imgPause, imgPrevious, imgNext;
    int om, ganesh, shiv, hanuman;
    public static MediaPlayer mediaPlayer , mediaPlayer2;
    public int length, length2;
    boolean check = true;
    //GifDrawable gifDrawable;
    //GifImageView musicGif;
    StorageReference storageReference;
    String ss;
    File fileName;
    File localFile;
    public static boolean open;
    LoopMediaPlayer lp, lp2;
    MediaPlayer mm, mm2;
    SoundPool soundPool1, soundPool2;
    int loopMp3;
    static AppCompatButton btn10, btn15,btn30;
    static CountDownTimer countDownTimer;
    final String FORMAT = "%02d:%02d";
    static ImageView reset;
    static boolean countDownRunning;
    public static int btns;
    static boolean buttonPressed;
    static long timeLeft;
    static boolean countDownResume;
    ObjectAnimator colorAnim;
    int imgId;
    Context context22;
    static int serviceMediaLength,serviceMediaLength2, musicIdStart, musicIdLoop;
    public static Activity activity;
    Intent musicService;
    static int musicPlaying;
    public static boolean  serviceOpen;
    static Activity ac;
    Notification notification;
    MediaSessionCompat mediaSessionCompat;
    boolean couter = false;
    TextView txtMusicTitle;
    static String notificationTitle, notificationContent;
    Intent ii;
    int intentData, mp3;
    int musicPlayingIndex;
    boolean onCreateTrue, startA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_music);


        musicService = new Intent(MusicActivity.this, MusicBackgroundService.class);
        activity = this;
        musicImg = findViewById(R.id.musicImg);
        musicGif = findViewById(R.id.musicGif);
        imgPlay = findViewById(R.id.imgPlay);
        imgPause = findViewById(R.id.imgPause);
        imgPrevious = findViewById(R.id.imgPrevious);
        imgNext = findViewById(R.id.imgNext);
        btn10 = findViewById(R.id.btn10);
        btn15 = findViewById(R.id.btn15);
        btn30 =  findViewById(R.id.btn30);
        reset = findViewById(R.id.reset);
        txtMusicTitle = findViewById(R.id.txtMusicTitle);
        storageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://om-meditation-e46fe.appspot.com");

        imgPlay.setOnClickListener(this);
        imgPause.setOnClickListener(this);
        imgPrevious.setOnClickListener(this);
        imgNext.setOnClickListener(this);
        musicGif.setOnClickListener(this);
        btn10.setOnClickListener(this);
        btn15.setOnClickListener(this);
        btn30.setOnClickListener(this);
        reset.setOnClickListener(this);
        musicGif.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ac = this;

        open = true;
        buttonPressed = false;
        serviceOpen = true;



        ii = getIntent();
        Bundle b = ii.getExtras();
        if (b != null){
            intentData = ii.getExtras().getInt("notiImageValue");
            mp3 = ii.getExtras().getInt("mp3");
            Log.d("TAGG", intentData+"");
        }



        if (MusicBackgroundService.servicePlaying && mp3 == musicIdStart){
            MainActivity.imgValues = intentData;

        }
        playPauseBackgroundGif();
        openImagesAndGif(MusicActivity.this);

        mediaPlayer = new MediaPlayer();
        mediaPlayer2 = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer2.setAudioStreamType(AudioManager.STREAM_MUSIC);

        mediaSessionCompat =  new MediaSessionCompat(this, "TAG");
        mediaSessionCompat.setMetadata(
                new MediaMetadataCompat.Builder()
                        .putString(MediaMetadata.METADATA_KEY_TITLE, "OM")
                        .putString(MediaMetadata.METADATA_KEY_ARTIST, "OM")
                        .build());

        if (MusicBackgroundService.servicePlaying){
            countDownRunning = true;
        }else{
            countDownRunning = false;
        }


        //musicService = new Intent(MusicActivity.this, MusicBackgroundService.class);





    }

    public void openImagesAndGifNotification(Context context){

            if (MusicBackgroundService.servicePlaying && intentData == 0 && musicIdStart == R.raw.om_start) {
                musicImgAndBackground(context,R.raw.om_start,0,R.drawable.om, "OM Chant", "OM");

            } else if (MusicBackgroundService.servicePlaying &&  intentData == 1 && musicIdStart == R.raw.ganeshji_mp3_start) {
                musicImgAndBackground(context,R.raw.ganeshji_mp3_start,1,R.drawable.ganeshji, "Ganpateh Namah", "Ganpati Bappa");

            } else if (MainActivity.imgValues == 2) {

                musicImgAndBackground(context,R.raw.shivji_mp3_start,2,R.drawable.shivji, "Shiv Shambhu","Bhole Nath");


            } else if (MainActivity.imgValues == 3) {

                musicImgAndBackground(context,R.raw.hanumanji_mp3_start,3,R.drawable.hanumanji, "Shri Hanumateh Namah","Bajrang Bali");


            } else if (MainActivity.imgValues == 4) {

                musicImgAndBackground(context,R.raw.matarani_mp3_start,4,R.drawable.matarani, "Yaaa Devi","Mata Rani");


            } else if (MainActivity.imgValues == 5) {

                musicImgAndBackground(context,R.raw.krishnaji_mp3_start,5,R.drawable.krishnaji, "Hare Krishna","Kanhiya");

            } else if (MainActivity.imgValues == 6) {
                musicImgAndBackground(context,R.raw.vishnuji_mp3_start,6,R.drawable.vishnuji, "Om Namoh Narayanaya","Narayan");

            } else if (MainActivity.imgValues == 7) {
                musicImgAndBackground(context,R.raw.malala_mp3_start,7,R.drawable.malala, "Malala Flute","Malala");
            }


    }


    public void openImagesAndGif(Context context){

        if (MainActivity.imgValues == 0) {
            musicImgAndBackground(context,R.raw.om_start,0,R.drawable.om, "OM Chant", "OM");

        } else if (MainActivity.imgValues == 1) {
            musicImgAndBackground(context,R.raw.ganeshji_mp3_start,1,R.drawable.ganeshji, "Ganpateh Namah", "Ganpati Bappa");

        } else if (MainActivity.imgValues == 2) {

            musicImgAndBackground(context,R.raw.shivji_mp3_start,2,R.drawable.shivji, "Shiv Shambhu","Bhole Nath");


        } else if (MainActivity.imgValues == 3) {

            musicImgAndBackground(context,R.raw.hanumanji_mp3_start,3,R.drawable.hanumanji, "Shri Hanumateh Namah","Bajrang Bali");


        } else if (MainActivity.imgValues == 4) {

            musicImgAndBackground(context,R.raw.matarani_mp3_start,4,R.drawable.matarani, "Yaaa Devi","Mata Rani");


        } else if (MainActivity.imgValues == 5) {

            musicImgAndBackground(context,R.raw.krishnaji_mp3_start,5,R.drawable.krishnaji, "Hare Krishna","Kanhiya");

        } else if (MainActivity.imgValues == 6) {
            musicImgAndBackground(context,R.raw.vishnuji_mp3_start,6,R.drawable.vishnuji, "Om Namoh Narayanaya","Narayan");

        } else if (MainActivity.imgValues == 7) {
            musicImgAndBackground(context,R.raw.malala_mp3_start,7,R.drawable.malala, "Malala Flute","Malala");
        }

    }

    public void musicImgAndBackground(Context context,int musicIdSt, int musicPlayingIndex, int playingImgId, String musicTitle, String notiTitle){

        if(MusicBackgroundService.mediaPlayer22.isPlaying() && musicIdStart == musicIdSt){
            imgPlay.setVisibility(View.INVISIBLE);
            imgPause.setVisibility(View.VISIBLE);
        }

        musicPlaying = musicPlayingIndex;
        imgId = playingImgId;

        Glide.with(context)
                .load(playingImgId)
                .centerCrop()
                .error(R.drawable.ic_launcher_foreground)
                .into(musicImg);

        txtMusicTitle.setText(musicTitle);
        notificationContent = musicTitle;
        notificationTitle = notiTitle;
        this.musicPlayingIndex = musicPlayingIndex;

    }


    private void openingIntentImageAndGif(int getFrontImageResourceValue, int getBgImageResourceValue) {
        Glide.with(MusicActivity.this)
                .load(getFrontImageResourceValue)
                .centerCrop()
                .into(musicImg);

        Glide.with(this)
                .load(getBgImageResourceValue)
                .into(musicGif);
    }

    private void music (){

        if (MainActivity.imgValues == 0){

            musicIdStart = R.raw.om_start;
            musicIdLoop = R.raw.om_loop;
            mediaPlayerLoop(R.raw.om_start, R.raw.om_loop, MusicActivity.this);

        }else if (MainActivity.imgValues == 1){
            //loadMp3DownloadUrlFromFirebase("ganeshji/ganeshji_mp3.mp3");
            musicIdStart = R.raw.ganeshji_mp3_start;
            musicIdLoop = R.raw.ganeshji_mp3_loop;
            mediaPlayerLoop(R.raw.ganeshji_mp3_start,R.raw.ganeshji_mp3_loop, MusicActivity.this);

        }else if (MainActivity.imgValues == 2){

            musicIdStart = R.raw.shivji_mp3_start;
            musicIdLoop = R.raw.shivji_mp3_loop;
            mediaPlayerLoop(R.raw.shivji_mp3_start, R.raw.shivji_mp3_loop, MusicActivity.this);

        }else if (MainActivity.imgValues == 3){
            //loadMp3DownloadUrlFromFirebase("hanumanji/hanumanji_mp3.mp3");
            musicIdStart = R.raw.hanumanji_mp3_start;
            musicIdLoop = R.raw.hanumanji_mp3_loop;
            mediaPlayerLoop(R.raw.hanumanji_mp3_start, R.raw.hanumanji_mp3_loop, MusicActivity.this);

        }else if (MainActivity.imgValues == 4){

            musicIdStart = R.raw.matarani_mp3_start;
            musicIdLoop = R.raw.matarani_mp3_loop;
            mediaPlayerLoop(R.raw.matarani_mp3_start, R.raw.matarani_mp3_loop, MusicActivity.this);


            //loadMp3DownloadUrlFromFirebase("matarani/matarani_mp3.mp3");

        }else if (MainActivity.imgValues == 5){
            //loadMp3DownloadUrlFromFirebase("krishnaji/krishnaji_mp3.mp3");
            musicIdStart = R.raw.krishnaji_mp3_start;
            musicIdLoop = R.raw.krishna_mp3_loop;
            mediaPlayerLoop(R.raw.krishnaji_mp3_start, R.raw.krishna_mp3_loop, MusicActivity.this);

            //mediaPlayer = MediaPlayer.create(MusicActivity.this, R.raw.krishnaji_mp3);
        }else if (MainActivity.imgValues == 6){
            //loadMp3DownloadUrlFromFirebase("krishnaji/krishnaji_mp3.mp3");
            musicIdStart = R.raw.vishnuji_mp3_start;
            musicIdLoop = R.raw.vishnuji_mp3_loop;
            mediaPlayerLoop(R.raw.vishnuji_mp3_start, R.raw.vishnuji_mp3_loop, MusicActivity.this);

            //mediaPlayer = MediaPlayer.create(MusicActivity.this, R.raw.krishnaji_mp3);
        }else if (MainActivity.imgValues == 7){
            //loadMp3DownloadUrlFromFirebase("krishnaji/krishnaji_mp3.mp3");
            musicIdStart = R.raw.malala_mp3_start;
            musicIdLoop = R.raw.malala_mp3_loop;
            mediaPlayerLoop(R.raw.malala_mp3_start, R.raw.malala_mp3_loop, MusicActivity.this);

            //mediaPlayer = MediaPlayer.create(MusicActivity.this, R.raw.krishnaji_mp3);
        }


    }

    public void mediaPlayerLoop(int startMp3 , int loopMp3, Context context){


        this.loopMp3 = loopMp3;
        this.context22 = context;



        if (open){
            mediaPlayer = MediaPlayer.create(context, startMp3);
        }else {
            mediaPlayer = MediaPlayer.create(context, loopMp3);
        }


        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {



                if (!mediaPlayer.isPlaying() && !MusicBackgroundService.servicePlaying && !MusicBackgroundService.counter){
                    mediaPlayer.seekTo(MusicBackgroundService.seek);
                    MusicBackgroundService.counter = true;
                    mediaPlayer.start();
                }else if (MusicBackgroundService.counter) {
                    if (open){
                        mediaPlayer.seekTo(length);
                        mediaPlayer.start();
                    }else {
                        mediaPlayer.seekTo(length2);
                        mediaPlayer.start();
                    }

                }





            }
        });
        mm= MediaPlayer.create(context, loopMp3);
        mediaPlayer.setNextMediaPlayer(mm);
        mediaPlayer.setOnCompletionListener(onCompletionListener);

    }
    private final MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            mp.release();
            mediaPlayer = mm;
            open = false;

            mm= MediaPlayer.create(context22, loopMp3);
            mediaPlayer.setNextMediaPlayer(mm);
            mediaPlayer.setOnCompletionListener(onCompletionListener);

        }
    };

    private void loadMp3DownloadUrlFromFirebase(String bucketLocationMp3){

        storageReference.child(bucketLocationMp3).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                try {
                    mediaPlayer.setDataSource(uri.toString());
                    mediaPlayer.prepareAsync();
                } catch (IOException e) {
                    e.printStackTrace();
                }catch (IllegalStateException ex){
                    ex.toString();
                }

            }
        });

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });

    }

    private void loadGif_ImgDownloadUrlFromFirebase(String bucketLocationGif, String bucketLocationImg){

        storageReference.child(bucketLocationGif).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uriGif) {

                Glide.with(MusicActivity.this)
                        .load(uriGif)
                        .into(musicGif);

            }
        });

        storageReference.child(bucketLocationImg).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uriImg) {

                Glide.with(MusicActivity.this)
                        .load(uriImg)
                        .centerCrop()
                        .into(musicImg);

            }
        });

    }


    private void playPauseBackgroundGif(){
        musicGif.setOnTouchListener(new View.OnTouchListener() {
            private final GestureDetector gestureDetector = new GestureDetector(MusicActivity.this, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    Drawable drawable = musicGif.getDrawable();
                    ((Animatable) drawable).start();
                    return super.onDoubleTap(e);
                }

                @Override
                public boolean onSingleTapConfirmed(MotionEvent event) {

                    Drawable drawable = musicGif.getDrawable();

                    if (drawable instanceof Animatable) {
                        ((Animatable) drawable).stop();
                    }
                    return false;
                }
            });



            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return false;
            }
        });
    }


    //TODO: Add Animation
    private void animateMinButtonText(AppCompatButton btnID){

        colorAnim = ObjectAnimator.ofInt(btnID, "textColor",getResources().getColor(android.R.color.holo_green_light), Color.TRANSPARENT);
        colorAnim.setDuration(1000);
        colorAnim.setEvaluator(new ArgbEvaluator());
        colorAnim.setRepeatCount(ValueAnimator.INFINITE);
        colorAnim.setRepeatMode(ValueAnimator.REVERSE);
        colorAnim.start();

    }


    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.imgPlay:

                MusicBackgroundService mm11 = new MusicBackgroundService();

                if (!mediaPlayer.isPlaying()){
                    //mediaPlayer.start();
                    //mediaPlayer.seekTo(MusicBackgroundService.seek);
                    //mediaPlayerLoop(musicIdStart,musicIdLoop,getApplicationContext());
                }

                if (MusicBackgroundService.servicePlaying){
                    MusicBackgroundService.mediaPlayer22.stop();
                    stopService(new Intent(this, MusicBackgroundService.class));
                    music();
                }else {
                    music();
                }


                if (btns == 10 && buttonPressed){
                    buttonPressed = false;
                    buttonTimer(btn10, 600000, "10MIN");
                }
                else if (btns == 15 && buttonPressed){
                    buttonPressed = false;
                    buttonTimer(btn15, 900000, "15MIN");
                }
                else if (btns == 30 && buttonPressed){
                    buttonPressed = false;
                    buttonTimer(btn30,1800000, "30MIN");
                }

                if (!mediaPlayer.isPlaying()){
                    if (countDownResume && btns == 10){
                        buttonTimer(btn10, timeLeft, "10MIN");
                    }else if (countDownResume && btns == 15){
                        buttonTimer(btn15, timeLeft, "15MIN");
                    }else if (countDownResume && btns == 30){
                        buttonTimer(btn30, timeLeft, "30MIN");
                    }
                }

                imgPause.setVisibility(View.VISIBLE);
                imgPlay.setVisibility(View.GONE);
                break;

            case R.id.imgPause:
                try {


                    Intent playIntent = new Intent("com.shivank.om.PLAY_MUSIC_ACTION");
                    PendingIntent playPending = PendingIntent.getBroadcast(MusicActivity.this, 3, playIntent, 0);
                    MusicBackgroundService mg = new MusicBackgroundService();
                    NotificationManager nm = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

                    if (MusicBackgroundService.mediaPlayer22.isPlaying() && serviceOpen){
                        MusicBackgroundService.mediaPlayer22.pause();
                        stopService(new Intent(this, MusicBackgroundService.class));
                        MusicBackgroundService.seek = MusicBackgroundService.mediaPlayer22.getCurrentPosition();
                        //mediaPlayer.start();
                        //mediaPlayerLoop(musicIdStart, musicIdLoop, MusicActivity.this);
                        //mediaPlayer.seekTo(MusicBackgroundService.seek);
                    }else if (MusicBackgroundService.mediaPlayer22.isPlaying() && !serviceOpen){
                        MusicBackgroundService.mediaPlayer22.pause();
                        stopService(new Intent(this, MusicBackgroundService.class));
                        MusicBackgroundService.seek = MusicBackgroundService.mediaPlayer22.getCurrentPosition();
                        //mediaPlayer.start();
                        //mediaPlayerLoop(musicIdStart, musicIdLoop, MusicActivity.this);
                        //mediaPlayer.seekTo(MusicBackgroundService.seek);
                    }



                    if (mediaPlayer.isPlaying() && open){
                        length = mediaPlayer.getCurrentPosition();
                        mediaPlayer.seekTo(length);
                        mediaPlayer.pause();

                    }
                    else if (mediaPlayer.isPlaying() && !open){
                        length2 = mediaPlayer.getCurrentPosition();
                        mediaPlayer.seekTo(length2);
                        mediaPlayer.pause();
                        MusicBackgroundService.mediaPlayer22.pause();
                    }

                    if (countDownRunning){
                        countDownResume = true;
                        if (btns == 10){
                            btn10.setText(btn10.getText().toString());
                            //animateMinButtonText(btn10);

                            /*if (btn10.getVisibility() == View.VISIBLE){
                                btn10.setText("PAUSED");
                            }*/
                        }else if (btns == 15){
                            btn15.setText(btn15.getText().toString());
                            //animateMinButtonText(btn15);
                        }else if (btns == 30){
                            btn30.setText(btn30.getText().toString());
                           // animateMinButtonText(btn30);
                        }

                        countDownTimer.cancel();
                        //buttonPressed = true;
                    }

                   /* if (countDownRunning){
                        reset.callOnClick();
                    }*/

                    //countDownTimer.cancel();
                  /*  btn10.setText("10MIN");
                    btn10.setEnabled(true);
                    btn15.setText("15MIN");
                    btn15.setEnabled(true);
                    btn30.setText("30MIN");
                    btn30.setEnabled(true);*/

                } catch (IllegalStateException e) {
                    Log.i("Play imgPause", e.toString());
                }
                imgPlay.setVisibility(View.VISIBLE);
                imgPause.setVisibility(View.GONE);
                break;

                //TODO: Make Next and Previous Workable
            case R.id.imgPrevious:
                //Toast.makeText(this, "in Progress", Toast.LENGTH_SHORT).show();
                break;

            case R.id.imgNext:
                //Toast.makeText(this, "in Progress1", Toast.LENGTH_SHORT).show();
                break;


            case R.id.btn10:
                btns = 10;
                //length = 0;
                buttonPressed = true;
                btn10.setTextColor(getResources().getColor(android.R.color.holo_green_light));

                disableMinButtons(btn15);
                disableMinButtons(btn30);

          /*      btn15.setEnabled(false);
                btn15.setBackground(getResources().getDrawable(R.drawable.min_bg_disable));
                btn15.setTextColor(getResources().getColor(android.R.color.darker_gray));
                btn30.setEnabled(false);
                btn30.setBackground(getResources().getDrawable(R.drawable.min_bg_disable));
                btn30.setTextColor(getResources().getColor(android.R.color.darker_gray));*/

                break;

            case R.id.btn15:
                btns = 15;
                buttonPressed = true;
                btn15.setTextColor(getResources().getColor(android.R.color.holo_green_light));
                disableMinButtons(btn10);
                disableMinButtons(btn30);

              /*  buttonTimer(btn15, 900000, "15MIN");
                btn10.setEnabled(false);
                btn30.setEnabled(false);*/
                break;

            case R.id.btn30:
                btns = 30;
                buttonPressed = true;
                btn30.setTextColor(getResources().getColor(android.R.color.holo_green_light));
                disableMinButtons(btn10);
                disableMinButtons(btn15);


               /* buttonTimer(btn30, 1800000, "30MIN");
                btn10.setEnabled(false);
                btn15.setEnabled(false);*/
                break;

            case R.id.reset:
                resetTextAndColors();
                btns = 0;
                countDownResume = false;
                //colorAnim.pause();
                if (countDownRunning){
                    countDownTimer.cancel();
                    resetTextAndColors();
                }

                length = 0;
                mediaPlayer.pause();
                mediaPlayer.seekTo(length);

                if (MusicBackgroundService.servicePlaying){
                    MusicBackgroundService.mediaPlayer22.pause();
                    stopService(new Intent(MusicActivity.this,MusicBackgroundService.class));
                }


                open = true;

                imgPlay.setVisibility(View.VISIBLE);
                imgPause.setVisibility(View.GONE);

                break;

        }

    }

    private void disableMinButtons(AppCompatButton disableButtonID){

        disableButtonID.setEnabled(false);
        disableButtonID.setBackground(getResources().getDrawable(R.drawable.min_bg_disable));
        disableButtonID.setTextColor(getResources().getColor(android.R.color.darker_gray));

    }

    private void resetTextAndColors(){

        btn10.setEnabled(true);
        btn15.setEnabled(true);
        btn30.setEnabled(true);

        btn10.setText("10MIN");
        btn15.setText("15MIN");
        btn30.setText("30MIN");

        btn10.setTextColor(getResources().getColor(android.R.color.white));
        btn15.setTextColor(getResources().getColor(android.R.color.white));
        btn30.setTextColor(getResources().getColor(android.R.color.white));

        btn10.setBackgroundDrawable(getResources().getDrawable(R.drawable.min_bg));
        btn15.setBackgroundDrawable(getResources().getDrawable(R.drawable.min_bg));
        btn30.setBackgroundDrawable(getResources().getDrawable(R.drawable.min_bg));



    }

    public void buttonTimer(AppCompatButton btnID, long timeInMilli, String textInMin){

        /*if (!mediaPlayer.isPlaying()){
            imgPlay.callOnClick();
        }*/

        countDownTimer =  new CountDownTimer(timeInMilli, 1000){


            @Override
            public void onTick(long millisUntilFinished) {
                countDownRunning = true;
                timeLeft = millisUntilFinished;
                btnID.setText(""+String.format(FORMAT,
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                //btn10.setText(millisUntilFinished / 60000 + ":" + 60000 / 1000);

            }

            @Override
            public void onFinish() {
                reset.callOnClick();

              /*  btnID.setText(textInMin);
                mediaPlayer.stop();
                imgPause.setVisibility(View.GONE);
                imgPlay.setVisibility(View.VISIBLE);*/
                //btnID.setClickable(true);

            }
        }.start();


        Handler handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                mediaPlayer.stop();
            }
        };

        class SleepTask extends TimerTask {
            @Override
            public void run(){
                handler.sendEmptyMessage(0);
            }
        }

        Timer timer = new Timer("timer",true);
        timer.schedule(new SleepTask(),timeInMilli);

        //btn10.setClickable(false);
        btnID.setEnabled(false);


    }

    @Override
    protected void onPause() {
        super.onPause();
      /*  imgPlay.setVisibility(View.VISIBLE);
        imgPause.setVisibility(View.GONE);*/


                musicService.putExtra("imgValue", imgId);
                musicService.putExtra("startMp3", musicIdStart);
                musicService.putExtra("loopMp3", musicIdLoop);
                musicService.putExtra("pp",musicPlaying);
                musicService.putExtra("notificationTitle", notificationTitle);
                musicService.putExtra("notificationContent", notificationContent);
                musicService.putExtra("notImage", MainActivity.imgValues);
                //startService(intent);
        if (mediaPlayer.isPlaying()){
            if (open){
                //Toast.makeText(MusicActivity.this, open + " OnPause", Toast.LENGTH_SHORT).show();
                serviceOpen = true;
                serviceMediaLength = mediaPlayer.getCurrentPosition();
            }else {
                //Toast.makeText(MusicActivity.this, open + " OnPause", Toast.LENGTH_SHORT).show();
                serviceOpen = false;
                serviceMediaLength2 = mediaPlayer.getCurrentPosition();
            }
                //serviceMediaLength = mediaPlayer.getCurrentPosition();
                ContextCompat.startForegroundService(MusicActivity.this, musicService);
                //Toast.makeText(MusicActivity.this, "onStop", Toast.LENGTH_SHORT).show();
            }


        try {
            /*if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            }*/
            /*if (mediaPlayer2.isPlaying()){
                mediaPlayer2.stop();
            }*/
        } catch (IllegalStateException e) {
            Log.i("Play onPause", e.toString());
        }catch (NullPointerException ex){
            Log.i("OnPause", ex.toString());
        }



    }


    @Override
    protected void onStop() {
        super.onStop();
        onCreateTrue = false;
        /*if (mediaPlayer.isPlaying()){
            musicService = new Intent(MusicActivity.this, MusicBackgroundService.class);
            musicService.putExtra("imgValue", imgId);
            musicService.putExtra("startMp3", musicIdStart);
            musicService.putExtra("loopMp3", musicIdLoop);
        //startService(intent);
        //serviceMediaLength = mediaPlayer.getCurrentPosition();
        ContextCompat.startForegroundService(MusicActivity.this, musicService);
        Toast.makeText(MusicActivity.this, "onStop", Toast.LENGTH_SHORT).show();
        }*/


        //startService(new Intent(MusicActivity.this, MusicBackgroundService.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //stopService(new Intent(MusicActivity.this, MusicBackgroundService.class));

    }

    @Override
    protected void onResume() {
        super.onResume();


        if (MusicBackgroundService.servicePlaying){
            if (countDownRunning && btns == 10){
                buttonTimer(btn10, timeLeft, "10MIN");
                disableMinButtons(btn15);
                disableMinButtons(btn30);
            }else if (countDownRunning && btns == 15){
                buttonTimer(btn15, timeLeft, "15MIN");
                disableMinButtons(btn10);
                disableMinButtons(btn30);
            }else if (countDownRunning && btns == 30){
                buttonTimer(btn30, timeLeft, "30MIN");
                disableMinButtons(btn10);
                disableMinButtons(btn15);
            }
        }else {
            if (countDownRunning){
                countDownTimer.cancel();
            }

        }
/*
        if (startA){
            //finish();
            startActivity(new Intent(MusicActivity.this, MusicActivity.class));
            Toast.makeText(MusicActivity.this, "Resume", Toast.LENGTH_SHORT).show();
        }*/
        //openImagesAndGif(MusicActivity.this);
        //openImagesAndGifNotification(MusicActivity.this);
       // stopService(new Intent(MusicActivity.this, MusicBackgroundService.class));


    }
}