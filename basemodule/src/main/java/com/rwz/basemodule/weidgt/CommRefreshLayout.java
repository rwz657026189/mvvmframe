package com.rwz.basemodule.weidgt;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import com.rwz.basemodule.R;
import com.rwz.commonmodule.base.BaseApplication;
import com.rwz.commonmodule.utils.app.ResourceUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

/**
 * Created by rwz on 2018/7/25.
 *
 * 参考： https://github.com/scwang90/SmartRefreshLayout/blob/master/art/md_property.md
 *
 */

public class CommRefreshLayout extends SmartRefreshLayout{

    public CommRefreshLayout(Context context) {
        super(context);
    }

    public CommRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CommRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    static {

        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @NonNull
            @Override
            public RefreshFooter createRefreshFooter(@NonNull Context context, @NonNull RefreshLayout layout) {
                return new ClassicsFooter(BaseApplication.getInstance());
            }
        });

        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(@NonNull Context context, @NonNull RefreshLayout layout) {
                return new ClassicsHeader(BaseApplication.getInstance());
            }
        });

    }

    public static void resetLanguage() {
        ClassicsFooter.REFRESH_FOOTER_PULLUP = ResourceUtil.getString(R.string.refresh_footer_pull_up);
        ClassicsFooter.REFRESH_FOOTER_RELEASE = ResourceUtil.getString(R.string.refresh_footer_release);
        ClassicsFooter.REFRESH_FOOTER_LOADING = ResourceUtil.getString(R.string.temp_loading);
        ClassicsFooter.REFRESH_FOOTER_REFRESHING = ResourceUtil.getString(R.string.refresh_footer_refreshing);
        ClassicsFooter.REFRESH_FOOTER_FINISH = ResourceUtil.getString(R.string.refresh_footer_finish);
        ClassicsFooter.REFRESH_FOOTER_FAILED = ResourceUtil.getString(R.string.load_error);
        ClassicsFooter.REFRESH_FOOTER_ALLLOADED = ResourceUtil.getString(R.string.refresh_footer_no_more);

        ClassicsHeader.REFRESH_HEADER_PULLDOWN = ResourceUtil.getString(R.string.refresh_header_pull_down);
        ClassicsHeader.REFRESH_HEADER_REFRESHING = ResourceUtil.getString(R.string.refresh_footer_refreshing);
        ClassicsHeader.REFRESH_HEADER_LOADING = ResourceUtil.getString(R.string.temp_loading);
        ClassicsHeader.REFRESH_HEADER_RELEASE = ResourceUtil.getString(R.string.refresh_header_release);
        ClassicsHeader.REFRESH_HEADER_FINISH = ResourceUtil.getString(R.string.refresh_header_finish);
        ClassicsHeader.REFRESH_HEADER_FAILED = ResourceUtil.getString(R.string.refresh_header_failed);
        ClassicsHeader.REFRESH_HEADER_LASTTIME = ResourceUtil.getString(R.string.refresh_header_last_time);
//        ClassicsHeader.REFRESH_HEADER_SECOND_FLOOR = ResourceUtil.getString(R.string.refresh_footer_pull_up);
    }

}
