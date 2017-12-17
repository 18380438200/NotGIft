package cn.ahabox.application;

import android.app.Activity;
import android.app.Application;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.ahabox.activity.MainActivity;
import cn.ahabox.config.Config;

/**
 * Created by libo on 2015/11/16.
 *
 * Application类
 */
public class MyApp extends Application {
    private static MyApp myApp;
    /** 用于存储和统一销毁一套操作的Activities */
    public static List<Activity> activities =  new ArrayList<>();
    public static ImageLoaderConfiguration config;
    public static DisplayImageOptions options,circleOption;

    @Override
    public void onCreate() {
        super.onCreate();

        init();
    }

    private void init() {
        //全局异常崩溃处理
        CrashHandler crashHandler = new CrashHandler(this);
        Thread.setDefaultUncaughtExceptionHandler(crashHandler);

        //友盟统计功能开关
        MobclickAgent.setCatchUncaughtExceptions(true);

        config = new ImageLoaderConfiguration.Builder(this)
                //线程池数量
                .threadPoolSize(3)
                .diskCache(new UnlimitedDiskCache(new File(Config.ROOT + "cachepics")))
                .memoryCache(new WeakMemoryCache())   //使用弱引用防止加载图片过多发生OOM
                //线程池优先级
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .build();

        ImageLoader.getInstance().init(config);
        options = new DisplayImageOptions.Builder()
//                //图片加载时显示的图片
//                .showImageOnLoading(R.mipmap.default_list_icon)
//                //图片加载失败时显示的图片
//                .showImageOnFail(R.mipmap.default_list_icon)
//                //图片Url为空时显示的图片
//                .showImageForEmptyUri(R.mipmap.default_list_icon)
                //缓存在内存中
                .cacheInMemory(true)
                        //缓存在硬盘中
                .cacheOnDisk(true)
                //设置图片的缩放类型
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                //设置图片质量
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        circleOption = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .displayer(new RoundedBitmapDisplayer(100))
                .build();
    }

    public static MyApp getInstance() {
        if(null == myApp){
            myApp = new MyApp();
        }
        return myApp;
    }

    /**
     * 将创建的activity入栈管理
     * @param activity
     */
    public static void addActivity(Activity activity){
        activities.add(activity);
    }

    /**
     * 将该activity出栈
     * @param activity
     */
    public static void removeActivity(Activity activity){
        activities.remove(activity);
    }

    /**
     * 退出应用结束所有activity
     */
    public static void finishAll(){
        for(Activity activity : activities){
            if(!activity.isFinishing()) {
                activity.finish();
            }
        }
        activities = null;
        //应用退出之前保存统计信息
        MobclickAgent.onKillProcess(getInstance());
        //杀死该应用进程
        android.os.Process.killProcess(android.os.Process.myPid());

    }

    /**
     * 结束除MainActivity的Activity
     */
    public static void finishSome(){
        for(Activity activity : activities){
            if(!activity.isFinishing()) {
                if(activity instanceof MainActivity){

                }else{
                    activity.finish();
                }

            }
        }
    }

}
