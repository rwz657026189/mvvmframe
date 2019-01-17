package com.rwz.basedatabinding.bindingadapter;

import android.databinding.BindingAdapter;
import android.widget.RadioButton;

import com.jakewharton.rxbinding2.widget.RxCompoundButton;
import com.rwz.baselist.entity.CommandEntity;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;


/**
 * Created by rwz on 2017/3/17.
 */

public class RadioButtonBindingAdapter {

    @BindingAdapter({"rbCheckChanged"})
    public static void onRbCheckChanged(RadioButton rb, final Consumer<CommandEntity> command) {
        if(command == null)
            return;
        final CommandEntity commandEntity = new CommandEntity(rb.getId(), false);
        RxCompoundButton.checkedChanges(rb).map(new Function<Boolean, CommandEntity>() {
            @Override
            public CommandEntity<Boolean> apply(Boolean isChecked) throws Exception {
                commandEntity.setT(isChecked);
                return commandEntity;
            }
        }).subscribe(command);
    }

}
