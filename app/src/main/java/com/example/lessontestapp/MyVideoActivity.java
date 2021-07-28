package com.example.lessontestapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class MyVideoActivity extends AppCompatActivity {
    Toolbar mMyVideoToolBar;
    RecyclerView mMyVideoRecyclerView;

//    private Map<String,String> videoNameMap = new TreeMap<>();
    private Map<String,Integer> videoNameMap = new TreeMap<>();

    @SuppressLint("SdCardPath")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_video);

        initWidget();

        //导航按钮图片点击事件
        mMyVideoToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //RecyclerView设计
//        videoNameMap.put("杨洋01","/data/data/com.example.lessontestapp/video/video01_rongyao.mp4");
//        videoNameMap.put("杨洋02","/data/data/com.example.lessontestapp/video/video02_rongyao.mp4");
//        videoNameMap.put("杨洋03","/data/data/com.example.lessontestapp/video/video03_rongyao.mp4");
//        videoNameMap.put("杨洋04","/data/data/com.example.lessontestapp/video/video04_rongyao.mp4");

        videoNameMap.put("杨洋01",R.raw.video01_rongyao);
        videoNameMap.put("杨洋02",R.raw.video02_rongyao);
        videoNameMap.put("杨洋03",R.raw.video03_rongyao);
        videoNameMap.put("杨洋04",R.raw.video04_rongyao);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mMyVideoRecyclerView.setLayoutManager(linearLayoutManager);
//        MyVideoActivity.MyAdapter myAdapter = new MyVideoActivity.MyAdapter(videoNameMap);
        MyVideoActivity.MyAdapter myAdapter = new MyVideoActivity.MyAdapter(videoNameMap);
        mMyVideoRecyclerView.setAdapter(myAdapter);
    }

    public void initWidget(){
        mMyVideoToolBar = findViewById(R.id.id_my_video_toolBar);
        mMyVideoRecyclerView = findViewById(R.id.id_my_video_recyclerView);
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{

        private ArrayList<String> videoNameList = new ArrayList<>();
//        private ArrayList<String> videoSrcList = new ArrayList<>();
        private ArrayList<Integer> videoSrcList = new ArrayList<>();
//        private Map<String,String> videoNameMap = new TreeMap<>();
        private Map<String,Integer> videoNameMap = new TreeMap<>();

//        public MyAdapter(Map<String,String> videoNameMap){
//            this.videoNameMap = videoNameMap;
//            this.videoNameList.addAll(videoNameMap.keySet());
//            this.videoSrcList.addAll(videoNameMap.values());
//        }
        public MyAdapter(Map<String,Integer> videoNameMap){
            this.videoNameMap = videoNameMap;
            this.videoNameList.addAll(videoNameMap.keySet());
            this.videoSrcList.addAll(videoNameMap.values());
        }

        @NonNull
        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.activity_my_video_item,parent,false);
            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, final int position) {
            final String name = this.videoNameList.get(position);

            holder.videoTextView.setText(name);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MyVideoActivity.this,PlayVideoActivity.class);
                    intent.putExtra("video_name",videoNameList.get(position));
                    intent.putStringArrayListExtra("video_name_list",videoNameList);
//                    intent.putStringArrayListExtra("video_src_list",videoSrcList);
                    intent.putIntegerArrayListExtra("video_src_list",videoSrcList);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            if (videoNameList == null){
                return 0;
            }else {
                return videoNameList.size();
            }
        }

        class MyViewHolder extends RecyclerView.ViewHolder{

            TextView videoTextView;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                videoTextView = itemView.findViewById(R.id.id_my_video_item_name);
            }
        }
    }

}