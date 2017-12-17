package cn.ahabox.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ahabox.feiliwu_help.R;
import cn.ahabox.application.MyApp;
import cn.ahabox.config.App;
import cn.ahabox.model.CreateOrderEntity;
import cn.ahabox.utils.RecordVoiceUtils;
import cn.ahabox.widget.CustomActionBar;

/**
 * Created by libo on 2015/1/27.
 * <p/>
 * 录制语音
 */
public class RecordVoiceActivity extends BaseActivity {

    @Bind(R.id.iv_wxpay_cover)
    ImageView ivWxpayCover;
    @Bind(R.id.tv_wxpay_introduce)
    TextView tvWxpayIntroduce;
    @Bind(R.id.tv_info_num)
    TextView tvInfoNum;
    @Bind(R.id.tv_info_price)
    TextView tvInfoPrice;
    private CustomActionBar customActionBar;
    private RecordVoiceUtils recordVoiceUtils;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_voice);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        customActionBar = (CustomActionBar) findViewById(R.id.recordvoice_header);
        customActionBar.initStyle(CustomActionBar.HeaderStyle.DEFAULT_TITLE);
        customActionBar.setTitle(getResources().getString(R.string.title_recordvoice));

        mediaPlayer = new MediaPlayer();
        recordVoiceUtils = new RecordVoiceUtils(this, mediaPlayer);

        CreateOrderEntity entity = App.orderEntity;
        ImageLoader.getInstance().displayImage(entity.getCover_url(), ivWxpayCover, MyApp.getInstance().options);
        tvWxpayIntroduce.setText(entity.getProduce_name());
        tvInfoNum.setText("" + entity.getQuantity());
        tvInfoPrice.setText("" + entity.getRealPrice());
    }

    @OnClick({R.id.tv_recordvoice_choose, R.id.tv_recordvoice_click})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_recordvoice_choose:
                startActivity(new Intent(getApplicationContext(), VoiceListActivity.class));
                break;
            case R.id.tv_recordvoice_click:
                recordVoiceUtils.startAudio();
                recordVoiceUtils.showDialog();
                break;
        }
    }

    /**
     * 当程序停止的时候
     */
    @Override
    protected void onStop() {
        if (null != recordVoiceUtils.mediaRecorder && recordVoiceUtils.isLuYin) {
            // 停止录音
            recordVoiceUtils.mediaRecorder.stop();
            recordVoiceUtils.mediaRecorder.release();
            recordVoiceUtils.mediaRecorder = null;
        }
        super.onStop();
    }

}
