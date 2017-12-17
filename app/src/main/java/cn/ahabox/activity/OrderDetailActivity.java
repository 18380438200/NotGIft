package cn.ahabox.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.ant.liao.GifView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ahabox.adapter.MyShipAdapter;
import cn.ahabox.adapter.ShipStatusAdapter;
import cn.ahabox.application.MyApp;
import cn.ahabox.feiliwu_help.R;
import cn.ahabox.interfaces.CallBackimpl;
import cn.ahabox.model.OrderDetailEntity;
import cn.ahabox.model.ShipEntity;
import cn.ahabox.network.DataParserImpl;
import cn.ahabox.utils.MyLayoutManager;
import cn.ahabox.utils.PlaySound;
import cn.ahabox.utils.StrUtils;
import cn.ahabox.widget.CustomActionBar;
import cn.ahabox.widget.ListViewForScrollView;

/**
 * Created by libo on 2015/2/4.
 * <p/>
 * 成交订单详情
 */
public class OrderDetailActivity extends BaseActivity {
    @Bind(R.id.iv_orderdetial_cover)
    ImageView ivOrderdetialCover;
    @Bind(R.id.tv_orderdetial_name)
    TextView tvOrderdetialName;
    @Bind(R.id.tv_orderdetail_buytime)
    TextView tvOrderdetailBuytime;
    @Bind(R.id.tv_orderdetail_code)
    TextView tvOrderdetailCode;
    @Bind(R.id.tv_orderdetail_price)
    TextView tvOrderdetailPrice;
    @Bind(R.id.tv_orderdetail_addrename)
    TextView tvAddrename;
    @Bind(R.id.tv_orderdetail_addrephone)
    TextView tvAddrephone;
    @Bind(R.id.tv_orderdetail_addrezipcode)
    TextView tvzipcode;
    @Bind(R.id.tv_orderdetail_addreadd)
    TextView tvAddreadd;
    @Bind(R.id.mygif)
    GifView mygif;
    @Bind(R.id.sv_orderdetail)
    ScrollView svOrderdetail;
    @Bind(R.id.tv_orderdetail_refund)
    TextView tvOrderdetailRefund;
    @Bind(R.id.tv_orderdetail_applyrefund)
    TextView tvOrderdetailApplyrefund;
    @Bind(R.id.ll_orderdetail_voice)
    LinearLayout llOrderdetailVoice;
    @Bind(R.id.ll_orderdetail_ship)
    LinearLayout llOrderdetailShip;
    @Bind(R.id.ll_orderdetail_refund)
    LinearLayout llOrderdetailRefund;
    @Bind(R.id.iv_audio)
    ImageView ivAudio;
    @Bind(R.id.ll_orderdetail_address)
    LinearLayout llOrderdetailAddress;
    @Bind(R.id.lv_orderdetail_ship)
    ListViewForScrollView lvOrderdetailShip;
    @Bind(R.id.rv_detail_logistics)
    RecyclerView rvDetailLogistics;
    private CustomActionBar customActionBar;
    private MyShipAdapter shipAdapter;
    private ArrayList<ShipEntity> datas = new ArrayList<>();
    private MediaPlayer mediaPlayer;
    /**
     * 订单编号
     */
    private String orderCode;
    public static CallBackimpl requestRefund;
    private OrderDetailEntity entity;
    private String[] statusNoAddress = {"下单", "付款", "发送链接", "收礼人确认", "已发货", "已完成"};    //送给他并且不知道地址
    private String[] statusAddress = {"下单", "付款", "已发货", "已完成"};    //送自己
    private String[] statusAdresssend = {"下单", "付款", "自填地址","已发货", "已完成"};   //送给Ta并且是自填地址
    private ArrayList<String> dates = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        ButterKnife.bind(this);

        initView();
        initData();
        initShipInfo();
    }

    private void initView() {
        customActionBar = (CustomActionBar) findViewById(R.id.orderdetail_header);
        customActionBar.initStyle(CustomActionBar.HeaderStyle.DEFAULT_TITLE);
        customActionBar.setTitle(getResources().getString(R.string.title_orderdetail));

        mediaPlayer = new MediaPlayer();
        orderCode = getIntent().getStringExtra("ordercode");

        shipAdapter = new MyShipAdapter(getApplicationContext(), datas);
        rvDetailLogistics.setLayoutManager(new MyLayoutManager(getApplicationContext()));
        rvDetailLogistics.setAdapter(shipAdapter);

        requestRefund = new CallBackimpl() {
            @Override
            public void confirmHandle() {
                tvOrderdetailApplyrefund.setVisibility(View.GONE);
                tvOrderdetailRefund.setTextSize(18);
                tvOrderdetailRefund.setText("退款处理中");
            }
        };
    }

    private void initData() {
        dialogUtils.progressDialog();
        dataParser.setCallBack(getApplicationContext(), new CallBackimpl() {
            @Override
            public void callBack(JSONObject obj) {
                if (null != dialogUtils.getProgressDialog()) {
                    dialogUtils.getProgressDialog().dismiss();
                }
                entity = JSON.parseObject(obj.toString(), OrderDetailEntity.class);
                svOrderdetail.setVisibility(View.VISIBLE);
                setData();
            }
        });
        dataParser.parseOrderDetail(orderCode);
    }

    /**
     * 订单流程状态
     */
    private void getOrderStatus() {
        ShipStatusAdapter shipStatusAdapter;
        setShipDate();
        if (null != entity.getAddress_input_type()) {
            if (entity.getAddress_input_type().equals("not_know_address")) {     //发送链接
                shipStatusAdapter = new ShipStatusAdapter(getApplicationContext(),
                        Arrays.asList(addressInputType(entity.getAddress_input_type())), dates, getOrderStatus(entity.getStatus()));
            }else if(entity.getAddress_input_type().equals("know_address")){      //自填地址
                shipStatusAdapter = new ShipStatusAdapter(getApplicationContext(),
                        Arrays.asList(addressInputType(entity.getAddress_input_type())), dates, getOrderStatus3(entity.getStatus()));
            }else{   //否则就是送自己
                shipStatusAdapter = new ShipStatusAdapter(getApplicationContext(),
                        Arrays.asList(addressInputType(entity.getAddress_input_type())), dates, getOrderStatus2(entity.getStatus())-1);
            }
            lvOrderdetailShip.setAdapter(shipStatusAdapter.getAdapter());
        }
    }

    /**
     * 获取物流信息
     */
    private void initShipInfo() {
        DataParserImpl shipDataParser = new DataParserImpl();
        shipDataParser.setCallBack(getApplicationContext(), new CallBackimpl() {
            @Override
            public void callBack(List list) {
                datas.addAll(list);
                shipAdapter.notifyDataSetChanged();
            }
        });
        shipDataParser.parseShip(orderCode);
    }

    @OnClick({R.id.tv_audio_play, R.id.tv_orderdetail_applyrefund})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_audio_play:
                PlaySound.playRecordVoice(this, mediaPlayer, entity.getVoice_url(), mygif, R.drawable.recordvoice_reading, R.drawable.recordvoice_reading, ivAudio);
                break;
            case R.id.tv_orderdetail_applyrefund:
                Intent intent = new Intent(getApplicationContext(), RefundReasonActivity.class);
                intent.putExtra("ordercode", orderCode);
                startActivity(intent);
                break;
        }
    }

    /**
     * 设置订单信息
     */
    private void setData() {
        ImageLoader.getInstance().displayImage(entity.getCover(), ivOrderdetialCover, MyApp.getInstance().options);
        tvOrderdetialName.setText(entity.getProduct_name());
        tvOrderdetailCode.setText(entity.getCode());
        tvOrderdetailPrice.setText(entity.getPrice());
        String buyTime = StrUtils.getDateTime(String.valueOf(entity.getPay_at()));

        tvOrderdetailBuytime.setText(buyTime + "  购买");
        tvAddrename.setText(entity.getUser_name());
        tvzipcode.setText(entity.getPostalcode());
        tvAddrephone.setText(entity.getPhone());
        tvAddreadd.setText(entity.getAddress());

        if (entity.getStatus().equals("wait_sign") || entity.getStatus().equals("signed")) {  //已发货或已签收显示物流信息
            llOrderdetailShip.setVisibility(View.VISIBLE);
            cantRefund();
        } else if (entity.getStatus().equals("paid") || entity.getStatus().equals("wait_confirm")) {  //购买成功但是没有送出，不显示收货地址
            llOrderdetailAddress.setVisibility(View.GONE);
        }

        if (entity.getNature().equals("commonweal") || entity.getNature().equals("on_sale")) {  //特价、公益、0元商品不能申请退款
            tvOrderdetailApplyrefund.setVisibility(View.GONE);
            tvOrderdetailRefund.setText(getResources().getString(R.string.cant_refund));
            tvOrderdetailRefund.setTextSize(16);
        } else {
            if (null != entity.getRefund_status())
                refundStatus(entity.getRefund_status());
        }

        if(null != entity.getAddress_input_type()){
            llOrderdetailShip.setVisibility(View.GONE);
            getOrderStatus();
        }

        if (null == entity.getVoice_url()) {
            llOrderdetailVoice.setVisibility(View.GONE);
        }

    }

    /**
     * 发送链接状态
     *
     * @param status
     * @return
     */
    private int getOrderStatus(String status) {
        if (status.equals("paid")) {    //付款
            return 1;
        } else if (status.equals("wait_confirm")) {    //发送链接
            return 2;
        } else if (status.equals("wait_ship")) {    //等待发货
            return 3;
        } else if (status.equals("wait_sign")) {    //已发货
            return 4;
        } else if (status.equals("signed")) {    //已完成
            return 5;
        }
        return 0;   //等待支付

    }

    /**
     * 送自己状态
     *
     * @return
     */
    private int getOrderStatus2(String status) {
        if (status.equals("paid")) {    //付款
            return 1;
        } else if (status.equals("wait_ship")) {    //等待发货
            return 2;
        } else if (status.equals("wait_sign")) {    //已发货
            return 3;
        } else if (status.equals("signed")) {    //已完成
            return 4;
        }
        return 0;   //等待支付
    }

    /**
     * 送给ta自填地址状态
     * @param status
     * @return
     */
    private int getOrderStatus3(String status){
        if (status.equals("paid")) {    //付款
            return 1;
        }else if(status.equals("wait_ship")){    //自填地址
            return 2;
        } else if (status.equals("wait_sign")) {    //已发货
            return 3;
        } else if (status.equals("signed")) {    //已完成
            return 4;
        }
        return 0;   //等待支付
    }

    /**
     * 设置订单状态日期
     */
    private void setShipDate() {
        ArrayList intTime = new ArrayList();
        intTime.add(entity.getCreated_at());
        intTime.add(entity.getPay_at());
        if(entity.getAddress_input_type().equals("not_know_address")){
            intTime.add(entity.getSend_link_at());
        }
        intTime.add(entity.getConfirm_at());
        intTime.add(entity.getShipped_at());
        intTime.add(entity.getSigned_at());
        for (int i = 0; i < intTime.size(); i++) {    //遍历日期列表，如果不是0，就添加到日期列表中，否则添加一个空字符串
            if (intTime.get(i) != 0) {
                dates.add(StrUtils.getDateSec(String.valueOf(intTime.get(i))));
            } else {
                dates.add("");
            }
        }
    }

    /**
     * 地址输入类型，知道地址，不知道地址，送自己，公益商品
     */
    private String[] addressInputType(String addressType) {
        if (addressType.equals("not_know_address")) {
            return statusNoAddress;
        }else if(addressType.equals("know_address")){
            return statusAdresssend;
        } else if(addressType.equals("self_address")){
            return statusAddress;
        }
        return statusAddress;
    }

    /**
     * 退款状态
     */
    private void refundStatus(String refundStatus) {
        tvOrderdetailRefund.setTextSize(16);
        if (refundStatus.equals("refund_in_request")) {
            tvOrderdetailApplyrefund.setVisibility(View.GONE);
            tvOrderdetailRefund.setText("退款处理中");
        } else if (refundStatus.equals("refund_in_process")) {
            tvOrderdetailApplyrefund.setVisibility(View.GONE);
            tvOrderdetailRefund.setText("退款处理中");
        } else if (refundStatus.equals("refund_completed")) {
            tvOrderdetailApplyrefund.setVisibility(View.GONE);
            tvOrderdetailRefund.setText("退款完成");
        } else if (refundStatus.equals("refund_confirm_return")) {
            tvOrderdetailApplyrefund.setVisibility(View.GONE);
            tvOrderdetailRefund.setText("退款处理中");
        }
    }

    /**
     * 能否退款条件，发货超过10天或者签收超过7天都不能申请退款
     */
    private void cantRefund() {
        long seperatShipTime = System.currentTimeMillis() / 1000 - entity.getShipped_at();
        long seperatSignTime = System.currentTimeMillis() / 1000 - entity.getSigned_at();
        if (seperatShipTime / 60 / 60 > 24 * 10 || seperatSignTime / 60 / 60 > 24 * 7) {   //按照小时来比较
            tvOrderdetailApplyrefund.setVisibility(View.GONE);
            tvOrderdetailRefund.setText(getResources().getString(R.string.cant_refund_tip));
            tvOrderdetailRefund.setTextSize(15);
        }
    }

}
