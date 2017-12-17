package cn.ahabox.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import com.ant.liao.GifView;

import java.io.File;
import java.io.IOException;

import cn.ahabox.feiliwu_help.R;
import cn.ahabox.activity.MainActivity;
import cn.ahabox.interfaces.CallBackimpl;

/**
 * Created by libo on 2015/11/24.
 *
 * 声音播放工具类
 */
public class PlaySound {
    public static String uri = "";
    //每次保存播放的gif控件
    public static GifView lastGifView;
    public static ImageView lastImageView;

    /**
     * 从网络播放语音
     * @param context
     * @param mediaPlayer
     * @param uri  资源地址
     * @param progressBar  语音加载
     * @param loadingId  加载gif资源id
     * @param readingId  播放gif资源id
     */
    public static void playVoice(Context context, final MediaPlayer mediaPlayer,Object uri, final GifView progressBar,int loadingId, final int readingId){
        try {
            //通过判断是否是同一个语音暂停开始播放还是重新加载新的语音资源
            if(progressBar == lastGifView){
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    progressBar.setVisibility(View.GONE);
                }else{
                    mediaPlayer.start();
                    progressBar.setVisibility(View.VISIBLE);
                }
            }else{
                progressBar.setGifImage(loadingId);
                progressBar.setVisibility(View.VISIBLE);
                if(null != lastGifView){
                    lastGifView.setVisibility(View.GONE);
                }
                lastGifView = progressBar;
                mediaPlayer.reset();
                if(uri instanceof String){
                    mediaPlayer.setDataSource(context, Uri.parse((String) uri));
                }else if(uri instanceof File){
                    mediaPlayer.setDataSource(context, Uri.fromFile((File) uri));
                }
                mediaPlayer.prepareAsync();
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        progressBar.setGifImage(readingId);
                        mediaPlayer.start();
                    }
                });

                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        progressBar.setVisibility(View.GONE);
                    }
                });

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 播放网络录制语音资源
     * @param context
     * @param mediaPlayer
     * @param uri
     * @param progressBar
     * @param loadingId
     * @param readingId
     * @param imageView
     */
    public static void playRecordVoice(Context context, final MediaPlayer mediaPlayer,Object uri, final GifView progressBar,int loadingId, final int readingId, final ImageView imageView){
        try {
            progressBar.setShowDimension(MainActivity.recordVoiceWidth,MainActivity.recordVoiceHeight);
            //通过判断是否是同一个语音暂停开始播放还是重新加载新的语音资源
            if(progressBar == lastGifView){
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    progressBar.setVisibility(View.GONE);
                    imageView.setVisibility(View.VISIBLE);
                }else{
                    mediaPlayer.start();
                    progressBar.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.GONE);
                }
            }else{
                progressBar.setGifImage(loadingId);
                progressBar.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.GONE);
                if(null != lastGifView){
                    lastGifView.setVisibility(View.GONE);
                }
                if(null != lastImageView){
                    lastImageView.setVisibility(View.VISIBLE);
                }
                lastImageView = imageView;
                lastGifView = progressBar;
                mediaPlayer.reset();
                if(uri instanceof String){
                    mediaPlayer.setDataSource(context, Uri.parse((String) uri));
                }else if(uri instanceof File){
                    mediaPlayer.setDataSource(context, Uri.fromFile((File) uri));
                }
                mediaPlayer.prepareAsync();
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        progressBar.setGifImage(readingId);
                        mediaPlayer.start();
                    }
                });

                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        progressBar.setVisibility(View.GONE);
                        imageView.setVisibility(View.VISIBLE);
                    }
                });

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 商品、设计师详情带圆环进度的播放功能
     * @param context
     * @param mediaPlayer
     * @param url
     * @param progressBar
     */
    public static void playVoice(Context context, final MediaPlayer mediaPlayer,String url, final GifView progressBar,View view, final CallBackimpl callBackimpl){
        try {
            //通过判断是否是同一个语音暂停开始播放还是重新加载新的语音资源
            if(progressBar == lastGifView && view.getVisibility() == View.GONE){
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    progressBar.setVisibility(View.GONE);
                }else{
                    mediaPlayer.start();
                    progressBar.setVisibility(View.VISIBLE);
                    callBackimpl.confirmHandle();
                }
            }else{
                view.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                if(null != lastGifView){
                    lastGifView.setVisibility(View.GONE);
                }
                lastGifView = progressBar;
                mediaPlayer.reset();
                progressBar.setGifImage(R.drawable.detail_voice_loading);
                mediaPlayer.setDataSource(context, Uri.parse(url));
                mediaPlayer.prepareAsync();
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        progressBar.setGifImage(R.drawable.detail_voice_reading);
                        mediaPlayer.start();
                        callBackimpl.confirmHandle();
                    }
                });
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
