package com.rwz.ui.tv;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by rwz on 2017/8/2.
 */

public class SelectorTextView extends AppCompatTextView{


    private WrapListener wrapListener;

    public SelectorTextView(Context context) {
        super(context);
    }

    public SelectorTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SelectorTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        setOnTouchListener(listener);
    }

    final OnTouchListener listener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (wrapListener != null) {
                    OnClickListener listener = wrapListener.listener;
                    if (listener != null) {
                        listener.onClick(v);
                    }
                }
            }
            return false;
        }
    };

    @Override
    public void setOnClickListener(OnClickListener l) {
        if (l != null) {
            wrapListener = new WrapListener(l);
            super.setOnClickListener(wrapListener);
        }
    }

    private class WrapListener implements OnClickListener {

        private final OnClickListener listener;

        public WrapListener(OnClickListener listener) {
            this.listener = listener;
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onClick(v);
            }
        }
    }

}
