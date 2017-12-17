package cn.ahabox.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ahabox.feiliwu_help.R;
import cn.ahabox.config.App;
import cn.ahabox.widget.CustomActionBar;

/**
 * Created by libo on 2015/1/24.
 * <p/>
 * 收礼人信息
 */
public class RecipientInfoActivity extends BaseActivity {
    @Bind(R.id.tv_recipientinfo_name)
    TextView tvRecipientinfoName;
    @Bind(R.id.tv_recipientinfo_phone)
    TextView tvRecipientinfoPhone;
    @Bind(R.id.tv_recipientinfo_zipcode)
    TextView tvRecipientinfoZipcode;
    @Bind(R.id.tv_recipientinfo_address)
    TextView tvRecipientinfoAddress;
    @Bind(R.id.rg_recipientinfo_shiptime)
    RadioGroup rgRecipientinfoShiptime;
    private CustomActionBar customActionBar;
    /** 配送时间选择，分别为任意时间，工作日，周末 */
    private String[] shipTimeStr = {"shipped_at_anytime","shipped_at_working_day","shipped_at_weekend"};
    /**
     * 配送时间，默认为任意时间
     */
    public static String shipTime = "shipped_at_anytime";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipient_info);
        ButterKnife.bind(this);

        initView();
        event();
    }

    private void initView() {
        customActionBar = (CustomActionBar) findViewById(R.id.recipientinfo_header);
        customActionBar.initStyle(CustomActionBar.HeaderStyle.DEFAULT_TITLE);
        customActionBar.setTitle(getResources().getString(R.string.title_recipient_info));

        if(App.BUY_WAY == App.BUY_MYSELF){
            tvRecipientinfoName.setText(BuymyselfAddressActivity.addressEntity.getName());
            tvRecipientinfoPhone.setText(BuymyselfAddressActivity.addressEntity.getPhone());
            tvRecipientinfoZipcode.setText(BuymyselfAddressActivity.addressEntity.getPostalcode());
            tvRecipientinfoAddress.setText(BuymyselfAddressActivity.addressEntity.getDisplay_str());
        }else if(App.BUY_WAY == App.BUY_OTHER_HAVEADD){
            tvRecipientinfoName.setText(RecipientAddressActivity.addressEntity.getName());
            tvRecipientinfoPhone.setText(RecipientAddressActivity.addressEntity.getPhone());
            tvRecipientinfoZipcode.setText(RecipientAddressActivity.addressEntity.getPostalcode());
            tvRecipientinfoAddress.setText(RecipientAddressActivity.addressEntity.getDisplay_str());
        }
    }

    private void event() {
        rgRecipientinfoShiptime.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for(int i=0;i<group.getChildCount();i++){
                    if(checkedId == group.getChildAt(i).getId()){
                        shipTime = shipTimeStr[i];
                    }
                }
            }
        });
        ((RadioButton)rgRecipientinfoShiptime.getChildAt(0)).setChecked(true);
    }

    @OnClick(R.id.tv_recipientinfo_confirm)
    void onClick() {
        if(App.BUY_WAY == App.BUY_MYSELF){
            if(App.PAY_WAY == 0){  //商品支付
                startActivity(new Intent(getApplicationContext(),WXpayActivity.class));
            }else if(App.PAY_WAY == 1){  //购物车支付
                WXShopcartPayActivity.changeAddress.confirmHandle();
                finish();
            }
        }else if(App.BUY_WAY == App.BUY_OTHER_HAVEADD){
            startActivity(new Intent(getApplicationContext(), RecordVoiceActivity.class));
        }
    }
}
