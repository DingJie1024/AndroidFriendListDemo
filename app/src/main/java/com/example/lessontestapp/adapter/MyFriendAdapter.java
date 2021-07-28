package com.example.lessontestapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.lessontestapp.R;
import com.example.lessontestapp.dao.MyFriendInfo;
import com.example.lessontestapp.dao.MyFriendInfoDao;
import com.example.lessontestapp.db.InfoDBAdapter;
import com.example.lessontestapp.fragment.FriendsFragment;

import java.util.Arrays;
import java.util.List;

public class MyFriendAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<MyFriendInfoDao> friendDaoList;
    private List<MyFriendInfoDao> friendDaoNameList;


//    public MyFriendAdapter(Context context,List<MyFriendInfo> friendList){
//        this.mInflater = LayoutInflater.from(context);
//        this.friendList = friendList;
//    }

    public MyFriendAdapter(Context context,List<MyFriendInfoDao> friendDaoList){
        this.mInflater = LayoutInflater.from(context);
        this.friendDaoList = friendDaoList;

        this.friendDaoNameList = Arrays.asList(FriendsFragment.infoDBAdapter.queryNameOfBaseInfo());
    }

    @Override
    public int getCount() {
        return friendDaoList.size();
    }

    @Override
    public Object getItem(int position) {
        return friendDaoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView holder;
        ContextMenu contextMenuHolder;
        if (convertView == null){
            convertView = mInflater.inflate(R.layout.list_view_friend_item_style,null);
            holder = convertView.findViewById(R.id.idTxtFriendViewList);
            convertView.setTag(holder);

        }else {
            holder = (TextView) convertView.getTag();
        }
//        holder.setText(friendDaoList.get(position).getName());
        holder.setText(friendDaoNameList.get(position).getName());
        if (friendDaoList.get(position).getFocus() == 1){
            holder.setTextSize(30);
            holder.setTextColor(Color.parseColor("#00FA9A"));
            holder.setBackgroundColor(Color.parseColor("#E1FFFF"));
        }
        if (friendDaoList.get(position).getFocus() == 0){
            holder.setTextSize(20);
            holder.setTextColor(Color.parseColor("#000000"));
            holder.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        return convertView;
    }
}
