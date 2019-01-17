package com.rwz.basemodule.base;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.AnimRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.rwz.basemodule.BR;
import com.rwz.basemodule.R;
import com.rwz.basemodule.abs.IView;
import com.rwz.basemodule.abs.IViewModule;
import com.rwz.basemodule.abs.PostEventType;
import com.rwz.basemodule.common.LoadingDialog;
import com.rwz.basemodule.common.MsgDialog;
import com.rwz.basemodule.entity.turnentity.LoadingDialogTurnEntity;
import com.rwz.basemodule.entity.turnentity.MsgDialogTurnEntity;
import com.rwz.basemodule.inf.CommBiConsumer;
import com.rwz.basemodule.manager.PushManager;
import com.rwz.basemodule.manager.StatisticsManager;
import com.rwz.basemodule.utils.FragmentUtil;
import com.rwz.basemodule.utils.StatusBarUtil;
import com.rwz.commonmodule.utils.show.ToastUtil;
import com.rwz.commonmodule.ImageLoader.ImageLoaderUtil;
import com.rwz.commonmodule.config.GlobalConfig;
import com.rwz.commonmodule.help.DialogHelp;
import com.rwz.commonmodule.utils.app.ResourceUtil;
import com.rwz.commonmodule.utils.show.LogUtil;
import com.rwz.commonmodule.utils.system.ScreenUtil;

import java.util.HashSet;
import java.util.Set;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * @function:
 * @author: rwz
 * @date: 2017-07-16 17:59
 */

public abstract class BaseActivity<VB extends ViewDataBinding,
        VM extends IViewModule>extends SwipeBackActivity implements IView, View.OnClickListener {

    public final static int TRANS_ANIM_SLIDE = 0; //侧滑转场动画
    public final static int TRANS_ANIM_ALPHA = 1; //alpha转场动画

    protected String TAG = "BaseActivity";
    private boolean isAlive = false;      //activity是否存在
    private boolean isRunning = false;    //activity是否可见

    protected VB mBind;
    protected VM mViewModule;

    /**
     * 第一次点击返回的系统时间
     */
    private long mFirstClickTime = 0;
    protected View mRootView;

    private static final int DEF_MSG_REQUEST_CODE = -1;//dialog标示符
    protected boolean isSetBarColor = true;//是否设置状态栏颜色
    protected @StringRes int TD_PAGE_ID; //TD统计页面标示 字串id
    protected @AnimRes int mInNextAnimResId = R.anim.slide_right_in;   // 进场动画id
    protected @AnimRes int mInCurrAnimResId = R.anim.slide_right_out;  // 进场动画id
    protected @AnimRes int mOutNextAnimResId = R.anim.slide_left_in;   // 出场动画id
    protected @AnimRes int mOutCurrAnimResId = R.anim.slide_left_out;  // 出场动画id
    protected boolean isForbidTransitionAnim;//是否禁用出场入场动画
    protected boolean isForbidOutTransitionAnim;//是否禁用出场动画
    protected boolean isForbidTouchScreen;//是否禁用触摸屏幕（禁用一切事件）
    protected boolean isAutoLoadingData = true;//是否自动加载数据

    protected TextView mTitle;
    protected TextView mLeftView;
    protected TextView mRightView;
    protected View mToolbar;

    private MsgDialog mMsgDialog;  //普通对话框
    private LoadingDialog mLoadDialog; //加载中对话框
    //注册广播的集合
    private Set<String> mRegisterBRData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isAlive = true;
        config();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        themeColorSetting(ResourceUtil.getColor(R.color.toolbar_bg));//设置状态栏颜色
        SwipeBackLayout layout = getSwipeBackLayout();
        layout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
        layout.setEdgeSize(ScreenUtil.getInstance().getScreenWidth(this)/4);
        if (mViewModule == null) {
            mViewModule = setViewModule();
        }
        if (mViewModule != null) {
            mViewModule.bindContext(this);
            mViewModule.attachWindow(this);
        }
        initialization();
        init(savedInstanceState);
        if (mBind != null && mViewModule != null) {
            mBind.setVariable(BR.viewModule, mViewModule);
            mBind.executePendingBindings();
        }
        if (isAutoLoadingData) {
            requestData();
        }
        if (mViewModule != null) {
            mViewModule.initCompleted();
        }
        PushManager.getInstance().onCreate(this);
        //统计事件
        if (mTitle != null && !TextUtils.isEmpty(mTitle.getText())) {
            StatisticsManager.onEvent(mTitle.getText() + "");
        } else {
            StatisticsManager.onEvent(TD_PAGE_ID);
        }
//        LogUtil.d("生命周期", getClass().getSimpleName(), "onCreate");


    }

    //统计事件
    protected void onEvent(String eventName) {
        StatisticsManager.onEvent(eventName);
    }

    protected void requestData() {
    }

    protected abstract VM setViewModule();

    /**
     * 主题颜色设置
     * @param color
     */
    public void themeColorSetting(int color) {
        if (isSetBarColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(color);
        }
    }

    private boolean isFullScreen;

    private boolean isDarkStatus;
    /**
     * 设置深色状态栏文字
     * @param isDarkStatus : true : 黑字;  false 白字
     */
    public void setDarkStatusBar(boolean isFullScreen, boolean isDarkStatus) {
        if (this.isFullScreen != isFullScreen || this.isDarkStatus != isDarkStatus) {
            this.isFullScreen = isFullScreen;
            this.isDarkStatus = isDarkStatus;
            StatusBarUtil.setDarkStatusBar(this, isFullScreen, isDarkStatus);
        }
    }
    @Override
    public void onPostEvent(@PostEventType int type, Object params) {
        if (!isAlive()) {
            return;
        }
        switch (type) {
            case IView.FINISH_ATY: //结束当前activity
                finish();
                break;
            case IView.SHOW_DIALOG://显示消息对话框
                if (params != null && params instanceof MsgDialogTurnEntity) {
                    showMsgDialog((MsgDialogTurnEntity) params);
                }
                break;
            case IView.SHOW_LOADING://显示正在加载对话框
                if (params != null && params instanceof LoadingDialogTurnEntity) {
                    showLoadDialog((LoadingDialogTurnEntity) params);
                }
                break;
            case IView.DISMISS_LOADING://隐藏正在加载对话框
                dismissLoadDialog();
                break;
            case IView.FORBID_TOUCH_SCREEN: //是否禁用触摸屏幕
                if (params != null && params instanceof Boolean) {
                    isForbidTouchScreen = (boolean) params;
                }
        }
    }

    protected void config() {
    }

    /**
     * 显示消息对话框
     * @param title
     * @param msg
     */
    protected void showMsgDialog(@StringRes int title, @StringRes int msg) {
        showMsgDialog(new MsgDialogTurnEntity(ResourceUtil.getString(title),
                ResourceUtil.getString(msg), DEF_MSG_REQUEST_CODE));
    }

    protected void showMsgDialog(String title, String msg) {
        showMsgDialog(new MsgDialogTurnEntity(title, msg, DEF_MSG_REQUEST_CODE));
    }
    protected void showMsgDialog(@StringRes int title, @StringRes int msg, int requestCode) {
        showMsgDialog(new MsgDialogTurnEntity(ResourceUtil.getString(title),
                ResourceUtil.getString(msg), requestCode));
    }
    protected void showMsgDialog(MsgDialogTurnEntity entity) {
        mMsgDialog = MsgDialog.newInstance(entity);
        CommBiConsumer<MsgDialogTurnEntity, Boolean> listener = entity.getListener();
        mMsgDialog.setListener(listener == null ? mDialogListener : listener);
        if (isAlive) {
            DialogHelp.show(getSupportFragmentManager(), mMsgDialog, "msgDialog");
        }
    }
    protected void showLoadDialog(LoadingDialogTurnEntity entity) {
        if (mLoadDialog == null) {
            mLoadDialog = FragmentUtil.newDialog(LoadingDialog.class, entity);
        } else {
            mLoadDialog.setEntity(entity);
        }
        DialogHelp.show(getSupportFragmentManager(), mLoadDialog, "mLoadDialog");
    }

    protected void dismissLoadDialog() {
        if (mLoadDialog != null) {
            mLoadDialog.dismissAllowingStateLoss();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return isForbidTouchScreen || super.dispatchTouchEvent(ev);
    }

    private CommBiConsumer mDialogListener = new CommBiConsumer<MsgDialogTurnEntity, Boolean>() {
        @Override
        public void accept(MsgDialogTurnEntity entity, Boolean isClickEnter) throws Exception {
            if (isClickEnter) {
                onClickDialogEnter(entity);
            } else {
                onClickDialogCancel(entity);
            }
        }
    };

    /**
     * 点击对话框确定
     * @param entity
     */
    protected void onClickDialogEnter(MsgDialogTurnEntity entity){
    }

    /**
     * 点击对话框取消
     * @param entity
     */
    protected void onClickDialogCancel(MsgDialogTurnEntity entity){
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        ImageLoaderUtil.getInstance().cleanGlide(this);
    }

    @Override
    public void finish() {
        super.finish();
        startPlayTransition(false);
    }

    /**执行转场动画**/
    private void startPlayTransition(boolean isEnter) {
        if (isEnter && !isForbidTransitionAnim) {
            overridePendingTransition(mInNextAnimResId, mInCurrAnimResId);
        }else if(!isForbidOutTransitionAnim){
            overridePendingTransition(mOutNextAnimResId, mOutCurrAnimResId);
        }
    }

    public void setTransitionAnim(int animType) {
        if (animType == TRANS_ANIM_ALPHA) { //alpha
            mInNextAnimResId = R.anim.slide_alpha_in;
            mInCurrAnimResId = R.anim.slide_alpha_out;
            mOutNextAnimResId = R.anim.slide_alpha_in;
            mOutCurrAnimResId = R.anim.slide_alpha_out;
        } else {//侧滑
            mInNextAnimResId = R.anim.slide_right_in;
            mInCurrAnimResId = R.anim.slide_right_out;
            mOutNextAnimResId = R.anim.slide_left_in;
            mOutCurrAnimResId = R.anim.slide_left_out;
        }
    }

    /**
     * @param transType 转场动画 e.g. TRANS_ANIM_ALPHA
     */
    public void startActivity(Intent intent, int transType) {
        super.startActivity(intent);
        //开始进入时的转场动画
        if(transType == TRANS_ANIM_ALPHA)
            overridePendingTransition(R.anim.slide_alpha_in, R.anim.slide_alpha_out);
        else
            overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
    }

    @Override
    public void startActivity(Intent intent, Bundle options) {
        super.startActivity(intent, options);
        startPlayTransition(true);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            super.startActivityForResult(intent, requestCode, options);
            startPlayTransition(false);
        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        startPlayTransition(false);
    }

    private void initialization() {
        mBind = DataBindingUtil.setContentView(this, setLayoutId());
        TAG =   getClass().getSimpleName();
        mRootView = mBind.getRoot();
        mTitle = (TextView) findViewById(R.id.title);
        mLeftView = (TextView) findViewById(R.id.left);
        mRightView = (TextView) findViewById(R.id.right);
        mToolbar = findViewById(R.id.toolbar);
        if (mTitle != null) {
            mTitle.setOnClickListener(this);
        }
        if (mLeftView != null) {
            mLeftView.setOnClickListener(this);
        }
        if (mRightView != null) {
            mRightView.setOnClickListener(this);
        }
    }

    public void setPageTitle(@StringRes int title) {
        if (mTitle != null) {
            mTitle.setText(title);
        }
    }

    public void setPageTitle(String title) {
        if (mTitle != null) {
            mTitle.setText(title);
        }
    }

    public void setLeftDrawable(@DrawableRes int leftRes) {
        if (mLeftView != null) {
            Drawable left = ResourceUtil.getDrawable(leftRes);
            mLeftView.setCompoundDrawablesWithIntrinsicBounds(left, null, null, null);
        }
    }
    public void setRightDrawable(@DrawableRes int rightRes) {
        if (mRightView != null) {
            Drawable right = ResourceUtil.getDrawable(rightRes);
            mRightView.setCompoundDrawablesWithIntrinsicBounds(null, null,right,  null);
        }
    }

    public void setRightText(@StringRes int rightText) {
        if (mRightView != null) {
            mRightView.setText(rightText);
        }
    }

    protected void init(Bundle savedInstanceState) {
    }

    public View getRootView() {
        return mRootView;
    }

    /**
     * 设置资源布局
     * @return
     */
    protected abstract int setLayoutId();

    /**
     * 获取binding对象
     */
    protected VB getBinding() {
        return mBind;
    }

    /**
     * 双击退出
     */
    private boolean onDoubleClickExit(long timeSpace) {
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - mFirstClickTime > timeSpace) {
            ToastUtil.getInstance().showShort(R.string.exit_app_click_again);
            mFirstClickTime = currentTimeMillis;
            return false;
        } else {
            return true;
        }
    }

    /**
     * 双击退出，间隔时间为2000ms
     * @return
     */
    public boolean onDoubleClickExit() {
        return onDoubleClickExit(GlobalConfig.EXIT_APP_DOUBLE_CLICK_TIME);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isRunning = true;
        if(mViewModule != null)
            mViewModule.onResume();
        //统计浏览时长
        if (mTitle != null && !TextUtils.isEmpty(mTitle.getText())) {
            onPageStart(String.valueOf(mTitle.getText()));
        } else {
            onPageStart(TD_PAGE_ID);
        }
    }

    protected void onPageStart(@StringRes int name) {
        StatisticsManager.onPageStart(name);
    }

    protected void onPageStart(String name) {
        StatisticsManager.onPageStart(name);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isRunning = false;

        //统计浏览时长
        if (mTitle != null && !TextUtils.isEmpty(mTitle.getText())) {
            onPageEnd(String.valueOf(mTitle.getText()));
        } else {
            onPageEnd(TD_PAGE_ID);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    protected void onPageEnd(@StringRes int name) {
        StatisticsManager.onPageEnd(name);
    }
    protected void onPageEnd(String name) {
        StatisticsManager.onPageEnd(name);
    }


    @Override
    protected void onDestroy() {
        LogUtil.d("BaseActivity","onDestroy");
        isAlive = false;
        isRunning = false;
        if (mViewModule != null) {
            mViewModule.detachWindow();
            mViewModule = null;
        }
        mRegisterBRData = null;
        super.onDestroy();
    }

    public final boolean isAlive() {
        return isAlive;// & ! isFinishing();导致finish，onDestroy内runUiThread不可用
    }
    public final boolean isRunning() {
        return isRunning & isAlive();
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.left) {
            onLeftClick();
        } else if (v.getId() == R.id.title) {
            onTitleClick();
        } else if (v.getId() == R.id.right) {
            onRightClick();
        }
    }

    /**
     * 滚动到顶部
     */
    public void scrollToTop() {
    }

    protected void onTitleClick() {
        scrollToTop();
    }

    protected void onLeftClick() {
        onBackPressed();
    }

    protected void onRightClick() {
    }

    @Override
    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        if (receiver != null) {
            if (mRegisterBRData == null) {
                mRegisterBRData = new HashSet<>();
            }
            mRegisterBRData.add(receiver.getClass().getCanonicalName());
        }
        return super.registerReceiver(receiver, filter);
    }

    @Override
    public void unregisterReceiver(BroadcastReceiver receiver) {
        if(isRegisterReceiver(receiver))
            mRegisterBRData.remove(receiver.getClass().getCanonicalName());
        super.unregisterReceiver(receiver);
    }

    /**
     * 是否注册过改广播
     */
    public boolean isRegisterReceiver(BroadcastReceiver receiver) {
        return receiver != null && mRegisterBRData != null && mRegisterBRData.contains(receiver.getClass().getCanonicalName());
    }

}
