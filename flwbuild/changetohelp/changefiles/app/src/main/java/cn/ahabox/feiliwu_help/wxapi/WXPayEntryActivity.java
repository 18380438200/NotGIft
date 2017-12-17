package cn.ahabox.feiliwu_help.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import cn.ahabox.activity.BaseActivity;
import cn.ahabox.activity.WXpayActivity;
import cn.ahabox.config.Config;
import cn.ahabox.feiliwu_help.R;

public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler{
    private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    	api = WXAPIFactory.createWXAPI(this, Config.APP_ID);
        api.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {

		switch (resp.errCode){
			case -1:
			case 0:
				if(App.PAY_WAY == 0){
					WXpayActivity.payComplish.confirmHandle();
				}else if(App.PAY_WAY == 1){
					WXShopcartPayActivity.payComplish.confirmHandle();
				}
				break;
			case -2:
				Toast.makeText(getApplicationContext(),R.string.payment_cancel,Toast.LENGTH_SHORT).show();
				break;
		}
		finish();
	}

}