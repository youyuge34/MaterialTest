package com.example.yousheng.materialtest_guolin.view;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVUser;
import com.example.yousheng.materialtest_guolin.R;
import com.example.yousheng.materialtest_guolin.adapter.ViewpagerAdapter;
import com.example.yousheng.materialtest_guolin.bean.Spot;
import com.example.yousheng.materialtest_guolin.util.ImageUtil;
import com.example.yousheng.materialtest_guolin.zxing.CaptureMadeByUsActivity;
import com.example.yousheng.materialtest_guolin.zxing.CreateQRCodeActivity;
import com.google.gson.Gson;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.main_pager_tabs)
    TabLayout tabLayout;
    @BindView(R.id.main_viewpager)
    ViewPager viewPager;
//    @BindView(R.id.button_4_login)
//    MenuItem itemLogin;
//    @BindView(R.id.mail)
//    TextView textMail;
//    @BindView(R.id.username)
//    TextView textUsername;
MenuItem itemLogin;
    TextView textMail;
    TextView textUsername;


    private static final int REQUEST_CODE = 0;
    private static final int REQUEST_IMAGE = 1;
    public static final int PAGE_COUNT = 3;
    //请求CAMERA权限码
    public static final int REQUEST_CAMERA_PERM = 101;
    public static  int LOGIN_STATE=2;
    public static final int LOGIN_IN=4;
    public static final int LOGIN_OUT=5;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
    }

    //在onResume中判断是否用户已经登录，若是登录了则设置drawlayout_menu相关text，且按钮text变注销
    //若是没登录，则设置drawlayout_menu相关text，且按钮text变登录
    @Override
    protected void onResume() {
        super.onResume();
        isLoginIn();
    }

    private void initView() {
        setToolbar();
        setNavigation();
        setViewPager();
    }

    private void setNavigation() {
        //在drawer抽屉中建立一个navigation的view，此view包含一个header布局和一个menu
        NavigationView navigationMenu = (NavigationView) findViewById(R.id.nav_view);
        itemLogin=navigationMenu.getMenu().getItem(4);
        textMail= (TextView) navigationMenu.getHeaderView(0).findViewById(R.id.mail);
        textUsername=(TextView) navigationMenu.getHeaderView(0).findViewById(R.id.username);
        navigationMenu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                setZXing(item.getItemId());
                drawerLayout.closeDrawers();
                return true;
            }
        });
    }

    private void setToolbar() {
        //设置toolbar
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //显示导航按钮
            actionBar.setDisplayHomeAsUpEnabled(true);
            //设置图标
            actionBar.setHomeAsUpIndicator(R.drawable.ic_action_show_navigation);
        }
    }

    private void setViewPager() {
        viewPager.setOffscreenPageLimit(PAGE_COUNT);
        ViewpagerAdapter adapter = new ViewpagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    //回到页面或者点击注销 都会调用，来判断是否已经登录，从而改变登录状态码和侧栏文字
    private void isLoginIn() {
        //若是已经登录了
        if(AVUser.getCurrentUser()!=null){
            //改变状态变量
            LOGIN_STATE=LOGIN_IN;
            itemLogin.setTitle("注销");
            textMail.setText(AVUser.getCurrentUser().getEmail());
            textUsername.setText(AVUser.getCurrentUser().getUsername());

        }else {
            //未登录状态
            LOGIN_STATE=LOGIN_OUT;
            itemLogin.setTitle("登录");
            textMail.setText("请先登录");
            textUsername.setText("");
        }
    }

    //设置navi子item的点击事件（扫一扫、登录注销等等）
    private void setZXing(int itemId) {
        switch (itemId) {
            case R.id.home_page:
                break;
            case R.id.button_1_capture:
                cameraTask();
                break;
            case R.id.button_2_from_gallery:
                cameraFromGalleryTask();
                break;
            case R.id.button_3_create_capture:
                cameraCreateQRCode();
                break;
            case R.id.button_4_login:
                login();
        }
    }

    //扫一扫
    @AfterPermissionGranted(REQUEST_CAMERA_PERM)
    public void cameraTask() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA)) {
            // Have permission, do the thing!拥有扫描的权限,启动扫一扫
            Intent intent = new Intent(getApplication(), CaptureMadeByUsActivity.class);
            startActivityForResult(intent, REQUEST_CODE);
        } else {
            // Ask for one permission
            EasyPermissions.requestPermissions(this, "需要请求camera权限",
                    REQUEST_CAMERA_PERM, Manifest.permission.CAMERA);
        }
    }

    //从相册识别二维码
    private void cameraFromGalleryTask() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    //开启生成二维码的应用
    private void cameraCreateQRCode() {
//        if(LOGIN_STATE==LOGIN_IN) {
            Intent intent = new Intent(this, CreateQRCodeActivity.class);
            startActivity(intent);
//        }else {
//            Toast.makeText(this,"请先登录！",Toast.LENGTH_SHORT).show();
//        }
    }

    //处理具体的登录注销操作
    private void login() {
        switch (LOGIN_STATE){
            //已经登录的状态，点击按钮是注销
            case LOGIN_IN:
                AVUser.getCurrentUser().logOut();
                //注销后刷新判断登录状态
                isLoginIn();
                break;
            //注销状态，点击按钮启动登录活动
            case LOGIN_OUT:

                break;
        }
    }

    //处理二维码返回结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /**
         * 处理二维码扫描结果
         */
        if (requestCode == REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    Spot spotTemp = new Gson().fromJson(result, Spot.class);
//                    Toast.makeText(this, "解析结果:" + spotTemp.getName(), Toast.LENGTH_LONG).show();
                    SpotDetailActivity.newInstance(this, spotTemp);
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(MainActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }
        /**
         * 选择gallery图片并解析
         */
        else if(requestCode == REQUEST_IMAGE){
            if (data != null) {
                Uri uri = data.getData();
                try {
                    CodeUtils.analyzeBitmap(ImageUtil.getImageAbsolutePath(this, uri), new CodeUtils.AnalyzeCallback() {
                        @Override
                        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
//                            Toast.makeText(MainActivity.this, "解析结果:" + result, Toast.LENGTH_LONG).show();
                            Spot spotTemp = new Gson().fromJson(result, Spot.class);
                            SpotDetailActivity.newInstance(MainActivity.this, spotTemp);
                        }

                        @Override
                        public void onAnalyzeFailed() {
                            Toast.makeText(MainActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        else if (requestCode == REQUEST_CAMERA_PERM) {
            Toast.makeText(this, "从设置页面返回...", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                Toast.makeText(this, "delete", Toast.LENGTH_SHORT).show();
                break;

            case R.id.setting:
                Toast.makeText(this, "setting", Toast.LENGTH_SHORT).show();
                break;

            case R.id.star:
                Toast.makeText(this, "star", Toast.LENGTH_SHORT).show();
                break;

            //点击到导航图标后
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);

        }
        return true;
    }


    /**
     * EsayPermissions接管权限处理逻辑
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Toast.makeText(this, "权限已允许!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this, "当前App需要申请camera权限,需要打开设置页面么?")
                    .setTitle("权限申请")
                    .setPositiveButton("确认")
                    .setNegativeButton("取消", null /* click listener */)
                    .setRequestCode(REQUEST_CAMERA_PERM)
                    .build()
                    .show();
        }
    }

}
