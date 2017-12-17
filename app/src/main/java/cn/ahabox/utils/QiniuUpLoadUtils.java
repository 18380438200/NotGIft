package cn.ahabox.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import cn.ahabox.activity.GiftinfoConfirmActivity;
import cn.ahabox.config.App;
import cn.ahabox.interfaces.CallBackimpl;
import cn.ahabox.network.DataParserImpl;

/**
 * Created by libo on 2015/11/26.
 *
 * 文件上传七牛服务器
 */
public class QiniuUpLoadUtils {
    /** 单例上传管理器 */
    private static UploadManager uploadManager;
    /** 上传语音名称 */
    public static String key;

    public static void getUploadToken(final String voiceFilePath,final Context context){
        DataParserImpl dataParse = new DataParserImpl();
        dataParse.setCallBack(context, new CallBackimpl() {
            @Override
            public void callBack(String str) {
                try {
                    JSONObject obj = new JSONObject(str).getJSONObject("data");
                    String upLoadToken = obj.getString("upload_token");
                    String domain = obj.getString("domain");
                    key = obj.getString("key");
                    upLoadFile(voiceFilePath, upLoadToken, key, context);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        dataParse.parseUploadToken();
    }

    /**
     *  文件上传
     * @param filePath 要上传的文件路径
     * @param token 服务器获取token
     * @param key 保存到服务器的文件名称
     */
    public static void upLoadFile(String filePath,String token,String key, final Context context){
        if(null == uploadManager){
            uploadManager = new UploadManager();
        }
        File file = new File(filePath);
        if(file.exists()){
            uploadManager.put(file, key, token, new UpCompletionHandler() {
                @Override
                public void complete(String s, ResponseInfo responseInfo, JSONObject jsonObject) {
                    Toast.makeText(context,"上传成功",Toast.LENGTH_SHORT).show();
                    App.isChooseVoice = false;
                    context.startActivity(new Intent(context, GiftinfoConfirmActivity.class));
                    ((Activity)context).finish();
                }
            }, null);
        }
    }

}
