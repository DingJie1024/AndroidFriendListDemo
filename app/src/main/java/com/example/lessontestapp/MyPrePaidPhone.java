package com.example.lessontestapp;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

public class MyPrePaidPhone extends AppCompatActivity {

    private EditText mPrePaidPhoneEdit;
    private TextView mPrePaidPriceTenText,mPrePaidPriceTwentyText,mPrePaidPriceFiftyText;
    private Toolbar mPrePaidToolBar;
    private int price;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_pre_paid_phone);

        //初始化组件
        initWidget();

        //导航按钮
        mPrePaidToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mPrePaidPriceTenText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                price = 10;
                mPrePaidPriceTenText.setBackgroundColor(Color.parseColor("#AFEEEE"));
                mPrePaidPriceTwentyText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                mPrePaidPriceFiftyText.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
        });
        mPrePaidPriceTwentyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                price = 20;
                mPrePaidPriceTwentyText.setBackgroundColor(Color.parseColor("#AFEEEE"));
                mPrePaidPriceTenText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                mPrePaidPriceFiftyText.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
        });
        mPrePaidPriceFiftyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                price = 50;
                mPrePaidPriceFiftyText.setBackgroundColor(Color.parseColor("#AFEEEE"));
                mPrePaidPriceTwentyText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                mPrePaidPriceTenText.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
        });

    }

    public void initWidget(){
        mPrePaidPhoneEdit = findViewById(R.id.id_my_pre_paid_phone_edit);
        mPrePaidPriceTenText = findViewById(R.id.id_my_pre_paid_phone10_text);
        mPrePaidPriceTwentyText = findViewById(R.id.id_my_pre_paid_phone20_text);
        mPrePaidPriceFiftyText = findViewById(R.id.id_my_pre_paid_phone50_text);
        mPrePaidToolBar = findViewById(R.id.id_my_pre_paid_phone_toolBar);
    }

    public void onConfirmPayClick(View view){
        Intent intent = new Intent(this,MyPayActivity.class);
        intent.putExtra("pay_phone_num",mPrePaidPhoneEdit.getText().toString());
        intent.putExtra("pay_price",String.valueOf(price));
        startActivity(intent);
    }
}