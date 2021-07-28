package com.example.lessontestapp;

import android.content.Intent;
import android.net.Uri;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

public class InfoActivityt extends AppCompatActivity {
    private TextView mDetailNameText, mDetailPhoneText, mDetailGenderText,
                    mDetailProvinceText, mDetailSpecialtyText, mDetailHobbiesText;
    private Toolbar mToolBar;
    private ImageView mHeadImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_activityt);


        //初始化组件
        initWidget();

        //接受FriendList页面好友的信息
        Intent intent = getIntent();
        ArrayList<String> detailInfo = intent.getStringArrayListExtra("detailInfo");

        //显示
        mDetailNameText.setText(detailInfo.get(0));
        mDetailPhoneText.setText(detailInfo.get(1));
        mDetailGenderText.setText(detailInfo.get(2));
        mDetailHobbiesText.setText(detailInfo.get(3));
        mDetailProvinceText.setText(detailInfo.get(4));
        mDetailSpecialtyText.setText(detailInfo.get(5));
        mHeadImageView.setImageURI(Uri.parse(detailInfo.get(7)));
        //标题栏左侧箭头
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //初始化组件
    public void initWidget(){
        mDetailNameText = findViewById(R.id.idDetailNameText);
        mDetailPhoneText = findViewById(R.id.idDetailPhoneText);
        mDetailGenderText = findViewById(R.id.idDetailGenderText);
        mDetailHobbiesText = findViewById(R.id.idDetailHobbiesText);
        mDetailProvinceText = findViewById(R.id.idDetailProvinceText);
        mDetailSpecialtyText = findViewById(R.id.idDetailSpecialtyText);
        mToolBar = findViewById(R.id.id_info_activity_tool_bar);
        mHeadImageView = findViewById(R.id.idDetailHeaderImage);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}