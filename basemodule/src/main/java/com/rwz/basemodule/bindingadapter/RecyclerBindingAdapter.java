package com.rwz.basemodule.bindingadapter;

import android.databinding.BindingAdapter;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.rwz.baselist.adapter.rv.mul.BaseMulTypeBindingAdapter;
import com.rwz.baselist.adapter.rv.mul.IBaseMulInterface;
import com.rwz.baselist.entity.CommandEntity;
import com.rwz.basemodule.entity.wrap.WrapList;
import com.rwz.commonmodule.utils.show.LogUtil;

import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * Created by rwz on 2017/3/15.
 */

public final class RecyclerBindingAdapter {

    @BindingAdapter({"setData","clickCommand"})
    public static void setData(RecyclerView view, WrapList<? extends IBaseMulInterface> list, final Consumer<CommandEntity> clickCommand) {
        LogUtil.d("list_setData() = "+ list);
        if (list != null && list.getList() != null && list.getList().size() > 0) {
            final List data = list.getList();
            RecyclerView.LayoutManager manager;
            if (list.getSpanCount() == 1) {
                manager = new LinearLayoutManager(view.getContext(), list.getOrientation(), false);
            }else{
                manager = new GridLayoutManager(view.getContext(), list.getSpanCount(), list.getOrientation(), false);
            }
            RecyclerView.ItemDecoration itemDecoration = list.getItemDecoration();
            if (itemDecoration != null) {
                view.removeItemDecoration(itemDecoration);
                view.addItemDecoration(itemDecoration);
            }
            view.setLayoutManager(manager);
            BaseMulTypeBindingAdapter adapter = new BaseMulTypeBindingAdapter(view.getContext(), data);
            view.setAdapter(adapter);
            if (clickCommand != null) {
                adapter.setOnClickCommand(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer position) throws Exception {
                        if (data != null) {
                            Object obj = data.get(position);
                            clickCommand.accept(new CommandEntity(obj));
                        }
                    }
                });
            }
        }
    }

    /** 仅支持横向 **/
    @BindingAdapter(value = {"data", "clickCommand", "spanCount"}, requireAll=false)
    public static void setData(RecyclerView view, final List<? extends IBaseMulInterface> data, final Consumer<CommandEntity> clickCommand, int spanCount) {
        if (data != null && data.size() > 0) {
            RecyclerView.LayoutManager manager;
            if(spanCount <= 0)
                spanCount = 1;
            if (spanCount == 1) {
                manager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false){
                    @Override
                    public boolean canScrollVertically() {
                        return true;
                    }
                };
            }else{
                manager = new GridLayoutManager(view.getContext(), spanCount, GridLayoutManager.HORIZONTAL, false){
                    @Override
                    public boolean canScrollVertically() {
                        return true;
                    }
                };
            }
            view.setLayoutManager(manager);
            //避免嵌套recyclerView在滑动折叠布局中的滑动冲突
            view.setNestedScrollingEnabled(false);
            BaseMulTypeBindingAdapter adapter = new BaseMulTypeBindingAdapter(view.getContext(), data);
            view.setAdapter(adapter);
            if (clickCommand != null) {
                adapter.setOnClickCommand(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer position) throws Exception {
                        if (data != null) {
                            Object obj = data.get(position);
                            clickCommand.accept(new CommandEntity(obj));
                        }
                    }
                });
            }
        }
    }


}

