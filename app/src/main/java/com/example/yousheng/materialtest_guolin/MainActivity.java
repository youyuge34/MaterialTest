package com.example.yousheng.materialtest_guolin;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private SwipeRefreshLayout refreshLayout;
    private Fruit[] fruits = {new Fruit("shaonv1", R.drawable.chigua_1),
            new Fruit("shaonv2", R.drawable.chigua_2),
            new Fruit("shaonv3", R.drawable.chigua_3),
            new Fruit("shaonv4", R.drawable.chigua_4),
            new Fruit("shaonv5", R.drawable.chigua_5),
            new Fruit("shaonv6", R.drawable.chigua_6),
            new Fruit("shaonv7", R.drawable.chigua_7),
    };
    private List<Fruit> fruitList = new ArrayList<>();
    private FruitAdapter adapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //布局填充toolbar菜单栏选项
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                Toast.makeText(this, "delete", Toast.LENGTH_SHORT).show();
                break;

            case R.id.setting:
                Toast.makeText(this, "setting", Toast.LENGTH_SHORT).show();
                break;

            case R.id.star:
                Toast.makeText(this, "star", Toast.LENGTH_SHORT).show();
                break;

            //点击到导航图标后
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);

        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //设置toolbar
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //显示导航按钮
            actionBar.setDisplayHomeAsUpEnabled(true);
            //设置图标
            actionBar.setHomeAsUpIndicator(R.drawable.list);
        }
        //在drawer抽屉中建立一个navigation的view，此view包含一个header布局和一个menu
        NavigationView navigationMenu = (NavigationView) findViewById(R.id.nav_view);
        navigationMenu.setCheckedItem(R.id.contacts);
        navigationMenu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                drawerLayout.closeDrawers();
                return true;
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Main", "onClick: " + v.toString());
//                Toast.makeText(MainActivity.this,"fab clicked",Toast.LENGTH_SHORT).show();
                //snackbar比起toast多一个按钮,传入的第一个参数为界面布局任意一个view，snackbar会自动查找最外布局来展示
                Snackbar.make(v, "data deleted", Snackbar.LENGTH_SHORT).setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this, "data restored", Toast.LENGTH_SHORT).show();
                    }
                }).show();
            }
        });

        //初始化水果list
        init();
        //设置recyclerView
        RecyclerView recycleView= (RecyclerView) findViewById(R.id.recycler_view);
        GridLayoutManager manger=new GridLayoutManager(this,2);
        recycleView.setLayoutManager(manger);
        adapter=new FruitAdapter(fruitList);
        recycleView.setAdapter(adapter);

        //设置下拉刷新
        refreshLayout= (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        refreshLayout.setColorSchemeResources(R.color.navigationBarColor);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshFruits();
            }
        });
    }

    private void refreshFruits() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        init();
                        adapter.notifyDataSetChanged();
                        //隐藏刷新图标
                        refreshLayout.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

    private void init() {
        fruitList.clear();
        for (int i = 0; i < 40; i++) {
            Random random = new Random();
            int index = random.nextInt(fruits.length);
            fruitList.add(fruits[index]);
        }
    }
}
