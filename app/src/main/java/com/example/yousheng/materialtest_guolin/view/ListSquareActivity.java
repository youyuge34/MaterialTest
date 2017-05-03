package com.example.yousheng.materialtest_guolin.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
    private static int mCurrentPage=0;
    private static final int  mLimitPage=10;

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
        setRecyclerView();
        setXRVrefresh();
        setFloatingButton();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void setRecyclerView() {
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
//        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setHasFixedSize(true);
        adapter = new SquareListAdapter(mList,this);
        recyclerView.setAdapter(adapter);
    }

    //设置下拉刷新和上拉调用的方法
    private void setXRVrefresh() {
        recyclerView.setRefreshProgressStyle(ProgressStyle.LineScale);
        recyclerView.setLoadingMoreProgressStyle(ProgressStyle.LineScale);
        recyclerView.setLoadingMoreEnabled(true);
        recyclerView.setPullRefreshEnabled(true);
        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                initData();
            }

            @Override
            public void onLoadMore() {
                loadNextPage();
            }
        });
    }

    //在onresume或者下拉刷新中初始化数据，
    private void initData() {
        //允许上拉加载下一页
        recyclerView.setLoadingMoreEnabled(true);
        //页码归零
        mCurrentPage=0;

        AVQuery<AVObject> avQuery = new AVQuery<>("Spot");
        avQuery.orderByDescending("createdAt");
        avQuery.limit(mLimitPage);
        //include字段表示包含owner字段的pointer的具体内容，即查询结果也包含user的字段name等等
        avQuery.include("owner");
        avQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    mList.clear();
                    mList.addAll(list);
                    adapter.notifyDataSetChanged();
                    //隐藏第一次加载时候的进度圈
                    hideFisrtProgress();
                    //隐藏下拉刷新
                    hideRefresh();
                    //若是初始值个数少，没有下一页，直接禁止上拉更多
//                    if(list.size()<mLimitPage){
//                        showNoNextPage();
//                    }
                } else {
                    e.printStackTrace();
                    TastyToast.makeText(ListSquareActivity.this, "加载错误： " + e.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                }
            }
        });
    }

    private void loadNextPage() {
        mCurrentPage+=mLimitPage;
        AVQuery<AVObject> avQuery = new AVQuery<>("Spot");
        avQuery.orderByDescending("createdAt");
        avQuery.skip(mCurrentPage);
        avQuery.limit(mLimitPage);
        //include字段表示包含owner字段的pointer的具体内容，即查询结果也包含user的字段name等等
        avQuery.include("owner");
        avQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    mList.addAll(list);
                    adapter.notifyDataSetChanged();
                    //隐藏上拉更多
                    recyclerView.loadMoreComplete();
                    //没有更多页了
                    if(list.isEmpty()){
                        showNoNextPage();
                    }
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

    //没有更多页了，关闭上拉加载，snackbar提示
    public void showNoNextPage() {
        recyclerView.setLoadingMoreEnabled(false);
        //snackbar比起toast多一个按钮,传入的第一个参数为界面布局任意一个view，snackbar会自动查找最外布局来展示
        Snackbar snackbar = Snackbar.make(recyclerView, "兄弟，没有更多数据了！", Snackbar.LENGTH_LONG).setAction("知道啦", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getActivity(), "data restored", Toast.LENGTH_SHORT).show();
//                recyclerView.smoothScrollToPosition(0);
            }
        });
        View view = snackbar.getView();//获取Snackbar的view
        if (view != null) {
            view.setBackgroundColor(getResources().getColor(R.color.fabBlue));//修改view的背景色
            ((TextView) view.findViewById(R.id.snackbar_text)).setTextColor(getResources().getColor(android.R.color.white));//获取Snackbar的message控件，修改字体颜色
        }
        snackbar.show();
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
