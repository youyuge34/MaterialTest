package com.example.yousheng.materialtest_guolin.zxing;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.yousheng.materialtest_guolin.R;
import com.example.yousheng.materialtest_guolin.bean.Spot;
import com.example.yousheng.materialtest_guolin.util.ImageUtil;
import com.example.yousheng.materialtest_guolin.view.BaseActivity;
import com.google.gson.GsonBuilder;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class CreateQRCodeActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks{
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.edittext_spot_name)
    EditText editTextName;
    @BindView(R.id.button_create_qrcode)
    Button buttonCreateQRCode;
    @BindView(R.id.image_qrcode)
    ImageView imageQRCode;
    @BindView(R.id.button_qrcode_save_to_gallery)
    Button buttonSaveQRCode;
    Bitmap mBitmap;
    //读写权限，android6.0以上需要运行时权限
    String[] perms={Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};
    //请求SD卡权限码
    public static final int REQUEST_EXTERNAL_PERM = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_qrcode);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        setToolbar();
        buttonCreateQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Spot spot=new Spot();
                spot.setPicUrl("http://onxlr7bsm.bkt.clouddn.com/image/jpg/chigua_4.jpg");
                spot.setName(editTextName.getText().toString());
                String sQRCode= new GsonBuilder().serializeNulls().create().toJson(spot);
                Log.d("test1", "onClick: "+sQRCode);
                editTextName.setText("");
                editTextName.setHint(R.string.string_enter_spot_name);
                mBitmap = CodeUtils.createImage(sQRCode, 400, 400, BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
                imageQRCode.setImageBitmap(mBitmap);
            }
        });

        buttonSaveQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveQRCode();
            }
        });
    }

    //授权成功后自动调用此方法
    @AfterPermissionGranted(REQUEST_EXTERNAL_PERM)
    private void saveQRCode() {
        if (EasyPermissions.hasPermissions(this,perms)) {
            // Have permission, do the thing!拥有扫描的权限,存储二维码
            if(mBitmap==null){
                Toast.makeText(CreateQRCodeActivity.this,"请先点击生成！",Toast.LENGTH_SHORT).show();
            }else {
                boolean isSuccess= ImageUtil.saveImageToGallery(CreateQRCodeActivity.this,mBitmap);
                if(isSuccess){
//                        Toast.makeText(CreateQRCodeActivity.this,"成功保存到/Android/此app包名/cache/pictures中",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(CreateQRCodeActivity.this,"保存失败！",Toast.LENGTH_LONG).show();
                }
            }
        } else {
            // Ask for one permission
            EasyPermissions.requestPermissions(this, "需要请求SD权限",
                    REQUEST_EXTERNAL_PERM, perms);
        }
    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Toast.makeText(this,"已允许权限！",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this, "当前App需要申请写SD卡权限,需要打开设置页面么?")
                    .setTitle("权限申请")
                    .setPositiveButton("确认")
                    .setNegativeButton("取消", null /* click listener */)
                    .setRequestCode(REQUEST_EXTERNAL_PERM)
                    .build()
                    .show();
        }
    }
}
