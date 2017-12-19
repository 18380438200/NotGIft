package cn.ahabox.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import cn.ahabox.activity.ProductDetailActivity;
import cn.ahabox.activity.WebViewActivity;
import cn.ahabox.application.MyApp;
import cn.ahabox.config.Config;
import cn.ahabox.model.FirstPageBannerEntity;
import cn.ahabox.widget.AdaptiveImageView;

/**
 * Created by libo on 2015/12/7.
 *
 * 首页图片轮播适配器
 */
public class BannerViewpagerAdapter extends PagerAdapter{
    private Context context;
    private List<FirstPageBannerEntity> datas;
    private final int WEB_TYPE = 1,PRODUCT_TYPE = 2;

    public BannerViewpagerAdapter(Context context, List<FirstPageBannerEntity> datas) {
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
    public Object instantiateItem(final ViewGroup container, final int position) {
        AdaptiveImageView imageView = new AdaptiveImageView(context);
        ImageLoader.getInstance().displayImage(datas.get(position % datas.size()).getCover_picture_url(),
                imageView, MyApp.getInstance().options);
        container.addView(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断type类型，0不能点击，1web页面，2商品id
                FirstPageBannerEntity entity = datas.get(position % datas.size());
                if (entity.getType() == PRODUCT_TYPE) {
                    //商品详情页
                    Intent intent = new Intent(context, ProductDetailActivity.class);
                    intent.putExtra(Config.PRODUCT_ID_KEY, Integer.parseInt(entity.getProduct_id()));
                    context.startActivity(intent);
                } else if (entity.getType() == WEB_TYPE) {
                    //webview页
                    Intent intent = new Intent(context, WebViewActivity.class);
                    intent.putExtra(Config.WEBVIEW_URL, entity.getUrl());
                    context.startActivity(intent);
                }
            }
        });
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        
    }
}
