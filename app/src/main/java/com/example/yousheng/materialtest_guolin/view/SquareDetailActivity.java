package com.example.yousheng.materialtest_guolin.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.GetCallback;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.yousheng.materialtest_guolin.R;
import com.example.yousheng.materialtest_guolin.util.ImageUtil;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by yousheng on 17/5/3.
 */

public class SquareDetailActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks{

    @BindView(R.id.spot_image_view)
    ImageView spotImage;
    @BindView(R.id.spot_content_text)
    TextView spotText;
    @BindView(R.id.toolbar2)
    Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.fab_share)
    FloatingActionButton fabShare;
    @BindView(R.id.scroll_view)
    NestedScrollView scrollView;
    @BindView(R.id.button_qrcode_save_to_gallery)
    Button buttonSave;
    @BindView(R.id.image_qrcode)
    ImageView imageQRCode;

    boolean clickedFabShare = false;
    private Bitmap mBitmap;
    private static final String CLICKED_ITEM_OBJECTID="CLICKED_ITEM_OBJECTID";
    AVObject mAvObject;
    //读写权限，android6.0以上需要运行时权限
    String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    //请求SD卡权限码
    public static final int REQUEST_EXTERNAL_PERM = 101;

    public static void newInstance(Context context,String id){
        Intent intent=new Intent(context,SquareDetailActivity.class);
        intent.putExtra(CLICKED_ITEM_OBJECTID,id);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivty_square_detail);
        ButterKnife.bind(this);
        //除了使用 AVQuery，还可以采用在本地构建一个 AVObject 的方式，通过接口和 objectId 把数据从云端拉取到本地：
        initView();
    }

    //除了使用 AVQuery，还可以采用在本地构建一个 AVObject 的方式，通过接口和 objectId 把数据从云端拉取到本地：
    private void initView() {
        String objectId=getIntent().getStringExtra(CLICKED_ITEM_OBJECTID);
        AVObject object = AVObject.createWithoutData("Spot", objectId);
        object.fetchInBackground("owner", new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                mAvObject=avObject;
                setSpotImage();
                setCollapseAndToolbar();
                setFab();
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
                TastyToast.makeText(SquareDetailActivity.this, "请先生成！", TastyToast.LENGTH_SHORT, TastyToast.CONFUSING);
            } else {
                boolean isSuccess = ImageUtil.saveImageToGallery(SquareDetailActivity.this,mBitmap);
                if (isSuccess) {
//                        Toast.makeText(CreateQRCodeActivity.this,"成功保存到/Android/此app包名/cache/pictures中",Toast.LENGTH_LONG).show();
                } else {
                    TastyToast.makeText(SquareDetailActivity.this, "保存失败", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                }
            }
        } else {
            // Ask for one permission
            EasyPermissions.requestPermissions(this, "需要请求SD权限",
                    REQUEST_EXTERNAL_PERM, perms);
        }
    }


    //设置顶部主图片、文字信息和填充二维码
    private void setSpotImage() {
        Glide.with(this).load(mAvObject.get("picUrl").toString()).into(spotImage);
//        Glide.with(this).load(mAvObject.getAVFile("QRCodePic").getUrl()).into(imageQRCode);
         spotText.setText("名称： "+mAvObject.get("name").toString()+"\n地点： "+mAvObject.get("location").toString()+"\n信息： "+mAvObject.get("info"));

        Glide.with(this).load(mAvObject.getAVFile("QRCodePic").getUrl()).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                imageQRCode.setImageBitmap(resource);
                mBitmap=resource;
            }
        });
    }

    private void setCollapseAndToolbar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbarLayout.setTitle(mAvObject.get("name").toString());
    }


    private void setFab() {
        fabShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //只有登录状态才能用收藏功能
                if(MainActivity.LOGIN_STATE==MainActivity.LOGIN_OUT){
                    TastyToast.makeText(SquareDetailActivity.this,"请先登录",TastyToast.LENGTH_SHORT,TastyToast.WARNING);
                    return;
                }
                if(!clickedFabShare){
                    //若是没收藏状态
                    fabShare.setImageResource(R.drawable.ic_love_selected);
                    clickedFabShare=!clickedFabShare;
                }else {
                    fabShare.setImageResource(R.drawable.ic_love_unselected);
                    clickedFabShare=!clickedFabShare;
                }
            }
        });
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
