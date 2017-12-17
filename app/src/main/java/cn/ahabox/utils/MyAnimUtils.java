package cn.ahabox.utils;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;

import cn.ahabox.feiliwu_help.R;

/**
 * Created by libo on 2016/3/21.
 *
 * 动画工具类
 */
public class MyAnimUtils {

    public static AnimationSet animationSet;

    public static void shakeAnim(Context context,View view){
        Animation shakeAnimation = android.view.animation.AnimationUtils.loadAnimation(context, R.anim.input_error);
        view.startAnimation(shakeAnimation);
    }

    public static AnimationSet getHeartAnim() {
        if (null == animationSet) {
            animationSet = new AnimationSet(true);
            Animation bigAnimation = new ScaleAnimation(0, 1, 0, 1,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
            bigAnimation.setDuration(600);
            animationSet.addAnimation(bigAnimation);
        }
        return animationSet;
    }

}
