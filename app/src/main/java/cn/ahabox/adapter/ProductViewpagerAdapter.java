package cn.ahabox.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import cn.ahabox.application.MyApp;
import cn.ahabox.widget.AdaptiveImageView;

/**
 * Created by libo on 2015/12/7.
 *
 * 商品详情轮播适配器
 */
public class ProductViewpagerAdapter extends PagerAdapter{
    private Context context;
    /** 商品介绍轮播图地址列表 */
    private List<String> datas;

    public ProductViewpagerAdapter(Context context, List<String> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }

    /**
     * 相当于baseadapter的getview，将复用的每一项数据显示到view中
     * @param container
     * @param position
     * @return
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        AdaptiveImageView imageView = new AdaptiveImageView(context);
        if(null != datas && datas.size() != 0){
            ImageLoader.getInstance().displayImage(datas.get(position % datas.size()),
                    imageView, MyApp.getInstance().options);
        }
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
