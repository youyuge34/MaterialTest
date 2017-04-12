package com.example.yousheng.materialtest_guolin.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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

    @BindView(R.id.drawer_layout)  DrawerLayout drawerLayout;
    @BindView(R.id.swipe_refresh) SwipeRefreshLayout refreshLayout;
    @BindView(R.id.fragment_main_recyclerview) RecyclerView recyclerView;

    private SpotAdapter adapter;
    private IListPresenter listPresenter;

    //活动调用此方法生成新的fragment
    public static SpotListFragment newInstance() {
        return new SpotListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        listPresenter=new ListPresenter(this);
    }

    @Nullable
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
        setToolbar(view);
        setNavigation(view);
        setFloatingButton(view);
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

    private void setFloatingButton(View view) {
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Main", "onClick: " + v.toString());
//                Toast.makeText(MainActivity.this,"fab clicked",Toast.LENGTH_SHORT).show();
                //snackbar比起toast多一个按钮,传入的第一个参数为界面布局任意一个view，snackbar会自动查找最外布局来展示
                Snackbar.make(v, "data deleted", Snackbar.LENGTH_SHORT).setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity(), "data restored", Toast.LENGTH_SHORT).show();
                    }
                }).show();
            }
        });
    }

    private void setNavigation(View view) {
        //在drawer抽屉中建立一个navigation的view，此view包含一个header布局和一个menu
        NavigationView navigationMenu = (NavigationView) view.findViewById(R.id.nav_view);
        navigationMenu.setCheckedItem(R.id.contacts);
        navigationMenu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                drawerLayout.closeDrawers();
                return true;
            }
        });
    }

    private void setToolbar(View view) {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        //设置toolbar
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            //显示导航按钮
            actionBar.setDisplayHomeAsUpEnabled(true);
            //设置图标
            actionBar.setHomeAsUpIndicator(R.drawable.list);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                Toast.makeText(getActivity(), "delete", Toast.LENGTH_SHORT).show();
                break;

            case R.id.setting:
                Toast.makeText(getActivity(), "setting", Toast.LENGTH_SHORT).show();
                break;

            case R.id.star:
                Toast.makeText(getActivity(), "star", Toast.LENGTH_SHORT).show();
                break;

            //点击到导航图标后
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);

        }
        return true;
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
