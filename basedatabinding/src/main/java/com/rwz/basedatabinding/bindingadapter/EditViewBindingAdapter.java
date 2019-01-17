package com.rwz.basedatabinding.bindingadapter;

import android.databinding.BindingAdapter;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewAfterTextChangeEvent;
import com.rwz.baselist.entity.CommandEntity;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;


/**
 * Created by rwz on 2017/3/24.
 */

public class EditViewBindingAdapter {

    @BindingAdapter("afterTextChanged")
    public static void afterTextChanged(final EditText view, final Consumer<CommandEntity> command) {
        if(command == null)
            return;
        RxTextView.afterTextChangeEvents(view)
                .map(new Function<TextViewAfterTextChangeEvent, CommandEntity<Editable>>() {
                    @Override
                    public CommandEntity<Editable> apply(TextViewAfterTextChangeEvent tv) throws Exception {
                        return new CommandEntity<>(tv.view().getId(), tv.editable());
                    }
                })
                .subscribe(command);
    }

    @BindingAdapter("onEnterKeyDown")
    public static void onEnterKeyDown(final EditText view, final Consumer<CommandEntity> command) {
        view.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if( event != null && EditorInfo.IME_ACTION_SEARCH == event.getAction()){
                    try {
                        command.accept(new CommandEntity(v.getId(), v.getText() + ""));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event != null && keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP){
                    try {
                        command.accept(new CommandEntity(v.getId(), ((TextView)v).getText() + ""));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });
    }

}
