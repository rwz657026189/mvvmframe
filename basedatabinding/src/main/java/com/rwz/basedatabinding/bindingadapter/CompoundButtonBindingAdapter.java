package com.rwz.basedatabinding.bindingadapter;

import android.databinding.BindingAdapter;
import android.widget.CompoundButton;

import com.jakewharton.rxbinding2.widget.RxCompoundButton;
import com.rwz.baselist.adapter.rv.mul.IBaseMulInterface;
import com.rwz.baselist.entity.BiCommandEntity;
import com.rwz.baselist.entity.CommandEntity;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;


/**
 * Created by rwz on 2017/4/1.
 */

public class CompoundButtonBindingAdapter {

    @BindingAdapter({"onCheckedChange"})
    public static void onRbCheckChanged(CompoundButton cb, Consumer<CommandEntity> command) {
        if(command == null)
            return;
        final int id = cb.getId();
        if (cb.getTag(id) == null) {
            cb.setTag(id, id);
            RxCompoundButton.checkedChanges(cb)
                    .map(new Function<Boolean, CommandEntity>() {
                        @Override
                        public CommandEntity<Boolean> apply(Boolean isChecked) throws Exception {
                            return new CommandEntity(id, isChecked);
                        }
                    }).subscribe(command);
        }
    }

    /**
     * android:checked : 必不可少， 省略时，如果checked先调用，如该控件已选中，会调用OnCheckedChanged(), 但此时setTag()还未更改过来
     */
    @BindingAdapter({"onCheckedChange","setEntity", "android:checked"})
    public static void onCheckedChange(final CompoundButton cb, Consumer<BiCommandEntity<Boolean,
            IBaseMulInterface>> command , IBaseMulInterface entity, boolean checked) {
        if(command == null)
            return;
        final int id = cb.getId();
        Object tag = cb.getTag(id);
        if (tag == null) {
            cb.setTag(id, entity);
            RxCompoundButton.checkedChanges(cb)
                    .map(new Function<Boolean, BiCommandEntity<Boolean, IBaseMulInterface>>() {
                        @Override
                        public BiCommandEntity<Boolean, IBaseMulInterface> apply(Boolean isChecked) throws Exception {
                            return new BiCommandEntity(id, isChecked, cb.getTag(id));
                        }
                    }).subscribe(command);
        } else {
            cb.setTag(id, entity);
        }
        //需要保证调用在setTag()之后
        cb.setChecked(checked);
    }

}
