package com.example.lessontestapp;

import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyActivity extends AppCompatActivity {

    private RecyclerView mMyPageRecyclerView;

    private List<String> myPageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        mMyPageRecyclerView = findViewById(R.id.id_my_page_recycler_view);

        myPageList = new ArrayList<>();
        myPageList.add("字体大小");
        myPageList.add("专业设置");

        mMyPageRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MyActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mMyPageRecyclerView.setLayoutManager(linearLayoutManager);

        MyAdapter myAdapter = new MyAdapter(myPageList);
        mMyPageRecyclerView.setAdapter(myAdapter);


    }


    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        private List<String> myPageList;

        public MyAdapter(List<String> myPageList) {
            this.myPageList = myPageList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.activity_my_item, parent, false);
            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
            final String item = myPageList.get(position);
            holder.myItemText.setText(item);
            holder.myItemText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    remove(position);
                    if (position == 0){
                        Intent intent = new Intent(MyActivity.this,SetFontSizeActivity.class);
                        startActivity(intent);
                    }
                    if (position == 1){
                        Intent intent = new Intent(MyActivity.this,MajorSettingActivity.class);
                        startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            if (myPageList==null) {
                return 0;
            } else {
                return myPageList.size();
            }
        }

        private void remove(int position) {
            myPageList.remove(position);
            notifyItemRemoved(position);
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView myItemText;
            public MyViewHolder(View itemVieww) {
                super(itemVieww);
                myItemText = itemView.findViewById(R.id.id_my_item_text);
            }
        }
    }
}