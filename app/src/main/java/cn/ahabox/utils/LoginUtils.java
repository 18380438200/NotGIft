package cn.ahabox.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import cn.ahabox.activity.LoginActivity;
import cn.ahabox.config.App;
import cn.ahabox.config.Config;
import cn.ahabox.fragment.FastChooseFragment;
import cn.ahabox.fragment.MineFragment;
import cn.ahabox.interfaces.CallBackimpl;
import cn.ahabox.model.User;
import cn.ahabox.myalertdialog.SweetAlertDialog;
import cn.ahabox.network.DataParserImpl;

/**
 * Created by libo on 2016/3/15.
 *
 * 登录功能及操作，登录成功后将Token保存在本地
 */
public class LoginUtils {
    private String phone,psd;
    private Context context;
    /** 判断是否是启动登录 */
    private boolean openLogin;

    public LoginUtils(Context context,String phone,String psd,boolean openLogin){
        this.phone = phone;
        this.psd = psd;
        this.context = context;
        this.openLogin = openLogin;
    }
    /**
     * 对用户名和密码进行登录处理
     * @param dataParser
     */
    public void login(DataParserImpl dataParser, final SweetAlertDialog progressDialog){
        Map<String,String> params = new HashMap();
        params.put("password",psd);
        params.put("mobile", phone);
        try {
            params.put("device_token", URLEncoder.encode(App.DEVICE_TOKEN, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        dataParser.setCallBack(context, new CallBackimpl() {
            @Override
            public void callBack(String str) {
                if(null != progressDialog){
                    progressDialog.dismiss();
                }
                handleLogin(str);
            }
        });
        dataParser.parseLogin(params);
    }

    private void handleLogin(String str){
        try {
            JSONObject obj = new JSONObject(str);
            int status = obj.getInt("status");
            if(status == Config.STATUS_SUCCESSED){
                JSONObject userObj = obj.getJSONObject("data").getJSONObject("user");
                User user = JSON.parseObject(userObj.toString(), User.class);
                saveLoginInfo(user);
            }else{
                if(!openLogin) {
                    LoginActivity.callBackimpl.confirmHandle();
                    Toast.makeText(context, obj.getString("errmsg"), Toast.LENGTH_SHORT).show();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 缓存用户登录token
     */
    private void saveLoginInfo(User user){
        App.setUser(user);
        App.setLoginStatus(App.LOGIN_YES);
        if(!openLogin){
            SharedPrefUtils.setParams(context, Config.SAVE_UID, user.getId());
            SharedPrefUtils.setParams(context, Config.SAVE_TOKEN, user.getToken());
            SharedPrefUtils.setParams(context, Config.SAVE_USERNAME, user.getNickname());
            SharedPrefUtils.setParams(context, Config.SAVE_AVATER, user.getAvatar_url());

            //登录成功刷新我的信息
            if(null != MineFragment.loginCallBack){
                MineFragment.loginCallBack.confirmHandle();
            }

            //登录成功刷新快选数据
            if(null != FastChooseFragment.loginCallBack){
                FastChooseFragment.loginCallBack.confirmHandle();
            }
            Toast.makeText(context, "登录成功", Toast.LENGTH_SHORT).show();
            ((Activity)context).finish();
        }
    }

}
