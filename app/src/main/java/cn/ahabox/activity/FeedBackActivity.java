package cn.ahabox.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ahabox.feiliwu_help.R;
import cn.ahabox.config.App;
import cn.ahabox.config.Config;
import cn.ahabox.interfaces.CallBackimpl;
import cn.ahabox.widget.CustomActionBar;

/**
 * Created by libo on 2015/2/26.
 *
 * 意见反馈
 */
public class FeedBackActivity extends BaseActivity {
    @Bind(R.id.et_feedback_question)
    EditText etFeedbackQuestion;
    @Bind(R.id.et_feedback_contact)
    EditText etFeedbackContact;
    @Bind(R.id.tv_feedback_post)
    TextView tvFeedbackPost;
    private CustomActionBar customActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        customActionBar = (CustomActionBar) findViewById(R.id.feedback_header);
        customActionBar.initStyle(CustomActionBar.HeaderStyle.DEFAULT_TITLE);
        customActionBar.setTitle(getResources().getString(R.string.title_feedback));

        if (App.IS_POST_FEEDBACK)
            tvFeedbackPost.setEnabled(false);
    }

    @OnClick({R.id.tv_feedback_cancel, R.id.tv_feedback_post})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_feedback_cancel:
                finish();
                break;
            case R.id.tv_feedback_post:
                checkCondition();
                break;
        }
    }

    private void checkCondition() {
        String content = etFeedbackQuestion.getText().toString().trim();
        String contact = etFeedbackContact.getText().toString().trim();
        if (!TextUtils.isEmpty(content)) {
            if (!TextUtils.isEmpty(contact)) {
                HashMap params = new HashMap();
                params.put("content", content);
                params.put("contact", contact);
                dataParser.setCallBack(getApplicationContext(), new CallBackimpl() {
                    @Override
                    public void callBack(String str) {
                        postFeedBack(str);
                    }
                });
                dataParser.parseFeedBack(params);
            } else {
                showshortText("联系方式不能为空");
            }
        } else {
            showshortText("反馈内容不能为空");
        }
    }

    private void postFeedBack(String str) {
        try {
            JSONObject obj = new JSONObject(str);
            int status = obj.getInt("status");
            if (status == Config.STATUS_SUCCESSED) {
                App.IS_POST_FEEDBACK = true;
                tvFeedbackPost.setEnabled(false);
                showshortText("提交成功");
                finish();
            } else {
                showshortText(obj.getString("errmsg"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
