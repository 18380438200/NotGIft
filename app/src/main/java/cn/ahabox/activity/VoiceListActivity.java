package cn.ahabox.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ahabox.adapter.VoiceListAdapter;
import cn.ahabox.feiliwu_help.R;
import cn.ahabox.interfaces.CallBackimpl;
import cn.ahabox.model.VoiceEntiy;
import cn.ahabox.widget.CustomActionBar;

/**
 * Created by libo on 2015/1/27.
 * <p/>
 * 语音列表操作
 */
public class VoiceListActivity extends BaseActivity {

    @Bind(R.id.listViewAudioFile)
    ListView listViewAudioFile;
    @Bind(R.id.tv_recorder_confire)
    TextView tvRecorderConfire;
    private CustomActionBar customActionBar;
    private MediaPlayer mediaPlayer;
    private VoiceListAdapter voiceListAdapter;
    private ArrayList<VoiceEntiy> datas = new ArrayList<>();
    public static CallBackimpl voiceReload;
    /**
     * 选择语音Id
     */
    public static int message_id;
    /**
     * 进入来源标志，1为语音管理，0为购买流程
     */
    private int enterTag = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_list);
        ButterKnife.bind(this);

        initView();
        initData();
        event();
    }

    /**
     * 初始化组件
     */
    private void initView() {
        customActionBar = (CustomActionBar) findViewById(R.id.recorder_header);
        customActionBar.initStyle(CustomActionBar.HeaderStyle.DEFAULT_TITLE);
        customActionBar.setTitle(getResources().getString(R.string.title_voice_list));
        mediaPlayer = new MediaPlayer();

        enterTag = getIntent().getIntExtra("entertag", 0);

        voiceListAdapter = new VoiceListAdapter(this, datas, mediaPlayer);
        listViewAudioFile.setAdapter(voiceListAdapter);

        voiceReload = new CallBackimpl() {
            @Override
            public void confirmHandle() {
                initData();
            }
        };

        if (enterTag == 1)
            tvRecorderConfire.setVisibility(View.GONE);

    }

    private void initData() {
        dialogUtils.progressDialog();
        dataParser.setCallBack(getApplicationContext(), new CallBackimpl() {
            @Override
            public void callBack(List list) {
                if (null != dialogUtils.getProgressDialog()) {
                    dialogUtils.getProgressDialog().dismiss();
                }
                datas.clear();
                voiceListAdapter.addAll(list);
                voiceListAdapter.setSelection(0);
            }
        });
        dataParser.parseVoiceList();
    }

    /**
     * 初始化数据
     */
    private void event() {
        listViewAudioFile.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                voiceListAdapter.setSelection(position);
                voiceListAdapter.notifyDataSetChanged();

            }
        });
    }

    @OnClick(R.id.tv_recorder_confire)
    void onClick() {
        startActivity(new Intent(getApplicationContext(), GiftinfoConfirmActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
        mediaPlayer = null;
    }

}