package cn.ahabox.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ahabox.feiliwu_help.R;
import cn.ahabox.application.MyApp;
import cn.ahabox.config.App;
import cn.ahabox.model.CreateOrderEntity;
import cn.ahabox.utils.AddressDialog;
import cn.ahabox.widget.CustomActionBar;

/**
 * Created by libo on 2015/1/27.
 * <p/>
 * 微信支付信息填写页面
 */
public class SuccessfulPayActivity extends BaseActivity {
    @Bind(R.id.iv_wxpay_cover)
    ImageView ivWxpayCover;
    @Bind(R.id.tv_wxpay_introduce)
    TextView tvWxpayIntroduce;
    @Bind(R.id.tv_info_num)
    TextView tvInfoNum;
    @Bind(R.id.tv_info_price)
    TextView tvInfoPrice;
    @Bind(R.id.tv_successful_next)
    TextView tvSuccessfulNext;
    private CustomActionBar customActionBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_successful_pay);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        customActionBar = (CustomActionBar) findViewById(R.id.successfulpay_header);
        customActionBar.initStyle(CustomActionBar.HeaderStyle.DEFAULT_TITLE);
        customActionBar.setTitle(getResources().getString(R.string.title_pay_success));

        CreateOrderEntity entity = App.orderEntity;
        ImageLoader.getInstance().displayImage(entity.getCover_url(), ivWxpayCover, MyApp.getInstance().options);
        tvWxpayIntroduce.setText(entity.getProduce_name());
        tvInfoNum.setText("" + entity.getQuantity());
        tvInfoPrice.setText(entity.getRealPrice());

        //如果购买多件商品，则进入礼物箱，分多次分享
        if (App.orderEntity.getQuantity() > 1) {
            tvSuccessfulNext.setText(getResources().getString(R.string.entermybox));
        }
    }

    @OnClick(R.id.tv_successful_next)
    void onClick() {
        if (App.BUY_WAY == App.BUY_MYSELF) {     //送自己
            startActivity(new Intent(getApplicationContext(), SuccessfulSendActivity.class));
        } else if(App.orderEntity.getQuantity() > 1){    //送他人买多件商品
            startActivity(new Intent(getApplicationContext(), GiftBoxActivity.class));
        }else {    //送他人买单件商品
            new AddressDialog(this);
        }
    }

}
