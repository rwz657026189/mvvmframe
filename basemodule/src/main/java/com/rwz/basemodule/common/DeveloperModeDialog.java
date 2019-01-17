package com.rwz.basemodule.common;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.rwz.baselist.adapter.lv.AbsSimpleAdapter;
import com.rwz.baselist.adapter.lv.AbsSimpleViewHolder;
import com.rwz.basemodule.R;
import com.rwz.basemodule.base.BaseDialog;
import com.rwz.basemodule.databinding.DialogDeveloperModeBinding;
import com.rwz.basemodule.entity.turnentity.MsgDialogTurnEntity;
import com.rwz.basemodule.inf.CommBiConsumer;
import com.rwz.basemodule.manager.DeveloperManager;
import com.rwz.basemodule.manager.StatisticsManager;
import com.rwz.commonmodule.config.GlobalConfig;
import com.rwz.commonmodule.help.CheckHelp;
import com.rwz.commonmodule.help.DialogHelp;
import com.rwz.commonmodule.utils.app.CommUtils;
import com.rwz.commonmodule.utils.app.DensityUtils;
import com.rwz.commonmodule.utils.app.ResourceUtil;
import com.rwz.commonmodule.utils.system.AndroidUtils;
import com.rwz.commonmodule.utils.system.ScreenUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by rwz on 2017/6/22.
 * 开发者模式Dialog
 */

public class DeveloperModeDialog extends BaseDialog<DialogDeveloperModeBinding> implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private static final List<String> devUsers = new ArrayList<>();

    private static int mClickCount = 0;
    private static final int MIN_CLICK_COUNT = 2;

    static {
        devUsers.add("102104");
    }

    private ListView mList;
    private List<Entity> mData;
    private AbsSimpleAdapter<Entity> mAdapter;
    private Map<String, View.OnClickListener> mEventData;


    /**
     *  打开开发者模式， 该方法上线可用，但必须是指定的用户
     *  更多设置 -> 第二个
     */
    public static DeveloperModeDialog openDialog(Context context) {
        if (GlobalConfig.ALLOW_OPEN_DEV_DIALOG) {
            //连击3次
            if (mClickCount >= MIN_CLICK_COUNT) {
                mClickCount = 0;
                DeveloperModeDialog dialog = new DeveloperModeDialog();
                DialogHelp.show(context, dialog, "DeveloperModeDialog");
                return dialog;
            } else if (!CheckHelp.checkClickTime()) {
                mClickCount++;
            } else {
                mClickCount = 0;
            }
        }
        return null;
    }

    public DeveloperModeDialog addEvent(String event, View.OnClickListener postEvent) {
        if(mEventData == null)
            mEventData = new LinkedHashMap<>();
        mEventData.put(event, postEvent);
        return this;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.dialog_developer_mode;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mList = mBind.list;
        mData = new ArrayList<>();
        setupBtn();
        setupData();
        mAdapter = new AbsSimpleAdapter<Entity>(getContext(), mData, R.layout.item_dialog_dev) {
            @Override
            public void convert(int position, AbsSimpleViewHolder helper, Entity item) {
                helper.setText(R.id.key, item.getKey());
                helper.setText(R.id.value, item.getValue());
            }
        };
        mList.setAdapter(mAdapter);
        mList.setOnItemClickListener(this);
        mList.setOnItemLongClickListener(this);
    }

    private void setupBtn() {
        ViewGroup viewGroup = mBind.btnContainer;
        if (mEventData != null) {
            Set<String> set = mEventData.keySet();
            for (String s : set) {
                Button button = new Button(getContext());
                button.setText(s);
                button.setOnClickListener(mEventData.get(s));
                viewGroup.addView(button);
            }
        }
    }

    private void setupData() {
        final Context context = getContext();
        DeveloperManager.getInstance().init();
        mData.add(new Entity("当前host ：", DeveloperManager.getInstance().getCurrIP()));
        mData.add(new Entity("cpu ：", DeveloperManager.getInstance().getCPU()));
        mData.add(new Entity("设备：", AndroidUtils.getDeviceModel() + "(" + AndroidUtils.getSystemVersion() + ")"));
        mData.add(new Entity("屏幕尺寸：", ScreenUtil.getInstance().getScreenWidth(context) + "*" + ScreenUtil.getInstance().getScreenHeight(context) + ",  h_100 = " + DensityUtils.px2dp(context, ResourceUtil.getDimen(R.dimen.h_100)) + ",  v_100 = " + DensityUtils.px2dp(context,ResourceUtil.getDimen(R.dimen.v_100))));
        mData.add(new Entity("文字缩放系数：", DeveloperManager.getInstance().getFontScale(getActivity().getWindowManager()) + ""));
        mData.add(new Entity("屏幕像素密度：", DeveloperManager.getInstance().getScreenScale(getActivity().getWindowManager()) + ""));
        mData.add(new Entity("最小宽度限定：", DeveloperManager.getInstance().getSmallestWidthDP(getActivity().getWindowManager()) + "sw"));
        mData.add(new Entity("状态栏高度：", ScreenUtil.getInstance().getStatusHeight(context) + ""));
        mData.add(new Entity("渠道ID ：", StatisticsManager.getChannel()));
        mData.add(new Entity("包名 ：", AndroidUtils.getPackageName(context)));
        mData.add(new Entity("最大分配内存 ：", (Runtime.getRuntime().maxMemory() >> 20) + "M"));
        mData.add(new Entity("已使用总内存 ：", (int)(Runtime.getRuntime().totalMemory() / 1024f / 1024) * 100 / 100f + "M"));
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        DeveloperManager.getInstance().switchServerIP();
        super.onDismiss(dialog);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Entity entity = mData.get(position);
        switch (position) {
            case 0: //切换ip
                entity.value = DeveloperManager.getInstance().getNewServerIP();
                mAdapter.notifyDataSetChanged();
                break;
        }
        CommUtils.copyText(entity.getValue());
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Entity entity = mData.get(position);
        switch (position) {
            case 0: //切换ip
                showIpEditDialog(entity);
                break;
        }
        return true;
    }

    private void showIpEditDialog(final Entity entity) {
        EditDialog dialog = EditDialog.newInstance(new MsgDialogTurnEntity("提示", entity.getValue(), 0));
        dialog.setListener(new CommBiConsumer<MsgDialogTurnEntity, Boolean>() {
            @Override
            public void accept(MsgDialogTurnEntity turnEntity, Boolean result) throws Exception {
                if (result) {
                    DeveloperManager.getInstance().setNewIp(turnEntity.getMsg());
                    entity.setValue(turnEntity.getMsg());
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
        DialogHelp.show(getContext(), dialog, "EditDialog");
    }

    private static class Entity{
        private String key;
        private String value;

        public Entity(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }
    }


}
