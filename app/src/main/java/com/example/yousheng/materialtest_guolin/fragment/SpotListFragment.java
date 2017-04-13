package com.example.yousheng.materialtest_guolin.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yousheng.materialtest_guolin.R;
import com.example.yousheng.materialtest_guolin.adapter.SpotAdapter;
import com.example.yousheng.materialtest_guolin.bean.Spot;
import com.example.yousheng.materialtest_guolin.presenter.IListPresenter;
import com.example.yousheng.materialtest_guolin.presenter.ListPresenter;
import com.example.yousheng.materialtest_guolin.view.IListFragment;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yousheng on 17/4/11.
 */

public class SpotListFragment extends Fragment implements IListFragment{

    @BindView(R.id.fragment_main_recyclerview) XRecyclerView recyclerView;

    private static final String COUNT_OF_FRAGMENT="count_of_this_fragment";

    private SpotAdapter adapter;
    private IListPresenter listPresenter;
    private int position;
    private int mCrurrentPage=1;
    List<Spot> mList=new ArrayList<>();

    //活动调用此方法生成新的fragment,并且用setArguments传达数据告诉这个fragment它是第几个
    public static SpotListFragment newInstance(int postion) {
        SpotListFragment listFragment=new SpotListFragment();
        Bundle bundle=new Bundle();
        bundle.putInt(COUNT_OF_FRAGMENT,postion);
        listFragment.setArguments(bundle);
        return listFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listPresenter=new ListPresenter(this);

        if(savedInstanceState==null){
            Bundle bundle=getArguments();
            position=bundle.getInt(COUNT_OF_FRAGMENT);

            setRetainInstance(true);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this,view);
        initView(view);
        return view;
    }

    //在resume()中就让p层给我数据，并且显示出来
    @Override
    public void onResume() {
        super.onResume();
        try {
            listPresenter.getSpotList();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //销毁同时让p层把对v层的引用置空，防止内存泄漏
    @Override
    public void onDestroy() {
        super.onDestroy();
        listPresenter.onDestroy();
    }

    private void initView(View view) {
        setXRVrefresh();
    }

    private void setXRVrefresh() {
        recyclerView.setRefreshProgressStyle(ProgressStyle.LineScale);
        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                try {
                    mCrurrentPage=1;
                    listPresenter.getSpotList();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onLoadMore() {
                mCrurrentPage++;
                listPresenter.loadNextPage(mCrurrentPage);
            }
        });
    }

    private void setRecyclerView() {
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
    }


    @Override
    public void showSpots(List<Spot> spots) {
        //将首页信息赋予全局变量，以便上拉加载下一页的时候好往里面add
        mList=spots;
        adapter=new SpotAdapter(mList);
        setRecyclerView();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void showProgressBar() {
        //已经没有必要

    }

    @Override
    public void hideProgressBar() {
        recyclerView.refreshComplete();
        recyclerView.setVisibility(View.VISIBLE);
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
        Snackbar.make(recyclerView, "兄弟，没有更多数据了！", Snackbar.LENGTH_SHORT).setAction("知道啦", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getActivity(), "data restored", Toast.LENGTH_SHORT).show();
            }
        }).show();
    }
}
