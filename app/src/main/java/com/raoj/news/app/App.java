package com.raoj.news.app;

import android.app.Application;
import android.content.Context;

import com.oubowu.slideback.ActivityHelper;
import com.socks.library.KLog;
import com.squareup.leakcanary.BuildConfig;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * 
 * @class App
 * @describe describe
 * @author raoj
 * @date 2018-07-18
 *
 */
public class App extends Application {

    private RefWatcher mRefWatcher;

    private static Context sApplicationContext;

    private ActivityHelper mActivityHelper;

    @Override
    public void onCreate() {
        super.onCreate();

        // 如果检测到某个 activity 有内存泄露，LeakCanary 就是自动地显示一个通知
        mRefWatcher = LeakCanary.install(this);

        sApplicationContext = this;

        //日志工具初始化
        KLog.init(BuildConfig.DEBUG);
    }

    public static ActivityHelper getActivityHelper() {
        return ((App)sApplicationContext).mActivityHelper;
    }

    // 获取ApplicationContext
    public static Context getContext() {
        return sApplicationContext;
    }
    
}
