package cn.ahabox.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ahabox.feiliwu_help.R;
import cn.ahabox.config.Config;
import cn.ahabox.interfaces.CallBackimpl;
import cn.ahabox.utils.BtnEnabledUtil;
import cn.ahabox.utils.LoginUtils;
import cn.ahabox.utils.MyAnimUtils;
import cn.ahabox.widget.CustomActionBar;

/**
 * Created by libo on 2015/3/8.
 *
 * 登录
 */
public class LoginActivity extends BaseActivity {
    @Bind(R.id.et_login_phone)
    EditText etLoginPhone;
    @Bind(R.id.et_login_psd)
    EditText etLoginPsd;
    @Bind(R.id.btn_login_login)
    TextView btnLoginLogin;
    private String phone, psd;
    public static CallBackimpl callBackimpl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        CustomActionBar customActionBar = (CustomActionBar) findViewById(R.id.login_header);
        customActionBar.initStyle(CustomActionBar.HeaderStyle.DEFAULT_TITLE);
        customActionBar.setTitle(getResources().getString(R.string.title_login));

        BtnEnabledUtil.checkEnable(etLoginPhone, etLoginPsd, btnLoginLogin);

        //当登录的密码输入错误时
        callBackimpl = new CallBackimpl() {
            @Override
            public void confirmHandle() {
                MyAnimUtils.shakeAnim(getApplicationContext(),etLoginPsd);
            }
        };
    }

    @OnClick({R.id.btn_login_login, R.id.btn_login_forget, R.id.btn_login_register})
    void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_login_login:
                dialogUtils.progressDialog();
                phone = etLoginPhone.getText().toString().trim();
                psd = etLoginPsd.getText().toString().trim();
                new LoginUtils(this,phone,psd,false).login(dataParser,dialogUtils.getProgressDialog());
                break;
            case R.id.btn_login_forget:
                Intent forgetIntent = new Intent(getApplicationContext(), CodesActivity.class);
                forgetIntent.putExtra(Config.REGISTER_TYPE, Config.TYPE_MODIFY);
                startActivity(forgetIntent);
                break;
            case R.id.btn_login_register:
                Intent registerIntent = new Intent(getApplicationContext(), CodesActivity.class);
                registerIntent.putExtra(Config.REGISTER_TYPE, Config.TYPE_REGISTER);
                startActivity(registerIntent);
                break;
        }
    }

}
