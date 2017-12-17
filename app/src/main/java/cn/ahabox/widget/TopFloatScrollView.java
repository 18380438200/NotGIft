package cn.ahabox.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by libo on 2016/1/19.
 *
 * 自定义带有浮动菜单栏的ScrollView
 */
public class TopFloatScrollView extends ScrollView{
    private OnScrollListener onScrollListener;
    private int lastScrollY;

    public TopFloatScrollView(Context context) {
        super(context);
    }

    public TopFloatScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TopFloatScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 设置滚动接口
     * @param onScrollListener
     */
    public void setOnScrollListener(OnScrollListener onScrollListener){
        this.onScrollListener = onScrollListener;
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            int scrollY = TopFloatScrollView.this.getScrollY();

            if(lastScrollY != scrollY){
                lastScrollY = scrollY;
                handler.sendMessageDelayed(handler.obtainMessage(), 5);
            }
            if(null != onScrollListener){
                onScrollListener.onScroll(scrollY);
            }
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(null != onScrollListener){
            onScrollListener.onScroll(lastScrollY = this.getScrollY());
        }
        switch (ev.getAction()){
            case MotionEvent.ACTION_UP:
                handler.sendMessageDelayed(handler.obtainMessage(), 20);
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 滚动回调接口
     */
    public interface OnScrollListener{
        /**
         * 在滚动的过程中返回ScrollView的纵坐标
         * @param scrollY
         */
        public void onScroll(int scrollY);
    }



}
