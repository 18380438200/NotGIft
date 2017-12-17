package cn.ahabox.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import cn.ahabox.activity.GuideActivity;
import cn.ahabox.activity.MainActivity;

/**
 * Created by libo on 2015/11/24.
 *
 * 判断是否是第一次进入工具类
 */
public class FirstIn {
	private Context context;
	/** 判断是否是第一次进入 */
	private boolean isFirstIn = true;
	/** 获取是否为第一次进入布尔值得键*/
	private final String ISFIRSTIN = "IsFirstIn";
	private final int GOHOME = 1;
	private final int GOGUIDE = 0;
	/** 闪屏延迟时间 */
	private final int DELAY_TIME = 1500;
	
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case GOHOME:
				context.startActivity(new Intent(context,MainActivity.class));
				((Activity)context).finish();
				break;
			case GOGUIDE:
				context.startActivity(new Intent(context,GuideActivity.class));
				((Activity)context).finish();
				break;
			}
		};
	};
	
	public FirstIn(Context context){
		this.context = context;
	}

	/**
	 * 发送程序进入下一步的标志
	 */
	public void sendDirection() {
		isFirstIn = (boolean) SharedPrefUtils.getParams(context,ISFIRSTIN,true);
		if(!isFirstIn){
			handler.sendEmptyMessageDelayed(GOHOME, DELAY_TIME);
		}else {
			handler.sendEmptyMessageDelayed(GOGUIDE, DELAY_TIME);
		}
	}
	
}
