package cn.ahabox.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import cn.ahabox.feiliwu_help.R;
import cn.ahabox.activity.RecipientAddressActivity;
import cn.ahabox.activity.RecordVoiceActivity;
import cn.ahabox.config.App;
import cn.ahabox.config.Config;

/**
 * Created by libo on 2016/4/5.
 *
 * 选择地址dialog
 */
public class AddressDialog {
    private Dialog dialog;
    private Context context;

    public AddressDialog(Context context){
        this.context = context;
        dialog = new Dialog(context, R.style.Dialog_Fullscreen);
        dialog.setContentView(R.layout.dialog_writeaddress);
        dialog.show();
        init();
    }

    private void init(){
        TextView tvWriteAdressMyself = (TextView) dialog.findViewById(R.id.tv_writeadd_myself);
        TextView tvWriteAddressOther = (TextView) dialog.findViewById(R.id.tv_writeadd_other);
        TextView tvCancel = (TextView) dialog.findViewById(R.id.tv_writeadd_cancel);
        MyListener listener = new MyListener();
        tvWriteAdressMyself.setOnClickListener(listener);
        tvWriteAddressOther.setOnClickListener(listener);
        tvCancel.setOnClickListener(listener);
    }

    class MyListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tv_writeadd_myself:
                    App.BUY_WAY = App.BUY_OTHER_HAVEADD;  //送给Ta有地址方式
                    Intent intent = new Intent(context, RecipientAddressActivity.class);
                    intent.putExtra(Config.ADDRESS_PARAMS,1);  //如果是购买流程，则显示下一步；0表示地址管理，1表示购买选择地址
                    context.startActivity(intent);
                    break;
                case R.id.tv_writeadd_cancel:
                    break;
                case R.id.tv_writeadd_other:
                    App.BUY_WAY = App.BUY_OTHER_NOADD;  //送给Ta没有地址方式
                    context.startActivity(new Intent(context, RecordVoiceActivity.class));
                    break;
            }
            if(null != dialog){
                dialog.dismiss();
            }
        }
    }
}
