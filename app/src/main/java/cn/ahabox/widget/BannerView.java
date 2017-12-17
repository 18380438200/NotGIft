package cn.ahabox.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import java.util.List;
import cn.ahabox.feiliwu_help.R;

/**
 * Created by libo on 2015/12/7.
 *
 * 封装活动轮播图类
 */
public class BannerView {
    private Context context;
    private ViewPager viewPager;
    public PagerAdapter pagerAdapter;
    /** 自动轮播时间间隔 */
    private final int DELAY_TIME = 4000;
    public LinearLayout linearLayout;
    public View bannerView;
    private List datas;
    private final int SCROLL_WHAT = 0;
    private LinearLayout.LayoutParams params1,params2;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            handler.sendEmptyMessageDelayed(SCROLL_WHAT,DELAY_TIME);
        }
    };

    public BannerView(Context context,List datas,PagerAdapter pagerAdapter,int layout){
        this.context = context;
        this.datas = datas;
        this.pagerAdapter = pagerAdapter;
        bannerView = LayoutInflater.from(context).inflate(layout,null);
        initView();
        event();
    }

    private void initView() {
        linearLayout = (LinearLayout) bannerView.findViewById(R.id.dot_linear);
        viewPager = (ViewPager) bannerView.findViewById(R.id.viewpager);
        //设置默认viewpager当前项
        viewPager.setCurrentItem(datas.size()*1024);
        viewPager.setAdapter(pagerAdapter);
        pagerAdapter.notifyDataSetChanged();
        handler.sendEmptyMessageDelayed(SCROLL_WHAT, DELAY_TIME);

        params1 = new LinearLayout.LayoutParams(13,13);
        params1.leftMargin = 13;
        params2 = new LinearLayout.LayoutParams(18,18);
        params2.leftMargin = 13;
        initDot();
    }

    public ViewPager getViewPager(){
        return viewPager;
    }

    private void event(){
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                setCurrentdot();
            }

            @Override
            public void onPageSelected(int i) {
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });

    }

    /**
     * 创建小圆点
     */
    private void initDot(){
        View dot;
        for(int i=0;i<datas.size();i++){
            dot = new View(context);
            if(i == 0){
                dot.setLayoutParams(params2);
                dot.setBackgroundResource(R.drawable.dot_focus);
            }else{
                dot.setLayoutParams(params1);
                dot.setBackgroundResource(R.drawable.dot_unfocus);
            }
            linearLayout.addView(dot);
        }
    }

    /**
     * 根据viewPager项切换指示点位置
     */
    private void setCurrentdot(){
        if(!datas.isEmpty()) {
            int currentPage = viewPager.getCurrentItem() % datas.size();
            for (int i = 0; i < linearLayout.getChildCount(); i++) {
                View dot = linearLayout.getChildAt(i);
                if(i == currentPage){
                    dot.setLayoutParams(params2);
                    dot.setBackgroundResource(R.drawable.dot_focus);
                }else{
                    dot.setLayoutParams(params1);
                    dot.setBackgroundResource(R.drawable.dot_unfocus);
                }

            }
        }
    }

}
