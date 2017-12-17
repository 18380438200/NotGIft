package cn.ahabox.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.SparseArray;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cn.ahabox.activity.ProductDetailActivity;
import cn.ahabox.activity.ShopCartActivity;
import cn.ahabox.application.MyApp;
import cn.ahabox.config.Config;
import cn.ahabox.feiliwu_help.R;
import cn.ahabox.interfaces.CallBackimpl;
import cn.ahabox.model.ShopcartEntity;
import cn.ahabox.network.DataParserImpl;
import cn.ahabox.utils.DialogUtils;

/**
 * Created by libo on 2016/9/12.
 * 购物车适配器
 */
public class ShopcartAdapter extends MyBaseAdapter{
    private DialogUtils dialogUtils;
    private DataParserImpl dataParser;
    private SparseArray<Boolean> mSelectState;
    private CheckBox checkAll;

    public ShopcartAdapter(Context context, List<ShopcartEntity> datas,SparseArray<Boolean> mSelectState,CheckBox checkAll) {
        super(context, datas);
        dialogUtils = new DialogUtils(context);
        this.mSelectState = mSelectState;
        this.checkAll = checkAll;
        dataParser = new DataParserImpl();
    }

    @Override
    public CommonAdapter createAdapter() {
        return new CommonAdapterPosition<ShopcartEntity>(context,datas, R.layout.item_shopcart) {
            @Override
            public void convert(BaseViewHolder baseViewHolder, Object o, final int position) {
                final ShopcartEntity entity = (ShopcartEntity) o;
                final CheckBox cbChoose = baseViewHolder.getView(R.id.cb_shopcart);
                ImageView ivCover = baseViewHolder.getView(R.id.iv_shopcart_cover);
                TextView tvName = baseViewHolder.getView(R.id.tv_shopcart_name);
                TextView tvPrice = baseViewHolder.getView(R.id.tv_shopcart_price);
                ImageButton ibDel = baseViewHolder.getView(R.id.ib_shopcart_del);
                final TextView tvReduce = baseViewHolder.getView(R.id.tv_detail_reduce);
                TextView tvPlus = baseViewHolder.getView(R.id.tv_detail_plus);
                final TextView tvNum = baseViewHolder.getView(R.id.tv_detail_productnum);

                ImageLoader.getInstance().displayImage(entity.getProduct_first_cover_url(), ivCover, MyApp.options);
                tvName.setText(entity.getProduct_name());
                tvPrice.setText(entity.getProduct_price());
                tvNum.setText("" + entity.getQuantity());

                MyListener myListener = new MyListener(entity);
                ivCover.setOnClickListener(myListener);
                ibDel.setOnClickListener(myListener);

                final int id = entity.getId();
                cbChoose.setChecked(mSelectState.get(id, false));
                cbChoose.setOnClickListener(new View.OnClickListener() {     //用onclick方法而不是onChecked方法，因为是自动调用onCheckedChange方法
                    @Override
                    public void onClick(View v) {
                        //通过保存的是否选中来判断操作
                        boolean isSelected = !mSelectState.get(id,false);
                        cbChoose.setChecked(isSelected);
                        if(isSelected){
                            mSelectState.put(id, true);
                        }else{
                            mSelectState.delete(id);
                        }
                        checkAll.setChecked(mSelectState.size() == datas.size());   //判断是否达到全选
                        notifyDataSetChanged();
                    }
                });

                tvReduce.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int quatity = ((ShopcartEntity) datas.get(position)).getQuantity();
                        if(quatity == 1) return;
                        ((ShopcartEntity) datas.get(position)).setQuantity(quatity - 1);
                        notifyDataSetChanged();
                    }
                });
                tvPlus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int quatity = ((ShopcartEntity) datas.get(position)).getQuantity();
                        if(quatity >= entity.getProduct_quantity()){
                            Toast.makeText(context,"超出库存量",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        ((ShopcartEntity) datas.get(position)).setQuantity(quatity + 1);
                        notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void convert(BaseViewHolder baseViewHolder, Object o) {
            }
        };
    }

    class MyListener implements View.OnClickListener{
        private ShopcartEntity entity;

        public MyListener(ShopcartEntity entity){
            this.entity = entity;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.iv_shopcart_cover:
                    Intent intent = new Intent(context, ProductDetailActivity.class);
                    intent.putExtra(Config.PRODUCT_ID_KEY,entity.getProduct_id());
                    context.startActivity(intent);
                    break;
                case R.id.ib_shopcart_del:
                    del();
                    break;
            }
        }

        private void del(){
            dialogUtils.confirmCancelDialog("是否删除", "取消", "确定", new CallBackimpl() {
                @Override
                public void confirmHandle() {
                    dataParser.setCallBack(context, new CallBackimpl() {
                        @Override
                        public void callBack(String str) {
                            try {
                                JSONObject obj = new JSONObject(str);
                                int status = obj.getInt("status");
                                if(status == Config.STATUS_SUCCESSED){
                                    Toast.makeText(context,"删除成功",Toast.LENGTH_SHORT).show();
                                    ShopCartActivity.delCallBack.confirmHandle();
                                }else{
                                    Toast.makeText(context,obj.getString("data"),Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    dataParser.parseShopcartDel(entity.getId());
                }
            });
        }
    }
}
