package cn.ahabox.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ahabox.adapter.ShopcartPayAdapter;
import cn.ahabox.config.App;
import cn.ahabox.config.Config;
import cn.ahabox.feiliwu_help.R;
import cn.ahabox.interfaces.Api;
import cn.ahabox.interfaces.CallBackimpl;
import cn.ahabox.model.ShopcartPayEntity;
import cn.ahabox.utils.MyAnimUtils;
import cn.ahabox.utils.Request;
import cn.ahabox.utils.StrUtils;
import cn.ahabox.widget.CustomActionBar;
import cn.ahabox.widget.ListViewForScrollView;

/**
 * Created by libo on 2016/9/20.
 * <p/>
 * 购物车支付信息填写页面
 */
public class WXShopcartPayActivity extends BaseActivity {

    @Bind(R.id.et_wxpay_remark)
    EditText etWxpayRemark;
    @Bind(R.id.et_wxpay_promo)
    EditText etWxpayPromo;
    @Bind(R.id.tv_wxpay_totalamount)
    TextView tvWxpayTotalamount;
    @Bind(R.id.rl_wxpay_promocode)
    RelativeLayout rlWxpayPromocode;
    @Bind(R.id.tv_wxpay_check)
    TextView tvWxpayCheck;
    @Bind(R.id.lv_pay)
    ListViewForScrollView lvPay;
    @Bind(R.id.tv_orderdetail_addrename)
    TextView tvOrderdetailAddrename;
    @Bind(R.id.tv_orderdetail_addrephone)
    TextView tvOrderdetailAddrephone;
    @Bind(R.id.tv_orderdetail_addrezipcode)
    TextView tvOrderdetailAddrezipcode;
    @Bind(R.id.tv_orderdetail_addreadd)
    TextView tvOrderdetailAddreadd;
    @Bind(R.id.rl_include_address)
    RelativeLayout rlIncludeAddress;
    @Bind(R.id.tv_choose_address)
    TextView tvChooseAddress;
    @Bind(R.id.tv_wxpay_count)
    TextView tvWxpayCount;
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
    public static CallBackimpl changeAddress;
    private ShopcartPayAdapter shopcartPayAdapter;
    /**
     * 交易号
     */
    private String code;
    private String shipTime = "shipped_at_anytime";
    private int addressId;
    /**
     * 配送时间选择，分别为任意时间，工作日，周末
     */
    private String[] shipTimeStr = {"shipped_at_working_day", "shipped_at_weekend", "shipped_at_anytime"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopcart_pay);
        ButterKnife.bind(this);

        initView();
        initData();
    }

    private void initView() {
        customActionBar = (CustomActionBar) findViewById(R.id.wxpay_header);
        customActionBar.initStyle(CustomActionBar.HeaderStyle.DEFAULT_TITLE);
        customActionBar.setTitle(getResources().getString(R.string.title_wxpay));

        iwxapi = WXAPIFactory.createWXAPI(this, Config.APP_ID, true);
        iwxapi.registerApp(Config.APP_ID);

        code = getIntent().getStringExtra("code");

        payComplish = new CallBackimpl() {
            @Override
            public void confirmHandle() {
                orderInfoPage();
            }
        };

        changeAddress = new CallBackimpl() {
            @Override
            public void confirmHandle() {
                tvOrderdetailAddrename.setText(BuymyselfAddressActivity.addressEntity.getName());
                tvOrderdetailAddrephone.setText(BuymyselfAddressActivity.addressEntity.getPhone());
                tvOrderdetailAddrezipcode.setText(BuymyselfAddressActivity.addressEntity.getPostalcode());
                tvOrderdetailAddreadd.setText(BuymyselfAddressActivity.addressEntity.getDisplay_str());
                addressId = BuymyselfAddressActivity.addressEntity.getId();
                shipTime = RecipientInfoActivity.shipTime;

                rlIncludeAddress.setVisibility(View.VISIBLE);
                tvChooseAddress.setVisibility(View.GONE);
            }
        };
    }

    private void initData() {
        dialogUtils.progressDialog();
        dataParser.parseShopcartPay(code);
        dataParser.setCallBack(getApplicationContext(), new CallBackimpl() {
            @Override
            public void callBack(String str) {
                if (null != dialogUtils.getProgressDialog()) {
                    dialogUtils.getProgressDialog().dismiss();
                }
                setData(str);
            }
        });
    }

    private void setData(String str) {
        try {
            JSONObject obj = new JSONObject(str).getJSONObject("data");
            tvWxpayTotalamount.setText(obj.getString("nice_price_str"));

            ArrayList<ShopcartPayEntity> datas = (ArrayList) JSON.parseArray(obj.getJSONArray("products_transactions").toString(), ShopcartPayEntity.class);
            shopcartPayAdapter = new ShopcartPayAdapter(getApplicationContext(), datas);
            lvPay.setAdapter(shopcartPayAdapter.getAdapter());
            countPriceProcess(datas);

            if (obj.has("last_address")) {   //如果有last_address这个字段
                JSONObject addressObj = obj.getJSONObject("last_address");
                tvOrderdetailAddrename.setText(addressObj.getString("name"));
                tvOrderdetailAddrephone.setText(addressObj.getString("nice_phone"));
                tvOrderdetailAddrezipcode.setText(addressObj.getString("postalcode"));
                tvOrderdetailAddreadd.setText(addressObj.getString("display_str"));
                addressId = addressObj.getInt("address_id");
                shipTime = shipTimeStr[addressObj.getInt("ship_time")];
            } else {
                rlIncludeAddress.setVisibility(View.GONE);
                tvChooseAddress.setVisibility(View.VISIBLE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 计算总价格过程
     */
    private void countPriceProcess(ArrayList<ShopcartPayEntity> datas){
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<datas.size();i++){
            sb.append(getString(R.string.siyn_money));
            sb.append(datas.get(i).getProduct_price());
            sb.append("x");
            sb.append(datas.get(i).getQuantity());
            sb.append("+");
        }
        sb.deleteCharAt(sb.length()-1);   //去掉末尾构造的+号
        tvWxpayCount.setText(sb.toString());
    }

    @OnClick({R.id.ll_wxpay_gotopay, R.id.tv_wxpay_check, R.id.tv_Change_Place, R.id.tv_choose_address})
    void onClick(View view) {

        switch (view.getId()) {
            case R.id.tv_Change_Place:
                startActivity(new Intent(getApplicationContext(), BuymyselfAddressActivity.class));
                break;
            case R.id.tv_choose_address:
                startActivity(new Intent(getApplicationContext(), BuymyselfAddressActivity.class));
                break;
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
                params.put("ship_time", shipTime);
                params.put("address_id", addressId);
            }
            Request.postAsync(String.format(Api.PREPAY, code), params, new CallBackimpl() {
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
        dataParser.parsePaySuccess(code);
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
        if (StrUtils.isStr(promoCode)) {
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
                        } else {     //如果验证失败则返回设置默认价格
                            showshortText(obj.getString("errmsg"));
                            MyAnimUtils.shakeAnim(getApplicationContext(), rlWxpayPromocode);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            dataParser.parsePromoCode(code, promoCode);
        } else {
            showshortText("请先输入优惠码");
        }

    }

}
