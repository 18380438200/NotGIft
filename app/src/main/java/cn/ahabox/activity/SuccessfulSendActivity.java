package cn.ahabox.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ahabox.application.MyApp;
import cn.ahabox.config.App;
import cn.ahabox.feiliwu_help.R;
import cn.ahabox.widget.CustomActionBar;

/**
 * Created by libo on 2015/1/29.
 * <p/>
 * 分享商品链接成功
 */
public class SuccessfulSendActivity extends AppCompatActivity {
    @Bind(R.id.tv_successful_send)
    TextView tvSuccessfulSend;
    private CustomActionBar customActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_successful_send);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        customActionBar = (CustomActionBar) findViewById(R.id.sended_header);
        customActionBar.initStyle(CustomActionBar.HeaderStyle.ONLY_TITLE);
        customActionBar.setTitle(getResources().getString(R.string.title_send_success));

        if (App.BUY_WAY == App.BUY_MYSELF) {
            tvSuccessfulSend.setText(R.string.quick_ship_tip);
        }

        //前面所有购买流程Activity删除
        MyApp.finishSome();
    }

    @OnClick(R.id.tv_send_enterbox)
    void onClick() {
        finish();
    }
}
