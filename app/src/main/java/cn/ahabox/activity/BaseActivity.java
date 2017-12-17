package cn.ahabox.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import cn.ahabox.application.MyApp;
import cn.ahabox.network.DataParserImpl;
import cn.ahabox.utils.DialogUtils;

/**
 * Created by libo on 2015/11/15.
 *
 * Activity基类
 */
public abstract class BaseActivity extends AppCompatActivity{
    protected DialogUtils dialogUtils;
    protected DataParserImpl dataParser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //统计应用启动数据
        PushAgent.getInstance(this).onAppStart();
        MyApp.addActivity(this);
        initial();
    }

    protected void initial() {
        //将所有activity添加到栈中进行管理
        MyApp.getInstance().addActivity(this);
        dialogUtils = new DialogUtils(this);
        dataParser = new DataParserImpl();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //将activity弹出栈
        MyApp.getInstance().removeActivity(this);
    }

    //友盟session的统计
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    /**
     * 短时间Toast
     * @param msg
     */
    protected void showshortText(String msg){
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
    }

    /**
     * 含有Bundle对象的跳转
     * @param cls
     * @param bundle
     */
    protected void startActivity(Class<?> cls, Bundle bundle){
        Intent intent = new Intent(this,cls);
        if(bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

}
