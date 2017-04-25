package com.example.yousheng.materialtest_guolin.zxing;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.ProgressCallback;
import com.avos.avoscloud.SaveCallback;
import com.example.yousheng.materialtest_guolin.R;
import com.example.yousheng.materialtest_guolin.bean.Spot;
import com.example.yousheng.materialtest_guolin.util.ImageUtil;
import com.example.yousheng.materialtest_guolin.view.BaseActivity;
import com.google.gson.GsonBuilder;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.sdsmdg.tastytoast.TastyToast;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class CreateQRCodeActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.spot_name)
    EditText editTextName;
    @BindView(R.id.button_create_qrcode)
    Button buttonCreateQRCode;
    @BindView(R.id.image_qrcode)
    ImageView imageQRCode;
//    @BindView(R.id.button_qrcode_save_to_gallery)
//    Button buttonSaveQRCode;
    @BindView(R.id.spot_location)
    EditText editLocation;
    @BindView(R.id.spot_info)
    EditText editInfo;
    @BindView(R.id.spot_web_url)
    EditText editWebUrl;
    @BindView(R.id.button_choose_photo)
    Button buttonChoosePhoto;
    @BindView(R.id.imageview_photo_choosed)
    ImageView imagePhtotChoosed;
    @BindView(R.id.progress_wheel)
    ProgressWheel progressWheel;
    @BindView(R.id.progress_bg)
    View viewProgressBg;
    @BindView(R.id.text_progress)
    TextView textProgress;
    @BindView(R.id.spot_pic_url)
    EditText editPicUrl;
//    @BindView(R.id.button_upload_spot_to_server)
//    Button buttonUploadServer;


    Bitmap mBitmap;
    String mName;
    String mLocation;
    String mInfo;
    String mWebUrl;
    String mPicUrl;

    //选择相片的二进制数据,用来上传
    private byte[] mImageBytes = null;
    //读写权限，android6.0以上需要运行时权限
    String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    //请求SD卡权限码
    public static final int REQUEST_EXTERNAL_PERM = 102;
    public static final int REQUEST_CHOOSE_PHOTO = 42;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_qrcode);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        setToolbar();
        //点击生成按钮后，进行复杂的事务逻辑判断步骤1、和生成二维码步骤2
        onClickCreateQRCode();
    }


    /**
     * 以下两个方法是点击生成二维码后的判断与生成
     */
    //1.点击生成按钮后，判断填写条件，若符合则开启生成二维码事务
    private void onClickCreateQRCode() {
        buttonCreateQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Spot spot = new Spot();
//                spot.setPicUrl("http://onxlr7bsm.bkt.clouddn.com/image/jpg/chigua_4.jpg");
//                spot.setName(editTextName.getText().toString());
//                String sQRCode = new GsonBuilder().serializeNulls().create().toJson(spot);
//                Log.d("test1", "onClick: " + sQRCode);
//                editTextName.setText("");
//                editTextName.setHint(R.string.string_enter_spot_name);
//                mBitmap = CodeUtils.createImage(sQRCode, 400, 400, BitmapFactory.decodeResource(getResources(), R.mipmap.icon1));
//                imageQRCode.setImageBitmap(mBitmap);

                editInfo.setError(null);
                editTextName.setError(null);
                editLocation.setError(null);
                editWebUrl.setError(null);
                editPicUrl.setError(null);
                mName = editTextName.getText().toString();
                mLocation = editLocation.getText().toString();
                mWebUrl = editWebUrl.getText().toString();
                mInfo = editInfo.getText().toString();
                mPicUrl = editPicUrl.getText().toString();

                boolean cancel = false;
                View focusView = null;

                if (TextUtils.isEmpty(mPicUrl) || !mPicUrl.contains("http")) {
                    editPicUrl.setError("请点击上传或\n直接填写图片链接");
                    cancel = true;
                    focusView = editPicUrl;
                }

                if (TextUtils.isEmpty(mLocation)) {
                    editLocation.setError("所在地区不能为空");
                    cancel = true;
                    focusView = editLocation;
                }

                if (TextUtils.isEmpty(mName)) {
                    editTextName.setError("地点名不能为空");
                    cancel = true;
                    focusView = editTextName;
                }


                if (cancel) {
                    focusView.requestFocus();
                } else {
                    //填写正确，则显示进度条，并且开始第2步生成二维码
                    showProgress();
                    createQRCode();
                }
            }
        });
    }

    //2.生成二维码：生成spot类后，用zxing生成二维码并显示
    private void createQRCode() {
        Spot spot = new Spot();
        spot.setName(mName);
        spot.setLocation(mLocation);
        spot.setInfo(mInfo);
        spot.setPicUrl(mPicUrl);
        spot.setWebUrl(mWebUrl);
        //转换spot类成json格式
        String sQRCode = new GsonBuilder().serializeNulls().create().toJson(spot);
        Log.d("test1", "onClick: " + sQRCode);
        //生成二维码并显示
        mBitmap = CodeUtils.createImage(sQRCode, 600, 600, BitmapFactory.decodeResource(getResources(), R.mipmap.icon1));
        imageQRCode.setImageBitmap(mBitmap);
    }

    /**
     * 以下三个方法是选择本地相册图片上传，作为mPicUrl
     */
    //点击选择图片的事务
    @OnClick(R.id.button_choose_photo)
    public void choosePhotoUpload() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CHOOSE_PHOTO);
    }

    //将相册中选中的相片显示预览，并且转换成二进制流，启动上传图片方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHOOSE_PHOTO && resultCode == RESULT_OK) {
            try {
                imagePhtotChoosed.setImageBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData()));
                //输入流转换成byte字符串数据
                mImageBytes = ImageUtil.getBytes(getContentResolver().openInputStream(data.getData()));
                //启动上传
                uploadImage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //显示进度圈，进行主图片上传并且自动把返回的url填写进picUrl框内
    private void uploadImage() {
        //显示进度圈
        showProgress();
        //上传图片文件
        final AVFile file = new AVFile("image", mImageBytes);
        file.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                // 成功或失败处理...
                if (e == null) {
                    Log.d("test1", file.getUrl());//返回一个唯一图片的 Url 地址
                    mPicUrl = file.getUrl();
                    editPicUrl.setText(mPicUrl);
                    TastyToast.makeText(CreateQRCodeActivity.this, "成功上传图片", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                } else {
                    TastyToast.makeText(CreateQRCodeActivity.this, "上传错误: " + e.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                }
                //无论上传成功与否，都关闭进度圈
                hideProgress();
            }
        }, new ProgressCallback() {
            @Override
            public void done(Integer integer) {
                // 上传进度数据，integer 介于 0 和 100。
                progressWheel.setProgress(integer);
                textProgress.setText(integer.toString() + "%");
            }
        });
    }

    /**
     * 1.点击保存到相册后调用
     * 2.授权sd卡读写成功后自动调用此方法存储生成的二维码
     */
    @OnClick(R.id.button_qrcode_save_to_gallery)
    @AfterPermissionGranted(REQUEST_EXTERNAL_PERM)
    public void saveLocalQRCode() {
        if (EasyPermissions.hasPermissions(this, perms)) {
            // Have permission, do the thing!拥有扫描的权限,存储二维码
            if (mBitmap == null) {
                TastyToast.makeText(CreateQRCodeActivity.this, "请先生成！", TastyToast.LENGTH_SHORT, TastyToast.CONFUSING);
            } else {
                boolean isSuccess = ImageUtil.saveImageToGallery(CreateQRCodeActivity.this, mBitmap);
                if (isSuccess) {
//                        Toast.makeText(CreateQRCodeActivity.this,"成功保存到/Android/此app包名/cache/pictures中",Toast.LENGTH_LONG).show();
                } else {
                    TastyToast.makeText(CreateQRCodeActivity.this, "保存失败", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                }
            }
        } else {
            // Ask for one permission
            EasyPermissions.requestPermissions(this, "需要请求SD权限",
                    REQUEST_EXTERNAL_PERM, perms);
        }
    }

    /**
     * 点击"上传到云端"按钮调用，上传spot类到云端
     */
    @OnClick(R.id.button_upload_spot_to_server)
    public void uploadToServer(){
        if (mBitmap == null) {
            TastyToast.makeText(CreateQRCodeActivity.this, "请先生成！", TastyToast.LENGTH_SHORT, TastyToast.CONFUSING);
        } else{
            showProgress();
            progressWheel.setProgress(0.9f);
            AVObject avSpot=new AVObject("Spot");
            avSpot.put("name",mName);
            avSpot.put("location",mLocation);
            avSpot.put("info",mInfo);
            avSpot.put("picUrl",mPicUrl);
            avSpot.put("webUrl",mWebUrl);
            avSpot.put("QRCodePic",new AVFile("QRPic",ImageUtil.getBitmapByte(mBitmap)));
            avSpot.put("owner", AVUser.getCurrentUser());
            avSpot.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    if(e==null){
                        TastyToast.makeText(CreateQRCodeActivity.this,"已存至云端",TastyToast.LENGTH_SHORT,TastyToast.SUCCESS);
                    }else {
                        TastyToast.makeText(CreateQRCodeActivity.this,"上传错误： "+e.getMessage(),TastyToast.LENGTH_SHORT,TastyToast.ERROR);
                    }
                    hideProgress();
                }
            });
        }
    }

    //显示进度圈，显示白色半透明遮罩背景，和百分百数字,禁止按钮触控
    private void showProgress() {
        viewProgressBg.setBackgroundColor(getResources().getColor(android.R.color.white));
        viewProgressBg.getBackground().setAlpha(100);
        progressWheel.setVisibility(View.VISIBLE);
        progressWheel.setProgress(0);
        progressWheel.spin();
        textProgress.setVisibility(View.VISIBLE);
        //拦截点击事件
        viewProgressBg.setOnClickListener(null);
    }

    private void hideProgress() {
        viewProgressBg.setVisibility(View.GONE);
    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("");
            TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
            toolbarTitle.setText("生成我的二维码");
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
        Toast.makeText(this, "已允许权限！", Toast.LENGTH_SHORT).show();
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
