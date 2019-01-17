package com.rwz.basemodule.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import com.rwz.commonmodule.utils.show.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 简单的Viewpager适配器
 */
public class SimpleVPAdapter<F extends Fragment> extends FragmentPagerAdapter {
    private final List<F> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();
    private final FragmentManager mFragmentManager;

    public SimpleVPAdapter(FragmentManager fm) {
        super(fm);
        mFragmentManager = fm;
    }

    /**
     * 清空缓存的fragment，一般在activity.onCreate()中操作
     */
    public void clearCacheFragments() {
        if(mFragmentManager == null)
            return;
        List<Fragment> list = mFragmentManager.getFragments();
        LogUtil.d("SimpleVPAdapter, clearCacheFragments", list);
        if (list != null && list.size() > 0) {
            try {
                FragmentTransaction ft = mFragmentManager.beginTransaction();
                for (Fragment fragment : list) {
                    ft.remove(fragment);
                }
                ft.commitAllowingStateLoss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    public void addFrag(F fragment) {
        addFrag(fragment, "");
    }

    public void addFrag(F fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    public void update(List<F> fragments) {
        if (fragments != null && fragments.size() > 0) {
            mFragmentList.clear();
            mFragmentList.addAll(fragments);
            notifyDataSetChanged();
        }
    }

    public List<F> getFragmentList() {
        return mFragmentList;
    }

    public List<String> getFragmentTitleList() {
        return mFragmentTitleList;
    }

    public F getFragment(int pos) {
        if (pos < mFragmentList.size()) {
            return mFragmentList.get(pos);
        }
        return null;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        if (mFragmentTitleList != null && mFragmentTitleList.size() > position) {
            return mFragmentTitleList.get(position);
        } else {
            return null;
        }
    }

    @Override
    public F getItem(int position) {
        if (mFragmentList != null && position < mFragmentList.size()) {
            return mFragmentList.get(position);
        } else {
            return null;
        }
    }

    @Override
    public int getCount() {
        return mFragmentList == null ? 0 : mFragmentList.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
    }
}