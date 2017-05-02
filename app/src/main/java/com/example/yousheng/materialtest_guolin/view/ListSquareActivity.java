package com.example.yousheng.materialtest_guolin.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.example.yousheng.materialtest_guolin.R;
import com.example.yousheng.materialtest_guolin.adapter.SquareListAdapter;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yousheng on 17/5/2.
 */

public class ListSquareActivity extends BaseActivity {
    @BindView(R.id.recycler_square)
    XRecyclerView recyclerView;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.progress_wheel)
    ProgressWheel progressWheel;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private SquareListAdapter adapter;
    private List<AVObject> mList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_square_list);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        //首次加载显示的进度条开始旋转
        progressWheel.spin();
        setToolbar();
        setXRVrefresh();
        setFloatingButton();
        setRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void setRecyclerView() {
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new SquareListAdapter(mList);
        recyclerView.setAdapter(adapter);
    }

    //在onresume或者下拉刷新中初始化数据，
    private void initData() {
        mList.clear();
        AVQuery<AVObject> avQuery = new AVQuery<>("Spot");
        avQuery.orderByDescending("createdAt");
        //include字段表示包含owner字段的pointer的具体内容，即查询结果也包含user的字段name等等
        avQuery.include("owner");
        avQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    mList.addAll(list);
                    adapter.notifyDataSetChanged();
                    //隐藏第一次加载时候的进度圈
                    hideFisrtProgress();
                    //隐藏下拉刷新
                    hideRefresh();
                } else {
                    e.printStackTrace();
                    TastyToast.makeText(ListSquareActivity.this, "加载错误： " + e.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                }
            }
        });
    }

    //隐藏第一次加载时候的进度圈
    private void hideFisrtProgress() {
        if (progressWheel.isSpinning()) {
            progressWheel.stopSpinning();
        }
    }

    //隐藏下拉刷新
    private void hideRefresh() {
        recyclerView.refreshComplete();
    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("");
            TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
            toolbarTitle.setText("广场");
        }
    }

    private void setXRVrefresh() {
        recyclerView.setRefreshProgressStyle(ProgressStyle.LineScale);
        recyclerView.setLoadingMoreProgressStyle(ProgressStyle.LineScale);
        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                initData();
            }

            @Override
            public void onLoadMore() {

            }
        });
    }

    private void setFloatingButton() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.smoothScrollToPosition(0);
            }
        });
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
}
