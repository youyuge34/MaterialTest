package com.example.yousheng.materialtest_guolin.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ProgressBar;

import com.example.yousheng.materialtest_guolin.R;
import com.example.yousheng.materialtest_guolin.util.GlideImageLoader;
import com.example.yousheng.materialtest_guolin.adapter.SpotAdapter;
import com.example.yousheng.materialtest_guolin.adapter.onRecyclerViewItemClicked;
import com.example.yousheng.materialtest_guolin.bean.Spot;
import com.example.yousheng.materialtest_guolin.presenter.IListPresenter;
import com.example.yousheng.materialtest_guolin.presenter.ListPresenter;
import com.example.yousheng.materialtest_guolin.view.IListFragment;
import com.example.yousheng.materialtest_guolin.view.SpotDetailActivity;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yousheng on 17/4/11.
 */

public class SpotListFragment extends Fragment implements IListFragment {

    @BindView(R.id.fragment_main_recyclerview) XRecyclerView recyclerView;
    @BindView(R.id.progress_first_page) ProgressBar progressFirstPage;
    @BindView(R.id.fab) FloatingActionButton fab;

    private static final String COUNT_OF_FRAGMENT = "count_of_this_fragment";

    private SpotAdapter adapter;
    private IListPresenter listPresenter;
    private int position;
    private int mCurrentPage = 1;
    List<Spot> mList = new ArrayList<>();
    Banner banner;

    //活动调用此方法生成新的fragment,并且用setArguments传达数据告诉这个fragment它是第几个
    public static SpotListFragment newInstance(int position) {
        SpotListFragment listFragment = new SpotListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(COUNT_OF_FRAGMENT, position);
        listFragment.setArguments(bundle);
        return listFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listPresenter = new ListPresenter(this);

        if (savedInstanceState == null) {
            Bundle bundle = getArguments();
            position = bundle.getInt(COUNT_OF_FRAGMENT);

            setRetainInstance(true);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        initView(view);
        //在resume()中就让p层给我数据，并且显示出来
        try {
            listPresenter.getSpotList();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    //销毁同时让p层把对v层的引用置空，防止内存泄漏
    @Override
    public void onDestroy() {
        super.onDestroy();
        listPresenter.onDestroy();
    }

    private void initView(View view) {
        setXRVrefresh();
        setBanner();
        setFloatingButton();
    }

    private void setXRVrefresh() {
        recyclerView.setRefreshProgressStyle(ProgressStyle.LineScale);
        recyclerView.setLoadingMoreProgressStyle(ProgressStyle.LineScale);
        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                try {
                    mCurrentPage = 1;
                    listPresenter.getSpotList();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onLoadMore() {
                mCurrentPage++;
                listPresenter.loadNextPage(mCurrentPage);
            }
        });
    }

    private void setBanner() {
        banner = new Banner(getActivity());

        //获取屏幕高度用来设定banner的height
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        banner.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (dm.heightPixels/3.7)));
        banner.setBannerStyle(BannerConfig.NUM_INDICATOR);
        banner.setBannerAnimation(Transformer.DepthPage);
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                SpotDetailActivity.newInstance(getActivity(),mList.get(position));
                getActivity().overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }
        });

        //为xrecyclerview添加banner作为头布局
        recyclerView.addHeaderView(banner);

    }

    //获得返回的数据后，设置layoutmanger与子item的点击事件
    private void setRecyclerView() {
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        //实现子item点击事件的接口回调,根据点击的位置，获取相应的spot实例数据后启动详情页activity
        adapter.setOnRecyclerViewItemClickedListener(new onRecyclerViewItemClicked() {
            @Override
            public void onClicked(int position) {
                SpotDetailActivity.newInstance(getActivity(),mList.get(position-2));
                getActivity().overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }
        });
    }

    //首页第一次加载完毕后让进度圈消失，以后再也不用出现了
    private void setProgressFirstPageOff() {
        if(progressFirstPage.isShown()){
            progressFirstPage.setVisibility(View.GONE);
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
    public void showRecycler(List<Spot> spots) {
        //将首页信息赋予全局变量，以便上拉加载下一页的时候好往里面add
        mList = spots;
        adapter = new SpotAdapter(mList);

        //设置layoutmanger与子item的点击事件
        setRecyclerView();
        recyclerView.setAdapter(adapter);

        //第一次加载首页的时候显示进度圈，加载完毕后让进度圈消失，以后刷新加载不用它了，我们有下拉刷新
        setProgressFirstPageOff();
    }

    @Override
    public void hideProgressBar() {
        //刷新的时候需要关闭下拉刷新
        recyclerView.refreshComplete();
    }

    @Override
    public int getFragmentPosition() {
        return position;
    }

    @Override
    public void showNextPage(List<Spot> spots) {
        recyclerView.setLoadingMoreEnabled(true);
        mList.addAll(spots);
        adapter.notifyDataSetChanged();
        recyclerView.loadMoreComplete();
    }

    @Override
    public void showNoNextPage() {
        recyclerView.setLoadingMoreEnabled(false);
        //snackbar比起toast多一个按钮,传入的第一个参数为界面布局任意一个view，snackbar会自动查找最外布局来展示
        Snackbar.make(recyclerView, "兄弟，没有更多数据了！", Snackbar.LENGTH_LONG).setAction("知道啦", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getActivity(), "data restored", Toast.LENGTH_SHORT).show();
//                recyclerView.smoothScrollToPosition(0);
            }
        }).show();
    }

    @Override
    public void showBanner(List<Spot> spots) {
        int pageCount=spots.size()>=5?5:spots.size();
        List<String> picUrls=new ArrayList<>();
        List<String> picTitles=new ArrayList<>();
        for(int i=0;i<pageCount;i++){
            picUrls.add(spots.get(i).picUrl);
        }
        for(int i=0;i<pageCount;i++){
            picTitles.add(spots.get(i).name);
        }

        banner.setImages(picUrls)
                .setBannerTitles(picTitles)
                .setImageLoader(new GlideImageLoader())
                .start();
    }
}
