package cn.ahabox.network;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.ahabox.config.App;

/**
 * Created by libo on 2015/11/20.
 *
 * 网络请求发送操作类
 */
public class NetUtil {

    /**
     * 发送GET请求返回JsonObject
     * @param context
     * @param requestUrl  请求url
     * @param listener    正确返回的listener
     * @param errorListener  错误返回的listener
     */
    public static void getJsonObjectRequest(Context context,String requestUrl,Response.Listener<JSONObject> listener,Response.ErrorListener errorListener){
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,requestUrl,null,listener,errorListener);
        RequestManager requestManager = RequestManager.getInstance(context);
        requestManager.getJsonRequestQueue().add(request);
    }

    /**
     * 发送POST请求返回JsonObject
     * @param context
     * @param requestUrl
     * @param listener
     * @param errorListener
     */
    public static void postJsonStringRequest(Context context,String requestUrl, final Map<String,String> params,Response.Listener<String> listener,Response.ErrorListener errorListener){
        StringRequest request = new StringRequest(Request.Method.POST,requestUrl,listener,errorListener){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
        RequestManager requestManager = RequestManager.getInstance(context);
        requestManager.getJsonRequestQueue().add(request);
    }

    /**
     *  发送带有headers的GET请求
     * @param context
     * @param requestUrl
     * @param listener
     * @param errorListener
     */
    public static void getTokenStringRequest(Context context,String requestUrl,Response.Listener<String> listener,Response.ErrorListener errorListener){
        StringRequest request;
        if(App.getLoginStatus() == App.LOGIN_YES){
            request = new StringRequest(Request.Method.GET,requestUrl,listener,errorListener){

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map token = new HashMap();
                    token.put("Authorization", App.user.getToken());
                    return token;
                }
            };
        }else{
            request = new StringRequest(Request.Method.GET,requestUrl,listener,errorListener);
        }
        RequestManager requestManager = RequestManager.getInstance(context);
        requestManager.getJsonRequestQueue().add(request);
    }

    /**
     * 发送仅带有Token的POST请求
     * @param context
     * @param requestUrl
     * @param listener
     * @param errorListener
     */
    public static void postTokenStringRequest(Context context,String requestUrl,Response.Listener<String> listener,Response.ErrorListener errorListener){
        StringRequest request = new StringRequest(Request.Method.POST,requestUrl,listener,errorListener){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map token = new HashMap();
                token.put("Authorization", App.user.getToken());
                return token;
            }
        };
        RequestManager requestManager = RequestManager.getInstance(context);
        requestManager.getJsonRequestQueue().add(request);
    }

    /**
     * 发送带有用户token的post请求
     * @param context
     * @param requestUrl 请求url
     * @param params  请求Map参数
     * @param listener  正确返回的listener
     * @param errorListener  错误返回的listener
     */
    public static void postTokenStringRequest(Context context,String requestUrl, final Map<String,String> params,Response.Listener<String> listener,Response.ErrorListener errorListener){
        StringRequest request = new StringRequest(Request.Method.POST,requestUrl,listener,errorListener){

            @Override
            protected Map<String,String> getParams() throws AuthFailureError {return params;}

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map token = new HashMap();
                token.put("Authorization", App.user.getToken());
                return token;
            }
        };
        RequestManager requestManager = RequestManager.getInstance(context);
        requestManager.getJsonRequestQueue().add(request);
    }

    /**
     * 验证码token调用
     */
    public static void postHeadersStringRequest(Context context,String requestUrl, final Map<String,String> params,final Map<String,String> headers,Response.Listener<String> listener,Response.ErrorListener errorListener){
        StringRequest request = new StringRequest(Request.Method.POST,requestUrl,listener,errorListener){

            @Override
            protected Map<String,String> getParams() throws AuthFailureError {return params;}

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {return headers;}
        };
        RequestManager requestManager = RequestManager.getInstance(context);
        requestManager.getJsonRequestQueue().add(request);
    }

}
