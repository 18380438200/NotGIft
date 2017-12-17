package cn.ahabox.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import cn.ahabox.application.MyApp;
import cn.ahabox.feiliwu_help.R;
import cn.ahabox.model.SpecialTopicEntity;

/**
 * Created by libo on 2016/3/1.
 *
 * 专题适配器
 */
public class SpecialTopicAdapter extends MyBaseAdapter<SpecialTopicEntity>{

    public SpecialTopicAdapter(Context context, List<SpecialTopicEntity> datas) {
        super(context, datas);
    }

    @Override
    public CommonAdapter createAdapter() {
        return new CommonAdapter(context,datas, R.layout.item_firstpage_quality) {
            @Override
            public void convert(BaseViewHolder baseViewHolder, Object o) {
                final SpecialTopicEntity entity = (SpecialTopicEntity)o;
                ImageView ivCover = baseViewHolder.getView(R.id.iv_quality_cover);

                ivCover.setBackgroundResource(R.mipmap.quality_default);
                ImageLoader.getInstance().displayImage(entity.getShow_picture(), ivCover, MyApp.getInstance().options);
            }
        };
    }
}
