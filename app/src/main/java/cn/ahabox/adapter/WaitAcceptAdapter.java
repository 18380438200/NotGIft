package cn.ahabox.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import cn.ahabox.feiliwu_help.R;
import cn.ahabox.activity.WaitacceptOrderdetailActivity;
import cn.ahabox.application.MyApp;
import cn.ahabox.model.WaitAcceptEntity;
import cn.ahabox.utils.StrUtils;

/**
 * Created by libo on 2016/4/5.
 *
 * Ta未接收礼物适配器
 */
public class WaitAcceptAdapter extends MyBaseAdapter<WaitAcceptEntity>{

    public WaitAcceptAdapter(Context context, List<WaitAcceptEntity> datas) {
        super(context, datas);
    }

    @Override
    public CommonAdapter createAdapter() {
        return new CommonAdapter(context,datas, R.layout.item_orderlist) {
            @Override
            public void convert(BaseViewHolder baseViewHolder, Object o) {
                final WaitAcceptEntity entity = (WaitAcceptEntity) o;
                ImageView ivCover = baseViewHolder.getView(R.id.iv_orderlist_cover);
                TextView tvTime = baseViewHolder.getView(R.id.tv_orderlist_time);
                TextView tvCode = baseViewHolder.getView(R.id.tv_orderlist_transactionnum);
                TextView tvName = baseViewHolder.getView(R.id.tv_orderlist_name);
                TextView tvDetail = baseViewHolder.getView(R.id.tv_orderlist_detail);
                TextView tvPrice = baseViewHolder.getView(R.id.tv_orderlist_price);
                ivCover.setBackgroundResource(R.mipmap.fastchoose_default);

                ImageLoader.getInstance().displayImage(entity.getCover(), ivCover, MyApp.getInstance().options);
                String returnTime = String.valueOf(entity.getSend_link_at());
                tvTime.setText("将于" + StrUtils.getDateMinite(returnTime) + "退回礼物箱");
                tvCode.setText(entity.getCode());
                tvName.setText(entity.getProduct_name());
                tvPrice.setText(entity.getPrice());
                tvDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, WaitacceptOrderdetailActivity.class);
                        intent.putExtra("ordercode",entity.getCode());
                        context.startActivity(intent);
                    }
                });
            }
        };
    }

}
