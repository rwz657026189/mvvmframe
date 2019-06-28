package com.rwz.basemodule.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.rwz.basemodule.manager.StatisticsManager;
import com.rwz.basemodule.utils.FragmentUtil;
import com.rwz.commonmodule.ImageLoader.ImageLoaderUtil;
import com.rwz.commonmodule.help.DialogHelp;
import com.rwz.commonmodule.utils.app.ResourceUtil;
import com.rwz.commonmodule.utils.app.StringUtil;
import com.rwz.commonmodule.utils.show.LogUtil;

/**
 * @function:
 * @author: rwz
 * @date: 2017-07-16 17:59
 */

public abstract class BaseFragment<VB extends ViewDataBinding,
        VM extends IViewModule>extends Fragment implements IView, View.OnClickListener {

    protected String TAG = "BaseFragment";
    private boolean isAlive = false;      //activity是否存在
    private boolean isRunning = false;    //activity是否可见
    protected VB mBind;
    protected VM mViewModule;

    protected TextView mTitle;
    protected TextView mLeftView;
    protected TextView mRightView;
    /**
     * 第一次点击返回的系统时间
     */
    protected View mRootView;
    private static final int DEF_MSG_REQUEST_CODE = -1;//dialog标示符
    protected @StringRes int TD_PAGE_ID; //TD统计页面标示 字串id

    private MsgDialog mDialog;
    private LoadingDialog mLoadDialog; //加载中对话框
    protected boolean isUseDelayLoad;//是否延迟加载
    private boolean isInit; //是否初始化

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBind = DataBindingUtil.inflate(inflater, setLayoutId(), container, false);
        mRootView = mBind.getRoot();
        initFragment();
        return mRootView;
    }

    private void initFragment() {
        TAG = StringUtil.getClassName(this);
        mTitle = mRootView.findViewById(R.id.title);
        mLeftView = mRootView.findViewById(R.id.left);
        mRightView = mRootView.findViewById(R.id.right);
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isAlive = true;
        isInit = true;
        config();
        if (mViewModule == null) {
            mViewModule = setViewModule();
        }
        if (mViewModule != null) {
            mViewModule.bindContext(getContext());
            mViewModule.attachWindow(this);
        }
        LogUtil.d(TAG,"onActivityCreated", "mViewModule = " + mViewModule);

        init(savedInstanceState);
        if (mBind != null && mViewModule != null) {
            LogUtil.d("BaseFragment", "mViewModule = " + mViewModule);
            mBind.setVariable(BR.viewModule, mViewModule);
            mBind.executePendingBindings();
        }

        if (getUserVisibleHint()) {
            onDelayLoad();
        }
        if (!isUseDelayLoad) {
            requestData();
        }
        if (mViewModule != null) {
            mViewModule.initCompleted();
        }
        //统计事件
        StatisticsManager.onEvent(TD_PAGE_ID);
    }

    protected void requestData() {
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    //统计事件
    protected void onEvent(String eventName) {
        StatisticsManager.onEvent(eventName);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isInit) {
            isInit = false;
            onDelayLoad();
        }
    }

    /**延迟加载，requestData()不都重写**/
    protected void onDelayLoad() {}

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        ImageLoaderUtil.getInstance().cleanGlide(getContext());
    }

    protected abstract VM setViewModule();

    @Override
    public void onPostEvent(@PostEventType int type, Object params) {
        if (!isAlive()) {
            return;
        }
        switch (type) {
            case IView.FINISH_ATY: //结束当前activity
                FragmentActivity activity = getActivity();
                if (activity != null) {
                    activity.finish();
                }
                break;
            case IView.SHOW_DIALOG:
                if (params != null && params instanceof MsgDialogTurnEntity) {
                    showMsgDialog((MsgDialogTurnEntity) params);
                }
            case IView.SHOW_LOADING://显示正在加载对话框
                if (params != null && params instanceof LoadingDialogTurnEntity) {
                    showLoadDialog((LoadingDialogTurnEntity) params);
                }
                break;
            case IView.DISMISS_LOADING://隐藏正在加载对话框
                dismissLoadDialog();
                break;
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
    protected void showMsgDialog(@StringRes int title, @StringRes int msg, int requestCode) {
        showMsgDialog(new MsgDialogTurnEntity(ResourceUtil.getString(title),
                ResourceUtil.getString(msg), requestCode));
    }
    protected void showMsgDialog(MsgDialogTurnEntity entity) {
        mDialog = MsgDialog.newInstance(entity);
        CommBiConsumer<MsgDialogTurnEntity, Boolean> listener = entity.getListener();
        mDialog.setListener(listener == null ? mDialogListener : listener);
        if (isAlive) {
            Fragment fragment = getParentFragment();
            DialogHelp.show(fragment != null ? getChildFragmentManager() : getFragmentManager(), mDialog, "msgDialog");
        }
    }

    protected void showLoadDialog(LoadingDialogTurnEntity entity) {
        if (mLoadDialog == null) {
            mLoadDialog = FragmentUtil.newDialog(LoadingDialog.class, entity);
        } else {
            mLoadDialog.setEntity(entity);
        }
        DialogHelp.show(getChildFragmentManager(), mLoadDialog, "mLoadDialog");
    }

    protected void dismissLoadDialog() {
        if (mLoadDialog != null) {
            mLoadDialog.dismissAllowingStateLoss();
        }
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


    @Override
    public void onResume() {
        super.onResume();
        isRunning = true;
        onPageStart(TD_PAGE_ID);
        if(mViewModule != null)
            mViewModule.onResume();
    }

    protected void onPageStart(@StringRes int name) {
        StatisticsManager.onPageStart(name);
    }

    protected void onPageStart(String name) {
        StatisticsManager.onPageStart(name);
    }

    @Override
    public void onPause() {
        super.onPause();
        isRunning = false;
        onPageEnd(TD_PAGE_ID);
    }

    protected void onPageEnd(@StringRes int name) {
        StatisticsManager.onPageEnd(name);
    }

    protected void onPageEnd(String name) {
        StatisticsManager.onPageEnd(name);
    }

    @Override
    public void onDestroy() {
        isAlive = false;
        isRunning = false;
        if (mViewModule != null) {
            mViewModule.detachWindow();
            mViewModule = null;
        }
        super.onDestroy();
    }

    public final boolean isAlive() {
        return isAlive;// & ! isFinishing();导致finish，onDestroy内runUiThread不可用
    }
    public final boolean isRunning() {
        return isRunning & isAlive();
    }


    public void setPageTitle(@StringRes int title) {
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

    protected void onTitleClick() {
        scrollToTop();
    }

    public void onLeftClick() {
    }

    public void onRightClick() {
    }

    /**
     * 回到顶部
     */
    public void scrollToTop() {
    }

    public void onFragmentVisibleChanged(boolean isVisible) {

    }

}
