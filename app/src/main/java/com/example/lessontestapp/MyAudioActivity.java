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
import java.util.List;
import java.util.Map;

public class MyAudioActivity extends AppCompatActivity {

    Toolbar mMyAudioToolBar;
    RecyclerView mMyAudioRecyclerView;

//    private Map<String,String> audioNameMap = new HashMap<>();
    private Map<String,Integer> audioNameMap = new HashMap<>();

    @SuppressLint("SdCardPath")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_audio);

        initWidget();

        //导航按钮图片点击事件
        mMyAudioToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //RecyclerView设计
//        audioNameMap.put("brave heart","/data/data/com.example.lessontestapp/music/music02_brave_heart.flac");
//        audioNameMap.put("see you again","/data/data/com.example.lessontestapp/music/music03_see_you_again.flac");
//        audioNameMap.put("Y","/data/data/com.example.lessontestapp/music/music01_y.mp3");
//        audioNameMap.put("岁月神偷","/data/data/com.example.lessontestapp/music/music04_echoes_of_the_rainbow.mp3");

        audioNameMap.put("brave heart",R.raw.music02_brave_heart);
        audioNameMap.put("see you again",R.raw.music03_see_you_again);
        audioNameMap.put("Y",R.raw.music01_y);
        audioNameMap.put("岁月神偷",R.raw.music04_echoes_of_the_rainbow);



        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mMyAudioRecyclerView.setLayoutManager(linearLayoutManager);
        MyAudioActivity.MyAdapter myAdapter = new MyAudioActivity.MyAdapter(audioNameMap);
        mMyAudioRecyclerView.setAdapter(myAdapter);

    }

    public void initWidget(){
        mMyAudioToolBar = findViewById(R.id.id_my_audio_toolBar);
        mMyAudioRecyclerView = findViewById(R.id.id_my_audio_recyclerView);
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{

        private ArrayList<String> audioNameList = new ArrayList<>();
//        private ArrayList<String> audioSrcList = new ArrayList<>();
        private ArrayList<Integer> audioSrcList = new ArrayList<>();

        //        private Map<String,String> audioNameMap;
        private Map<String,Integer> audioNameMap;


//        public MyAdapter(Map<String,String> audioNameMap){
//            this.audioNameMap = audioNameMap;
//            this.audioNameList.addAll(audioNameMap.keySet());
//            this.audioSrcList.addAll(audioNameMap.values());
//        }

        public MyAdapter(Map<String,Integer> audioNameMap){
            this.audioNameMap = audioNameMap;
            this.audioNameList.addAll(audioNameMap.keySet());
            this.audioSrcList.addAll(audioNameMap.values());
        }

        @NonNull
        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.activity_my_audio_item,parent,false);
            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder,final int position) {

            final String name = this.audioNameList.get(position);

            holder.audioTextView.setText(name);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MyAudioActivity.this,PlayAudioActivity.class);
                    intent.putExtra("music_name",audioNameList.get(position));
                    intent.putStringArrayListExtra("music_name_list",audioNameList);
//                    intent.putStringArrayListExtra("music_src_list",audioSrcList);
                    intent.putIntegerArrayListExtra("music_src_list",audioSrcList);
                    startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            if (audioNameList == null){
                return 0;
            }else {
                return audioNameList.size();
            }
        }


        class MyViewHolder extends RecyclerView.ViewHolder{

            TextView audioTextView;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                audioTextView = itemView.findViewById(R.id.id_my_audio_item_name);
            }
        }
    }
}