package com.example.lessontestapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import com.example.lessontestapp.adapter.EditInfoActivityAdapter;
import com.example.lessontestapp.adapter.MyAdapter;
import com.example.lessontestapp.dao.MyFriendInfo;
import com.example.lessontestapp.dao.MyFriendInfoDao;
import com.example.lessontestapp.dao.MyInfoDao;
import com.example.lessontestapp.dao.MyMajorDao;
import com.example.lessontestapp.db.InfoDBAdapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class EditFriendsInfoActivity extends AppCompatActivity {

    private EditText mEditedInfoNameET, mEditedInfoPhoneET;
    private RadioGroup mEditedGenderRG;
    private RadioButton mEditMaleRB, mEditFemaleRB;
    private CheckBox mEditedSportCB, mEditedTravelCB, mEditedOtherCB;
    private Spinner mEditedProvinceSpinner;
    private ListView mEditedSpecialtyLV;
    private Button  mEditedConfirmBtn, mEditedCancelBtn;
    private ImageView mHeaderImageView;

    //照片文件
    private Uri imageUri;
    private Uri imageUriToFiles;

    private String fileName;

    private int InfoID;

    public  static  int  select_item = -1 ;

    private final String[] provinceArr = {
            "河北省","山西省","辽宁省","吉林省","黑龙江省","江苏省","浙江省","安徽省","福建省",
            "江西省","山东省","河南省","湖北省","湖南省","广东省","海南省","四川省","贵州省",
            "云南省","陕西省","甘肃省","青海省","台湾省"
    };

    List<String> specialtyList = new ArrayList<>();

    CheckBox[] hobbiesArr;

    ArrayAdapter<String> provinceAdapter;

    EditInfoActivityAdapter specialtyAdapter;

    ArrayList<String> modifiedInfoList = new ArrayList<>();

    //数据库实例
    private InfoDBAdapter infoDBAdapter;

    /* 头像文件 */
    private static final String IMAGE_FILE_NAME = "temp_head_image.jpg";

    /* 请求识别码 */
    public static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
    public static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    public static final int PHOTO_REQUEST_CORP = 3;// 结果



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_friends_info);

        //初始化组件
        initWidget();

        //创建数据库实例和打开数据库
        infoDBAdapter = new InfoDBAdapter(this);
        infoDBAdapter.open();

        //获取intent的数据
        Intent intent = getIntent();
        ArrayList<String> editGetInfoData = intent.getStringArrayListExtra("EditInfoData");

        //设置Spinner
        ArrayList<String> provinceList = new ArrayList<>();
        for(int i = 0;i < provinceArr.length;i++){
            provinceList.add(provinceArr[i]);
        }
        provinceAdapter = new ArrayAdapter<>(
                this, R.layout.spinner_item_style, R.id.idSpinnerStyle,provinceList
        );
        mEditedProvinceSpinner.setAdapter(provinceAdapter);

        //设置ListView
//        specialtyList.add("计算机");
//        specialtyList.add("软件工程");
//        specialtyList.add("物联网");
//        specialtyAdapter = new EditInfoActivityAdapter(this,specialtyList);
//        mEditedSpecialtyLV.setAdapter(specialtyAdapter);

        MyMajorDao[] majorDaos = infoDBAdapter.queryMajor();
        for (int i = 0; i<majorDaos.length; i++){
            specialtyList.add(majorDaos[i].getMajorName());
        }
        specialtyAdapter = new EditInfoActivityAdapter(this,specialtyList);
        mEditedSpecialtyLV.setAdapter(specialtyAdapter);

        //infoID
        System.out.println("@@@@@@InfoID:"+editGetInfoData.get(6));
        this.InfoID = Integer.parseInt(editGetInfoData.get(6));

        //姓名
        mEditedInfoNameET.setText(editGetInfoData.get(0));

        //手机
        mEditedInfoPhoneET.setText(editGetInfoData.get(1));
        //性别
        if (editGetInfoData.get(2).equals("男")){
            mEditMaleRB.setChecked(true);
        }
        if (editGetInfoData.get(2).equals("女")){
            mEditFemaleRB.setChecked(true);
        }
        //爱好
        String hobbiesStr = editGetInfoData.get(3);
        hobbiesArr = new CheckBox[]{mEditedSportCB, mEditedTravelCB, mEditedOtherCB};
        hobbiesStr = hobbiesStr.substring(1,hobbiesStr.length()-1);
        hobbiesStr = hobbiesStr.replace(" ", "");
        String[] str = hobbiesStr.split(",");
        for (String s : str) {
            for (CheckBox checkBox : hobbiesArr) {
                if (s.contentEquals(checkBox.getText())) {
                    checkBox.setChecked(true);
                }
            }
        }
        //籍贯
        for (int i = 0; i < provinceArr.length; i++){
            if (provinceArr[i].contentEquals(editGetInfoData.get(4))){
                mEditedProvinceSpinner.setSelection(i,true);
            }
        }
        //专业
        for (int i = 0; i < specialtyList.size(); i++){
            if (specialtyList.get(i).contentEquals(editGetInfoData.get(5))){
                select_item = i;
                specialtyAdapter.notifyDataSetChanged();
            }
        }
        mEditedSpecialtyLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                select_item = position;
                specialtyAdapter.notifyDataSetChanged();
            }
        });

        System.out.println("警察看见"+editGetInfoData.get(7));
        System.out.println(Uri.parse(editGetInfoData.get(7)));
//        System.out.println(Environment.getExternalStorageDirectory().getPath());
//        System.out.println(Environment.getExternalStorageDirectory());
//        System.out.println(getExternalFilesDir(Environment.DIRECTORY_PICTURES));
        mHeaderImageView.setImageURI(Uri.parse(editGetInfoData.get(7)));

//        mHeaderImageView.setImageBitmap(imageBit);
        //ImageView注册快捷菜单
        registerForContextMenu(mHeaderImageView);
    }


    public void updateInfo(){
        MyFriendInfoDao friendInfoDao = new MyFriendInfoDao();
        //姓名
        friendInfoDao.setName(mEditedInfoNameET.getText().toString());
        //手机号
        friendInfoDao.setPhone(mEditedInfoPhoneET.getText().toString());
        //性别
        String genderStr = null;
        for (int i = 0; i < mEditedGenderRG.getChildCount(); i++){
            RadioButton rb = (RadioButton) mEditedGenderRG.getChildAt(i);
            if (rb.isChecked()){
                genderStr = rb.getText().toString();
            }
        }
        friendInfoDao.setGender(genderStr);
        //爱好
        List<String> hobbiesList = new ArrayList<>();
        for (int i = 0; i < hobbiesArr.length; i++){
            if (hobbiesArr[i].isChecked()){
                hobbiesList.add(hobbiesArr[i].getText().toString());
            }
        }
        friendInfoDao.setHobbies(hobbiesList);
        //籍贯
        friendInfoDao.setBirthPlace(mEditedProvinceSpinner.getSelectedItem().toString());
        //专业
        friendInfoDao.setSpecialtyID(select_item+1);
        //头像
        friendInfoDao.setProfile(imageUriToFiles+"");

        //身份ID
        infoDBAdapter.updateFriendsAllBaseInfo(friendInfoDao,this.InfoID);
    }

    public void initWidget(){
        mEditedInfoNameET = findViewById(R.id.idEditInfoNameEditText);
        mEditedInfoPhoneET = findViewById(R.id.idEditInfoPhoneEditText);
        mEditedGenderRG = findViewById(R.id.idEditGenderRadioGroup);
        mEditMaleRB = findViewById(R.id.idEditMaleRB);
        mEditFemaleRB = findViewById(R.id.idEditFemaleRB);
        mEditedProvinceSpinner = findViewById(R.id.idEditInfoProvinceSpinner);
        mEditedSpecialtyLV = findViewById(R.id.idEditInfoListView);
        mEditedConfirmBtn = findViewById(R.id.idEditInfoYesButton);
        mEditedCancelBtn = findViewById(R.id.idEditInfoCancelButton);
        mEditedTravelCB = findViewById(R.id.idEditInfoTripCheckBox);
        mEditedSportCB = findViewById(R.id.idEditInfoSportCheckBox);
        mEditedOtherCB = findViewById(R.id.idEditInfoOtherCheckBox);
        mHeaderImageView = findViewById(R.id.id_edit_info_header_Image_view);
    }

    @SuppressLint("NonConstantResourceId")
    public void onEditClick(View view){
        switch (view.getId()){
            case R.id.idEditInfoYesButton:
                updateInfo();
                Intent intent = new Intent(this,IndexActivity.class);
                startActivity(intent);
                break;
            case R.id.idEditInfoCancelButton:
                Intent intent1 = new Intent(this,IndexActivity.class);
                startActivity(intent1);
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






}