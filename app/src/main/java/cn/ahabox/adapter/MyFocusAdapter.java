package cn.ahabox.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import cn.ahabox.application.MyApp;
import cn.ahabox.feiliwu_help.R;
import cn.ahabox.model.DesignerFocusEntity;

/**
 * Created by libo on 2016/6/15.
 */
public class MyFocusAdapter extends MyBaseAdapter<DesignerFocusEntity>{

    public MyFocusAdapter(Context context, List<DesignerFocusEntity> datas) {
        super(context, datas);
    }

    @Override
    public CommonAdapter createAdapter() {
        return new CommonAdapter(context,datas, R.layout.item_portrait) {
            @Override
            public void convert(BaseViewHolder baseViewHolder, Object o) {
                DesignerFocusEntity entity = (DesignerFocusEntity)o;
                ImageView ivAvater = baseViewHolder.getView(R.id.iv_portrait_pic);
                TextView tvName = baseViewHolder.getView(R.id.tv_portrait_name);
                TextView tvIntro = baseViewHolder.getView(R.id.tv_portrait_intro);

                ImageLoader.getInstance().displayImage(entity.getThumbnail_url(),ivAvater, MyApp.getInstance().options);
                tvName.setText(entity.getName());
                tvIntro.setText(entity.getSummary());
            }
        };
    }
}
