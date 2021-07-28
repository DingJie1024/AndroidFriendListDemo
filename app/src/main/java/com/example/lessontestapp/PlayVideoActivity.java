package com.example.lessontestapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.*;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.lessontestapp.service.PlayAudioService;
import com.example.lessontestapp.service.PlayVideoService;
import com.example.lessontestapp.utils.IsServiceRunning;
import com.example.lessontestapp.utils.MyTimeToFormat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class PlayVideoActivity extends AppCompatActivity {

    private Toolbar mPlayVideoToolBar;
    private static TextView mPlayVideoTitleText,
            mPlayVideoCurrentTimeHourText, mPlayVideoCurrentTimeMinutesText,mPlayVideoCurrentTimeSecondText,
            mPlayVideoTotalTimeHourText,mPlayVideoTotalTimeMinutesText,mPlayVideoTotalTimeSecondText;
    private static SeekBar mPlayVideoSeekBar;
    private Button mPlayVideoPreviousBtn;
    private static Button mPlayVideoPlayBtn;
    private Button mPlayVideoStopBtn;
    private Button mPlayVideoNextBtn;

    public static VideoView mPlayVideoView;

    public static SurfaceView mPlayVideoSurfaceView;

    private ArrayList<String> mVideoNameList;
//    private ArrayList<String> mVideoSrcList;
    private ArrayList<Integer> mVideoSrcList;
//    private Map<String,String> mVideoNameAndSrcMap;
    private Map<String,Integer> mVideoNameAndSrcMap;

    private int currentVideoNum = -1;
//    private String currentVideoSrc;
    private int currentVideoSrc;

    private boolean isFirst;
    private boolean isLast;
    private boolean isStop = false;

    private static String mPlayOrPauseStr = "播放";

    public static MediaPlayer mMediaPlayer;

    private Intent mPlayVideoIntent;

    private static MyTimeToFormat myTimeToFormat;

    public final static Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            mPlayOrPauseStr = msg.obj.toString();
            System.out.println("playVideoActivity::" + mPlayOrPauseStr);
            mPlayVideoPlayBtn.setText(mPlayOrPauseStr);
        }
    };
    static int currentProgress = 0;
    static int durationTime = 0;
    public final static Handler handler1 = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            if (msg.obj != null){
                currentProgress = (int) msg.obj;
                mPlayVideoSeekBar.setProgress(currentProgress);
            }
        }
    };
    public static void updateUI(String mPlayOrPauseStr) {
        Message message = Message.obtain();
        message.obj = mPlayOrPauseStr;
        handler.sendMessage(message);
    }

    public static void updateProgress(int currentPosition,int duration) {
        System.out.println("duration::"+duration);
        durationTime = duration;
        mPlayVideoSeekBar.setMax(durationTime);

        Message message = Message.obtain();
        message.obj = currentPosition;
        handler1.sendMessage(message);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);

        //初始化组件
        initWidget();

        mPlayVideoView.getForeground().setAlpha(0xff);

        //获取MyVideoActivity传递的数据
        Intent intent = getIntent();
        mPlayVideoTitleText.setText(intent.getStringExtra("video_name"));
        mVideoNameList = intent.getStringArrayListExtra("video_name_list");
//        mVideoSrcList = intent.getStringArrayListExtra("video_src_list");
        mVideoSrcList = intent.getIntegerArrayListExtra("video_src_list");

        mVideoNameAndSrcMap = new TreeMap<>();
        for (int i = 0; i < mVideoNameList.size(); i++){
            System.out.println(mVideoNameList.get(i) + " : " + mVideoSrcList.get(i));
            mVideoNameAndSrcMap.put(mVideoNameList.get(i),mVideoSrcList.get(i));
        }

        //导航按钮图片点击事件
        mPlayVideoToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        for (int i = 0; i < mVideoNameList.size(); i++) {
            if (mPlayVideoTitleText.getText().toString().equals(mVideoNameList.get(i))){
                currentVideoSrc = mVideoNameAndSrcMap.get(mVideoNameList.get(i));
            }
        }

        mPlayVideoSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser){
                    currentProgress = progress;
                    mPlayVideoView.seekTo(currentProgress);
                    if (currentProgress == durationTime){
                        if (PlayVideoService.updateThread != null){
                            PlayVideoService.updateThread.interrupt();
                        }
                        if (IsServiceRunning.isServiceRunning(PlayVideoActivity.this, "com.example.lessontestapp.service.PlayVideoService")) {
                            stopService(mPlayVideoIntent);
                        }
                    }
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void initWidget(){
        mPlayVideoToolBar = findViewById(R.id.id_play_video_toolBar);
        mPlayVideoTitleText = findViewById(R.id.id_play_video_title_txt);
        mPlayVideoCurrentTimeHourText = findViewById(R.id.id_play_video_current_time_hour_txt);
        mPlayVideoCurrentTimeMinutesText = findViewById(R.id.id_play_video_current_time_minutes_txt);
        mPlayVideoCurrentTimeSecondText = findViewById(R.id.id_play_video_current_time_second_txt);
        mPlayVideoTotalTimeHourText = findViewById(R.id.id_play_video_total_time_hour_txt);
        mPlayVideoTotalTimeMinutesText = findViewById(R.id.id_play_video_total_time_minutes_txt);
        mPlayVideoTotalTimeSecondText = findViewById(R.id.id_play_video_total_time_second_txt);
        mPlayVideoSeekBar = findViewById(R.id.id_play_video_seekBar);
        mPlayVideoPreviousBtn = findViewById(R.id.id_play_video_previous_btn);
        mPlayVideoPlayBtn = findViewById(R.id.id_play_video_play_btn);
        mPlayVideoStopBtn = findViewById(R.id.id_play_video_stop_btn);
        mPlayVideoNextBtn = findViewById(R.id.id_play_video_next_btn);
//        mPlayVideoSurfaceView = findViewById(R.id.id_play_video_surface);
        mPlayVideoView = findViewById(R.id.id_play_video_videoView);
    }


    //前一个视频
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onVideoPreviousClick(View view){
        if (PlayVideoService.updateThread != null){
            PlayVideoService.updateThread.interrupt();
        }
        if (IsServiceRunning.isServiceRunning(this, "com.example.lessontestapp.service.PlayVideoService")) {
            stopService(mPlayVideoIntent);
        }
        mPlayVideoView.getForeground().setAlpha(0xff);
//        mMediaPlayer.release();
//        mPlayVideoSurfaceView.getForeground().setAlpha(0xff);
        for (int i = 0; i < mVideoNameList.size(); i++) {
            if (mPlayVideoTitleText.getText().toString().equals(mVideoNameList.get(0))){
                isFirst = true;
            }else {
                isFirst = false;
            }
        }
        if (!isFirst){
            mPlayOrPauseStr = "播放";
            mPlayVideoPlayBtn.setText("播放");
            for (int i = 0; i < mVideoNameList.size(); i++) {
                if (mPlayVideoTitleText.getText().toString().equals(mVideoNameList.get(i))){
                    currentVideoNum = i;
                }
            }
            currentVideoNum = currentVideoNum - 1;
            mPlayVideoTitleText.setText(mVideoNameList.get(currentVideoNum));
            currentVideoSrc = mVideoNameAndSrcMap.get(mVideoNameList.get(currentVideoNum));
        }else {
            Toast.makeText(this,"已经是第一首",Toast.LENGTH_SHORT).show();
            currentVideoNum = 0;
//            flag = 0;
        }
    }

    //下一个视频
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onVideoNextClick(View view){
//        mMediaPlayer.release();
        if (PlayVideoService.updateThread != null){
            PlayVideoService.updateThread.interrupt();
        }
        if (IsServiceRunning.isServiceRunning(this, "com.example.lessontestapp.service.PlayVideoService")) {
            stopService(mPlayVideoIntent);
        }
        mPlayVideoView.getForeground().setAlpha(0xff);
        for (int i = 0; i < mVideoNameList.size(); i++) {
            if (mPlayVideoTitleText.getText().toString().equals(mVideoNameList.get(mVideoNameList.size()-1))){
                isLast = true;
            }else {
                isLast = false;
            }
        }
        if (!isLast){
            mPlayOrPauseStr = "播放";
            mPlayVideoPlayBtn.setText("播放");
            for (int i = 0; i < mVideoNameList.size(); i++) {
                if (mPlayVideoTitleText.getText().toString().equals(mVideoNameList.get(i))){
                    currentVideoNum = i;
                }
            }
            currentVideoNum = currentVideoNum + 1;
            mPlayVideoTitleText.setText(mVideoNameList.get(currentVideoNum));
            currentVideoSrc = mVideoNameAndSrcMap.get(mVideoNameList.get(currentVideoNum));
        }else {
            Toast.makeText(this,"已经最后一首",Toast.LENGTH_SHORT).show();
            currentVideoNum = mVideoNameList.size()-1;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onVideoPlayClick(View view){

        mPlayVideoView.getForeground().setAlpha(0);
        mPlayVideoIntent = new Intent(this, PlayVideoService.class);
        mPlayVideoIntent.putExtra("video_src_toService", currentVideoSrc);
        mPlayVideoIntent.putExtra("is_play_pause_toService", mPlayOrPauseStr);

        startService(mPlayVideoIntent);

    }

    public void onVideoStopClick(View view){
        mPlayVideoPlayBtn.setText("播放");
        mPlayOrPauseStr = "播放";
        isStop = true;
        PlayVideoService.updateThread.interrupt();
//        PlayVideoActivity.mPlayVideoView.stopPlayback();
        if (IsServiceRunning.isServiceRunning(this, "com.example.lessontestapp.service.PlayVideoService")) {
            stopService(mPlayVideoIntent);
        }
//        PlayVideoActivity.mPlayVideoView.stopPlayback();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (PlayVideoService.updateThread != null){
            PlayVideoService.updateThread.interrupt();
        }
        if (IsServiceRunning.isServiceRunning(this, "com.example.lessontestapp.service.PlayVideoService")) {
            stopService(mPlayVideoIntent);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (PlayVideoService.updateThread != null){
            PlayVideoService.updateThread.interrupt();
        }
        if (IsServiceRunning.isServiceRunning(this, "com.example.lessontestapp.service.PlayVideoService")) {
            stopService(mPlayVideoIntent);
        }
    }
}