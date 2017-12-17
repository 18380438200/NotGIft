package cn.ahabox.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.ahabox.feiliwu_help.R;
import cn.ahabox.application.MyApp;
import cn.ahabox.config.App;
import cn.ahabox.config.Config;
import cn.ahabox.interfaces.Api;
import cn.ahabox.interfaces.CallBackimpl;
import cn.ahabox.model.CreateOrderEntity;
import cn.ahabox.utils.QiniuUpLoadUtils;
import cn.ahabox.utils.Request;
import cn.ahabox.widget.CustomActionBar;
import cn.ahabox.widget.SharePopupWindow;

public class GiftinfoConfirmActivity extends BaseActivity {
    @Bind(R.id.tv_giftinfo_next)
    TextView tvGiftinfoNext;
    @Bind(R.id.infoconfirm_header)
    CustomActionBar customActionBar;
    @Bind(R.id.iv_wxpay_cover)
    ImageView ivWxpayCover;
    @Bind(R.id.tv_wxpay_introduce)
    TextView tvWxpayIntroduce;
    @Bind(R.id.tv_info_num)
    TextView tvInfoNum;
    @Bind(R.id.tv_info_price)
    TextView tvInfoPrice;
    @Bind(R.id.tv_confirm_sharetip)
    EditText tvConfirmSharetip;
    @Bind(R.id.et_confirm_sharecontent)
    EditText etConfirmSharecontent;
    @Bind(R.id.ll_infoconfirm)
    LinearLayout llInfoconfirm;
    private View parentView;
    private SharePopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentView = getLayoutInflater().inflate(R.layout.activity_giftinfo_confirm, null);
        setContentView(parentView);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        customActionBar.initStyle(CustomActionBar.HeaderStyle.DEFAULT_TITLE);
        customActionBar.setTitle(getResources().getString(R.string.title_giftinfoconfirm));

        CreateOrderEntity entity = App.orderEntity;
        ImageLoader.getInstance().displayImage(entity.getCover_url(), ivWxpayCover, MyApp.getInstance().options);
        tvWxpayIntroduce.setText(entity.getProduce_name());
        tvInfoNum.setText("" + entity.getQuantity());
        tvInfoPrice.setText(entity.getRealPrice());

        if(App.BUY_WAY == App.BUY_OTHER_HAVEADD){  //有地址，发送礼物
            llInfoconfirm.setVisibility(View.GONE);
        }

        tvGiftinfoNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shareTitle = tvConfirmSharetip.getText().toString().trim();
                String shareContent = etConfirmSharecontent.getText().toString().trim();
                if(!TextUtils.isEmpty(shareTitle) || !TextUtils.isEmpty(shareContent)){
                    sendOut();
                }else{
                    showshortText("分享内容不能为空");
                }
            }
        });

    }

    /**
     * 确认送出礼物
     */
    private void sendOut() {
        dialogUtils.progressDialog();
        Request.postAsync(String.format(Api.SEND_OUT, App.orderids), createParams(), new CallBackimpl() {
            @Override
            public void callBack(String str) {
                if (null != dialogUtils.getProgressDialog()) {
                    dialogUtils.getProgressDialog().dismiss();
                }
                try {
                    JSONObject obj = new JSONObject(str);
                    if (obj.get("status") == Config.STATUS_SUCCESSED) {
                        if(App.BUY_WAY == App.BUY_OTHER_HAVEADD){
                            startActivity(new Intent(GiftinfoConfirmActivity.this, SuccessfulSendActivity.class));
                        }else if(App.BUY_WAY == App.BUY_OTHER_NOADD){
                            //弹出框选择分享方式
                            popupWindow = new SharePopupWindow(GiftinfoConfirmActivity.this);
                            popupWindow.showAtLocation(parentView, Gravity.TOP, 0, 0);
                            JSONObject shareObj = obj.getJSONObject("data");
                            popupWindow.init(shareObj.getString("url"),shareObj.getString("title"), shareObj.getString("content"));
                        }
                    } else {
                        showshortText((String) obj.get("errmsg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private HashMap createParams(){
        HashMap params = new HashMap();

        if(App.isChooseVoice){
            params.put("message_id", VoiceListActivity.message_id);  //选择语音
        }else{
            params.put("message_key", QiniuUpLoadUtils.key);   //录制语音上传
        }

        if(App.BUY_WAY == App.BUY_OTHER_HAVEADD){
            params.put("address_id", RecipientAddressActivity.addressEntity.getId());   //自填地址
            params.put("ship_time", RecipientInfoActivity.shipTime);
        }else if(App.BUY_WAY == App.BUY_OTHER_NOADD){
            params.put("sharelink_title", tvConfirmSharetip.getText().toString().trim());   //发送链接
            params.put("sharelink_content", etConfirmSharecontent.getText().toString().trim());
        }
        return  params;
    }

}
