package com.example.yousheng.materialtest_guolin.view;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.yousheng.materialtest_guolin.R;
import com.example.yousheng.materialtest_guolin.bean.Spot;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SpotDetailActivity extends BaseActivity {
    private static final String INFO_OF_SPOT = "info_of_the_spot_to_send_into";
    Spot mSpot;
    boolean clickedFabShare = false;

    @BindView(R.id.spot_image_view) ImageView spotImage;
    @BindView(R.id.spot_content_text) TextView spotText;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.fab_share) FloatingActionButton fabShare;
    @BindView(R.id.fab_up_to_top) FloatingActionButton fabUpToTop;
    @BindView(R.id.webview) WebView webView;
    @BindView(R.id.scroll_view) NestedScrollView scrollView;

    public static void newInstance(Context context, Spot spot) {
        Intent intent = new Intent(context, SpotDetailActivity.class);
        intent.putExtra(INFO_OF_SPOT, spot);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spot_detail);
        ButterKnife.bind(this);

        //获取列表传来的spot数据
        mSpot = (Spot) getIntent().getSerializableExtra(INFO_OF_SPOT);
        initView();
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    private void initView() {
        setSpotImage();
        setCollapseAndToolbar();
        setWebView();
        setFabUpToTop();
        setFabShare();
    }

    private void setFabShare() {
        fabShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    private void setFabUpToTop() {
        fabUpToTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView.smoothScrollTo(0,0);
            }
        });

        //设置滑动监听
        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(!fabUpToTop.isShown()&&(scrollY-oldScrollY)>15){
                    //手指上滑动显示fab
                    fabUpToTop.show();
                }else if((oldScrollY-scrollY)>15){
                    //手指下滑隐藏fab
                    fabUpToTop.hide();
                }

            }
        });
    }

    private void setSpotImage() {
        Glide.with(this).load(mSpot.picUrl).into(spotImage);
        spotText.setText("ereregsgsg234324sdfsdf\nrewrwerfs");
    }

    private void setCollapseAndToolbar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbarLayout.setTitle(mSpot.name);
    }

    private void setWebView() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setAppCacheEnabled(true);
        settings.setSupportZoom(true);
        webView.setWebViewClient(new LoveClient());

        webView.loadUrl("http://qr14.cn/DpKTsu");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getMenuInflater().inflate(R.menu.menu_web, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_refresh:
                webView.reload();
                return true;
            case R.id.action_copy_url:
                ClipData clipData = ClipData.newPlainText("spot_detail_url_copy", webView.getUrl());
                ClipboardManager manager = (ClipboardManager) this.getSystemService(
                        Context.CLIPBOARD_SERVICE);
                manager.setPrimaryClip(clipData);
                Toast.makeText(this,"复制成功", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_open_url:
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                Uri uri = Uri.parse(webView.getUrl());
                intent.setData(uri);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(this,"打开失败！",Toast.LENGTH_SHORT).show();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private class LoveClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url != null) view.loadUrl(url);
            return true;
        }
    }
}
