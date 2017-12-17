package cn.ahabox.activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.ant.liao.GifView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ahabox.feiliwu_help.R;
import cn.ahabox.application.MyApp;
import cn.ahabox.config.Config;
import cn.ahabox.interfaces.Api;
import cn.ahabox.interfaces.CallBackimpl;
import cn.ahabox.model.OrderDetailEntity;
import cn.ahabox.utils.PlaySound;
import cn.ahabox.utils.Request;
import cn.ahabox.utils.StrUtils;
import cn.ahabox.widget.CustomActionBar;
import cn.ahabox.widget.SharePopupWindow;

/**
 * Created by libo on 2015/3/4.
 * 等待接收详情
 */
public class WaitacceptOrderdetailActivity extends BaseActivity {
    private View parentView;
    @Bind(R.id.iv_orderdetial_cover)
    ImageView ivOrderdetialCover;
    @Bind(R.id.tv_orderdetail_buytime)
    TextView tvOrderdetailBuytime;
    @Bind(R.id.tv_orderdetail_code)
    TextView tvOrderdetailCode;
    @Bind(R.id.tv_orderdetial_name)
    TextView tvOrderdetialName;
    @Bind(R.id.tv_orderdetail_price)
    TextView tvOrderdetailPrice;
    @Bind(R.id.mygif)
    GifView mygif;
    @Bind(R.id.tv_audio_play)
    RelativeLayout tvAudioPlay;
    @Bind(R.id.sv_orderdetail)
    ScrollView svOrderdetail;
    @Bind(R.id.et_orderdetail_linktitle)
    EditText tvOrderdetailLinktitle;
    @Bind(R.id.et_orderdetail_linkcontent)
    EditText etOrderdetailLinkcontent;
    @Bind(R.id.iv_audio)
    ImageView ivAudio;
    /**
     * 订单编号
     */
    private String orderCode;
    private MediaPlayer mediaPlayer;
    private OrderDetailEntity entity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentView = getLayoutInflater().inflate(R.layout.activity_waitaccept_orderdetail, null);
        setContentView(parentView);
        ButterKnife.bind(this);

        init();
        initData();
    }

    private void init() {
        CustomActionBar customActionBar = (CustomActionBar) findViewById(R.id.orderdetail_header);
        customActionBar.initStyle(CustomActionBar.HeaderStyle.DEFAULT_TITLE);
        customActionBar.setTitle(getResources().getString(R.string.title_wait_accept));
        orderCode = getIntent().getStringExtra("ordercode");
        mediaPlayer = new MediaPlayer();
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
     * 设置订单信息
     */
    private void setData() {
        ImageLoader.getInstance().displayImage(entity.getCover(), ivOrderdetialCover, MyApp.getInstance().options);
        tvOrderdetialName.setText(entity.getProduct_name());
        tvOrderdetailCode.setText(entity.getCode());
        tvOrderdetailPrice.setText(entity.getPrice());
        String buyTime = StrUtils.getDateTime(String.valueOf(entity.getPay_at()));
        tvOrderdetailBuytime.setText(buyTime + "  购买");
        tvOrderdetailLinktitle.setText(entity.getSharelink_title());
        etOrderdetailLinkcontent.setText(entity.getSharelink_content());

    }

    @OnClick({R.id.tv_orderdetail_sendagan, R.id.tv_audio_play})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_audio_play:
                PlaySound.playRecordVoice(getApplicationContext(), mediaPlayer, entity.getVoice_url(), mygif, R.drawable.recordvoice_reading,
                        R.drawable.recordvoice_reading, ivAudio);
                break;
            case R.id.tv_orderdetail_sendagan:
                String shareTitle = tvOrderdetailLinktitle.getText().toString().trim();
                String shareContent = etOrderdetailLinkcontent.getText().toString().trim();
                if(!TextUtils.isEmpty(shareTitle) || !TextUtils.isEmpty(shareContent)){
                    sendOut();
                }else{
                    showshortText("分享内容不能为空");
                }
                break;
        }
    }

    /**
     * 再次分享礼物需要再次请求获取新的分享链接
     */
    private void sendOut(){
        dialogUtils.progressDialog();
        Request.postAsync(String.format(Api.SEND_OUT, entity.getCode()),createParams() , new CallBackimpl() {
            @Override
            public void callBack(String str) {
                if (null != dialogUtils.getProgressDialog()) {
                    dialogUtils.getProgressDialog().dismiss();
                }

                try {
                    JSONObject obj = new JSONObject(str);
                    if (obj.get("status") == Config.STATUS_SUCCESSED) {
                        SharePopupWindow popupWindow = new SharePopupWindow(WaitacceptOrderdetailActivity.this);
                        popupWindow.showAtLocation(parentView, Gravity.TOP, 0, 0);
                        JSONObject shareObj = obj.getJSONObject("data");
                        popupWindow.init(shareObj.getString("url"),shareObj.getString("title"), shareObj.getString("content"));
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
        params.put("message_id", entity.getMessage_id());  //选择语音
        params.put("sharelink_title", tvOrderdetailLinktitle.getText().toString().trim());   //发送链接
        params.put("sharelink_content", etOrderdetailLinkcontent.getText().toString().trim());

        return  params;
    }

}
