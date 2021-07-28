package com.example.lessontestapp.fragment;

import android.Manifest;
import android.content.*;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import com.example.lessontestapp.*;
import com.example.lessontestapp.adapter.MyFriendAdapter;
import com.example.lessontestapp.dao.MyFriendInfo;
import com.example.lessontestapp.dao.MyFriendInfoDao;
import com.example.lessontestapp.dao.MyMajorDao;
import com.example.lessontestapp.db.InfoDBAdapter;
import com.example.lessontestapp.service.HintAppCloseService;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FriendsFragment extends Fragment {

    private ListView mFriendListView;
    public  static  int  select_item = 0 ;
    private int infoID;

    public int getInfoID(){
        return this.infoID;
    }


    String followStr = "关注";

    List<MyFriendInfo> friendList = new ArrayList<>();
    List<MyFriendInfoDao> friendDaoList = new ArrayList<>();

    List<MyFriendInfo> friendModifiedList = new ArrayList<>();
    List<String> hobbiesList = new ArrayList<>();

    MyFriendAdapter friendAdpter;
    Map<Integer, List> friendInfoMap = new HashMap<>();

    ArrayList<String> getModifiedDataList;

    private HintAppCloseService hintAppCloseService;

    private Intent hintAppCloseServiceIntent;

    private int phoneEnd1, phoneEnd2;

    private boolean isConnected;

    int delayTime;

    private static final String INNER_STORAGE_FILE_NAME = "innerStorage";
    private static final String OUTER_STORAGE_FILE_NAME = "outerStorage";

    //数据库实例
    public static InfoDBAdapter infoDBAdapter;

    ContentResolver resolver;

    private androidx.appcompat.widget.Toolbar mToolbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_friend_list,container,false);

        //获取ContentResolver对象
        resolver = getActivity().getContentResolver();

        infoDBAdapter = new InfoDBAdapter(view.getContext());
        infoDBAdapter.open();
//        String[] majors = {"计算机","软件工程","物联网"};
//        for (int i = 0; i < majors.length; i++){
//            MyMajorDao majorDao = new MyMajorDao();
//            majorDao.setMajorName(majors[i]);
//            infoDBAdapter.insertMajor(majorDao);
//        }
        MyMajorDao[] majorDaos = infoDBAdapter.queryMajor();
        MyFriendInfoDao[] friendInfoDaos = infoDBAdapter.queryFriendsBaseInfo();


//        hintAppCloseServiceIntent = new Intent(FriendsFragment.this, HintAppCloseService.class);
        hintAppCloseServiceIntent = new Intent();
        hintAppCloseServiceIntent.setClass(getActivity(),HintAppCloseService.class);

        if (friendInfoDaos != null){
            for (int i = 0; i < friendInfoDaos.length; i++){
                MyFriendInfoDao info = new MyFriendInfoDao();
                if (friendInfoDaos[i].getFocus() == 1){
                    info.setName(friendInfoDaos[i].getName());
                    info.setInfoID(friendInfoDaos[i].getInfoID());
                    info.setPhone(friendInfoDaos[i].getPhone());
                    info.setGender(friendInfoDaos[i].getGender());
                    info.setFocus(friendInfoDaos[i].getFocus());
                    info.setHobbies(friendInfoDaos[i].getHobbies());
                    info.setBirthPlace(friendInfoDaos[i].getBirthPlace());
                    info.setSpecialtyID(friendInfoDaos[i].getSpecialtyID());
                    info.setProfile(friendInfoDaos[i].getProfile());
                    info.setPassword(friendInfoDaos[i].getPassword());
                    info.setBalance(friendInfoDaos[i].getBalance());
                    friendDaoList.add(info);
                }
//                friendInfoMap.put(i,friendList);
            }
            for (int j = 0; j < friendInfoDaos.length; j++){
                MyFriendInfoDao info = new MyFriendInfoDao();
                if (friendInfoDaos[j].getFocus() == 0){
                    info.setName(friendInfoDaos[j].getName());
                    info.setInfoID(friendInfoDaos[j].getInfoID());
                    info.setPhone(friendInfoDaos[j].getPhone());
                    info.setGender(friendInfoDaos[j].getGender());
                    info.setFocus(friendInfoDaos[j].getFocus());
                    info.setHobbies(friendInfoDaos[j].getHobbies());
                    info.setBirthPlace(friendInfoDaos[j].getBirthPlace());
                    info.setSpecialtyID(friendInfoDaos[j].getSpecialtyID());
                    info.setProfile(friendInfoDaos[j].getProfile());
                    info.setPassword(friendInfoDaos[j].getPassword());
                    info.setBalance(friendInfoDaos[j].getBalance());
                    friendDaoList.add(info);
                }
            }
        }else {

        }


        friendAdpter = new MyFriendAdapter(getActivity(),friendDaoList);
        mFriendListView = view.findViewById(R.id.idActivityFriendListView);
        mFriendListView.setAdapter(friendAdpter);
        //点击事件
        mFriendListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                select_item = position;
                setModifiedTranmitedData(select_item);

                ArrayList<String> infoDataTranmit = addTranmitedData(select_item);
//                Intent intent = new Intent(FriendList.this, InfoActivityt.class);
                Intent intent = new Intent();
                intent.setClass(getActivity(),InfoActivityt.class);
                intent.putStringArrayListExtra("detailInfo", infoDataTranmit);
                startActivity(intent);
            }

        });
        //长按事件
        mFriendListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("^^^^^^^"+friendDaoList.get(position).getFocus());
                if (friendDaoList.get(position).getFocus() == 1){
                    followStr = "取消关注";
                    friendAdpter.notifyDataSetChanged();
                }
                if (friendDaoList.get(position).getFocus() == 0){
                    followStr = "关注";
                    friendAdpter.notifyDataSetChanged();
                }
                return false;
            }
        });
        //注册快捷菜单
        registerForContextMenu(mFriendListView);

        //toolbar
        mToolbar = view.findViewById(R.id.id_friend_list_toolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Uri uri = Uri.parse("content://edu.dj.myprovider");

                switch (item.getItemId()){
                    case R.id.idAddStranger:
//                        Intent intent = new Intent(FriendList.this, MainActivity.class);
                        Intent intent = new Intent();
                        intent.setClass(getActivity(),MainActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.idShareFriends:
                        resolver.query(uri,null,null,null,null);
//                        Intent intent1 = new Intent(FriendList.this, ClassmateFriendsActivity.class);
                        Intent intent1 = new Intent();
                        intent1.setClass(getActivity(),ClassmateFriendsActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.idInnerStorageImport:
                        System.out.println("￥￥￥￥内部存储导入");
                        try {
                            FileInputStream fis = getActivity().openFileInput(INNER_STORAGE_FILE_NAME);
                            byte[] readBytes = new byte[fis.available()];
                            while (fis.read(readBytes) != -1){

                            }
                            System.out.println(new String(readBytes));
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case R.id.idInnerStorageExport:
                        System.out.println("￥￥￥￥内部存储导出");
                        try {
                            FileOutputStream fos = getActivity().openFileOutput(INNER_STORAGE_FILE_NAME, getActivity().MODE_APPEND);
                            fos.write(friendList.toString().getBytes());
                            fos.flush();
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case R.id.idOuterStorageImport:
                        System.out.println("￥￥￥￥外部存储导入");
                        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                == PackageManager.PERMISSION_GRANTED){
                            if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                                try {
                                    String filename = getActivity().getExternalFilesDir("Music").getCanonicalPath()+"/"+OUTER_STORAGE_FILE_NAME;
                                    FileInputStream fis = new FileInputStream(filename);
                                    byte[] readBytes = new byte[fis.available()];
                                    while (fis.read(readBytes) != -1){

                                    }
                                    System.out.println(new String(readBytes));
                                    fis.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                        }else {
                            //没有授权
                            ActivityCompat.requestPermissions(getActivity(),new String[]{
                                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    1
                            );
                        }
                        break;
                    case R.id.idOuterStorageExport:
                        System.out.println("￥￥￥￥外部存储导出");
                        //已经授权
                        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                == PackageManager.PERMISSION_GRANTED){
                            if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                                try {
                                    String filename = getActivity().getExternalFilesDir("Music").getCanonicalPath()+"/"+OUTER_STORAGE_FILE_NAME;
                                    FileOutputStream fos = new FileOutputStream(filename);
                                    fos.write(friendList.toString().getBytes());
                                    fos.flush();
                                    fos.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                        }else {
                            //没有授权
                            ActivityCompat.requestPermissions(getActivity(),new String[]{
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                        }
                        break;
                }
                return false;
            }
        });
        return view;
    }

    //添加传递的数据
    public ArrayList<String> addTranmitedData(int select_item){
        ArrayList<String> infoDataTranmitList = new ArrayList<>();
        MyMajorDao[] majorDaos = infoDBAdapter.queryMajor();
        infoDataTranmitList.add(friendDaoList.get(select_item).getName());
        infoDataTranmitList.add(friendDaoList.get(select_item).getPhone());
        infoDataTranmitList.add(friendDaoList.get(select_item).getGender());
        infoDataTranmitList.add(friendDaoList.get(select_item).getHobbies().toString());
        infoDataTranmitList.add(friendDaoList.get(select_item).getBirthPlace());
        for (int i = 0; i < majorDaos.length; i++){
            if (majorDaos[i].getId() == friendDaoList.get(select_item).getSpecialtyID()){
                infoDataTranmitList.add(majorDaos[i].getMajorName());
            }
        }
        infoDataTranmitList.add(String.valueOf(friendDaoList.get(select_item).getInfoID()));
        infoDataTranmitList.add(friendDaoList.get(select_item).getProfile());

        return infoDataTranmitList;
    }
    //设置修改后的数据
    public void setModifiedTranmitedData(int select_item){
        Intent intent = getActivity().getIntent();
        boolean flag = false;
        getModifiedDataList = intent.getStringArrayListExtra("ModifiedData");
        if (getModifiedDataList != null){
            int infoID = Integer.parseInt(getModifiedDataList.get(6));
            int infoID2 = friendList.get(select_item).getInfoID();
            if (infoID == infoID2){
                flag = true;
            }
        }
        if (flag){
            //获取修改后的姓名
            friendList.get(select_item).setName(getModifiedDataList.get(0));
            //获取修改后的手机号
            friendList.get(select_item).setPhone(getModifiedDataList.get(1));
            //获取修改后的性别
            friendList.get(select_item).setGender(getModifiedDataList.get(2));
            //获取修改后的爱好
            String str = getModifiedDataList.get(3);
            ArrayList<String> strList = new ArrayList<>();
            str = str.substring(1,str.length()-1);
            str = str.replace(" ","");
            String[] strArr = str.split(",");
            for (String s : strArr){
                strList.add(s);
            }
            friendList.get(select_item).setHobbies(strList);
            //获取修改后的籍贯
            friendList.get(select_item).setBirthPlace(getModifiedDataList.get(4));
            //获取修改后的专业
            friendList.get(select_item).setSpecialty(getModifiedDataList.get(5));

        }
    }

//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        setIntent(intent);
//        getModifiedDataList = intent.getStringArrayListExtra("ModifiedData");
//    }

    //快捷菜单
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(0,1,0,"删除");
        menu.add(0,2,0,"编辑");
        menu.add(0,3,0,followStr);
        menu.add(0,4,0,"呼叫");
        super.onCreateContextMenu(menu, v, menuInfo);
    }
    //快捷菜单选择事件
    @Override
    public boolean onContextItemSelected(@NonNull final MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int position = info.position;
        System.out.println("position:"+position);
        switch (item.getItemId()){
            case 1:
                System.out.println(friendDaoList.get(position).getPhone());
                infoDBAdapter.deleteBaseInfo(friendDaoList.get(position).getPhone());
                friendDaoList.remove(position);
                friendAdpter.notifyDataSetChanged();
                break;
            case 2:         //编辑页面
                select_item = position;
                ArrayList<String> infoDataTranmit = addTranmitedData(select_item);
//                Intent intent = new Intent(FriendList.this, EditFriendsInfoActivity.class);
                Intent intent = new Intent();
                intent.setClass(getActivity(),EditFriendsInfoActivity.class);
                System.out.println(infoDataTranmit);
                intent.putStringArrayListExtra("EditInfoData",infoDataTranmit);
                startActivity(intent);
                break;
            case 3:
                select_item = position;
                if (friendDaoList.get(select_item).getFocus() == 0){
                    this.infoID = friendDaoList.get(select_item).getInfoID();
                    //获取关注好友的手机号后两位
                    String phoneStr = friendDaoList.get(select_item).getPhone();
                    String str = phoneStr.substring(phoneStr.length()-2,phoneStr.length());
                    phoneEnd1 = Integer.parseInt(str.substring(0,str.length()-1));
                    phoneEnd2 = Integer.parseInt(str.substring(1,str.length()));
                    delayTime = hintAppCloseService.sumEndTwoNum(phoneEnd1,phoneEnd2);
                    //关注置顶
                    friendDaoList.get(select_item).setFocus(1);
                    infoDBAdapter.updateFocusBaseInfo(friendDaoList.get(select_item),friendDaoList.get(select_item).getInfoID());
                    friendDaoList.add(0,friendDaoList.get(select_item));
                    friendDaoList.remove(select_item+1);
                }else {
                    friendDaoList.get(select_item).setFocus(0);
                    infoDBAdapter.updateFocusBaseInfo(friendDaoList.get(select_item),friendDaoList.get(select_item).getInfoID());
                    friendDaoList.add(friendDaoList.size(),friendDaoList.get(select_item));
                    friendDaoList.remove(friendDaoList.get(select_item));
                }
                friendAdpter.notifyDataSetChanged();
                break;
            case 4:
                //跳转到拨号界面，同时传递电话号码
                select_item = position;
                String phoneStr = friendDaoList.get(select_item).getPhone();
                Intent intentCall =  new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneStr));
                startActivity(intentCall);
                break;
        }
        return false;
    }

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            HintAppCloseService.MyBinder myBinder= (HintAppCloseService.MyBinder) service;
            hintAppCloseService = myBinder.getService();
            isConnected = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isConnected = false;
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        getActivity().bindService(hintAppCloseServiceIntent,conn, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onStop() {
        super.onStop();
        infoDBAdapter.close();
    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//
//        //延时1秒提示"程序已经关闭"
//        hintAppCloseService.cueAppClosed();
//        //解绑服务
//        getActivity().unbindService(conn);
//        //开启服务
//        Intent intent = new Intent(getActivity(), HintAppCloseService.class);
//
//        getActivity().startService(intent);
//
//    }
}
