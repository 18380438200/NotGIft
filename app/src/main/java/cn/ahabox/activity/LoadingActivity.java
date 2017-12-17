package cn.ahabox.activity;

import android.app.Activity;
import android.os.Bundle;

import com.umeng.analytics.AnalyticsConfig;

import butterknife.ButterKnife;
import cn.ahabox.feiliwu_help.R;
import cn.ahabox.config.App;
import cn.ahabox.config.Config;
import cn.ahabox.model.User;
import cn.ahabox.utils.FirstIn;
import cn.ahabox.utils.SharedPrefUtils;

/**
 * Created by libo on 2015/11/24.
 *
 * 加载页面
 */
public class LoadingActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);
        ButterKnife.bind(this);

        /** 设置是否对友盟日志信息进行加密, 默认false(不加密). */
        AnalyticsConfig.enableEncrypt(true);
        getLoginStatus();
        new FirstIn(this).sendDirection();
    }

    public void getLoginStatus() {
        try {
            int getUserId = (int) SharedPrefUtils.getParams(this,Config.SAVE_UID,1);
            String getToken = (String) SharedPrefUtils.getParams(this, Config.SAVE_TOKEN, "");
            String getAvater = (String) SharedPrefUtils.getParams(this, Config.SAVE_AVATER, "");
            String getNickName = (String) SharedPrefUtils.getParams(this, Config.SAVE_USERNAME, "");
            if (!getToken.equals("") && !getAvater.equals("") && !getNickName.equals("")) {
                User saveUser = new User();
                saveUser.setId(getUserId);
                saveUser.setToken(getToken);
                saveUser.setAvatar_url(getAvater);
                saveUser.setNickname(getNickName);
                App.setUser(saveUser);
                App.setLoginStatus(App.LOGIN_YES);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
