package com.example.lessontestapp.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;
import com.example.lessontestapp.FriendList;
import com.example.lessontestapp.R;

public class HintAppCloseService extends Service {

    private int delayTime;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        delayTime = Integer.parseInt(intent.getStringExtra("myDelayTime"));
        System.out.println("~~~~test-delayTime:~~~"+delayTime);
        //延时
        try {
            Thread.sleep(delayTime*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Intent intent1 = new Intent(this,FriendList.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent1);

        Toast.makeText(this,"程序关闭"+this.delayTime+"秒后重新启动",Toast.LENGTH_LONG).show();
        return START_STICKY;
//        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        IBinder binder = new MyBinder();
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    public class MyBinder extends Binder{
        public HintAppCloseService getService(){
            return HintAppCloseService.this;
        }
    }

    public int sumEndTwoNum(int a, int b){
        this.delayTime = a+b;
        return a + b;
    }

    public void cueAppClosed(){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Toast.makeText(this,"程序已经关闭",Toast.LENGTH_LONG).show();
    }

}