package com.example.lessontestapp;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import cn.dj.remoteserviceapp.IMyService;
import cn.dj.remoteserviceapp.Result;
import com.example.lessontestapp.dao.MyFriendInfoDao;
import com.example.lessontestapp.db.InfoDBAdapter;

import java.util.List;


public class MyPayActivity extends AppCompatActivity {

    private TextView mMyPayPhoneNumText,mMyPayPriceText;
    private EditText mMyPayPasswordEdit;
    private Toolbar mMyPayToolBar;

    private IMyService myService = null;
    private boolean isBind = false;

    private String phoneNumDB;
    private String passwordDB;
    private String balanceDB;

    private String[] phoneNumDBs;

    private String inputPhoneNum;
    private String inputPassword;
    private String inputPrice;

    private boolean isUserExist = false;


    //数据库实例
    private InfoDBAdapter infoDBAdapter;

    private ServiceConnection myConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myService = IMyService.Stub.asInterface(service);
            System.out.println("我被绑定");
            isBind = true;
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBind = false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_pay);

        infoDBAdapter = new InfoDBAdapter(this);
        infoDBAdapter.open();

        //初始化组件
        initWidget();

        //导航栏按钮
        mMyPayToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //获取接收道德值
        Intent intent = getIntent();
        System.out.println(intent.getStringExtra("pay_phone_num"));
        System.out.println(intent.getStringExtra("pay_price"));
        inputPhoneNum = intent.getStringExtra("pay_phone_num");
        mMyPayPhoneNumText.setText(intent.getStringExtra("pay_phone_num"));
        mMyPayPriceText.setText(intent.getStringExtra("pay_price"));



        //
        final Intent remotePayService = new Intent("cn.dj.remotepayservice");
        remotePayService.setPackage("cn.dj.remoteserviceapp");
        bindService(remotePayService,myConnection,BIND_AUTO_CREATE);
        myService = null;
    }

    public void initWidget(){
        mMyPayPhoneNumText = findViewById(R.id.id_my_pay_phoneNum_text);
        mMyPayPriceText = findViewById(R.id.id_my_pay_price_text);
        mMyPayPasswordEdit = findViewById(R.id.id_my_pay_password_edit);
        mMyPayToolBar = findViewById(R.id.id_my_pay_toolBar);

    }

    public void onImmediatelyPayClick(View view){
        //判断用户是否存在
        MyFriendInfoDao[] myFriendInfoDaos = infoDBAdapter.queryPhoneOfBaseInfo();
        for (int i = 0;i < myFriendInfoDaos.length;i++){
            if (myFriendInfoDaos[i].getPhone().equals(inputPhoneNum)){
                isUserExist = true;
                phoneNumDB = myFriendInfoDaos[i].getPhone();
                MyFriendInfoDao[] myFriendPasswordDao = infoDBAdapter.queryPasswordOfBaseInfo(inputPhoneNum);
                passwordDB = myFriendPasswordDao[0].getPassword();
                MyFriendInfoDao[] myFriendBalanceDao = infoDBAdapter.queryBalanceOfBaseInfo(inputPhoneNum);
                balanceDB = myFriendBalanceDao[0].getBalance();
            }
        }
        inputPassword = mMyPayPasswordEdit.getText().toString();
        System.out.println(phoneNumDB);
        System.out.println(passwordDB);
        System.out.println(balanceDB);
        System.out.println(inputPassword);
        Result r = null;
        System.out.println("isBind::"+isBind);
        if (isBind){
            try {
                r = myService.result(inputPhoneNum, inputPrice, inputPassword);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            if (isUserExist){
                if (passwordDB.equals(inputPassword)){
                    if ((Integer.parseInt(balanceDB) > 70)){
                        Toast.makeText(this,r.getPaySuccess(),Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(this,r.getNoEnoughBalance(),Toast.LENGTH_SHORT).show();
                    }
                }
                if (!passwordDB.equals(inputPassword)){
                    Toast.makeText(this,r.getErrorPassword(),Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(this,r.getNoneUser(),Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBind){
            unbindService(myConnection);
            Toast.makeText(this,"解绑成功",Toast.LENGTH_SHORT).show();
            isBind = false;
        }
        infoDBAdapter.close();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isBind){
            unbindService(myConnection);
            Toast.makeText(this,"解绑成功",Toast.LENGTH_SHORT).show();
            isBind = false;
        }
        infoDBAdapter.close();
    }
}