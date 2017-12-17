package cn.ahabox.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import cn.ahabox.feiliwu_help.R;
import cn.ahabox.application.MyApp;
import cn.ahabox.config.App;
import cn.ahabox.model.CreateOrderEntity;
import cn.ahabox.model.WaitSendEntity;
import cn.ahabox.utils.AddressDialog;

/**
 * Created by libo on 2016/4/5.
 *
 * 为送出礼物适配器
 */
public class WaitSendAdapter extends MyBaseAdapter<WaitSendEntity>{


    public WaitSendAdapter(Context context, List<WaitSendEntity> datas) {
        super(context, datas);
    }

    @Override
    public CommonAdapter createAdapter() {
        return new CommonAdapter(context,datas, R.layout.item_giftbox_waitsend) {
            @Override
            public void convert(BaseViewHolder baseViewHolder, Object o) {
                final WaitSendEntity entity = (WaitSendEntity) o;
                ImageView ivCover = baseViewHolder.getView(R.id.iv_waitsend_cover);
                TextView tvCode = baseViewHolder.getView(R.id.tv_waitsend_code);
                TextView tvName = baseViewHolder.getView(R.id.tv_waitsend_name);
                TextView tvSend = baseViewHolder.getView(R.id.tv_waitsend_send);
                TextView tvPrice = baseViewHolder.getView(R.id.tv_waitsend_price);
                ivCover.setBackgroundResource(R.mipmap.fastchoose_default);

                ImageLoader.getInstance().displayImage(entity.getCover(),ivCover, MyApp.getInstance().options);
                tvCode.setText(entity.getCode());
                tvName.setText(entity.getProduct_name());
                tvPrice.setText(entity.getPrice());

                tvSend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CreateOrderEntity entity1 = new CreateOrderEntity();
                        entity1.setCover_url(entity.getCover());
                        entity1.setRealPrice(entity.getPrice());
                        entity1.setProduce_name(entity.getProduct_name());
                        entity1.setCode(entity.getCode());
                        entity1.setQuantity(1);
                        entity1.setRealTotalamount(entity.getPrice());
                        App.orderEntity = entity1;
                        App.orderids = entity.getCode();
                        new AddressDialog(context);
                    }
                });
            }
        };
    }
}
