package cn.ahabox.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ahabox.feiliwu_help.R;
import cn.ahabox.application.MyApp;
import cn.ahabox.config.App;
import cn.ahabox.config.Config;
import cn.ahabox.interfaces.Api;
import cn.ahabox.interfaces.CallBackimpl;
import cn.ahabox.model.CreateOrderEntity;
import cn.ahabox.utils.MyAnimUtils;
import cn.ahabox.utils.Request;
import cn.ahabox.utils.StrUtils;
import cn.ahabox.widget.CustomActionBar;

/**
 * Created by libo on 2016/12/20.
 * 微信支付信息填写页面
 */
public class WXpayActivity extends BaseActivity {

    @Bind(R.id.et_wxpay_remark)
    EditText etWxpayRemark;
    @Bind(R.id.et_wxpay_promo)
    EditText etWxpayPromo;
    @Bind(R.id.iv_wxpay_cover)
    ImageView ivWxpayCover;
    @Bind(R.id.tv_wxpay_introduce)
    TextView tvWxpayIntroduce;
    @Bind(R.id.tv_info_num)
    TextView tvInfoNum;
    @Bind(R.id.tv_info_price)
    TextView tvInfoPrice;
    @Bind(R.id.tv_wxpay_price)
    TextView tvWxpayPrice;
    @Bind(R.id.tv_wxpay_num)
    TextView tvWxpayNum;
    @Bind(R.id.tv_wxpay_totalamount)
    TextView tvWxpayTotalamount;
    @Bind(R.id.tv_wxpay_gopay)
    TextView tvWxpayGopay;
    @Bind(R.id.tr_wxpay_promocode)
    TableRow trWxpayPromocode;
    @Bind(R.id.ll_wxpay_buy_tip)
    LinearLayout llWxpayBuyTip;
    @Bind(R.id.rl_wxpay_promocode)
    RelativeLayout rlWxpayPromocode;
    @Bind(R.id.tv_wxpay_check)
    TextView tvWxpayCheck;
    private CustomActionBar customActionBar;
    /**
     * 优惠码
     */
    public static String promoCode;
    /**
     * 备注信息
     */
    public static String remark;
    private IWXAPI iwxapi;
    /**
     * 微信支付请求参数对象
     */
    private JSONObject prepayObj = null;
    public static CallBackimpl payComplish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wxpay);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        customActionBar = (CustomActionBar) findViewById(R.id.wxpay_header);
        customActionBar.initStyle(CustomActionBar.HeaderStyle.DEFAULT_TITLE);
        customActionBar.setTitle(getResources().getString(R.string.title_wxpay));

        iwxapi = WXAPIFactory.createWXAPI(this, Config.APP_ID, true);
        iwxapi.registerApp(Config.APP_ID);

        CreateOrderEntity entity = App.orderEntity;
        ImageLoader.getInstance().displayImage(entity.getCover_url(), ivWxpayCover, MyApp.getInstance().options);
        tvWxpayIntroduce.setText(entity.getProduce_name());
        tvInfoNum.setText("" + entity.getQuantity());
        tvInfoPrice.setText(App.orderEntity.getRealPrice());
        tvWxpayPrice.setText(App.orderEntity.getRealPrice());
        tvWxpayNum.setText("" + entity.getQuantity());
        tvWxpayTotalamount.setText(App.orderEntity.getRealTotalamount());
        tvWxpayGopay.setText(App.orderEntity.getRealTotalamount());

        if (!entity.isCan_use_promo_code()) {
            trWxpayPromocode.setVisibility(View.GONE);
        }
        if (App.BUY_WAY == App.BUY_DONATIONS || App.BUY_WAY == App.BUY_MYSELF) {   //如果是爱心活动或者是送自己，则没有温馨提示
            llWxpayBuyTip.setVisibility(View.GONE);
        }

        payComplish = new CallBackimpl() {
            @Override
            public void confirmHandle() {
                orderInfoPage();
            }
        };
    }

    @OnClick({R.id.ll_wxpay_gotopay, R.id.tv_wxpay_check})
    void onClick(View view) {

        switch (view.getId()) {
            case R.id.tv_wxpay_check:
                checkPromoCode();
                break;
            case R.id.ll_wxpay_gotopay:
                getPayParams();
                break;
        }
    }

    /**
     * 获取微信支付参数
     */
    private void getPayParams() {
        dialogUtils.progressDialog();
        if (null != prepayObj) {
            sendWxPayRes(prepayObj);
        } else {
            HashMap params = new HashMap();
            promoCode = etWxpayPromo.getText().toString().trim();
            remark = etWxpayRemark.getText().toString().trim();
            params.put("promo_code", promoCode);
            params.put("customer_memo", remark);
            if (App.BUY_WAY == App.BUY_MYSELF) {
                params.put("ship_time", RecipientInfoActivity.shipTime);
                params.put("address_id", BuymyselfAddressActivity.addressEntity.getId());
            }
            Request.postAsync(String.format(Api.PREPAY, App.orderEntity.getCode()), params, new CallBackimpl() {
                @Override
                public void callBack(String str) {
                    try {
                        if (null != dialogUtils.getProgressDialog()) {
                            dialogUtils.getProgressDialog().dismiss();
                        }
                        JSONObject obj = new JSONObject(str);
                        int status = obj.getInt("status");
                        if (status == Config.STATUS_SUCCESSED) {
                            prepayObj = obj.getJSONObject("data");
                            sendWxPayRes(prepayObj);
                        } else {
                            showshortText(obj.getString("errmsg"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    /**
     * 发送微信支付请求
     */
    private void sendWxPayRes(JSONObject prepayObj) {
        if (!iwxapi.isWXAppInstalled()) {
            showshortText("您还未安装微信客户端");
            return;
        }

        PayReq req = new PayReq();
        try {
            req.appId = prepayObj.getString("appid");
            req.partnerId = prepayObj.getString("partnerid");
            req.prepayId = prepayObj.getString("prepayid");
            req.nonceStr = prepayObj.getString("noncestr");
            req.timeStamp = prepayObj.getString("timestamp");
            req.packageValue = prepayObj.getString("package");
            req.sign = prepayObj.getString("sign");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        iwxapi.sendReq(req);
    }

    /**
     * 是否进入支付成功订单信息页
     */
    private void orderInfoPage() {
        dialogUtils.progressDialog();
        dataParser.setCallBack(getApplicationContext(), new CallBackimpl() {
            @Override
            public void callBack(String str) {
                if (null != dialogUtils.getProgressDialog()) {
                    dialogUtils.getProgressDialog().dismiss();
                }
                isPaySuccess(str);
            }
        });
        dataParser.parsePaySuccess(App.orderEntity.getCode());
    }

    /**
     * 是否购买成功判断
     *
     * @param str
     */
    private void isPaySuccess(String str) {

        try {
            JSONObject obj = new JSONObject(str).getJSONObject("data");
            boolean isPaySuccess = obj.getBoolean("paid_success");
            if (isPaySuccess) {
                prepayObj = null;  //如果支付成功，将预支付参数清空，能够再次发起预支付请求并作判断
                if (App.BUY_WAY == App.BUY_DONATIONS || App.BUY_WAY == App.BUY_MYSELF) {   //如果为公益商品或者是送自己，则直接跳转到发送成功页面
                    startActivity(new Intent(getApplicationContext(), SuccessfulSendActivity.class));
                } else {   //否则为正常购买
                    JSONArray array = obj.getJSONArray("order_ids");
                    for (int i = 0; i < array.length(); i++) {
                        App.orderids = (String) array.get(i);
                    }
                    startActivity(new Intent(getApplicationContext(), SuccessfulPayActivity.class));
                }
            } else {
                Toast.makeText(getApplicationContext(), R.string.payment_failed, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * 验证优惠码
     */
    private void checkPromoCode() {
        String promoCode = etWxpayPromo.getText().toString().trim();
        if(StrUtils.isStr(promoCode)){
            dialogUtils.progressDialog();
            dataParser.setCallBack(getApplicationContext(), new CallBackimpl() {
                @Override
                public void callBack(String str) {
                    if (null != dialogUtils.getProgressDialog()) {
                        dialogUtils.getProgressDialog().dismiss();
                    }
                    try {
                        JSONObject obj = new JSONObject(str);
                        if (obj.getInt("status") == Config.STATUS_SUCCESSED) {   //优惠码验证成功进行优惠
                            //此处无法修改优惠状态
                            tvWxpayCheck.setBackgroundResource(R.color.btn_cantuse);
                            etWxpayPromo.setFocusable(false);
                            tvWxpayCheck.setClickable(false);
                            showshortText("验证通过");
                            String promoPrice = obj.getJSONObject("data").getString("price");
                            String finalAmount = obj.getJSONObject("data").getString("final_amount");
                            tvWxpayTotalamount.setText(App.orderEntity.getRealTotalamount() + " - ¥ " + promoPrice + " = ¥ " + finalAmount);
                            tvWxpayGopay.setText(finalAmount);
                        } else {     //如果验证失败则返回设置默认价格
                            showshortText(obj.getString("errmsg"));
                            MyAnimUtils.shakeAnim(getApplicationContext(), rlWxpayPromocode);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            dataParser.parsePromoCode(App.orderEntity.getCode(), promoCode);
        }else{
            showshortText("请先输入优惠码");
        }

    }

}
