package cn.ahabox.network;

import android.content.Context;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by libo on 2015/11/20.
 *
 * 网络请求队列管理类
 */
public class RequestManager {

    /** json请求队列 */
    private static RequestQueue requestQueue = null;
    private static RequestManager requestManager = null;

    private RequestManager(){}

    /**
     * 单例模式创建RequestManager
     * @return
     */
    public static RequestManager getInstance(Context context){
        if(requestManager == null){
            requestManager = new RequestManager();
            requestQueue = Volley.newRequestQueue(context);
        }
        return requestManager;
    }

    /**
     * 得到json的请求队列
     */
    public RequestQueue getJsonRequestQueue(){
        return requestQueue;
    }
}

