package com.example.yousheng.materialtest_guolin.zxing;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.yousheng.materialtest_guolin.R;
import com.example.yousheng.materialtest_guolin.view.BaseActivity;
import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 自行定制扫描二维码的布局界面
 */
public class CaptureMadeByUsActivity extends BaseActivity {
    private CaptureFragment captureFragment;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.button_flashlight)
    ImageButton buttonFlash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_made_by_us);
        ButterKnife.bind(this);
        captureFragment = new CaptureFragment();
        // 为二维码扫描界面设置定制化界面
        CodeUtils.setFragmentArgs(captureFragment, R.layout.my_camera);
        captureFragment.setAnalyzeCallback(analyzeCallback);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_my_container, captureFragment).commit();

        initView();
    }

    private void initView() {
        setToolbar();
        setButtonFlashlight();
    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("");
            TextView toolbarTitle= (TextView) toolbar.findViewById(R.id.toolbar_title);
            toolbarTitle.setText("扫一扫");
        }
    }

    public static boolean isOpen = false;

    private void setButtonFlashlight() {
        buttonFlash.getBackground().setAlpha(120);
        buttonFlash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isOpen) {
                    CodeUtils.isLightEnable(true);
                    isOpen = true;
                    buttonFlash.getBackground().setAlpha(190);
                    buttonFlash.setBackgroundResource(R.drawable.ic_flashlight_off);
                } else {
                    CodeUtils.isLightEnable(false);
                    isOpen = false;
                    buttonFlash.setBackgroundResource(R.drawable.ic_flashlight_on);
                    buttonFlash.getBackground().setAlpha(120);
                }
            }
        });
    }


    /**
     * 二维码解析回调函数
     */
    CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
        @Override
        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_SUCCESS);
            bundle.putString(CodeUtils.RESULT_STRING, result);
            resultIntent.putExtras(bundle);
            CaptureMadeByUsActivity.this.setResult(RESULT_OK, resultIntent);
            CaptureMadeByUsActivity.this.finish();
        }

        @Override
        public void onAnalyzeFailed() {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_FAILED);
            bundle.putString(CodeUtils.RESULT_STRING, "");
            resultIntent.putExtras(bundle);
            CaptureMadeByUsActivity.this.setResult(RESULT_OK, resultIntent);
            CaptureMadeByUsActivity.this.finish();
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
