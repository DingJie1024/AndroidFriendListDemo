package com.example.lessontestapp;

import android.annotation.SuppressLint;
import android.content.*;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import com.example.lessontestapp.adapter.MyAdapter;
import com.example.lessontestapp.dao.MyFriendInfoDao;
import com.example.lessontestapp.dao.MyInfoDao;
import com.example.lessontestapp.dao.MyMajorDao;
import com.example.lessontestapp.db.InfoDBAdapter;
import com.example.lessontestapp.fragment.FriendsFragment;
import com.example.lessontestapp.utils.ImageUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private EditText mNameEditText, mHeightEditText, mWeightEditText, mPhoneEditText;
    private RadioGroup mGenderRadioGroup;
    private CheckBox mSportCheckBox, mTripCheckBox, mOtherCheckBox;
    private Spinner mProvinceSpinner;
    private Button mYesButton, mCancelButton;
    private ListView mInfoListView;
    private Switch mIsFollowSwitch;
    private ImageView mHeaderImageView;
    private Toolbar mToolbar;

    public  static  int  select_item = 0 ;
    String provinceArr[] = {
            "河北省","山西省","辽宁省","吉林省","黑龙江省","江苏省","浙江省","安徽省","福建省",
            "江西省","山东省","河南省","湖北省","湖南省","广东省","海南省","四川省","贵州省",
            "云南省","陕西省","甘肃省","青海省","台湾省"
    };

    CheckBox[] cbHobbyArr;

    String specialtyStr;

    List<String> infoList = new ArrayList<>();
    //选中的爱好
    List<String> hobbyCheckedList;

    MyAdapter infoAdapter;

    Set<String> hobbiesSet = new TreeSet<>();

    //访问模式
    private static int MY_MODE = MODE_PRIVATE;
    //文件名
    private static final String MY_NAME = "setting";
    //定义SharedPreferences实例
    private SharedPreferences shared;

    //数据库实例
    private InfoDBAdapter infoDBAdapter;

    //照片文件
    private Uri imageUri;

    private Uri imageUriToFiles;

    private String fileName;


    public static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
    public static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    public static final int PHOTO_REQUEST_CORP = 3;// 结果


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化组件
        initWigdet();

        //创建数据库实例
        infoDBAdapter = new InfoDBAdapter(this);
        infoDBAdapter.open();
        //查询专业表
        MyMajorDao[] majorDaos = infoDBAdapter.queryMajor();


        //Spinner
        List<String> provinceList = new ArrayList<>();
        for(int i = 0;i<provinceArr.length;i++){
            provinceList.add(provinceArr[i]);
        }
        ArrayAdapter<String> provinceAdapter = new ArrayAdapter<String>(
                this, R.layout.spinner_item_style, R.id.idSpinnerStyle,provinceList
        );
        mProvinceSpinner = findViewById(R.id.idProvinceSpinner);
        mProvinceSpinner.setAdapter(provinceAdapter);

        //ListView
        for (int i = 0; i < majorDaos.length; i++){
            infoList.add(majorDaos[i].getMajorName());
        }

        infoAdapter = new MyAdapter(this,infoList);
        mInfoListView = findViewById(R.id.idInfoListView);
        mInfoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                specialtyStr = infoList.get(position);
                select_item = position;
                infoAdapter.notifyDataSetChanged();
            }
        });
        mInfoListView.setAdapter(infoAdapter);

        registerForContextMenu(mInfoListView);

        //下拉选项
        //设置下拉选项的默认值
        mProvinceSpinner.setSelection(5,true);
        mProvinceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //输入框改变事件监听
        myNameAndPhoneEditTextListener();

        //ImageView注册快捷菜单
        registerForContextMenu(mHeaderImageView);

    }

    //输入框改变事件监听
    public void myNameAndPhoneEditTextListener(){
        mNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String nameStr = mNameEditText.getText().toString();
                Pattern p = Pattern.compile("[\u4E00-\u9FA5]{2,5}(?:·[\u4E00-\u9FA5]{2,5})*");
                Matcher m = p.matcher(nameStr);
                if (m.matches()){
//                    Toast.makeText(MainActivity.this, "[实时]请输入中文名称："+s.toString(), Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(MainActivity.this, "[实时]请输入中文名称", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mPhoneEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String phoneStr = mPhoneEditText.getText().toString();
                Pattern p = Pattern.compile("^((13[0-9])|(15[^4])|(18[0-9])|(17[0-8])|(147,145))\\d{8}$");//正则
                Matcher m = p.matcher(phoneStr);
                if (m.matches() && phoneStr.length() == 11){
//                    Toast.makeText(MainActivity.this, "[实时]请输入中文名称："+s.toString(), Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(MainActivity.this, "[实时]请输入正确手机号", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //SharedPreferences
        shared = getSharedPreferences(MY_NAME,MY_MODE);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void initWigdet(){
        mNameEditText = findViewById(R.id.nameEditText);
        mPhoneEditText = findViewById(R.id.phoneEditText);
        mGenderRadioGroup = findViewById(R.id.idGenderRadioGroup);

        mSportCheckBox = findViewById(R.id.idSportCheckBox);
        mTripCheckBox = findViewById(R.id.idTripCheckBox);
        mOtherCheckBox = findViewById(R.id.idOtherCheckBox);
        mProvinceSpinner = findViewById(R.id.idProvinceSpinner);
        mIsFollowSwitch = findViewById(R.id.idIsFollowSwitch);
        mHeaderImageView = findViewById(R.id.idHeaderImage);
        mToolbar = findViewById(R.id.id_add_friend_toolbar);
        cbHobbyArr = new CheckBox[]{mTripCheckBox,mSportCheckBox,mOtherCheckBox};
        for (int i = 0; i<cbHobbyArr.length; i++){
            final int finalI = i;
            cbHobbyArr[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println(cbHobbyArr[finalI].isChecked());
                    if (cbHobbyArr[finalI].isChecked()){
                        hobbyCheckedList.add(cbHobbyArr[finalI].getText().toString());
                    }else {
                        hobbyCheckedList.remove(cbHobbyArr[finalI].getText().toString());
                    }
                    System.out.println("~~~"+hobbyCheckedList);
                }

            });
        }
    }




    public void onClick(View view){
        mYesButton = findViewById(R.id.idYesButton);
        mCancelButton = findViewById(R.id.idCancelButton);
        String genderStr = "";
        int focus = 0;
        RadioButton rbGender = null;
        //获取性别id
        if (mGenderRadioGroup.getCheckedRadioButtonId() == -1){
            genderStr = "";
        }else {
            rbGender = findViewById(mGenderRadioGroup.getCheckedRadioButtonId());
            genderStr = rbGender.getText().toString();
        }
        //获取是否关注
        if (mIsFollowSwitch.isChecked()){
            focus = 1;
            System.out.println("已经关注"+focus);
        }
        //
//        Intent intent = new Intent(this, FriendsFragment.class);
        Intent intent = new Intent(this, IndexActivity.class);
        switch (view.getId()){
            case R.id.idYesButton: {
                MyFriendInfoDao[] friendPhones = infoDBAdapter.queryPhoneOfBaseInfo();
                boolean isAlreadyExist = true;
                if (friendPhones != null){
                    for (MyFriendInfoDao friendPhone : friendPhones) {
                        if ((mPhoneEditText.getText().toString()).equals(friendPhone.getPhone())) {
                            isAlreadyExist = false;
                            Toast.makeText(this, "The Phone already exists", Toast.LENGTH_SHORT).show();
                        }
                    }
                    if (isAlreadyExist){
                        MyFriendInfoDao friendInfoDao = new MyFriendInfoDao();
                        friendInfoDao.setName(mNameEditText.getText().toString());
                        friendInfoDao.setPhone(mPhoneEditText.getText().toString());
                        friendInfoDao.setGender(genderStr);
                        friendInfoDao.setFocus(focus);
                        friendInfoDao.setHobbies(hobbyCheckedList);
                        friendInfoDao.setBirthPlace(mProvinceSpinner.getSelectedItem().toString());
                        friendInfoDao.setSpecialtyID(select_item+1);
//                        friendInfoDao.setProfile(imageUri+"");
                        friendInfoDao.setProfile(imageUriToFiles+"");

                        infoDBAdapter.insertBaseInfo(friendInfoDao);
                        startActivity(intent);
                    }
                }else {
                    if (isAlreadyExist){
                        MyFriendInfoDao friendInfoDao = new MyFriendInfoDao();
                        friendInfoDao.setName(mNameEditText.getText().toString());
                        friendInfoDao.setPhone(mPhoneEditText.getText().toString());
                        friendInfoDao.setGender(genderStr);
                        friendInfoDao.setFocus(focus);
                        friendInfoDao.setHobbies(hobbyCheckedList);
                        friendInfoDao.setBirthPlace(mProvinceSpinner.getSelectedItem().toString());
                        friendInfoDao.setSpecialtyID(select_item+1);
//                        friendInfoDao.setProfile(imageUri+"");
                        friendInfoDao.setProfile(imageUriToFiles+"");

                        infoDBAdapter.insertBaseInfo(friendInfoDao);
                        startActivity(intent);
                    }
                }
                break;
            }
            case R.id.idCancelButton: {
                startActivity(intent);
                break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //xml方法生成
        getMenuInflater().inflate(R.menu.menu, menu);
        //代码方式
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case 1:
                Toast.makeText(MainActivity.this, "菜单项内容："+item.getTitle().toString()+","+"ID:"+item.getItemId(), Toast.LENGTH_SHORT).show();
                break;
            case 11:
                Toast.makeText(MainActivity.this, "菜单项内容："+item.getTitle().toString()+","+"ID:"+item.getItemId(), Toast.LENGTH_SHORT).show();
                break;
            case android.R.id.home:
                Intent intent = new Intent(MainActivity.this, FriendList.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;
            case R.id.idSetISpeciatyItem:
                Intent intent1 = new Intent(this,MajorSettingActivity.class);
                startActivity(intent1);
//                finish();
        }
        return false;
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case PHOTO_REQUEST_GALLERY:
                imageUri = data.getData();
                imageUriToFiles =  Uri.parse("file://"+getPath(this,imageUri));
                System.out.println(imageUriToFiles);
                System.out.println(getPath(this,imageUri));
                Bitmap bitmap= null;
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
                    Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Images.Media._ID},selection,new String[]{fileName},null);
                    if (cursor != null && cursor.moveToFirst()) {
                        do {
                            Uri uri =  ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cursor.getLong(0));
                            imageUriToFiles =  Uri.parse("file://"+getPath(this,uri));
                            MyInfoDao myInfoDao1 = new MyInfoDao();
                            myInfoDao1.setProfile(imageUriToFiles+"");
                            infoDBAdapter.updateMyProfileInfo(myInfoDao1,1);
                            mHeaderImageView.setImageURI(uri);
                        }while (cursor.moveToNext());
                    }else {
                        Toast.makeText(this,"no photo",Toast.LENGTH_SHORT).show();
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
        uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 启动系统相机

        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, PHOTO_REQUEST_TAKEPHOTO);
    }


    @Override
    protected void onStart() {
        super.onStart();
        mNameEditText.setText(shared.getString("sharedKeyName",""));
        mPhoneEditText.setText(shared.getString("sharedKeyPhone",""));
        mGenderRadioGroup.check(shared.getInt("sharedKeyGender",R.id.idMainFemaleRB));
        mProvinceSpinner.setSelection(shared.getInt("sharedKeyProvince",5));
        mIsFollowSwitch.setChecked(shared.getBoolean("sharedKeyFollow",false));
        Set<String> sharedKeyHobbies = shared.getStringSet("sharedKeyHobbies", hobbiesSet);
        for (String str : sharedKeyHobbies){
            for (CheckBox checkBox : cbHobbyArr) {
                if (checkBox.getText().equals(str)) {
                    checkBox.setChecked(true);
                }
            }
        }
        hobbyCheckedList = new ArrayList<>();
        hobbyCheckedList.addAll(sharedKeyHobbies);
        select_item = shared.getInt("sharedKeySpecialty",select_item);
        specialtyStr = infoList.get(select_item);
        infoAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = shared.edit();
        editor.putString("sharedKeyName",mNameEditText.getText().toString());
        editor.putString("sharedKeyPhone",mPhoneEditText.getText().toString());
        editor.putInt("sharedKeyGender",mGenderRadioGroup.getCheckedRadioButtonId());
        editor.putBoolean("sharedKeyFollow",mIsFollowSwitch.isChecked());
        for (int i = 0; i < cbHobbyArr.length; i++){
            if (cbHobbyArr[i].isChecked()){
                hobbyCheckedList.add(cbHobbyArr[i].getText().toString());
            }
        }
        hobbiesSet.addAll(hobbyCheckedList);
        editor.putStringSet("sharedKeyHobbies",hobbiesSet);
        editor.putInt("sharedKeyProvince",mProvinceSpinner.getSelectedItemPosition());
        editor.putInt("sharedKeySpecialty",select_item);
        editor.apply();
    }
}