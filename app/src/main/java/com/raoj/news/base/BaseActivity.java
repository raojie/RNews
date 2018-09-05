package com.raoj.news.base;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.oubowu.slideback.SlideBackHelper;
import com.oubowu.slideback.SlideConfig;
import com.oubowu.slideback.widget.SlideBackLayout;
import com.raoj.news.BuildConfig;
import com.raoj.news.R;
import com.raoj.news.annotation.ActivityFragmentInject;
import com.raoj.news.app.App;
import com.raoj.news.utils.MeasureUtil;
import com.raoj.news.utils.SpUtil;
import com.socks.library.KLog;

import rx.Observable;

/**
 * class: BaseActivity
 * describe: Activity的基类
 * author: raoj
 * date: 16:35
 */
public abstract class BaseActivity<T extends BasePresenter> extends AppCompatActivity implements View.OnClickListener, BaseView {

    /**
     * 将代理类通用行为抽出来
     */
    protected T mPresenter;

    /**
     * 标示该activity是否可滑动退出,默认false
     */
    protected boolean mEnableSlidr;

    /**
     * 布局的id
     */
    protected int mContentViewId;

    /**
     * 是否存在NavigationView
     */
    protected boolean mHasNavigationView;

    /**
     * 滑动布局
     */
    protected DrawerLayout mDrawerLayout;

    /**
     * 侧滑导航布局
     */
    protected NavigationView mNavigationView;

    /**
     * 菜单的id
     */
    private int mMenuId;

    /**
     * Toolbar标题
     */
    private int mToolbarTitle;

    /**
     * 默认选中的菜单项
     */
    private int mMenuDefaultCheckedItem;

    /**
     * Toolbar左侧按钮的样式
     */
    private int mToolbarIndicator;

    /**
     * 控制滑动与否的接口
     */
    //    protected SlidrInterface mSlidrInterface;
    protected SlideBackLayout mSlideBackLayout;

    /**
     * 结束Activity的可观测对象
     */
    private Observable<Boolean> mFinishObservable;

    /**
     * 跳转的类
     */
    private Class mClass;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        KLog.d(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>BaseActivity enter onCreate>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        if (getClass().isAnnotationPresent(ActivityFragmentInject.class)) {
            ActivityFragmentInject annotation = getClass().getAnnotation(ActivityFragmentInject.class);
            mContentViewId = annotation.contentViewId();
            mEnableSlidr = annotation.enableSlidr();
            mHasNavigationView = annotation.hasNavigationView();
            mMenuId = annotation.menuId();
            mToolbarTitle = annotation.toolbarTitle();
            mToolbarIndicator = annotation.toolbarIndicator();
            mMenuDefaultCheckedItem = annotation.menuDefaultCheckedItem();
        } else {
            throw new RuntimeException("Class must add annotations of ActivityFragmentInitParams.class");
        }

        //严苛模式
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());
        }

//        if (this instanceof SettingsActivity) {
//            SkinManager.getInstance().register(this);
//        }

//        initTheme();

        setContentView(mContentViewId);

        if (mEnableSlidr && !SpUtil.readBoolean("disableSlide")) {
            // 默认开启侧滑，默认是整个页码侧滑
            mSlideBackLayout = SlideBackHelper.attach(this, App.getActivityHelper(), new SlideConfig.Builder()
                    // 是否侧滑
                    .edgeOnly(SpUtil.readBoolean("enableSlideEdge"))
                    // 是否会屏幕旋转
                    .rotateScreen(false)
                    // 是否禁止侧滑
                    .lock(false)
                    // 侧滑的响应阈值，0~1，对应屏幕宽度*percent
                    .edgePercent(0.1f)
                    // 关闭页面的阈值，0~1，对应屏幕宽度*percent
                    .slideOutPercent(0.35f).create(), null);


        }
    }

    /**
     * 初始化主题
     */
//    private void initTheme() {
//        KLog.d(">>>>>>>>>>>>>>>>>>>>>>>BaseActivity enter initTheme>>>>>>>>>>>>>>>>>>>>>>>");
//        if (this instanceof NewsActivity) {
//            setTheme(SpUtil.readBoolean("enableNightMode") ? R.style.BaseAppThemeNight_LauncherAppTheme : R.style.BaseAppTheme_LauncherAppTheme);
//        } else if (!mEnableSlidr && mHasNavigationView) {
//            setTheme(SpUtil.readBoolean("enableNightMode") ? R.style.BaseAppThemeNight_AppTheme : R.style.BaseAppTheme_AppTheme);
//        } else {
//            setTheme(SpUtil.readBoolean("enableNightMode") ? R.style.BaseAppThemeNight_SlidrTheme : R.style.BaseAppTheme_SlidrTheme);
//        }
//    }

    private void initToolbar() {
        KLog.d(">>>>>>>>>>>>>>>>>>>>>>>BaseActivity enter initToolbar>>>>>>>>>>>>>>>>>>>>>>>");
        // 针对父布局非DrawerLayout的状态栏处理方式
        // 设置toolbar上面的View实现类状态栏效果，这里是因为状态栏设置为透明的了，而默认背景是白色的，不设的话状态栏处就是白色
        final View statusView = findViewById(R.id.status_view);
        if (statusView != null) {
            statusView.getLayoutParams().height = MeasureUtil.getStatusBarHeight(this);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            // 24.0.0版本后导航图标会有默认的与标题的距离，这里设置去掉
            toolbar.setContentInsetStartWithNavigation(0);
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
            if (mToolbarTitle != -1) {
                setToolbarTitle(mToolbarTitle);
            }
            if (mToolbarIndicator != -1) {
                setToolbarIndicator(mToolbarIndicator);
            } else {
                setToolbarIndicator(R.drawable.ic_menu_back);
            }
        }
    }

    protected void setToolbarIndicator(int resId) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(resId);
        }
    }

    protected void setToolbarTitle(String str) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(str);
        }
    }

    protected void setToolbarTitle(int strId) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(strId);
        }
    }
}
