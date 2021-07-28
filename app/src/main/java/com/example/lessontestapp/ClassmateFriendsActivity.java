package com.example.lessontestapp;

import android.annotation.SuppressLint;
import android.view.*;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ClassmateFriendsActivity extends AppCompatActivity {

    private RecyclerView mClassmateRecyclerView;
    private Toolbar mToolbar;

    private List<String> classmateNameList;
    private List<String> classmatePhoneList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classmate_friends);

        mClassmateRecyclerView = findViewById(R.id.id_classmate_recyclerview);
        mToolbar = findViewById(R.id.id_classmate_friend_toolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.id_classmate_friend_add_confirm:
                        //插入操作
                        Toast.makeText(ClassmateFriendsActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }
        });

        classmateNameList = new ArrayList<>();
        classmateNameList.add("张三");
        classmateNameList.add("李四");
        classmateNameList.add("王二");
        classmatePhoneList = new ArrayList<>();
        classmatePhoneList.add("1234567890");
        classmatePhoneList.add("1234567891");
        classmatePhoneList.add("1234567892");

        mClassmateRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mClassmateRecyclerView.setLayoutManager(linearLayoutManager);

        MyAdapter myAdapter = new MyAdapter(classmateNameList,classmatePhoneList);
        mClassmateRecyclerView.setAdapter(myAdapter);
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        private List<String> nameList;
        private List<String> phoneList;

        public MyAdapter(List<String> nameList,List<String> phoneList) {
            this.nameList = nameList;
            this.phoneList = phoneList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.activity_classmate_friends_item, parent, false);
            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
            final String name = nameList.get(position);
            final String phone = phoneList.get(position);
            holder.nameTextView.setText(name);
            holder.phoneTextView.setText(phone);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    remove(position);
                }
            });
        }

        @Override
        public int getItemCount() {
            if (nameList==null) {
                return 0;
            } else {
                return nameList.size();
            }
        }

        private void remove(int position) {
            nameList.remove(position);
            phoneList.remove(position);
            notifyItemRemoved(position);
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView nameTextView,phoneTextView;

            public MyViewHolder(View itemVieww) {
                super(itemVieww);
                nameTextView = itemView.findViewById(R.id.id_classmate_item_name);
                phoneTextView = itemView.findViewById(R.id.id_classmate_item_phone);
            }
        }
    }

}