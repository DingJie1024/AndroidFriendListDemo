package com.example.lessontestapp;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.*;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.lessontestapp.service.PlayAudioService;
import com.example.lessontestapp.service.PlayVideoService;
import com.example.lessontestapp.utils.IsServiceRunning;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PlayAudioActivity extends AppCompatActivity{

    private Toolbar mPlayAudioToolBar;
    private TextView mPlayAudioTitleText,
                     mPlayAudioCurrentTimeHourText, mPlayAudioCurrentTimeMinutesText,mPlayAudioCurrentTimeSecondText,
                     mPlayAudioTotalTimeHourText,mPlayAudioTotalTimeMinutesText,mPlayAudioTotalTimeSecondText;
    private static SeekBar mPlayAudioSeekBar;
    private static Button mPlayAudioPreviousBtn, mPlayAudioPlayBtn, mPlayAudioStopBtn, mPlayAudioNextBtn;


    private ArrayList<String> mMusicNameList;
//    private ArrayList<String> mMusicSrcList;
    private ArrayList<Integer> mMusicSrcList;
//    private Map<String, String> mMusicNameAndSrcMap;
    private Map<String, Integer> mMusicNameAndSrcMap;

    private MediaPlayer mMediaPlayer;

    private int currentMusicNum = -1;
//    private String currentMusicSrc;
    private int currentMusicSrc;

    private boolean isFirst;
    private boolean isLast;

    private static String mPlayOrPauseStr = "播放";

    private Intent mPlayAudioIntent;



    public final static Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            mPlayOrPauseStr = msg.obj.toString();
            System.out.println("playAudioActivity::" + mPlayOrPauseStr);
            mPlayAudioPlayBtn.setText(mPlayOrPauseStr);
        }
    };
    static int currentProgress = 0;
    static int durationTime = 0;
    public final static Handler handler1 = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

//            int progress = (int) msg.obj;
            if (msg.obj != null){
                currentProgress = (int) msg.obj;
                mPlayAudioSeekBar.setProgress(currentProgress);
            }
            System.out.println("msg.obj"+msg.obj);
            System.out.println("(int)msg.obj"+currentProgress);

//            mPlayAudioSeekBar.setProgress(msg.what);
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
        mPlayAudioSeekBar.setMax(durationTime);
        Message message = Message.obtain();
        message.obj = currentPosition;
        handler1.sendMessage(message);

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_audio);

        initWidget();

        Intent intent = getIntent();
        mPlayAudioTitleText.setText(intent.getStringExtra("music_name"));
        mMusicNameList = intent.getStringArrayListExtra("music_name_list");
//        mMusicSrcList = intent.getStringArrayListExtra("music_src_list");
        mMusicSrcList = intent.getIntegerArrayListExtra("music_src_list");


        mMusicNameAndSrcMap = new HashMap<>();
        for (int i = 0; i < mMusicNameList.size(); i++) {
            System.out.println(mMusicNameList.get(i) + " : " + mMusicSrcList.get(i));
            mMusicNameAndSrcMap.put(mMusicNameList.get(i), mMusicSrcList.get(i));
        }

        //导航按钮图片点击事件
        mPlayAudioToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mMediaPlayer = new MediaPlayer();
        for (int i = 0; i < mMusicNameList.size(); i++) {
            if (mPlayAudioTitleText.getText().toString().equals(mMusicNameList.get(i))) {
                currentMusicSrc = mMusicNameAndSrcMap.get(mMusicNameList.get(i));
            }
        }

        mPlayAudioSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser){
                    currentProgress = progress;
                    PlayAudioService.mediaPlayer.seekTo(progress);
                    if (currentProgress == durationTime){
                        System.out.println("currentProgress == durationTime"+currentProgress + ","+durationTime);
                        if (PlayAudioService.updateThread != null){
                            PlayAudioService.updateThread.interrupt();
                        }
                        if (IsServiceRunning.isServiceRunning(PlayAudioActivity.this, "com.example.lessontestapp.service.PlayAudioService")) {
                            stopService(mPlayAudioIntent);
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

        mPlayAudioIntent = new Intent(this, PlayAudioService.class);
    }

    public void initWidget() {
        mPlayAudioToolBar = findViewById(R.id.id_play_audio_toolBar);
        mPlayAudioTitleText = findViewById(R.id.id_play_audio_title_txt);
        mPlayAudioCurrentTimeHourText = findViewById(R.id.id_play_audio_current_time_hour_txt);
        mPlayAudioCurrentTimeMinutesText = findViewById(R.id.id_play_audio_current_time_minutes_txt);
        mPlayAudioCurrentTimeSecondText = findViewById(R.id.id_play_audio_current_time_second_txt);
        mPlayAudioTotalTimeHourText = findViewById(R.id.id_play_audio_total_time_hour_txt);
        mPlayAudioTotalTimeMinutesText = findViewById(R.id.id_play_audio_total_time_minutes_txt);
        mPlayAudioTotalTimeSecondText = findViewById(R.id.id_play_audio_total_time_second_txt);
        mPlayAudioSeekBar = findViewById(R.id.id_play_audio_seekBar);
        mPlayAudioPreviousBtn = findViewById(R.id.id_play_audio_previous_btn);
        mPlayAudioPlayBtn = findViewById(R.id.id_play_audio_play_btn);
        mPlayAudioStopBtn = findViewById(R.id.id_play_audio_stop_btn);
        mPlayAudioNextBtn = findViewById(R.id.id_play_audio_next_btn);
    }

    //点击播放
    public void onAudioPlayClick(View view) {
        System.out.println(currentMusicSrc);
//        mPlayAudioIntent = new Intent(this, PlayAudioService.class);
        mPlayAudioIntent.putExtra("music_src_toService", currentMusicSrc);
        mPlayAudioIntent.putExtra("is_play_pause_toService", mPlayOrPauseStr);

        startService(mPlayAudioIntent);
    }

    //点击停止
    public void onAudioStopClick(View view) {
        mPlayAudioPlayBtn.setText("播放");
        mPlayOrPauseStr = "播放";

//        audioStop();
        if (PlayAudioService.updateThread != null){
            PlayAudioService.updateThread.interrupt();
        }
        if (IsServiceRunning.isServiceRunning(this, "com.example.lessontestapp.service.PlayAudioService")) {
            stopService(mPlayAudioIntent);
        }
//        mPlayAudioSeekBar.setMax(100);
//        mPlayAudioSeekBar.setProgress(0);
    }

    //点击上一首
    public void onAudioPreviousClick(View view) {
        mPlayAudioSeekBar.setMax(100);
        mPlayAudioSeekBar.setProgress(0);
        if (IsServiceRunning.isServiceRunning(this, "com.example.lessontestapp.service.PlayAudioService")) {
            stopService(mPlayAudioIntent);
        }
        for (int i = 0; i < mMusicNameList.size(); i++) {
            if (mPlayAudioTitleText.getText().toString().equals(mMusicNameList.get(0))) {
                isFirst = true;
            } else {
                isFirst = false;
            }
        }
        if (!isFirst) {
            mPlayOrPauseStr = "播放";
            mPlayAudioPlayBtn.setText("播放");
            for (int i = 0; i < mMusicNameList.size(); i++) {
                if (mPlayAudioTitleText.getText().toString().equals(mMusicNameList.get(i))) {
                    currentMusicNum = i;
                }
            }
            currentMusicNum = currentMusicNum - 1;
            mPlayAudioTitleText.setText(mMusicNameList.get(currentMusicNum));
            currentMusicSrc = mMusicNameAndSrcMap.get(mMusicNameList.get(currentMusicNum));
        } else {
            Toast.makeText(this, "已经是第一首", Toast.LENGTH_SHORT).show();
            currentMusicNum = 0;
        }
    }

    //点击下一首
    public void onAudioNextClick(View view) {
        mPlayAudioSeekBar.setMax(100);
        mPlayAudioSeekBar.setProgress(0);
        if (IsServiceRunning.isServiceRunning(this, "com.example.lessontestapp.service.PlayAudioService")) {
            stopService(mPlayAudioIntent);
        }

        for (int i = 0; i < mMusicNameList.size(); i++) {
            if (mPlayAudioTitleText.getText().toString().equals(mMusicNameList.get(mMusicNameList.size() - 1))) {
                isLast = true;
            } else {
                isLast = false;
            }
        }
        if (!isLast) {
            mPlayOrPauseStr = "播放";
            mPlayAudioPlayBtn.setText("播放");
            for (int i = 0; i < mMusicNameList.size(); i++) {
                if (mPlayAudioTitleText.getText().toString().equals(mMusicNameList.get(i))) {
                    currentMusicNum = i;
                }
            }
            currentMusicNum = currentMusicNum + 1;
            System.out.println(mMusicNameList);
            System.out.println(currentMusicNum);
            mPlayAudioTitleText.setText(mMusicNameList.get(currentMusicNum));
            currentMusicSrc = mMusicNameAndSrcMap.get(mMusicNameList.get(currentMusicNum));
        } else {
            Toast.makeText(this, "已经最后一首", Toast.LENGTH_SHORT).show();
            currentMusicNum = mMusicNameList.size() - 1;
        }


    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

//    @Override
//    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//        //进度条改变
//        currentProgress = progress;
//        System.out.println("currentProgress"+currentProgress);
//        PlayAudioService.mediaPlayer.seekTo(currentProgress);
//    }
//
//    @Override
//    public void onStartTrackingTouch(SeekBar seekBar) {
//
//    }
//
//    @Override
//    public void onStopTrackingTouch(SeekBar seekBar) {
//
//    }



}