package com.example.yousheng.materialtest_guolin.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.yousheng.materialtest_guolin.R;

/**
 * Created by yousheng on 17/3/19.
 */

/**
 * 创建抽象类，避免代码重复，因为列表页的activity和详情页的activity用的是差不多的代码和一样的布局文件activity_fragment
 */
public abstract class SingleFragmentActivity extends AppCompatActivity {

    //由子类实现方法，提供对应activity托管的fragment实例
    protected abstract Fragment createFragment();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //activity类中添加了fragmentmanger类，负责管理fragment并将它们的视图添加到activity的视图层级结构中
        //manger类具体管理：fragment队列和fragment事务回退栈
        FragmentManager manager = getSupportFragmentManager();

        //向manger请求获取fragment，若要获取的fragment已存在于队列中，manger就直接返回它
        //因为旋转或内存回收后会销毁activity，重建时会调用activity.oncreate()方法。
        //activity被销毁时，manger会将fragment队列保存下来，下次activity重建时会首先获取保存的队列并重建队列
        Fragment fragment = manager.findFragmentById(R.id.fragment_container);

        //若指定容器视图资源id的fragment不在队列中，就新建一个
        //事务用来管理fragment队列中的fragment，而manger管理fragment事务回退栈。
        if (fragment == null) {
            fragment = createFragment();
            //manger的开启事务方法得到事务的实例，添加操作并提交事务
            manager.beginTransaction()
                    /**
                     * add方法有两个作用，一个是告诉manger，fragment应出现在哪里
                     * 二是将资源id用作队列中fragment的唯一标识，所以想获取某个fragment，
                     * 只要使用捆绑的资源id就行（如上放的manger.findFragmentById方法）
                     */
                    .add(R.id.fragment_container, fragment)
                    .commit();

        }
    }
}
