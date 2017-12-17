package cn.ahabox.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import cn.ahabox.application.MyApp;
import cn.ahabox.feiliwu_help.R;
import cn.ahabox.model.FirstPageQualityEntity;

/**
 * Created by libo on 2016/3/6.
 *
 * 精品推荐适配器
 */
public class QualityRecommendAdapter extends MyBaseAdapter<FirstPageQualityEntity>{

    public QualityRecommendAdapter(Context context, List<FirstPageQualityEntity> datas) {
        super(context, datas);
    }

    @Override
    public CommonAdapterPosition createAdapter() {
        return new CommonAdapterPosition(context,datas, R.layout.item_firstpage_quality) {
            @Override
            public void convert(BaseViewHolder baseViewHolder, Object o, int position) {
                final FirstPageQualityEntity entity = (FirstPageQualityEntity)o;
                ImageView ivCover = baseViewHolder.getView(R.id.iv_quality_cover);

                ivCover.setBackgroundResource(R.mipmap.quality_default);
                ImageLoader.getInstance().displayImage(entity.getCover_picture_url(), ivCover, MyApp.getInstance().options);
            }

            @Override
            public void convert(BaseViewHolder baseViewHolder, Object o) {}
        };
    }
}
