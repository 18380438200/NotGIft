package cn.ahabox.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by libo on 2016/8/5.
 * 解决滑动冲突的scrollView
 */
public class TouchEventScrollView extends ScrollView {

    public TouchEventScrollView(Context context) {
        super(context);
    }
    public TouchEventScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public TouchEventScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

}
