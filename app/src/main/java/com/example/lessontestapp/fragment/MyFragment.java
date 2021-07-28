package com.example.lessontestapp.fragment;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.*;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.lessontestapp.*;
import com.example.lessontestapp.dao.MyInfoDao;
import com.example.lessontestapp.dao.MyMajorDao;
import com.example.lessontestapp.db.InfoDBAdapter;

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyFragment extends Fragment {

    private RecyclerView mMyPageRecyclerView;

    private List<String> myPageList;

    //照片文件
    private Uri imageUri;
    private Uri imageUriToFiles;

    private String fileName;

    private ImageView mHeaderImageView;

    private TextView mNameTextView, mPhoneTextView;

    private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果

    //数据库实例
    private InfoDBAdapter infoDBAdapter;

    MyInfoDao myInfoDao;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_my,container,false);

        infoDBAdapter = new InfoDBAdapter(view.getContext());
        infoDBAdapter.open();
        myInfoDao = infoDBAdapter.queryMyInfo();

        mMyPageRecyclerView = view.findViewById(R.id.id_my_page_recycler_view);
        mHeaderImageView = view.findViewById(R.id.id_my_header);
        mNameTextView = view.findViewById(R.id.id_my_name);
        mPhoneTextView = view.findViewById(R.id.id_my_phone);

        mNameTextView.setText(myInfoDao.getName());
        mPhoneTextView.setText(myInfoDao.getPhone());
        mHeaderImageView.setImageURI(Uri.parse(myInfoDao.getProfile()));

        myPageList = new ArrayList<>();
        myPageList.add("字体大小");
        myPageList.add("专业设置");
        myPageList.add("我的音乐");
        myPageList.add("我的视频");
        myPageList.add("手机充值");

        mMyPageRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mMyPageRecyclerView.setLayoutManager(linearLayoutManager);

        MyAdapter myAdapter = new MyAdapter(myPageList);
        mMyPageRecyclerView.setAdapter(myAdapter);

        //ImageView注册快捷菜单
        registerForContextMenu(mHeaderImageView);

        mNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<String> infoDataTranmit = addTranmitedData();
                Intent intent = new Intent();
                intent.setClass(getActivity(), InfoActivityt.class);
                intent.putStringArrayListExtra("detailInfo", infoDataTranmit);
                startActivity(intent);
            }
        });

//        mHeaderImageView.setImageURI(imageUriToFiles);

        return view;
    }

    //添加传递的数据
    public ArrayList<String> addTranmitedData(){
        ArrayList<String> infoDataTranmitList = new ArrayList<>();
        MyMajorDao[] majorDaos = infoDBAdapter.queryMajor();
        infoDataTranmitList.add(myInfoDao.getName());
        infoDataTranmitList.add(myInfoDao.getPhone());
        infoDataTranmitList.add(myInfoDao.getGender());
        infoDataTranmitList.add(myInfoDao.getHobbies().toString());
        infoDataTranmitList.add(myInfoDao.getBirthPlace());
        for (int i = 0; i < majorDaos.length; i++){
            if (majorDaos[i].getId() == myInfoDao.getSpecialtyID()){
                infoDataTranmitList.add(majorDaos[i].getMajorName());
            }
        }
        infoDataTranmitList.add(String.valueOf(myInfoDao.getInfoID()));
        infoDataTranmitList.add(myInfoDao.getProfile());

        return infoDataTranmitList;
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        private List<String> myPageList;

        public MyAdapter(List<String> myPageList) {
            this.myPageList = myPageList;
        }

        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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
//                        Intent intent = new Intent(MyActivity.this, SetFontSizeActivity.class);
                        Intent intent = new Intent();
                        intent.setClass(getActivity(),SetFontSizeActivity.class);
                        startActivity(intent);
                    }
                    if (position == 1){
//                        Intent intent = new Intent(MyActivity.this, MajorSettingActivity.class);
                        Intent intent = new Intent();
                        intent.setClass(getActivity(),MajorSettingActivity.class);
                        startActivity(intent);
                    }
                    if (position == 2){
                        Intent intent = new Intent();
                        intent.setClass(getActivity(),MyAudioActivity.class);
                        startActivity(intent);
                    }
                    if (position == 3){
                        Intent intent = new Intent();
                        intent.setClass(getActivity(),MyVideoActivity.class);
                        startActivity(intent);
                    }
                    if (position == 4){
                        Intent intent = new Intent();
                        intent.setClass(getActivity(),MyPrePaidPhone.class);
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, 1, 0, "从相册选择图片");
        menu.add(0, 2, 0, "拍照");
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case 1:
                Intent albumIntent = new Intent(Intent.ACTION_PICK, null);
                albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(albumIntent, PHOTO_REQUEST_GALLERY);
                break;
            case 2:
                openCamera_2();
                break;
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case PHOTO_REQUEST_GALLERY:
                imageUri = data.getData();
                imageUriToFiles =  Uri.parse("file://"+getPath(getActivity(),imageUri));
//                MyInfoDao myInfoDao = new MyInfoDao();
                myInfoDao.setProfile(imageUriToFiles+"");
                infoDBAdapter.updateMyProfileInfo(myInfoDao,1);

                System.out.println(imageUriToFiles);
                System.out.println(getPath(getActivity(),imageUri));
                try {
                    System.out.println(imageUri);
                    mHeaderImageView.setImageURI(imageUri);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case PHOTO_REQUEST_TAKEPHOTO:
                try {
                    //查询的条件语句
                    String selection = MediaStore.Images.Media.DISPLAY_NAME + "=? ";
                    //查询的sql
                    //Uri：指向外部存储Uri
                    //projection：查询那些结果
                    //selection：查询的where条件
                    //sortOrder：排序
                    Cursor cursor = getActivity().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Images.Media._ID},selection,new String[]{fileName},null);
                    if (cursor != null && cursor.moveToFirst()) {
                        do {
                            Uri uri =  ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cursor.getLong(0));
                            imageUriToFiles =  Uri.parse("file://"+getPath(getActivity(),uri));
//                            MyInfoDao myInfoDao1 = new MyInfoDao();
                            myInfoDao.setProfile(imageUriToFiles+"");
                            infoDBAdapter.updateMyProfileInfo(myInfoDao,1);
                            mHeaderImageView.setImageURI(uri);
                        }while (cursor.moveToNext());
                    }else {
                        Toast.makeText(getActivity(),"no photo",Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private String getPath(Context context, Uri uri) {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{"_data"}, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    String data = cursor.getString(cursor.getColumnIndex("_data"));
                    cursor.close();
                    return data;
                }
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    // 拍照后存储并显示图片
    private void openCamera_2() {

        File fileDir = new File(Environment.getExternalStorageDirectory(),"Pictures");
        if (!fileDir.exists()) {
            fileDir.mkdir();
        }
        fileName = "IMG_" + System.currentTimeMillis() + ".jpg";

        String mFilePath = fileDir.getAbsolutePath()+"/"+ fileName;
        Uri uri = null;
        ContentValues contentValues = new ContentValues();
        //设置文件名

        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

            contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, "DCIM/Pictures");
        }else {

            contentValues.put(MediaStore.Images.Media.DATA, mFilePath);
        }
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/JPEG");
        uri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 启动系统相机

        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, PHOTO_REQUEST_TAKEPHOTO);
    }

}
