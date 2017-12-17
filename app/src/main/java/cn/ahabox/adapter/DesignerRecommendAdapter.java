package cn.ahabox.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import cn.ahabox.feiliwu_help.R;
import cn.ahabox.application.MyApp;
import cn.ahabox.model.FirstPageDesignerEntity;
import cn.ahabox.utils.StrUtils;
import cn.ahabox.widget.VoicePlayer;

/**
 * Created by libo on 2016/3/6.
 */
public class DesignerRecommendAdapter extends MyBaseAdapter<FirstPageDesignerEntity>{

    public DesignerRecommendAdapter(Context context, List<FirstPageDesignerEntity> datas) {
        super(context, datas);
    }

    @Override
    public CommonAdapter createAdapter() {
        return new CommonAdapter(context,datas, R.layout.item_firstpage_designer) {
            @Override
            public void convert(BaseViewHolder baseViewHolder, Object o) {
                final FirstPageDesignerEntity entity = (FirstPageDesignerEntity)o;
                ImageView ivCover = baseViewHolder.getView(R.id.iv_firstpage_designer);
                VoicePlayer voicePlayer = baseViewHolder.getView(R.id.rl_player);

                ivCover.setBackgroundResource(R.mipmap.quality_default);
                if(!StrUtils.isStr(entity.getMessage_url())){
                    voicePlayer.setVisibility(View.GONE);
                }else {
                    voicePlayer.init(context,entity.getMessage_url());
                }

                ImageLoader.getInstance().displayImage(entity.getCover_picture_url(), ivCover, MyApp.getInstance().options);
            }
        };
    }
}
