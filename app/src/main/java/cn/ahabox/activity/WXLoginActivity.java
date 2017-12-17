package cn.ahabox.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ahabox.feiliwu_help.R;
import cn.ahabox.config.Config;
import cn.ahabox.widget.CustomActionBar;

/**
 * Created by libo on 2015/3/10.
 *
 * 微信登录页面
 */
public class WXLoginActivity extends BaseActivity {
    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wxlogin);
        ButterKnife.bind(this);

        initView();
        initWX();
    }

    private void initView() {
        CustomActionBar customActionBar = (CustomActionBar) findViewById(R.id.wxlogin_header);
        customActionBar.initStyle(CustomActionBar.HeaderStyle.DEFAULT_TITLE);
        customActionBar.setTitle(getResources().getString(R.string.title_login));

    }

    /**
     * 向微信终端注册id
     */
    private void initWX() {
        api = WXAPIFactory.createWXAPI(this, Config.APP_ID, true);
        api.registerApp(Config.APP_ID);
    }

    @OnClick({R.id.tv_wx_login,R.id.tv_otherway_login})
    void onClick(View view){
        switch (view.getId()){
            case R.id.tv_wx_login:
                if (!api.isWXAppInstalled()) {
                    showshortText("您还未安装微信客户端");
                    return;
                }
                SendAuth.Req req = new SendAuth.Req();
                req.scope = "snsapi_userinfo";
                req.state = "feiliwu_android";
                api.sendReq(req);
                break;
            case R.id.tv_otherway_login:
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                finish();
                break;
        }
    }

}
