package cn.ahabox.activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Toast;

import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengRegistrar;

import butterknife.ButterKnife;
import cn.ahabox.application.MyApp;
import cn.ahabox.config.App;
import cn.ahabox.feiliwu_help.R;
import cn.ahabox.fragment.AboutUsFragment;
import cn.ahabox.fragment.ClassifyFragment;
import cn.ahabox.fragment.FastChooseFragment;
import cn.ahabox.fragment.FirstPageFragment;
import cn.ahabox.fragment.MineFragment;
import cn.ahabox.interfaces.CallBackimpl;
import cn.ahabox.interfaces.IcallBack;
import cn.ahabox.utils.SharedPrefUtils;
import cn.ahabox.utils.StrUtils;
import cn.ahabox.widget.FlowMenu;

/**
 * Created by libo on 2015/11/23.
 *
 * 功能切换主界面
 */
public class MainActivity extends BaseActivity {
    private View parentView;
    private FirstPageFragment firstPageFragment;
    private FastChooseFragment fastChooseFragment;
    private MineFragment mineFragment;
    private ClassifyFragment classifyFragment;
    private AboutUsFragment aboutUsFragment;
    private FragmentManager fragmentManager;
    /**
     * 返回键退出间隔时间
     */
    public long EXITTIME = 2000;
    private Bundle instanceState;
    /**
     * 切换主页面的回调
     */
    public static IcallBack callBack;
    /**
     * 全局语音播放器
     */
    public static MediaPlayer mediaPlayer;
    public static PushAgent mPushAgent;
    /**
     * 语音大小尺寸
     */
    public static int voiceDimens = 0;
    /**
     * 进度条语音大小尺寸
     */
    public static int progressVoiceDimens = 0;
    public static int recordVoiceWidth = 0;
    public static int recordVoiceHeight = 0;
    public static IcallBack menuCallBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentView = getLayoutInflater().inflate(R.layout.activity_main,null);
        setContentView(parentView);
        ButterKnife.bind(this);
        instanceState = savedInstanceState;

        MyApp.getInstance().addActivity(this);
        init();
        getVoiceDimens();
    }

    private void init() {
        fragmentManager = getSupportFragmentManager();
        //默认选择首页
        setFragment(0,0);

        mediaPlayer = new MediaPlayer();

        new FlowMenu(this,parentView);
        UmengPush();

        menuCallBack = new CallBackimpl() {
            @Override
            public void callBack(int page,int position) {
                setFragment(page,position);
            }
        };
    }

    private void getVoiceDimens() {
        //设置各个语音gif显示的大小
        voiceDimens = StrUtils.dpTopx(this, 34);
        progressVoiceDimens = StrUtils.dpTopx(this, 40);
        recordVoiceWidth = StrUtils.dpTopx(this, 10);
        recordVoiceHeight = StrUtils.dpTopx(this, 14);
    }

    /**
     * 友盟推送设置
     */
    private void UmengPush() {
        //开启友盟推送服务
        mPushAgent = PushAgent.getInstance(this);
        if (SharedPrefUtils.getParams(this, "umengpush", true) == true) {
            mPushAgent.enable(new IUmengRegisterCallback() {
                @Override
                public void onRegistered(String s) {
                }
            });
        } else {
            mPushAgent.disable();
        }

        App.DEVICE_TOKEN = UmengRegistrar.getRegistrationId(this);
    }

    /**
     * 设置切换页面
     *
     * @param page
     */
    private void setFragment(int page,int position) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragments(transaction);
        switch (page) {
            case 0:
                if (null == firstPageFragment) {
                    firstPageFragment = new FirstPageFragment();
                    if (null == instanceState)
                        transaction.add(R.id.frame_main, firstPageFragment);
                } else {
                    transaction.show(firstPageFragment);
                }
                break;
            case 1:
                if (null == aboutUsFragment) {
                    aboutUsFragment = new AboutUsFragment();
                    if (null == instanceState)
                        transaction.add(R.id.frame_main, aboutUsFragment);
                } else {
                    transaction.show(aboutUsFragment);
                }
                break;
            case 2:
                if (null == fastChooseFragment) {    //fastChooseFragment为空通过创建传值，不为空通过回调传值
                    fastChooseFragment = FastChooseFragment.newInstance(position);
                    if (null == instanceState)
                        transaction.add(R.id.frame_main, fastChooseFragment);
                } else {
                    fastChooseFragment.cateCallBack.callBack(position);
                    transaction.show(fastChooseFragment);
                }
                break;
            case 3:
                if(null == classifyFragment){
                    classifyFragment = new ClassifyFragment();
                    if(null == instanceState)
                        transaction.add(R.id.frame_main,classifyFragment);
                }else{
                    transaction.show(classifyFragment);
                }
                break;
            case 4:
                if (null == mineFragment) {
                    mineFragment = new MineFragment();
                    if (null == instanceState)
                        transaction.add(R.id.frame_main, mineFragment);
                } else {
                    transaction.show(mineFragment);
                }
                break;
        }
        transaction.commitAllowingStateLoss();
    }

    /**
     * 将其他fragment隐藏
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (null != firstPageFragment) transaction.hide(firstPageFragment);
        if (null != aboutUsFragment) transaction.hide(aboutUsFragment);
        if (null != fastChooseFragment) transaction.hide(fastChooseFragment);
        if (null != mineFragment) transaction.hide(mineFragment);
        if (null != classifyFragment) transaction.hide(classifyFragment);
    }

    /**
     * 连续按返回键退出程序
     */
    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - EXITTIME) > 2000) {
            Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
            EXITTIME = System.currentTimeMillis();
        } else {
            MyApp.getInstance().finishAll();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer = null;
    }

}
