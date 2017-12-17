package cn.ahabox.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import cn.ahabox.feiliwu_help.R;

/**
 * Created by libo on 2016/3/15.
 *
 * 根据输入框判断按钮是否可以点击
 */
public class BtnEnabledUtil {

    public static void checkEnable(final EditText editText1, final EditText editText2, final TextView btn){
        btn.setClickable(false);
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str1 = editText1.getText().toString().trim();
                String str2 = editText2.getText().toString().trim();
                if (!str1.isEmpty() && !str2.isEmpty()) {
                    btn.setBackgroundResource(R.color.pressed_red);
                    btn.setClickable(true);
                }else{
                    btn.setBackgroundResource(R.color.btn_cantuse);
                    btn.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
        editText1.addTextChangedListener(watcher);
        editText2.addTextChangedListener(watcher);
    }

}
