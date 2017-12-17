package cn.ahabox.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ahabox.feiliwu_help.R;
import cn.ahabox.config.Config;
import cn.ahabox.interfaces.CallBackimpl;
import cn.ahabox.widget.CustomActionBar;

/**
 * Created by libo on 2016/4/5.
 *
 * 退款理由提交
 */
public class RefundReasonActivity extends BaseActivity {
    @Bind(R.id.et_refundreason_reason)
    EditText etRefundreasonReason;
    /**
     * 订单号
     */
    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refund_reason);
        ButterKnife.bind(this);

        init();
    }

    private void init() {
        CustomActionBar customActionBar = (CustomActionBar) findViewById(R.id.refundreason_header);
        customActionBar.initStyle(CustomActionBar.HeaderStyle.DEFAULT_TITLE);
        customActionBar.setTitle(getResources().getString(R.string.title_refund_reason));
        code = getIntent().getStringExtra("ordercode");
    }

    @OnClick({R.id.tv_refundreason_cancel, R.id.tv_refundreason_post})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_refundreason_cancel:
                finish();
                break;
            case R.id.tv_refundreason_post:
                applyRefund();
                break;
        }
    }

    /**
     * 申请退款
     */
    private void applyRefund() {

        String reason = etRefundreasonReason.getText().toString().trim();
        if(!TextUtils.isEmpty(reason)){
            HashMap<String, String> params = new HashMap<>();
            params.put("reason", reason);
            dialogUtils.progressDialog();
            dataParser.setCallBack(getApplicationContext(), new CallBackimpl() {
                @Override
                public void callBack(String response) {
                    if (null != dialogUtils.getProgressDialog())
                        dialogUtils.getProgressDialog().dismiss();
                    handleResult(response);
                }
            });
            dataParser.parseRefund(code, params);
        }else{
            showshortText("退款理由不能为空");
        }
    }

    private void handleResult(String response){
        try {
            JSONObject obj = new JSONObject(response);
            int status = obj.getInt("status");
            if (status == Config.STATUS_SUCCESSED) {
                showshortText("申请成功");
                finish();
                OrderDetailActivity.requestRefund.confirmHandle();
            } else {
                showshortText(obj.getString("errmsg"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
