package cn.ahabox.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ahabox.feiliwu_help.R;
import cn.ahabox.adapter.ViewPagerAdapter;
import cn.ahabox.utils.SharedPrefUtils;

/**
 * Created by libo on 2015/11/24.
 *
 * 应用第一次进入引导界面
 */
public class GuideActivity extends Activity {

    @Bind(R.id.viewpager_guide)
    ViewPager viewpagerGuide;
    @Bind(R.id.btn_guide_enter)
    Button btnGuideEnter;
    final ArrayList<View> views = new ArrayList<>();
    @Bind(R.id.linear_guide)
    LinearLayout linearGuide;
    /** 引导页图片 */
    private int[] images = {R.mipmap.slide1,R.mipmap.slide2,R.mipmap.slide3,R.mipmap.slide4};
    private int currentIndex = 0;

    /** 获取是否为第一次进入布尔值得键*/
    private final String ISFIRSTIN = "IsFirstIn";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide);
        ButterKnife.bind(this);

        initViews();
    }

    private void initViews() {

        //初始化引导图片列表
        for (int i = 0; i < images.length; i++) {
            ImageView iv = new ImageView(this);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            iv.setImageResource(images[i]);
            views.add(iv);
            initDots();
        }

        linearGuide.getChildAt(currentIndex).setBackgroundResource(R.mipmap.guide_order_current);
        viewpagerGuide.setOnPageChangeListener(new MyPageChangeListener());
        viewpagerGuide.setAdapter(new ViewPagerAdapter(this, views));
    }

    /**
     * 初始化圆点
     */
    private void initDots(){
        View view = new View(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(25,25);
        layoutParams.setMargins(20, 0, 0, 0);
        view.setLayoutParams(layoutParams);
        view.setBackgroundResource(R.mipmap.guide_order_nochoice);
        linearGuide.addView(view);
    }

    @OnClick(R.id.btn_guide_enter)
    void onClick() {
        //进入应用存入非第一次进入
        SharedPrefUtils.setParams(this, ISFIRSTIN, false);
        startActivity(new Intent(GuideActivity.this, MainActivity.class));
        finish();
    }

    class MyPageChangeListener implements OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int position) {
            linearGuide.getChildAt(position).setBackgroundResource(R.mipmap.guide_order_current);
            linearGuide.getChildAt(currentIndex).setBackgroundResource(R.mipmap.guide_order_nochoice);
            currentIndex = position;
            if(position == images.length-1) {
                btnGuideEnter.setVisibility(View.VISIBLE);
                Animation alphaAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alphaanim);
                btnGuideEnter.startAnimation(alphaAnim);
            }
            else if(position == images.length-2){
                btnGuideEnter.setVisibility(View.GONE);
            }
        }

    }

}

