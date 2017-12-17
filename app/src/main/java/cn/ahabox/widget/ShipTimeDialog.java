package cn.ahabox.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cn.ahabox.activity.BuymyselfAddressActivity;
import cn.ahabox.activity.WXpayActivity;
import cn.ahabox.config.App;
import cn.ahabox.config.Config;
import cn.ahabox.feiliwu_help.R;
import cn.ahabox.interfaces.Api;
import cn.ahabox.interfaces.CallBackimpl;
import cn.ahabox.model.CreateOrderEntity;
import cn.ahabox.model.ProductDetailEntity;
import cn.ahabox.utils.DialogUtils;
import cn.ahabox.utils.Request;
import cn.ahabox.utils.StrUtils;

/**
 * Created by libo on 2016/9/13.
 *
 * 发货时间提示弹出框
 */
public class ShipTimeDialog {
    private Context context;
    private ProductDetailEntity entity;
    private int productId;
    private Dialog dialog;
    private DialogUtils dialogUtils;
    private final String BUY_MYSELF = "send_to_myself";
    private final String BUY_OTHER = "send_to_another";
    private final String BUY_DONATIONS = "donations";
    /** 加入到购物车或者直接购买的数量 */
    private int quantity = 1;
    private CallBackimpl callBackimpl;

    public ShipTimeDialog(Context context,ProductDetailEntity entity,int productId){
        this.context = context;
        this.entity = entity;
        this.productId = productId;
        this.dialogUtils = new DialogUtils(context);
    }

    /**
     * 提示发货时间并选择商品数量
     * @param kind  为0，则为加入购物车，为1，则为立即购买
     */
    public void showshipTime(int kind,int productNum) {
        Log.i("minfo","数量" + productNum);
        quantity = productNum;
        dialog = new Dialog(context, R.style.Dialog_Fullscreen);
        dialog.setContentView(R.layout.shiptime_popupwindow);
        dialog.setCanceledOnTouchOutside(true);
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialogWindow.setAttributes(layoutParams);

        TextView tvShipTime = (TextView) dialog.findViewById(R.id.tv_shiptime);
        tvShipTime.setText((CharSequence) entity.getShip_time());
        TextView tvAddto = (TextView) dialog.findViewById(R.id.tv_shiptime_addto);
        TextView tvSendSelf = (TextView) dialog.findViewById(R.id.tv_shiptime_sendself);
        TextView tvSendTa = (TextView) dialog.findViewById(R.id.tv_shiptime_sendta);
        ImageButton ibClose = (ImageButton) dialog.findViewById(R.id.ib_shiptime_close);
        LinearLayout llbuy = (LinearLayout) dialog.findViewById(R.id.ll_shiptime_buy);
        TextView tvAdd = (TextView) dialog.findViewById(R.id.tv_detail_plus);
        TextView tvReduce = (TextView) dialog.findViewById(R.id.tv_detail_reduce);
        TextView tvNum = (TextView) dialog.findViewById(R.id.tv_detail_productnum);
        tvNum.setText("" + quantity);

        if(kind == 0){   //加入购物车
            llbuy.setVisibility(View.GONE);
            tvAddto.setVisibility(View.VISIBLE);
        }else if(kind == 1){   //立即购买
            tvAddto.setVisibility(View.GONE);
            llbuy.setVisibility(View.VISIBLE);
        }

        dialog.show();
        MyListener listener = new MyListener(tvNum);
        tvAddto.setOnClickListener(listener);
        tvSendSelf.setOnClickListener(listener);
        tvSendTa.setOnClickListener(listener);
        ibClose.setOnClickListener(listener);
        tvAdd.setOnClickListener(listener);
        tvReduce.setOnClickListener(listener);
    }

    class MyListener implements View.OnClickListener{
        private TextView tvNum;
        public MyListener(TextView tvNum){
            this.tvNum = tvNum;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tv_shiptime_addto:
                    addtoShopcart();
                    break;
                case R.id.tv_shiptime_sendself:
                    App.BUY_WAY = App.BUY_MYSELF;
                    App.PAY_WAY = 0;
                    buy(BUY_MYSELF, BuymyselfAddressActivity.class);
                    break;
                case R.id.tv_shiptime_sendta:
                    App.BUY_WAY = App.BUY_OTHER_HAVEADD;
                    buy(BUY_OTHER, WXpayActivity.class);
                    break;
                case R.id.ib_shiptime_close:
                    dialog.dismiss();
                    break;
                case R.id.tv_detail_reduce:
                    if(quantity == 1){
                        return;
                    }
                    tvNum.setText("" + --quantity);
                    callBackimpl.callBack(quantity);
                    break;
                case R.id.tv_detail_plus:
                    if(quantity == entity.getQuantity()){
                        Toast.makeText(context,"超出库存量",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    tvNum.setText("" + ++quantity);
                    callBackimpl.callBack(quantity);
                    break;
            }
        }
    }

    /**
     * 点击添加到购物车
     */
    private void addtoShopcart(){
        HashMap params = new HashMap();
        params.put("product_id",productId);
        params.put("quantity",quantity);
        new Request().postAsync(Api.SHOP_CART, params, new CallBackimpl() {
            @Override
            public void callBack(String str) {
                try {
                    JSONObject obj = new JSONObject(str);
                    int status = obj.getInt("status");
                    if(status == Config.STATUS_SUCCESSED){
                        dialog.dismiss();
                        Toast.makeText(context,"已加入到购物车",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(context,obj.getString("errmsg"),Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 点击购买创建订单
     */
    public void buy(String buyType, final Class cls) {
        dialogUtils.progressDialog();
        HashMap params = new HashMap();
        params.put("product_id", productId);
        params.put("quantity", quantity);
        if (!buyType.equals(BUY_DONATIONS)) {
            params.put("order_type", buyType);
        }
        Request.postAsync(Api.PUT_ORDER, params, new CallBackimpl() {
            @Override
            public void callBack(String str) {
                try {
                    if (null != dialogUtils.getProgressDialog()) {
                        dialogUtils.getProgressDialog().dismiss();
                    }
                    JSONObject obj = new JSONObject(str);
                    int status = obj.getInt("status");
                    if (status == Config.STATUS_SUCCESSED) {
                        App.orderEntity = JSON.parseObject(obj.getJSONObject("data").toString(), CreateOrderEntity.class);
                        //默认第一张展示图为封面图
                        if (entity.getCover_pictures().size() != 0) {
                            App.orderEntity.setCover_url(entity.getCover_pictures().get(0));
                        }
                        App.orderEntity.setProduce_name(entity.getName());
                        App.orderEntity.setRealPrice(StrUtils.getNum(App.orderEntity.getProduct_price()));
                        App.orderEntity.setRealTotalamount(StrUtils.getNum(App.orderEntity.getTotal_amount()));
                        context.startActivity(new Intent(context, cls));
                    } else {
                        Toast.makeText(context, obj.getString("errmsg"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void setNumChangeListener(CallBackimpl callBackimpl){
        this.callBackimpl = callBackimpl;
    }

}
