package cn.ahabox.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ahabox.activity.GiftBoxActivity;
import cn.ahabox.activity.GiftCollectionActivity;
import cn.ahabox.activity.MyFocusActivity;
import cn.ahabox.activity.RecipientAddressActivity;
import cn.ahabox.activity.SettingActivity;
import cn.ahabox.activity.TransactionOrderActivity;
import cn.ahabox.activity.VoiceListActivity;
import cn.ahabox.activity.WXLoginActivity;
import cn.ahabox.application.MyApp;
import cn.ahabox.config.App;
import cn.ahabox.config.Config;
import cn.ahabox.feiliwu_help.R;
import cn.ahabox.interfaces.CallBackimpl;
import cn.ahabox.interfaces.IcallBack;
import cn.ahabox.utils.BlurImageView;
import cn.ahabox.utils.DownLoadUtils;
import cn.ahabox.utils.RoundImageView;

/**
 *  Created by libo on 2015/1/20.
 *
 * 我的
 */
public class MineFragment extends Fragment {
    @Bind(R.id.iv_mine_avaterbg) ImageView ivAvaterBg;
    @Bind(R.id.iv_mine_avater) RoundImageView ivAvater;
    @Bind(R.id.btn_mine_login) Button btn_login;
    @Bind(R.id.tv_mine_nichname) TextView tvNickname;
    public static IcallBack loginCallBack,signOutCallBack;
    private Bitmap bitmap;
    private File avaterFile;
    /** 本地文件头像名 */
    private final String AVATER_FILENAME = Config.ROOT + "avater.jpg";

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            ivAvaterBg.setBackgroundDrawable(BlurImageView.BlurImages(bitmap, getActivity()));
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        ButterKnife.bind(this,view);
        initView();
        return view;
    }

    private void initView() {

        if(App.getLoginStatus() == App.LOGIN_YES){
            showUserInfo();
            getAvaterBg();
        }else{
            ivAvater.setImageResource(R.mipmap.avater);
            bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.mine_default_bg);
            ivAvaterBg.setBackgroundDrawable(BlurImageView.BlurImages(bitmap, getActivity()));
        }

        //登录成功回调
        loginCallBack = new CallBackimpl() {
            @Override
            public void confirmHandle() {
                showUserInfo();
                getAvaterBg();
            }
        };

        //退出登录回调
        signOutCallBack = new CallBackimpl() {
            @Override
            public void confirmHandle() {
                btn_login.setVisibility(View.VISIBLE);
                tvNickname.setVisibility(View.GONE);
                ivAvater.setImageResource(R.mipmap.avater);
                bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.mine_default_bg);
                ivAvaterBg.setBackgroundDrawable(BlurImageView.BlurImages(bitmap, getActivity()));
            }
        };
    }

    /**
     * 我的页面获取头像逻辑
     */
    private void getAvaterBg(){
        avaterFile = new File(AVATER_FILENAME);
        if(avaterFile.exists()){
            bitmap = BitmapFactory.decodeFile(AVATER_FILENAME);
            if(null != bitmap){
                ivAvaterBg.setBackgroundDrawable(BlurImageView.BlurImages(bitmap,getActivity()));
            }else{
                downLoadAvater();
            }
        }else {
            if (App.user.getAvatar_url().equals("")) {
                bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.avater);
                ivAvaterBg.setBackgroundDrawable(BlurImageView.BlurImages(bitmap, getActivity()));
            }else{
                downLoadAvater();
            }
        }
    }

    @OnClick({R.id.rl_mine_box,R.id.rl_mine_order, R.id.rl_mine_coll,R.id.rl_mine_address,R.id.rl_mine_voice,
            R.id.rl_mine_setting,R.id.btn_mine_login,R.id.rl_mine_focus})
    void onClick(View view){
        switch (view.getId()){
            case R.id.rl_mine_box:
                //礼物箱
                App.gotoLogin(getActivity(), new CallBackimpl() {
                    @Override
                    public void confirmHandle() {
                        startActivity(new Intent(getActivity(), GiftBoxActivity.class));
                    }
                });
                break;
            case R.id.rl_mine_order:
                //成交订单
                App.gotoLogin(getActivity(), new CallBackimpl() {
                    @Override
                    public void confirmHandle() {
                        startActivity(new Intent(getActivity(), TransactionOrderActivity.class));
                    }
                });
                break;
            case R.id.rl_mine_coll:
                //礼物收藏
                App.gotoLogin(getActivity(), new CallBackimpl() {
                    @Override
                    public void confirmHandle() {
                        startActivity(new Intent(getActivity(), GiftCollectionActivity.class));
                    }
                });
                break;
            case R.id.rl_mine_address:
                //地址管理
                App.gotoLogin(getActivity(), new CallBackimpl() {
                    @Override
                    public void confirmHandle() {
                        Intent intent = new Intent(getActivity(), RecipientAddressActivity.class);
                        intent.putExtra(Config.ADDRESS_PARAMS, 0);
                        startActivity(intent);
                    }
                });
                break;
            case R.id.rl_mine_voice:
                App.gotoLogin(getActivity(), new CallBackimpl() {
                    @Override
                    public void confirmHandle() {
                        Intent intent = new Intent(getActivity(), VoiceListActivity.class);
                        intent.putExtra("entertag",1);
                        startActivity(intent);
                    }
                });
                break;
            case R.id.rl_mine_setting:
                //设置
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
            case R.id.btn_mine_login:
                //登录
                startActivity(new Intent(getActivity(), WXLoginActivity.class));
                break;
            case R.id.rl_mine_focus:
                //我的关注
                App.gotoLogin(getActivity(), new CallBackimpl() {
                    @Override
                    public void confirmHandle() {
                        startActivity(new Intent(getActivity(), MyFocusActivity.class));
                    }
                });
                break;
        }
    }

    private void showUserInfo(){
        btn_login.setVisibility(View.GONE);
        tvNickname.setVisibility(View.VISIBLE);
        tvNickname.setText(App.user.getNickname());
        String getAvater = App.user.getAvatar_url();
        if(!getAvater.equals("")){
            ImageLoader.getInstance().displayImage(getAvater,ivAvater, MyApp.getInstance().options);
        }else{
            ivAvater.setImageResource(R.mipmap.avater);
        }
    }

    private void downLoadAvater(){
        DownLoadUtils.downloadFile(App.user.getAvatar_url(), AVATER_FILENAME, new CallBackimpl() {
            @Override
            public void confirmHandle() {
                bitmap = BitmapFactory.decodeFile(AVATER_FILENAME);
                //获取bitmap需短时间耗时
                handler.sendEmptyMessageDelayed(0, 500);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

}
