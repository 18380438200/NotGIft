package cn.ahabox.activity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ahabox.feiliwu_help.R;
import cn.ahabox.config.App;
import cn.ahabox.config.Config;
import cn.ahabox.interfaces.CallBackimpl;
import cn.ahabox.utils.BtnEnabledUtil;
import cn.ahabox.utils.MyAnimUtils;
import cn.ahabox.widget.CustomActionBar;

/**
 * Created by libo on 2015/3/8.
 *
 * 注册输入手机号
 */
public class EnterPassWordActivity extends BaseActivity {
    @Bind(R.id.et_psd_enter)
    EditText etPsdEnter;
    @Bind(R.id.et_psd_confirm)
    EditText etPsdConfirm;
    @Bind(R.id.btn_psd_complite)
    TextView btnPsdComplite;
    @Bind(R.id.psd_header)
    CustomActionBar customActionBar;
    /**
     * 注册或修改密码的类型
     */
    private int psdType;
    public static String postPsd;
    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_pass_word);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        customActionBar.initStyle(CustomActionBar.HeaderStyle.DEFAULT_TITLE);
        customActionBar.setTitle(getResources().getString(R.string.title_registeror_repsd));
        psdType = getIntent().getIntExtra(Config.REGISTER_TYPE, 0);

        api = WXAPIFactory.createWXAPI(this, Config.APP_ID, true);
        api.registerApp(Config.APP_ID);
        BtnEnabledUtil.checkEnable(etPsdEnter, etPsdConfirm, btnPsdComplite);

        if(psdType == Config.TYPE_MODIFY)
            btnPsdComplite.setText(getResources().getString(R.string.enterpsd_modify_psd));
    }

    @OnClick(R.id.btn_psd_complite)
    void onClick() {
        String psd = etPsdEnter.getText().toString().trim();
        String rePsd = etPsdConfirm.getText().toString().trim();
        if (psd.equals(rePsd)) {
            typeHandle(rePsd);
        } else {
            MyAnimUtils.shakeAnim(getApplicationContext(),etPsdConfirm);
            Toast.makeText(getApplicationContext(), "两次输入密码不一致", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 根据注册或者修改密码进行相应处理
     */
    private void typeHandle(String rePsd){
        if(psdType == Config.TYPE_REGISTER){
            //如果两次输入密码符合条件则调起微信登录
            postPsd = rePsd;
            App.IS_REGISTER = true;
            SendAuth.Req req = new SendAuth.Req();
            req.scope = "snsapi_userinfo";
            req.state = "feiliwu_android";
            api.sendReq(req);
        }else{
            modify(rePsd);
        }
    }

    /**
     * 提交参数进行修改密码
     */
    private void modify(final String psd) {
        Map<String, String> params = new HashMap();
        Map<String, String> headers = new HashMap<>();
        params.put("password", psd);
        headers.put("Authorization",CodesActivity.Token);
        dataParser.setCallBack(getApplicationContext(), new CallBackimpl() {
            @Override
            public void callBack(String str) {
                handleResult(str);
            }
        });
        dataParser.parseModifyPassword(params, headers);
    }

    /**
     * 处理返回结果
     *
     * @param str
     */
    private void handleResult(String str) {
        try {
            JSONObject obj = new JSONObject(str);
            int status = obj.getInt("status");
            if (status == Config.STATUS_SUCCESSED) {
                Toast.makeText(getApplicationContext(), "修改成功", Toast.LENGTH_SHORT).show();
                finish();
            } else{
                Toast.makeText(getApplicationContext(), obj.getString("errmsg"), Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
