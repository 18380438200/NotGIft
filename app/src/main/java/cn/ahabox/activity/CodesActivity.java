package cn.ahabox.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ahabox.feiliwu_help.R;
import cn.ahabox.config.Config;
import cn.ahabox.interfaces.CallBackimpl;
import cn.ahabox.utils.BtnEnabledUtil;
import cn.ahabox.utils.MyAnimUtils;
import cn.ahabox.utils.StrUtils;
import cn.ahabox.widget.CustomActionBar;

/**
 * Created by libo on 2015/3/8.
 * <p/>
 * 向手机发送验证码
 */
public class CodesActivity extends BaseActivity {
    @Bind(R.id.et_codes_phonenum)
    EditText etCodesPhonenum;
    @Bind(R.id.et_codes_input)
    EditText etCodesInput;
    @Bind(R.id.codes_header)
    CustomActionBar customActionBar;
    @Bind(R.id.tv_codes_get)
    TextView tvCodesGet;
    @Bind(R.id.btn_codes_register)
    TextView btnCodesRegister;
    /**
     * 用户注册还是修改密码标志
     */
    private int psdType;
    /**
     * 短信接收器
     */
    private SmsReciever reciever;
    private final String smsFilter = "android.provider.Telephony.SMS_RECEIVED";
    /**
     * 倒计时时间，单位为毫秒
     */
    private final int COUNTTIME = 60 * 1000;
    public static String Token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_codes);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        psdType = getIntent().getIntExtra(Config.REGISTER_TYPE, 0);
        customActionBar.initStyle(CustomActionBar.HeaderStyle.DEFAULT_TITLE);
        if (psdType == Config.TYPE_REGISTER) {
            customActionBar.setTitle(getResources().getString(R.string.title_register));
            btnCodesRegister.setText("立 即 注 册");
        } else {
            customActionBar.setTitle(getResources().getString(R.string.title_modify_psd));
            btnCodesRegister.setText("修 改 密 码");
        }
        reciever = new SmsReciever();
        registerReceiver(reciever, new IntentFilter(smsFilter));

        BtnEnabledUtil.checkEnable(etCodesPhonenum, etCodesInput, btnCodesRegister);
    }

    @OnClick({R.id.tv_codes_get, R.id.btn_codes_register})
    void onClick(View view) {

        switch (view.getId()) {
            case R.id.tv_codes_get:
                getCodes();
                break;
            case R.id.btn_codes_register:
                testCodes();
                break;
        }
    }

    /**
     * 获取验证码
     */
    private void getCodes() {
        String phoneNum = etCodesPhonenum.getText().toString().trim();
        Map<String, String> params = new HashMap<>();
        params.put("mobile", phoneNum);
        dataParser.setCallBack(getApplicationContext(), new CallBackimpl() {
            @Override
            public void callBack(String str) {
                try {
                    JSONObject obj = new JSONObject(str);
                    int status = obj.getInt("status");
                    if (status == Config.STATUS_SUCCESSED) {
                        startCountTime(tvCodesGet);
                        etCodesInput.setEnabled(true);
                    } else {
                        Toast.makeText(getApplicationContext(), obj.getString("errmsg"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        dataParser.parseCodes(params);
    }

    /**
     * 验证验证码
     */
    private void testCodes() {
        String rephoneNum = etCodesPhonenum.getText().toString().trim();
        String codes = etCodesInput.getText().toString().trim();
        Map<String, String> reparams = new HashMap<>();
        reparams.put("mobile", rephoneNum);
        reparams.put("code", codes);
        if (psdType == Config.TYPE_REGISTER) {
            reparams.put("type", "register");
        } else {
            reparams.put("type", "modify_password");
        }
        dataParser.setCallBack(getApplicationContext(), new CallBackimpl() {
            @Override
            public void callBack(String str) {
                handleResult(str);
            }
        });
        dataParser.parseTestCodes(reparams);
    }

    /**
     * 重发验证码倒计时
     */
    private void startCountTime(final TextView tvCountTime) {
        tvCodesGet.setClickable(false);
        new CountDownTimer(COUNTTIME, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvCountTime.setText(millisUntilFinished / 1000 + "s后重新获取");
            }

            @Override
            public void onFinish() {
                tvCodesGet.setText(getResources().getString(R.string.login_getcodes));
                tvCodesGet.setClickable(true);
            }
        }.start();
    }

    private void handleResult(String str) {
        try {
            JSONObject obj = new JSONObject(str);
            int status = obj.getInt("status");
            //验证手机和验证码成功
            if (status == Config.STATUS_SUCCESSED) {
                Token = obj.getJSONObject("data").getString("token");
                Intent intent = new Intent(getApplicationContext(), EnterPassWordActivity.class);
                intent.putExtra(Config.REGISTER_TYPE, psdType);
                startActivity(intent);
                finish();
            } else {
                MyAnimUtils.shakeAnim(getApplicationContext(),etCodesInput);
                Toast.makeText(getApplicationContext(), obj.getString("errmsg"), Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class SmsReciever extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //广播拦截短信，通过Intent获取内容
            Object[] pduses = (Object[]) intent.getExtras().get("pdus");
            for (Object pdus : pduses) {
                byte[] pdusmsg = (byte[]) pdus;
                SmsMessage sms = SmsMessage.createFromPdu(pdusmsg);
                String content = sms.getMessageBody();
                String code = StrUtils.patternCode(content);
                if (!TextUtils.isEmpty(code)) {
                    etCodesInput.setText(code);
                }
            }
        }
    }

}
