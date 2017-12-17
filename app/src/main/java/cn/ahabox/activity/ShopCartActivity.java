package cn.ahabox.activity;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.ahabox.adapter.ShopcartAdapter;
import cn.ahabox.config.App;
import cn.ahabox.config.Config;
import cn.ahabox.feiliwu_help.R;
import cn.ahabox.interfaces.Api;
import cn.ahabox.interfaces.CallBackimpl;
import cn.ahabox.model.ShopcartEntity;
import cn.ahabox.utils.Request;
import cn.ahabox.widget.CustomActionBar;

/**
 * Created by libo on 2016/9/9.
 *
 * 购物车
 */
public class ShopCartActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.lv_shopcart)
    ListView lvShopcart;
    @Bind(R.id.tv_nodatas)
    TextView tvNodatas;
    @Bind(R.id.tv_shopcart_totalmoney)
    TextView tvShopcartTotalmoney;
    @Bind(R.id.cb_shopcart_all)
    CheckBox cbShopcartAll;
    @Bind(R.id.tv_billing)
    TextView tvBilling;
    private CustomActionBar customActionBar;
    private ShopcartAdapter adapter;
    private ArrayList<ShopcartEntity> datas = new ArrayList();
    /**
     * 用来记录checkBox列表当前选中状态，购物车id是键，是否选中状态是值
     */
    private SparseArray<Boolean> mSelectState = new SparseArray();
    public static CallBackimpl delCallBack;
    /**
     * 购物车商品总价格
     */
    private float totalMoney = 0;
    /**
     * 创建数量改变观察者对象
     */
    private DataSetObserver totalPriceObserver = new DataSetObserver() {

        /**
         * 当Adapter的notifyDataSetChanged方法执行时被调用
         */
        public void onChanged() {
            calculateTotalPrice();
        }

        /**
         * 当Adapter 调用 notifyDataSetInvalidate方法执行时被调用
         */
        public void onInvalidated() {}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_cart);
        ButterKnife.bind(this);

        initView();
        initData();
    }

    private void initView() {
        customActionBar = (CustomActionBar) findViewById(R.id.shopcart_header);
        customActionBar.initStyle(CustomActionBar.HeaderStyle.DEFAULT_TITLE);
        customActionBar.setTitle("购物车");

        adapter = new ShopcartAdapter(this, datas, mSelectState,cbShopcartAll);
        lvShopcart.setAdapter(adapter.getAdapter());
        adapter.getAdapter().registerDataSetObserver(totalPriceObserver);

        cbShopcartAll.setOnClickListener(this);
        tvBilling.setOnClickListener(this);

        delCallBack = new CallBackimpl() {
            @Override
            public void confirmHandle() {
                initData();
            }
        };
    }

    private void initData() {
        dialogUtils.progressDialog();
        dataParser.setCallBack(getApplicationContext(), new CallBackimpl() {
            @Override
            public void callBack(List list) {
                if (null != dialogUtils.getProgressDialog())
                    dialogUtils.getProgressDialog().dismiss();

                if (list.size() != 0) {
                    adapter.clearData();
                    adapter.addAll(list);
                } else {
                    lvShopcart.setVisibility(View.GONE);
                    tvNodatas.setVisibility(View.VISIBLE);
                }

            }
        });
        dataParser.parseShopcart();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cb_shopcart_all:
                checkAll();
                break;
            case R.id.tv_billing:
                //判断是否有一项商品的选择
                if (mSelectState.size() == 0) {
                    showshortText(getString(R.string.shopcart_no_checked));
                }else{
                    billingHandle();
                }
                break;
        }
    }

    /**
     *
     */
    private void calculateTotalPrice(){
        totalMoney = 0;
        for(int i=0;i<mSelectState.size();i++){
            for(ShopcartEntity entity : datas) {
                if(mSelectState.keyAt(i) == entity.getId()){    //表明选中了当前这项
                    totalMoney += entity.getQuantity() * Float.parseFloat(entity.getProduct_price());
                }
            }
        }
        tvShopcartTotalmoney.setText("" + totalMoney);
    }

    private void checkAll(){
        mSelectState.clear();
        if (cbShopcartAll.isChecked()) {   //全选
            for (int i = 0; i < datas.size(); i++) {
                int id = datas.get(i).getId();
                mSelectState.put(id, true);
            }
            adapter.getAdapter().notifyDataSetChanged();
        } else {   //全不选
            adapter.getAdapter().notifyDataSetChanged();
        }
    }

    /**
     * 结算处理
     */
    private void billingHandle(){
        dialogUtils.progressDialog();

        HashMap params = new HashMap();
        for(int i=0;i<mSelectState.size();i++){
            for(ShopcartEntity entity : datas) {
                if(mSelectState.keyAt(i) == entity.getId()){    //表明选中了当前这项
                    params.put("quantity_" + entity.getId(), entity.getQuantity());
                }
            }
        }
        Request.postAsync(Api.SHOPCART_BILLING, params, new CallBackimpl() {
            @Override
            public void callBack(String str) {
                try {
                    if (null != dialogUtils.getProgressDialog())
                        dialogUtils.getProgressDialog().dismiss();

                    JSONObject obj = new JSONObject(str);
                    int status = obj.getInt("status");
                    if (status == Config.STATUS_SUCCESSED) {
                        App.BUY_WAY = App.BUY_MYSELF;     //购物车购买与送自己(buy_myself)的流程一样，
                        //购物车支付方式标识，使支付弯沉返回WXShopcartPayActivity
                        App.PAY_WAY = 1;

                        String code = obj.getJSONObject("data").getString("code");
                        Intent intent = new Intent(getApplicationContext(), WXShopcartPayActivity.class);
                        intent.putExtra("code", code);
                        startActivity(intent);
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
