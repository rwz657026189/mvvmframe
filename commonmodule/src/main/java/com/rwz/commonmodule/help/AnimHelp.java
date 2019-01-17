package com.rwz.commonmodule.help;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;

import com.rwz.commonmodule.R;
import com.rwz.commonmodule.base.BaseApplication;
import com.rwz.commonmodule.utils.show.LogUtil;

/**
 * Created by rwz on 2017/3/30.
 */

public class AnimHelp {

    private static final int DEF_DURATION = 1000;

    /**
     * 属性动画:先放大透明度降低,再缩小透明度增加
     * @param view
     * @return
     */
    public static AnimatorSet zoom(View view) {
        return zoom(view, 0.6f, 1.3f, 300);
    }

    public static AnimatorSet zoom(View view, float alpha, float scale, int duration) {
        if (view == null && alpha > 0 && scale > 0 && duration > 0) {
            return null;
        }
        /**
         * alpha、 scaleX 、scaleY并没有固定，只有对象中含有响应的set方法，就可以调用
         */
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(view, "alpha",  1, alpha);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(view, "scaleX", 1, scale);
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(view, "scaleY", 1, scale);

        ObjectAnimator animator4 = ObjectAnimator.ofFloat(view, "alpha",  alpha, 1);
        ObjectAnimator animator5 = ObjectAnimator.ofFloat(view, "scaleX", scale, 1f);
        ObjectAnimator animator6 = ObjectAnimator.ofFloat(view, "scaleY", scale, 1f);
        AnimatorSet set1=new AnimatorSet();
        set1.playTogether(animator1,animator2,animator3); //同步触发
        AnimatorSet set2=new AnimatorSet();
        set2.playTogether(animator4,animator5,animator6); //同步触发
        AnimatorSet set3=new AnimatorSet();

        set3.playSequentially(set1 ,set2);//先后触发
        set3.setInterpolator(new AccelerateDecelerateInterpolator());//（加速减速插值器） 动画两头慢，中间快。
        set3.setDuration(duration);

        return set3;
    }

    public static void startFadeInAnim(View target) {
        startFadeInAnim(target,DEF_DURATION);
    }

    /** 淡入淡出动画 **/
    public static void startFadeInAnim(View target,int duration) {
        if (target == null) {
            return;
        }
        LogUtil.d("开始淡入动画");
        Animation animation = target.getAnimation();
        if (animation != null) {
            animation.cancel();
        }
        Animation anim = new AlphaAnimation(0, 1);
        anim.setDuration(duration);
        anim.setFillAfter(true);
        anim.setFillEnabled(true);
        target.startAnimation(anim);
    }

    /** 淡出动画 **/
    public static void startFadeOutAnim(View target) {
        startFadeOutAnim(target,DEF_DURATION);
    }
    public static void startFadeOutAnim(View target,int duration) {
        if (target == null || duration <= 0) {
            return;
        }
        LogUtil.d("开始淡出动画");
        Animation animation = target.getAnimation();
        if (animation != null) {
            animation.cancel();
        }
        Animation anim = new AlphaAnimation(1, 0);
        anim.setDuration(duration);
        anim.setFillAfter(true);
        anim.setFillEnabled(true);
        target.startAnimation(anim);
    }

    /**
     * 绕中心点循环旋转动画
     * @param target
     * @param duration
     * @return
     */
    public static Animation rotateAnimForever(View target,int duration) {
        if (target == null || duration <= 0) {
            return null;
        }
        Animation animation = target.getAnimation();
        if (animation != null) {
            animation.cancel();
        }
        Animation anim = AnimationUtils.loadAnimation(BaseApplication.getInstance(), R.anim.rotate_forever);
        anim.setInterpolator(new LinearInterpolator());//匀速
        target.setAnimation(anim);
        return anim;
    }

    /**
     * 绕中心点循环旋转动画、 属性动画
     * @param target
     * @param duration
     * @return
     */
    public static ValueAnimator rotateAnimForeverAttr(View target, int duration) {
        if (target == null || duration <= 0) {
            return null;
        }
        float rotation = target.getRotation();
        LogUtil.d("rotateAnimForeverAttr", "rotation = " + rotation);
        ObjectAnimator anim = ObjectAnimator.ofFloat(target, "rotation", rotation, rotation + 360f);
        anim.setDuration(duration);
        anim.setRepeatCount(ValueAnimator.INFINITE); //无限循环
        anim.setInterpolator(new LinearInterpolator());//匀速
        return anim;
    }

    /**
     * 纵向位移属性动画
     * @param target
     * @param duration
     * @param startY
     * @param endY
     * @return
     */
    public static ValueAnimator tranlateYAnimAttr(View target, int duration, int startY, int endY) {
        if (checkParams(target, duration)) {
            ObjectAnimator anim = ObjectAnimator.ofFloat(target, "translationY", startY, endY);
            anim.setDuration(duration);
            return anim;
        }
        return null;
    }

    /**
     * 等比大小缩放 属性动画
     * @param target
     * @param duration
     * @param startScale  1f表示原尺寸
     * @param endScale 1f表示原尺寸
     * @return
     */
    public static Animator scaleAnimAttr(View target, int duration, float startScale, float endScale) {
        if (checkParams(target, duration) && startScale >= 0f && endScale >= 0f) {
            int width = target.getMeasuredWidth();
            int height = target.getMeasuredHeight();
            ObjectAnimator animX = ObjectAnimator.ofFloat(target, "scaleX", width * startScale, width * endScale);
            ObjectAnimator animY = ObjectAnimator.ofFloat(target, "scaleY", height * startScale, height * endScale);
            AnimatorSet set = new AnimatorSet();
            set.playTogether(animX, animY); //同步触发
            return set;
        }
        return null;
    }

    private static boolean checkParams(View target, int duration) {
        return target != null || duration > 0;
    }


}
