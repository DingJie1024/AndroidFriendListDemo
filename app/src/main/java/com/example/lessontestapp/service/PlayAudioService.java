package com.example.lessontestapp.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import com.example.lessontestapp.PlayAudioActivity;

import java.io.IOException;

public class PlayAudioService extends Service {

    public static MediaPlayer mediaPlayer;

    public String mPlayOrPauseStr;

    public static Thread workThread;
    public static Thread updateThread;

//    private String mAudioSrc;
    private int mAudioSrc;

    private boolean isServiceOpen;

    public PlayAudioService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return new MyBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        updateThread = new Thread(null,updateBack,"update");
        isServiceOpen = true;
        mediaPlayer = new MediaPlayer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        workThread = new Thread(null,workBack,"work");
        mPlayOrPauseStr = intent.getStringExtra("is_play_pause_toService");
        if (isServiceOpen){
            try {
//                mAudioSrc = intent.getStringExtra("music_src_toService");
                mAudioSrc = intent.getIntExtra("music_src_toService",12);
                System.out.println("mAudioSrc::"+mAudioSrc);
//                mediaPlayer.setDataSource(mAudioSrc);
                mediaPlayer = MediaPlayer.create(this,mAudioSrc);
//                mediaPlayer.prepare();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("isServiceOpen:"+isServiceOpen);
        System.out.println("from activity::"+mPlayOrPauseStr);

        if (mPlayOrPauseStr.equals("播放")){
            mediaPlayer.start();
            System.out.println(mediaPlayer.isPlaying());
        }else {
            mediaPlayer.pause();
            System.out.println(mediaPlayer.isPlaying());
        }
        if (!workThread.isAlive()){
            workThread.start();
        }
        if (!updateThread.isAlive()){
            updateThread.start();
        }
        isServiceOpen = false;
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        isServiceOpen = false;
        mediaPlayer.stop();
//        try {
//            mediaPlayer.prepare();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        System.out.println("线程结束");
        if (workThread.isAlive()){
            workThread.interrupt();
        }
        if (updateThread.isAlive()){
            updateThread.interrupt();
        }
    }

    private Runnable workBack = new Runnable() {
        @Override
        public void run() {
            while (!Thread.interrupted()){
                try {
                    if (mPlayOrPauseStr.equals("播放")){
                        System.out.println("暂停");
                        mPlayOrPauseStr = "暂停";
                        workThread.interrupt();
                    }else {
                        System.out.println("播放");
                        mPlayOrPauseStr = "播放";

                        workThread.interrupt();
                    }
//                    workThread.interrupt();
                    PlayAudioActivity.updateUI(mPlayOrPauseStr);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private Runnable updateBack = new Runnable() {
        @Override
        public void run() {
            while (!Thread.interrupted()){
                System.out.println(mediaPlayer.getCurrentPosition());
                PlayAudioActivity.updateProgress(mediaPlayer.getCurrentPosition(),mediaPlayer.getDuration());
                if (mediaPlayer.getCurrentPosition() == mediaPlayer.getDuration()){
                    updateThread.interrupt();
                }
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            }
        }
    };



    //该方法包含关于歌曲的操作
    public class MyBinder extends Binder {

        //返回歌曲的长度，单位为毫秒
        public int getDuration(){
            return mediaPlayer.getDuration();
        }

        //返回歌曲目前的进度，单位为毫秒
        public int getCurrentPosition(){
            return mediaPlayer.getCurrentPosition();
        }

        //设置歌曲播放的进度，单位为毫秒
        public void seekTo(int mesc){
            mediaPlayer.seekTo(mesc);
        }
    }

}