package com.example.lessontestapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.lessontestapp.EditFriendsInfoActivity;
import com.example.lessontestapp.R;

import java.util.List;

public class EditInfoActivityAdapter extends BaseAdapter {

    private int  select_item = -1 ;
    List<String> infoList;

    private LayoutInflater mInflater;

    public EditInfoActivityAdapter(Context context,List<String> infoList){
        this.mInflater = LayoutInflater.from(context);
        this.infoList = infoList;
    }

    @Override
    public int getCount() {
        return infoList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView holder;
        if (convertView == null){
            convertView = mInflater.inflate(R.layout.list_view_item_edit_friends_style,null);
            holder = convertView.findViewById(R.id.idtxtSpecialtyEditFriendViewList);
            convertView.setTag(holder);
        }else {
            holder = (TextView) convertView.getTag();
        }
        holder.setText(infoList.get(position));
        this.select_item = EditFriendsInfoActivity.select_item;
        if (select_item == position){
            holder.setTextSize(30);
            holder.setBackgroundColor(Color.parseColor("#E1FFFF"));
            holder.setTextColor(Color.parseColor("#FFFF00"));
        }else {
            holder.setTextSize(20);
            holder.setBackgroundColor(Color.parseColor("#FFFFFF"));
            holder.setTextColor(Color.parseColor("#3700B3"));
        }
        return convertView;
    }
}
