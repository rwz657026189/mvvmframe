package com.rwz.basemodule.viewmodule;

import com.rwz.basemodule.abs.IView;
import com.rwz.basemodule.base.BaseViewModule;

import io.reactivex.functions.Consumer;

/**
 * Created by rwz on 2017/7/19.
 */

public class TabVpViewModule extends BaseViewModule<IView>{

    public TabVpViewModule(Consumer onClickEventCommand) {
        super(onClickEventCommand);
    }

}
