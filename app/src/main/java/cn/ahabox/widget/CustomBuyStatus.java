package cn.ahabox.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import cn.ahabox.feiliwu_help.R;

/**
 * Created by libo on 2016/4/21.
 *
 * 商品详情购买状态栏样式
 */
public class CustomBuyStatus extends LinearLayout{
    private LayoutInflater mInflater;
    private View view;
    private LinearLayout llCommon;
    private Button btnBuyMyself,btnDonations,btnContact;

    public CustomBuyStatus(Context context) {
        super(context);
        init(context);
    }

    public CustomBuyStatus(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        mInflater = LayoutInflater.from(context);
        view = mInflater.inflate(R.layout.custombuystatus, null);
        LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
        addView(view, params);
        initView(view);
    }

    private void initView(View view) {
//        btnBuyMyself = (Button) view.findViewById(R.id.btn_detail_myself);
        llCommon = (LinearLayout) view.findViewById(R.id.ll_productdetail_common);
        btnDonations = (Button) view.findViewById(R.id.btn_detail_donations);
        btnContact = (Button) view.findViewById(R.id.btn_detail_contact);
    }

    public void initStyle(String nature){
        switch (nature){
            case "commonweal":  //公益商品
                llCommon.setVisibility(GONE);
                btnDonations.setVisibility(VISIBLE);
                break;
            case "on_sale":  //特价商品，只能送给ta
//                btnBuyMyself.setVisibility(GONE);
                break;
            case "not_sale":  //非卖商品
                llCommon.setVisibility(GONE);
                btnContact.setVisibility(VISIBLE);
                break;
            case "presaleing":  //预售中

                break;
            case "presale":  //预售前
                llCommon.setVisibility(GONE);
                btnContact.setVisibility(VISIBLE);
                btnContact.setClickable(false);
                btnContact.setBackgroundResource(R.color.btn_cantuse);
                break;
            case "presale_end":   //预售结束
                llCommon.setVisibility(GONE);
                btnContact.setVisibility(VISIBLE);
                btnContact.setText(getResources().getString(R.string.productdetail_buy_finish));
                btnContact.setClickable(false);
                btnContact.setBackgroundResource(R.color.btn_cantuse);
                break;
            case "sold_out":   //售罄
                llCommon.setVisibility(GONE);
                btnContact.setVisibility(VISIBLE);
                btnContact.setText(getResources().getString(R.string.sold_out));
                btnContact.setClickable(false);
                btnContact.setBackgroundResource(R.color.btn_cantuse);
                break;
        }
    }

    public void setPresaleTime(String time){
        btnContact.setText(time);
    }

}
