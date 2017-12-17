package cn.ahabox.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import cn.ahabox.application.MyApp;
import cn.ahabox.feiliwu_help.R;
import cn.ahabox.model.ShopcartPayEntity;

/**
 * Created by libo on 2016/9/20.
 *
 * 购物车支付页面的商品适配器
 */
public class ShopcartPayAdapter extends MyBaseAdapter<ShopcartPayEntity>{

    public ShopcartPayAdapter(Context context, List<ShopcartPayEntity> datas) {
        super(context, datas);
    }

    @Override
    public CommonAdapter<ShopcartPayEntity> createAdapter() {
        return new CommonAdapter(context,datas, R.layout.product_info) {
            @Override
            public void convert(BaseViewHolder baseViewHolder, Object o) {
                ShopcartPayEntity entity = (ShopcartPayEntity) o;
                ImageView ivCover = baseViewHolder.getView(R.id.iv_wxpay_cover);
                TextView tvName = baseViewHolder.getView(R.id.tv_wxpay_introduce);
                TextView tvNum = baseViewHolder.getView(R.id.tv_info_num);
                TextView tvPrice = baseViewHolder.getView(R.id.tv_info_price);

                ImageLoader.getInstance().displayImage(entity.getCover_picture_display(), ivCover, MyApp.getInstance().options);
                tvName.setText(entity.getProduct_name());
                tvPrice.setText(entity.getProduct_price());
                tvNum.setText("" + entity.getQuantity());
            }
        };
    }
}
