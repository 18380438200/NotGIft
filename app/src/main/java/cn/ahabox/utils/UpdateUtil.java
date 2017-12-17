package cn.ahabox.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.alibaba.fastjson.JSON;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import cn.ahabox.application.MyApp;
import cn.ahabox.config.Config;
import cn.ahabox.feiliwu_help.R;
import cn.ahabox.interfaces.CallBackimpl;
import cn.ahabox.model.UpdateEntity;
import cn.ahabox.network.DataParserImpl;

/**
 * Created by libo on 2015/12/4.
 *
 * App版本升级工具类
 */
public class UpdateUtil {

    private Context context;
    private NotificationManager notiManager;
    private int maxLen = 0;
    /** 当前下载百分比进度 */
    private int downPercent = 0;
    private String notifyTag;
    private NotificationCompat.Builder builder;
    private DialogUtils dialogUtils;
    /** 从服务器获取的版本号 */
    private int getVersionCode;
    private final String VERSION_CODE = "versioncode";
    /** 下载的Apk文件名 */
    private final String APKNAME = "feiliwu.apk";

    public UpdateUtil(Context context){
        this.context = context;
        notiManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(context);
        builder.setContentTitle("正在下载:非礼物")
                .setSmallIcon(R.mipmap.ic_launcher);
        dialogUtils = new DialogUtils(context);
    }

    /**
     * （设置）检查版本号并更新
     */
    public void checkUpdate() {
        dialogUtils.progressDialog();
        DataParserImpl dataParser = new DataParserImpl();
        dataParser.setCallBack(context, new CallBackimpl() {
            @Override
            public void callBack(JSONObject obj) {
                if (null != dialogUtils.getProgressDialog()) {
                    dialogUtils.getProgressDialog().dismiss();
                }
                UpdateEntity entity = JSON.parseObject(obj.toString(), UpdateEntity.class);
                notifyTag = entity.getNotify_type();
                getVersionCode = Integer.parseInt(entity.getVersion_number());
                int currentVersionCode = getVersionCode(context);
                if (getVersionCode > currentVersionCode) {
                    updateWay(entity,0);
                } else {
                    dialogUtils.tipDialog("当前已经是最新版");
                }
            }
        });
        dataParser.parseVersion();
    }

    /**
     * 首页弹框提示更新，更新方式有：手动更新，提示更新，强制更新
     */
    public void firstPageCheckUpdate(){
        DataParserImpl dataParser = new DataParserImpl();
        dataParser.setCallBack(context, new CallBackimpl() {
            @Override
            public void callBack(JSONObject obj) {
                UpdateEntity entity = JSON.parseObject(obj.toString(), UpdateEntity.class);
                notifyTag = entity.getNotify_type();
                getVersionCode = Integer.parseInt(entity.getVersion_number());
                int currentVersionCode = getVersionCode(context);
                if (getVersionCode > currentVersionCode) {
                    updateWay(entity,1);
                }

            }
        });
        dataParser.parseVersion();
    }

    /**
     * 根据情况选择更新方式
     * @param entity
     * @throws JSONException
     */
    private void updateWay(UpdateEntity entity,int way){
        //可以更新；如果本地有最新版，直接安装，否则下载后安装
        int saveVersionCode = (int) SharedPrefUtils.getParams(context,VERSION_CODE,0);
        File file = new File(Config.ROOT,APKNAME);  //下载完成apk文件
        if(saveVersionCode == getVersionCode && file.exists()){
            updateWayCancel();   //如果最新apk存在，则直接更新，否则去下载
        }else{
            if(way == 0){ //设置里更新，为0
                tipUpdate(entity);
            }else {  //首页里更新，为1
                if (notifyTag.equals("remind_update")) {  //提示更新
                    tipUpdate(entity);
                } else if (notifyTag.equals("force_update")) {  //强制更新
                    firstPageTipUpdate(entity);
                }
            }

        }
    }

    private void updateWayCancel(){
        if(notifyTag.equals("remind_update")){
            dialogUtils.confirmCancelDialog("已有最新版本，是否立即更新", "取消", "确定", new CallBackimpl() {
                @Override
                public void confirmHandle() {
                    context.startActivity(installIntent());
                }
            });
        }else if(notifyTag.equals("force_update")){
            dialogUtils.confirmCancelHandelDialog("已有最新版本，是否立即更新", "取消", "确定", new CallBackimpl() {
                @Override
                public void confirmHandle() {
                    MyApp.getInstance().finishAll();
                }
            }, new CallBackimpl() {
                @Override
                public void confirmHandle() {
                    context.startActivity(installIntent());
                }
            });
        }

    }

    /**
     * 根据情况
     */
    private void firstPageTipUpdate(final UpdateEntity entity){
        dialogUtils.contentCancleDialog("是否立即更新？", entity.getNotice(), "取消", "立即更新", new CallBackimpl() {
            @Override
            public void confirmHandle() {
                MyApp.getInstance().finishAll();
            }
        } ,new CallBackimpl() {
            @Override
            public void confirmHandle() {
                DownLoadUtils.downloadFile(entity.getUrl(), new CallBackimpl() {
                    @Override
                    public void callBack(int max) {
                        maxLen = max;
                    }
                }, new CallBackimpl() {
                    @Override
                    public void callBack(int progress) {
                        //builder.setProgress(maxLen, progress, false);
                        //notiManager.notify(0, builder.build());
                    }
                }, new CallBackimpl() {
                    @Override
                    public void callBack(byte[] bytes) {
                        downLoadFinish(bytes);
                    }
                });
            }
        });
    }

    /**
     * 弹出框提示是否进行更新
     */
    private void tipUpdate(final UpdateEntity entity) {

        dialogUtils.contentDialog("是否立即更新？", entity.getNotice(), "取消", "立即更新", new CallBackimpl() {
            @Override
            public void confirmHandle() {
                DownLoadUtils.downloadFile(entity.getUrl(), new CallBackimpl() {
                    @Override
                    public void callBack(int max) {
                        maxLen = max;
                    }
                }, new CallBackimpl() {
                    @Override
                    public void callBack(int progress) {
                        //builder.setProgress(maxLen, progress, false);
                        //notiManager.notify(0, builder.build());
                    }
                }, new CallBackimpl() {
                    @Override
                    public void callBack(byte[] bytes) {
                        downLoadFinish(bytes);
                    }
                });
            }
        });

    }

    /**
     * 获取App当前版本号
     * @param context
     * @return
     */
    private int getVersionCode(Context context){
        int versionCode = 0;
        try {
            versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(),0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 获取App当前版本名
     * @param context
     * @return
     */
    public String getVersionName(Context context){
        String versionName = "";
        try {
           versionName = context.getPackageManager().getPackageInfo(context.getPackageName(),0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * apk下载完成操作
     */
    private void downLoadFinish(byte[] bytes) {
        context.startActivity(installIntent());
        SDcardTools.saveFileToSDcard(bytes, APKNAME);
        //下载完成后将当前存储的apk的版本号保存起来
        SharedPrefUtils.setParams(context, VERSION_CODE, getVersionCode);
        notiManager.cancel(0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle("下载完成,点击安装")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setAutoCancel(true);
        builder.setContentIntent(PendingIntent.getActivity(context, 0, installIntent(), PendingIntent.FLAG_ONE_SHOT));
        notiManager.notify(1, builder.build());
    }

    /**
     * 安装指定路径的apk
     */
    private Intent installIntent(){
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(Config.ROOT, APKNAME)),
                "application/vnd.android.package-archive");
        return intent;
    }

}
