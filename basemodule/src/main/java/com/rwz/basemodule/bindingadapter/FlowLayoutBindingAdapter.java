package com.rwz.basemodule.bindingadapter;

import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;

import com.jakewharton.rxbinding2.view.RxView;
import com.rwz.baselist.entity.CommandEntity;
import com.rwz.basemodule.BR;
import com.rwz.basemodule.R;
import com.rwz.commonmodule.utils.app.ResourceUtil;
import com.rwz.ui.view_group.FlowLayout;

import java.util.List;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by rwz on 2017/12/20.
 */

public class FlowLayoutBindingAdapter {

    /**
     * 流式布局添加item
     */
    @BindingAdapter({"childLayout", "data", "clickCommand"})
    public static void addChildView(FlowLayout flowLayout, int childLayout, List data, final Consumer command) {
        if (flowLayout.getChildCount() == 0 && data != null) {
            flowLayout.setHorizontalSpacing(ResourceUtil.getDimen(R.dimen.h_10));
            flowLayout.setVerticalSpacing(ResourceUtil.getDimen(R.dimen.h_10));
            LayoutInflater inflater = LayoutInflater.from(flowLayout.getContext());
            for (int i = 0; i < data.size(); i++) {
                final Object entity = data.get(i);
                ViewDataBinding binding = DataBindingUtil.inflate(inflater, childLayout, null, false);
                binding.setVariable(BR.entity, entity);
                View view = binding.getRoot();
                if (command != null) {
                    final int id = view.getId();
                    RxView.clicks(view).map(new Function<Object, Object>() {
                        @Override
                        public CommandEntity apply(Object CommandEntity) throws Exception {return new CommandEntity(id, entity);
                        }
                    }).subscribe(command);
                }
                flowLayout.addView(view);
            }
        }
    }

}
