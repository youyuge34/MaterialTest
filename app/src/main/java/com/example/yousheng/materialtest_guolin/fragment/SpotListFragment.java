package com.example.yousheng.materialtest_guolin.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yousheng on 17/4/11.
 */

public class SpotListFragment extends Fragment implements IListFragment{

    @BindView(R.id.swipe_refresh) SwipeRefreshLayout refreshLayout;
    @BindView(R.id.fragment_main_recyclerview) RecyclerView recyclerView;

    private static final String COUNT_OF_FRAGMENT="count_of_this_fragment";

    private SpotAdapter adapter;
    private IListPresenter listPresenter;
    private int position;

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
        refreshLayout.setRefreshing(true);
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
        setRefreshLayout();
    }

    private void setRecyclerView() {
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
    }

    private void setRefreshLayout() {
        refreshLayout.setColorSchemeResources(R.color.navigationBarColor);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    listPresenter.getSpotList();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void showSpots(List<Spot> spots) {
        adapter=new SpotAdapter(spots);
        setRecyclerView();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void showProgressBar() {
//        recyclerView.setVisibility(View.INVISIBLE);
        refreshLayout.setRefreshing(true);
    }

    @Override
    public void hideProgressBar() {
        refreshLayout.setRefreshing(false);
        recyclerView.setVisibility(View.VISIBLE);
    }
}
