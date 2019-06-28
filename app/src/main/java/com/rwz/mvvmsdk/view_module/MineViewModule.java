package com.rwz.mvvmsdk.view_module;

import android.databinding.ObservableField;
import android.support.annotation.Nullable;

import com.rwz.baselist.adapter.rv.mul.IBaseMulInterface;
import com.rwz.basemodule.base.BaseViewModule;
import com.rwz.basemodule.entity.turnentity.MsgDialogTurnEntity;
import com.rwz.commonmodule.base.BaseApplication;
import com.rwz.commonmodule.utils.app.ResourceUtil;
import com.rwz.commonmodule.utils.show.ToastUtil;
import com.rwz.commonmodule.utils.system.AndroidUtils;
import com.rwz.mvvmsdk.R;
import com.rwz.network.CommonObserver;
import com.rwz.network.SimpleObserver;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class MineViewModule extends BaseViewModule{

    public final ObservableField<String> versionName = new ObservableField<>();

    public MineViewModule() {
        versionName.set("v" + AndroidUtils.getVersionName(BaseApplication.getInstance()));
    }

    @Override
    protected void onClickView(int id, @Nullable IBaseMulInterface iEntity) {

        switch (id) {
            case R.id.collect:
                ToastUtil.getInstance().showShortSingle(R.string.collect);
                break;
            case R.id.follow:
                ToastUtil.getInstance().showShortSingle(R.string.follow);
                break;
            case R.id.history_record:
                ToastUtil.getInstance().showShortSingle(R.string.history_record);
                break;
            case R.id.version:
                checkNewVersion();
                break;
            case R.id.setting:
                ToastUtil.getInstance().showShortSingle(R.string.setting);
                break;

        }

    }

    private void checkNewVersion() {
        showLoadingDialog("检查新版本...");
        Observable.just(1)
                .delay(1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleObserver<Integer>() {
                    @Override
                    public void onNext(Integer value) {
                        dismissLoadingDialog();
                        MsgDialogTurnEntity entity = new MsgDialogTurnEntity("提示", "暂无新版本", 0);
                        entity.setEnterText("知道了");
                        entity.setCancelText("");
                        showDialog(entity);
                    }
                });
    }

}
