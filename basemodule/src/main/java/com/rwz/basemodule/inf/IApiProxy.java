package com.rwz.basemodule.inf;


import com.rwz.baselist.adapter.rv.mul.IBaseMulInterface;
import com.rwz.network.CommonObserver;

/**
 * Created by rwz on 2018/7/25.
 */

public interface IApiProxy {

    void onItemClick(int position, IBaseMulInterface iEntity);

    void onClickView(int id, IBaseMulInterface iEntity);

    void cancelRequest();

    void submitRequest();

    CommonObserver getObserver(int requestCode);

}
