package com.example.lessontestapp.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.VideoView;
import androidx.annotation.NonNull;
import com.example.lessontestapp.PlayAudioActivity;
import com.example.lessontestapp.PlayVideoActivity;
import com.example.lessontestapp.R;

import java.io.IOException;

public class PlayVideoService extends Service {

    public String mPlayOrPauseStr;

    private Thread workThread;
    public static Thread updateThread;

//    private String mVideoSrc;
    private int mVideoSrc;

    private boolean isServiceOpen;

    private String testStr = "我不是";

//    private SurfaceView mPlayVideoSurfaceView;
    private MediaPlayer mPlayVideoMediaPlayer;

    public PlayVideoService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        updateThread = new Thread(null,updateBack,"update");
        isServiceOpen = true;
//        PlayVideoActivity.mMediaPlayer = new MediaPlayer();
//        final SurfaceHolder surfaceHolder = PlayVideoActivity.mPlayVideoSurfaceView.getHolder();
//        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
//            @Override
//            public void surfaceCreated(@NonNull SurfaceHolder holder) {
//                PlayVideoActivity.mMediaPlayer.setDisplay(surfaceHolder);
//            }
//
//            @Override
//            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
//
//            }
//
//            @Override
//            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
//
//            }
//        });
//        mPlayVideoMediaPlayer = new MediaPlayer();
//        mPlayVideoVideoView;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        workThread = new Thread(null,workBack,"work");
        mPlayOrPauseStr = intent.getStringExtra("is_play_pause_toService");
        if (isServiceOpen){
            //                PlayVideoActivity.mMediaPlayer = new MediaPlayer();
//            mVideoSrc = intent.getStringExtra("video_src_toService");
            mVideoSrc = intent.getIntExtra("video_src_toService",12);
            System.out.println("mVideoSrc::"+mVideoSrc);
//            PlayVideoActivity.mPlayVideoView.setVideoPath(mVideoSrc);
            PlayVideoActivity.mPlayVideoView.setVideoURI(Uri.parse("android.resource://" +getPackageName() + "/"+ mVideoSrc));
        }
        System.out.println("isServiceOpen:"+isServiceOpen);
        System.out.println("from activity::"+mPlayOrPauseStr);

        if (mPlayOrPauseStr.equals("播放")){
            PlayVideoActivity.mPlayVideoView.start();
        }else {
            PlayVideoActivity.mPlayVideoView.pause();
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
//        PlayVideoActivity.mPlayVideoView.stopPlayback();
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
        testStr = "我是你爸爸";
        PlayVideoActivity.mPlayVideoView.stopPlayback();
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
                    PlayVideoActivity.updateUI(mPlayOrPauseStr);
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
                if (PlayVideoActivity.mPlayVideoView.isPlaying()){
                    System.out.println(testStr);
                    System.out.println("PlayVideoActivity.mPlayVideoView.CurrentPosition():"+PlayVideoActivity.mPlayVideoView.getCurrentPosition());
                    System.out.println("PlayVideoActivity.mPlayVideoView.Duration():"+PlayVideoActivity.mPlayVideoView.getDuration());
                    if (PlayVideoActivity.mPlayVideoView.getCurrentPosition()>=0 && PlayVideoActivity.mPlayVideoView.getDuration()>0){
                        PlayVideoActivity.updateProgress(PlayVideoActivity.mPlayVideoView.getCurrentPosition(),PlayVideoActivity.mPlayVideoView.getDuration());
                    }else {
                        return;
                    }
                }
                if (PlayVideoActivity.mPlayVideoView.getCurrentPosition() == PlayVideoActivity.mPlayVideoView.getDuration()){
                    updateThread.interrupt();
                }
            }
        }
    };
}