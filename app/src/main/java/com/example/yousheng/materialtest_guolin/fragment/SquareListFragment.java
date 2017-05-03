package com.example.yousheng.materialtest_guolin.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
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
 * Created by yousheng on 17/5/3.
 */

public class SquareListFragment extends Fragment {
    @BindView(R.id.fragment_main_recyclerview)
    XRecyclerView recyclerView;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.progress_wheel)
    ProgressWheel progressWheel;

    private static int mCurrentPage=0;
    private static final int  mLimitPage=10;

    private SquareListAdapter adapter;
    private List<AVObject> mList = new ArrayList<>();
    private int position;

    private static final String COUNT_OF_FRAGMENT = "COUNT_OF_FRAGMENT";

    //活动调用此方法生成新的fragment,并且用setArguments传达数据告诉这个fragment它是第几个
    public static SquareListFragment newInstance(int position) {
        SquareListFragment listFragment = new SquareListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(COUNT_OF_FRAGMENT, position);
        listFragment.setArguments(bundle);
        return listFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            Bundle bundle = getArguments();
            position = bundle.getInt(COUNT_OF_FRAGMENT);
            setRetainInstance(true);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        initView();

        return view;
    }

    private void initView() {
        //首次加载显示的进度条开始旋转
        progressWheel.spin();
        setRecyclerView();
        setXRVrefresh();
        setFloatingButton();
    }
    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void setRecyclerView() {
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
//        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setHasFixedSize(true);
        adapter = new SquareListAdapter(mList,getActivity());
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
        if(position==1){
            avQuery.whereEqualTo("owner", AVUser.getCurrentUser());
        }
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
                } else {
                    e.printStackTrace();
                    TastyToast.makeText(getActivity(), "加载错误： " + e.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
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
        if(position==1){
            avQuery.whereEqualTo("owner", AVUser.getCurrentUser());
        }
        avQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    mList.addAll(list);
                    adapter.notifyDataSetChanged();
                    //隐藏上拉更多
                    recyclerView.loadMoreComplete();
                    //list为空，表示没有更多页了
                    if(list.isEmpty()){
                        showNoNextPage();
                    }
                } else {
                    e.printStackTrace();
                    TastyToast.makeText(getActivity(), "加载错误： " + e.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
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


    private void setFloatingButton() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.smoothScrollToPosition(0);
            }
        });
    }

}
