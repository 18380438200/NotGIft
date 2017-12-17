package cn.ahabox.feiliwu_help.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

import cn.ahabox.activity.BaseActivity;
import cn.ahabox.activity.CodesActivity;
import cn.ahabox.activity.EnterPassWordActivity;
import cn.ahabox.activity.SuccessfulSendActivity;
import cn.ahabox.application.MyApp;
import cn.ahabox.config.App;
import cn.ahabox.config.Config;
import cn.ahabox.fragment.FastChooseFragment;
import cn.ahabox.fragment.MineFragment;
import cn.ahabox.interfaces.CallBackimpl;
import cn.ahabox.model.User;
import cn.ahabox.network.DataParserImpl;
import cn.ahabox.utils.SharedPrefUtils;

/**
 * Created by libo on 2016/3/30.
 *
 * 微信登录和分享回调类
 */
public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler{
    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, Config.APP_ID,true);
        api.handleIntent(getIntent(),this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        switch (baseResp.errCode){

            case BaseResp.ErrCode.ERR_OK:
                if(baseResp.getType() == ConstantsAPI.COMMAND_SENDAUTH){    //登录回调
                    String code = ((SendAuth.Resp)baseResp).code;
                    if(App.IS_REGISTER){
                        wxRegister(code);
                    }else{
                        wxLogin(code);
                    }
                }else if(baseResp.getType() == ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX){  //分享回调
                    startActivity(new Intent(getApplication(), SuccessfulSendActivity.class));
                }

                break;

            case BaseResp.ErrCode.ERR_AUTH_DENIED:// 用户拒绝授权
                Toast.makeText(getApplicationContext(),"取消授权",Toast.LENGTH_SHORT).show();
                break;

            case BaseResp.ErrCode.ERR_USER_CANCEL:// 用户取消
                Toast.makeText(getApplicationContext(),"操作取消",Toast.LENGTH_SHORT).show();
                break;
        }
        finish();
    }

    /**
     * 微信注册或登录
     */
    private void wxRegister(String code){
        DataParserImpl dataParser = new DataParserImpl();
        HashMap<String,String> params = new HashMap<>();
        HashMap<String,String> headers = new HashMap<>();
        params.put("password", EnterPassWordActivity.postPsd);
        params.put("code",code);
        try {
            params.put("device_token",URLEncoder.encode(App.DEVICE_TOKEN, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        headers.put("Authorization", CodesActivity.Token);
        dataParser.setCallBack(getApplicationContext(), new CallBackimpl() {
            @Override
            public void callBack(JSONObject obj) {
                if(null != obj) {
                    User user = JSON.parseObject(obj.toString(), User.class);
                    saveLoginInfo(user);
                }
            }
        });
        dataParser.parsePostRegister(params, headers);
    }

    /**
     * 微信登录
     * @param code
     */
    private void wxLogin(String code){
        DataParserImpl dataParser = new DataParserImpl();
        HashMap<String,String> wxLoginparams = new HashMap<>();
        wxLoginparams.put("code", code);
        try {
            wxLoginparams.put("device_token", URLEncoder.encode(App.DEVICE_TOKEN, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        dataParser.setCallBack(getApplicationContext(), new CallBackimpl() {
            @Override
            public void callBack(String response) {

                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getInt("status") == Config.STATUS_SUCCESSED) {
                        JSONObject userObj = obj.getJSONObject("data").getJSONObject("user");
                        User user = JSON.parseObject(userObj.toString(), User.class);
                        saveLoginInfo(user);
                    } else {
                        if(obj.getInt("errcode") == 4){  //未绑定手机号
                            showshortText("请先关联手机号");
                            Intent registerIntent = new Intent(getApplicationContext(), CodesActivity.class);
                            registerIntent.putExtra(Config.REGISTER_TYPE, Config.TYPE_REGISTER);
                            startActivity(registerIntent);
                        }
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        dataParser.parseWxLogin(wxLoginparams);
    }

    /**
     * 缓存用户登录信息，包括token，用户名，头像
     */
    private void saveLoginInfo(User user){
        App.setUser(user);
        App.setLoginStatus(App.LOGIN_YES);
        SharedPrefUtils.setParams(this, Config.SAVE_AVATER, user.getAvatar_url());
        SharedPrefUtils.setParams(this, Config.SAVE_USERNAME,user.getNickname());
        SharedPrefUtils.setParams(this, Config.SAVE_TOKEN, user.getToken());
        MineFragment.loginCallBack.confirmHandle();
        //登录成功刷新快选数据
        if(null != FastChooseFragment.loginCallBack){
            FastChooseFragment.loginCallBack.confirmHandle();
        }
        Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
        MyApp.finishSome();
    }
}
