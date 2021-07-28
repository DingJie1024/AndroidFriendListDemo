package cn.dj.remoteserviceapp;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;

public class MyService extends Service {

    private IMyService.Stub myBinder = new IMyService.Stub(){
        @Override
        public Result result(String phoneNum, String balance, String password) throws RemoteException {
            String paySuccess = "支付成功";
            String noneUser = "用户不存在";
            String errorPassword = "密码错误";
            String noEnoughBalance = "余额不足";
            return new Result(paySuccess,noneUser,errorPassword,noEnoughBalance);
        }
    };


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return myBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return true;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}