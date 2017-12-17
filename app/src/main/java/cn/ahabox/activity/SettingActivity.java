package cn.ahabox.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ahabox.config.App;
import cn.ahabox.config.Config;
import cn.ahabox.feiliwu_help.R;
import cn.ahabox.interfaces.CallBackimpl;
import cn.ahabox.utils.SDcardTools;
import cn.ahabox.utils.SharedPrefUtils;
import cn.ahabox.utils.StrUtils;
import cn.ahabox.utils.UpdateUtil;
import cn.ahabox.widget.CheckSwitchButton;
import cn.ahabox.widget.CustomActionBar;

/**
 * Created by libo on 2015/3/9.
 *
 * 设置
 */
public class SettingActivity extends BaseActivity {

    @Bind(R.id.mCheckSwithcButton)
    CheckSwitchButton mCheckSwithcButton;
    @Bind(R.id.tv_setting_signout)
    TextView LoginOrOut;
    @Bind(R.id.tv_setting_vername)
    TextView tvSettingVername;
    @Bind(R.id.tv_setting_cachesize)
    TextView tvSettingCachesize;
    private UpdateUtil updateUtil;
    private String versionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        initView();
        event();
    }

    private void initView() {
        CustomActionBar customActionBar = (CustomActionBar) findViewById(R.id.setting_header);
        customActionBar.initStyle(CustomActionBar.HeaderStyle.DEFAULT_TITLE);
        customActionBar.setTitle(getResources().getString(R.string.title_setting));

        UpdateUtil updateUtil = new UpdateUtil(getApplicationContext());
        versionName = updateUtil.getVersionName(getApplicationContext());
        tvSettingVername.setText("V" + versionName);
        try {
            tvSettingCachesize.setText(StrUtils.getNum((int)(SDcardTools.getFileSize(new File(Config.ROOT))/10000)) + " M");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (App.loginStatus == App.LOGIN_NO) {
            LoginOrOut.setVisibility(View.GONE);
        }

        if (SharedPrefUtils.getParams(getApplicationContext(), "umengpush", true) == true) {
            mCheckSwithcButton.setChecked(true);
        } else {
            mCheckSwithcButton.setChecked(false);
        }
    }

    private void event() {
        mCheckSwithcButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //开启友盟推送
                    SharedPrefUtils.setParams(getApplicationContext(), "umengpush", true);
                    MainActivity.mPushAgent.enable();
                } else {
                    //关闭友盟推送
                    SharedPrefUtils.setParams(getApplicationContext(), "umengpush", false);
                    MainActivity.mPushAgent.disable();
                }

            }
        });
    }

    @OnClick({R.id.tv_setting_signout, R.id.rl_setting_update, R.id.rl_setting_clearcache
            , R.id.rl_setting_modifypsd, R.id.rl_setting_feedback, R.id.rl_setting_aboutus})
    void onClick(View view) {
        switch (view.getId()) {

            case R.id.tv_setting_signout:
                dialogUtils.confirmCancelDialog("退出", "取消", "确认", new CallBackimpl() {
                    @Override
                    public void confirmHandle() {
                        App.clearStatus(SettingActivity.this, LoginOrOut);
                    }
                });
                break;
            case R.id.rl_setting_update:
                if (null == updateUtil) {
                    updateUtil = new UpdateUtil(this);
                }
                updateUtil.checkUpdate();
                break;
            case R.id.rl_setting_clearcache:
                SDcardTools.delFile(new File(Config.ROOT));
                showshortText("清除成功");
                tvSettingCachesize.setText("" + (float) new File(Config.ROOT).length() / 1000 + "M");
                break;
            case R.id.rl_setting_modifypsd:
                Intent forgetIntent = new Intent(getApplicationContext(), CodesActivity.class);
                forgetIntent.putExtra(Config.REGISTER_TYPE, 1);
                startActivity(forgetIntent);
                break;
            case R.id.rl_setting_feedback:
                startActivity(new Intent(getApplicationContext(), FeedBackActivity.class));
                break;
            case R.id.rl_setting_aboutus:
                MainActivity.menuCallBack.callBack(1,0);
                finish();
                break;
        }
    }

}
