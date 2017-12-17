package cn.ahabox.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cn.ahabox.activity.OrderDetailActivity;
import cn.ahabox.application.MyApp;
import cn.ahabox.config.Config;
import cn.ahabox.feiliwu_help.R;
import cn.ahabox.interfaces.CallBackimpl;
import cn.ahabox.model.OrderEntity;
import cn.ahabox.network.DataParserImpl;
import cn.ahabox.utils.DialogUtils;
import cn.ahabox.utils.StrUtils;

/**
 * Created by libo on 2016/3/31.
 *
 * 订单列表的适配器
 */
public class OrderListAdapter extends MyBaseAdapter<OrderEntity>{
    private DataParserImpl dataParser;
    private DialogUtils dialogUtils;

    public OrderListAdapter(Context context, List<OrderEntity> datas,DataParserImpl dataParser) {
        super(context, datas);
        this.dataParser = dataParser;
        dialogUtils = new DialogUtils(context);
    }

    @Override
    public CommonAdapterPosition createAdapter() {
        return new CommonAdapterPosition(context, datas, R.layout.item_orderlist) {
            @Override
            public void convert(BaseViewHolder baseViewHolder, Object o, final int position) {
                final OrderEntity entity = (OrderEntity) o;
                ImageView ivCover = baseViewHolder.getView(R.id.iv_orderlist_cover);
                TextView tvBuyTime = baseViewHolder.getView(R.id.tv_orderlist_time);
                TextView tvCode = baseViewHolder.getView(R.id.tv_orderlist_transactionnum);
                TextView tvName = baseViewHolder.getView(R.id.tv_orderlist_name);
                TextView tvPrice = baseViewHolder.getView(R.id.tv_orderlist_price);
                TextView tvDel = baseViewHolder.getView(R.id.tv_orderlist_delete);
                TextView tvOrderDetail = baseViewHolder.getView(R.id.tv_orderlist_detail);
                ivCover.setBackgroundResource(R.mipmap.fastchoose_default);

                ImageLoader.getInstance().displayImage(entity.getCover(), ivCover, MyApp.getInstance().options);
                String buyTime = StrUtils.getDateTime(String.valueOf(entity.getPay_at()));
                tvBuyTime.setText(buyTime + "  购买");
                tvCode.setText(entity.getCode());
                tvName.setText(entity.getProduct_name());
                tvPrice.setText(entity.getPrice());
                tvOrderDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, OrderDetailActivity.class);
                        intent.putExtra("ordercode", entity.getCode());
                        context.startActivity(intent);
                    }
                });

                ivCover.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, OrderDetailActivity.class);
                        intent.putExtra("ordercode", entity.getCode());
                        context.startActivity(intent);
                    }
                });

                tvDel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogUtils.confirmCancelDialog("是否确认删除", "取消", "确认", new CallBackimpl() {
                            @Override
                            public void confirmHandle() {
                                orderDel(position,entity.getCode());
                            }
                        });
                    }
                });
            }

            @Override
            public void convert(BaseViewHolder baseViewHolder, Object o) {}

        };
    }

    private void orderDel(final int position,String orderCode){
        dataParser.setCallBack(context, new CallBackimpl() {
            @Override
            public void callBack(String str) {
                try {
                    JSONObject obj = new JSONObject(str);
                    int status = obj.getInt("status");
                    if(status == Config.STATUS_SUCCESSED){    //删除成功删除当前项
                        Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                        datas.remove(position);
                        getAdapter().notifyDataSetChanged();
                    }else{
                        Toast.makeText(context,obj.getString("errmsg"),Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        dataParser.parseOrderDel(orderCode);
    }

}
