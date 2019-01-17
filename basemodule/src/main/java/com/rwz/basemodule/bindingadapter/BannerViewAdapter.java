package com.rwz.basemodule.bindingadapter;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.rwz.baselist.entity.CommandEntity;
import com.rwz.basemodule.R;
import com.rwz.basemodule.entity.IBannerEntity;
import com.rwz.basemodule.entity.wrap.WrapList;
import com.rwz.basemodule.weidgt.CommonBanner;
import com.rwz.commonmodule.ImageLoader.ImageLoader;
import com.rwz.commonmodule.ImageLoader.ImageLoaderUtil;
import com.rwz.commonmodule.utils.app.ResourceUtil;
import com.rwz.commonmodule.utils.show.LogUtil;

import io.reactivex.functions.Consumer;


/**
 * Created by rwz on 2017/3/20.
 */

public class BannerViewAdapter {

    @BindingAdapter({"bannerData","onClickView"})
    public static void binnerData(final CommonBanner banner, final WrapList wrapList , final Consumer<CommandEntity> command) {
        if (wrapList != null && wrapList.getList() != null && wrapList.getList().size() > 0 && wrapList.getList().get(0) instanceof IBannerEntity) {
            //自定义你的Holder，实现更多复杂的界面，不一定是图片翻页，其他任何控件翻页亦可。
            banner.setPages(new CBViewHolderCreator<LocalImageHolderView>() {
                @Override
                public LocalImageHolderView createHolder() {
                    return new LocalImageHolderView();
                }
            }, wrapList.getList())
                    //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                    .setPageIndicator(new int[]{R.drawable.radius_art, R.drawable.radius_primary})
                    //设置指示器的方向
                    .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
                    .setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            if (command != null) {
                                try {
                                    command.accept(new CommandEntity(banner.getId(), wrapList.getList().get(position)));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
        }else{
            LogUtil.e("banner 实体类必须为IBannerEntity");
        }
    }


    public static class LocalImageHolderView implements Holder<IBannerEntity> {

        private ImageView imageView;
        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setAdjustViewBounds(true);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, final int position, IBannerEntity data) {
            int radius = ResourceUtil.getDimen(R.dimen.h_2);
            LogUtil.d("banner radius = " + radius);
            ImageLoaderUtil.getInstance()
                    .loadImage(context,new ImageLoader.Builder()
                            .url(data.getImgUrl())
                            .setCrossFade(0)
                            .isCenterCrop(true)
                            .setRounded(radius)
                            .imgView(imageView).build());
        }

    }
}
