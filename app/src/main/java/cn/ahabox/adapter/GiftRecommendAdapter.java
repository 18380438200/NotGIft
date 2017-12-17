package cn.ahabox.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import cn.ahabox.application.MyApp;
import cn.ahabox.feiliwu_help.R;
import cn.ahabox.model.RecommendEntity;

/**
 * Created by libo on 2016/3/6.
 *
 * 首页礼物推荐适配器
 */
public class GiftRecommendAdapter extends MyBaseAdapter<RecommendEntity>{

    public GiftRecommendAdapter(Context context, List<RecommendEntity> datas) {
        super(context, datas);
    }

    @Override
    public CommonAdapter createAdapter() {
        return new CommonAdapter(context,datas, R.layout.item_firstpage_gift) {
            @Override
            public void convert(BaseViewHolder baseViewHolder, Object o) {
                RecommendEntity entity = (RecommendEntity)o;
                ImageView imageView = baseViewHolder.getView(R.id.iv_firstpage_product);
                imageView.setBackgroundResource(R.mipmap.quality_default);
                ImageLoader.getInstance().displayImage(entity.getCover_picture_url(), imageView, MyApp.getInstance().options);
            }
        };
    }
}
