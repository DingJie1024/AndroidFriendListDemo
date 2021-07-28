package com.example.lessontestapp;

import android.content.DialogInterface;
import android.view.*;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MajorSettingActivity extends AppCompatActivity {

    private RecyclerView mMajorRecyclerView;
    private Toolbar mToolBar;

    private List<String> majorNameList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_major_setting);

        //导航按钮
        majorNameList = new ArrayList<>();
        majorNameList.add("计算机");
        majorNameList.add("软件工程");
        majorNameList.add("物联网");

        mMajorRecyclerView = findViewById(R.id.id_major_recyclerview);
        mMajorRecyclerView.setLongClickable(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mMajorRecyclerView.setLayoutManager(linearLayoutManager);
        MajorSettingActivity.MyAdapter myAdapter = new MajorSettingActivity.MyAdapter(majorNameList);
        mMajorRecyclerView.setAdapter(myAdapter);

        mToolBar = findViewById(R.id.id_major_setting_tool_bar);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.id_major_setting:
                        //插入操作
                        AlertDialog dialog = new AlertDialog.Builder(MajorSettingActivity.this).create();
                        dialog.setTitle("添加专业");
                        dialog.setMessage("请输入专业名称：");
                        dialog.setCancelable(false);
                        dialog.setView(getLayoutInflater().inflate(R.layout.activity_major_setting_dialog,null,false));
                        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        dialog.show();
                        Toast.makeText(MajorSettingActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }
        });

        //注册快捷菜单
        registerForContextMenu(mMajorRecyclerView);
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        private List<String> majorNameList;

        public MyAdapter(List<String> majorNameList) {
            this.majorNameList = majorNameList;
        }

        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.activity_major_setting_item, parent, false);
            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, final int position) {
            final String name = majorNameList.get(position);
            holder.majorTextView.setText(name);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    remove(position);
                }
            });
        }

        @Override
        public int getItemCount() {
            if (majorNameList==null) {
                return 0;
            } else {
                return majorNameList.size();
            }
        }

        private void remove(int position) {
            majorNameList.remove(position);
            notifyItemRemoved(position);
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView majorTextView;

            public MyViewHolder(View itemVieww) {
                super(itemVieww);
                majorTextView = itemView.findViewById(R.id.id_major_item_name);
            }
        }
    }

    //快捷菜单
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(0,1,0,"编辑");
        menu.add(0,2,0,"删除");
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case 1:
                AlertDialog updateDialog = new AlertDialog.Builder(this).create();
                updateDialog.setTitle("更新专业");
                updateDialog.setMessage("请输入专业名称：");
                updateDialog.setCancelable(false);
                updateDialog.setView(getLayoutInflater().inflate(R.layout.activity_major_setting_dialog,null,false));
                updateDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                updateDialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                updateDialog.show();
                break;
            case 2:
                AlertDialog deleteDialog = new AlertDialog.Builder(this).create();
                deleteDialog.setTitle("提示");
                deleteDialog.setMessage("确定删除吗？");
                deleteDialog.setCancelable(false);
                deleteDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                deleteDialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                deleteDialog.show();
                break;
        }
        return super.onContextItemSelected(item);
    }
}