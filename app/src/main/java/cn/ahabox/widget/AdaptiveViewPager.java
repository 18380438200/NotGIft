package cn.ahabox.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by libo on 2016/4/29.
 *
 * 自适应高度viewpager
 */
public class AdaptiveViewPager extends ViewPager{

    public AdaptiveViewPager(Context context) {
        super(context);
    }

    public AdaptiveViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //遍历所有子控件，将高度最大的设置为ViewPager的高度
        int height = 0;
        for(int i=0;i<getChildCount();i++){
            View child = getChildAt(i);
            int h = child.getMeasuredHeight();
            if(h > height){
                height = h;
            }
        }
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height,MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
