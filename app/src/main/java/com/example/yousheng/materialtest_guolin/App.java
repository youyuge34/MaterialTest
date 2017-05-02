package com.example.yousheng.materialtest_guolin;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackManager;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:16/12/28 下午11:55
 * 描述:「必须在 Application 的 onCreate 方法中执行 BGASwipeBackManager.getInstance().init(this) 来初始化滑动返回」
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // 必须在 Application 的 onCreate 方法中执行 BGASwipeBackManager.getInstance().init(this) 来初始化滑动返回
        BGASwipeBackManager.getInstance().init(this);
        //初始化二维码扫描
        ZXingLibrary.initDisplayOpinion(this);
        //leancloud云初始化
        AVOSCloud.initialize(this, "csOq3edhDJ1vEiw98wOVetdu-gzGzoHsz", "LTadyMCEHoAJ8EvMdfUszOPX");
//        AVOSCloud.setDebugLogEnabled(true);
//        AVAnalytics.enableCrashReport(this, true);
    }
}
