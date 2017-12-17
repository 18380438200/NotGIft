package cn.ahabox.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import cn.ahabox.feiliwu_help.R;
import cn.ahabox.application.MyApp;
import cn.ahabox.model.BrandEntity;

/**
 * Created by libo on 2016/3/1.
 *
 * 品牌汇适配器
 */
public class BrandAdapter extends MyBaseAdapter<BrandEntity>{

    public BrandAdapter(Context context, List<BrandEntity> datas) {
        super(context, datas);
    }

    @Override
    public CommonAdapter createAdapter() {
        return new CommonAdapter(context,datas, R.layout.item_portrait) {
            @Override
            public void convert(BaseViewHolder baseViewHolder, Object o) {
                BrandEntity entity = (BrandEntity)o;
                ImageView ivAvater = baseViewHolder.getView(R.id.iv_portrait_pic);
                TextView tvName = baseViewHolder.getView(R.id.tv_portrait_name);
                TextView tvIntro = baseViewHolder.getView(R.id.tv_portrait_intro);

                ImageLoader.getInstance().displayImage(entity.getThumbnail_url(),ivAvater, MyApp.getInstance().options);
                tvName.setText(entity.getName());
                tvIntro.setText(entity.getIntroduce());
            }
        };
    }
}
