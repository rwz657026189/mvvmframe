package com.rwz.basemodule.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.rwz.commonmodule.utils.app.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * 简单的Viewpager适配器
 */
public class SimpleViewAdapter<F> extends PagerAdapter {
    private final List<F> mViewList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public void update(List<F> list) {
        if (list != null && list.size() > 0) {
            mViewList.clear();
            mViewList.addAll(list);
            notifyDataSetChanged();
        }
    }

    public F getData(int pos) {
        if (pos < mViewList.size()) {
            return mViewList.get(pos);
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }


    @Override
    public int getCount() {
        return mViewList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        F f = Assert.isCursorValid(position, mViewList) ? mViewList.get(position) : null;
        if(f != null && f instanceof View)
            container.addView((View) f);
        return f;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}