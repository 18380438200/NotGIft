package cn.ahabox.config;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import cn.ahabox.activity.WXLoginActivity;
import cn.ahabox.fragment.FastChooseFragment;
import cn.ahabox.fragment.MineFragment;
import cn.ahabox.interfaces.CallBackimpl;
import cn.ahabox.model.CreateOrderEntity;
import cn.ahabox.model.User;
import cn.ahabox.utils.SharedPrefUtils;

/**
 * Created by libo on 2016/3/11.
 *
 * 应用全局状态
 */
public class App {

    /** 应用登录状态:0,未登录;1,已登录;2,微信登录*/
    public static int loginStatus;

    /** 用户登录成功后返回信息 */
    public static User user;

    /** 未登录 */
    public static final int LOGIN_NO = 0;

    /** 已登录 */
    public static final int LOGIN_YES = 1;

    /** 友盟推送设备号 */
    public static String DEVICE_TOKEN = "";

    /** 购买方式标志:0,送自己  1，送给Ta有地址 2，送给Ta无地址 3，爱心捐赠-1,默认*/
    public static int BUY_WAY = -1;

    public static final int BUY_MYSELF = 0;

    public static final int BUY_OTHER_HAVEADD = 1;

    public static final int BUY_OTHER_NOADD = 2;

    public static final int BUY_DONATIONS = 3;

    /** 支付方式标志：0，商品直接支付方式; 1,购物车支付方式 */
    public static int PAY_WAY = 0;

    /** 本次使用是否提交过意见反馈，每次启动只能提交一次 */
    public static boolean IS_POST_FEEDBACK = false;

    /** 是否是注册 */
    public static boolean IS_REGISTER = false;

    /** 订单号 */
    public static String orderids;

    /** 是否是选择语音还是录制语音 */
    public static boolean isChooseVoice = true;

    /**
     * 订单信息展示对象
     */
    public static CreateOrderEntity orderEntity;

    public static int getLoginStatus() {
        return loginStatus;
    }

    public static void setLoginStatus(int loginStatus) {
        App.loginStatus = loginStatus;
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        App.user = user;
    }

    /**
     * 退出登录时清空登录用户账号
     */
    public static void clearStatus(Context context,TextView textView){
        //消除当前登录状态
        SharedPrefUtils.setParams(context, Config.SAVE_TOKEN, "");
        setLoginStatus(App.LOGIN_NO);
        user = null;
        textView.setVisibility(View.GONE);
        MineFragment.signOutCallBack.confirmHandle();
        ((Activity)context).finish();
        //退出登录刷新快选数据
        if(null != FastChooseFragment.loginCallBack){
            FastChooseFragment.loginCallBack.confirmHandle();
        }
    }

    /**
     * 在未登录的情况，操作需先登录
     */
    public static void gotoLogin(Context context,CallBackimpl callBackimpl){
        if(getLoginStatus() == LOGIN_NO){
            context.startActivity(new Intent(context, WXLoginActivity.class));
            Toast.makeText(context,"请先登录",Toast.LENGTH_SHORT).show();
        }else{
            callBackimpl.confirmHandle();
        }
    }

}
