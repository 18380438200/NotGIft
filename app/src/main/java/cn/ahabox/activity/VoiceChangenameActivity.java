package cn.ahabox.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ahabox.feiliwu_help.R;
import cn.ahabox.config.Config;
import cn.ahabox.interfaces.CallBackimpl;
import cn.ahabox.widget.CustomActionBar;

/**
 * Created by libo on 2015/3/27.
 *
 * 语音改名
 */
public class VoiceChangenameActivity extends BaseActivity {
    @Bind(R.id.et_changename_name)
    EditText etChangenameName;
    /**
     * 语音id
     */
    private int voiceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_changename);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        CustomActionBar customActionBar = (CustomActionBar) findViewById(R.id.voicechangename_header);
        customActionBar.initStyle(CustomActionBar.HeaderStyle.DEFAULT_TITLE);
        customActionBar.setTitle(getResources().getString(R.string.title_voice_change));

        voiceId = getIntent().getIntExtra("voiceid", 1);
    }

    @OnClick({R.id.tv_changename_cancel,R.id.tv_changename_confirm})
    void onClick(View view){
        switch (view.getId()){
            case R.id.tv_changename_cancel:
                finish();
                break;
            case R.id.tv_changename_confirm:
                voiceRename();
                break;
        }
    }

    private void voiceRename(){
        dialogUtils.progressDialog();
        String newName = etChangenameName.getText().toString().trim();
        if(TextUtils.isEmpty(newName)){
            showshortText("语音名称不能为空");
        }else{
            HashMap params = new HashMap();
            params.put("message_name",newName);
            dataParser.setCallBack(getApplicationContext(), new CallBackimpl() {
                @Override
                public void callBack(String str) {
                    dialogUtils.getProgressDialog().dismiss();
                    handleResult(str);
                }
            });
            dataParser.parseVoiceRename(voiceId, params);
        }
    }

    private void handleResult(String str){
        try {
            JSONObject obj = new JSONObject(str);
            int status = obj.getInt("status");
            if (status == Config.STATUS_SUCCESSED) {
                showshortText("修改成功");
                finish();
                VoiceListActivity.voiceReload.confirmHandle();
            } else {
                showshortText(obj.getString("errmsg"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
