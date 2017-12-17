package cn.ahabox.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ant.liao.GifView;

import java.io.File;
import java.io.IOException;

import cn.ahabox.feiliwu_help.R;
import cn.ahabox.config.Config;
import cn.ahabox.interfaces.CallBackimpl;

/**
 * Created by libo on 2016/3/13.
 *
 * 语音录制工具类
 */
public class RecordVoiceUtils {
    public String filePath = Config.ROOT + "audio/"; //音频文件保存的路径
    public String fileAudioName; // 保存的音频文件的名字
    private boolean isStopCount = false;
    /** 开始录音 */
    public boolean isPlaying = false;
    public MediaRecorder mediaRecorder; // 录音控制
    public boolean isLuYin; // 是否在录音 true 是 false否
    /** 倒计时时间，单位为毫秒 */
    public final int COUNTTIME = 60 * 1000;
    private int resttime = COUNTTIME;
    private Context context;
    private MediaPlayer mediaPlayer;

    public RecordVoiceUtils(Context context, MediaPlayer mediaPlayer){
        this.context = context;
        this.mediaPlayer = mediaPlayer;
    }

    public void startAudio() {
        // 创建录音频文件
        // 这种创建方式生成的文件名是随机的
        isStopCount = false;
        File file = new File(filePath);
        if(!file.exists()){
            file.mkdirs();
        }
        fileAudioName = StrUtils.currentTime("yyyyMMddhhmmss") + ".mp3";
        // 设置录音的来源为麦克风
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        mediaRecorder.setOutputFile(filePath + fileAudioName);
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
            isLuYin = true;
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void showDialog(){
        final Dialog dialog = new Dialog(context, R.style.Dialog_Fullscreen);
        dialog.setContentView(R.layout.toast_record);
        final TextView tvCountTime = (TextView) dialog.findViewById(R.id.tv_record_counttime);
        final TextView tvStop = (TextView) dialog.findViewById(R.id.tv_record_stop);
        //设置录制动画
        ImageView ivShowRecord = (ImageView) dialog.findViewById(R.id.iv_show_record);
        ivShowRecord.setImageResource(R.drawable.make_volumce_anim);
        ((AnimationDrawable) ivShowRecord.getDrawable()).start();

        dialog.show();

        tvStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != dialog) {
                    dialog.dismiss();
                    //在这里再弹出试听弹出框
                    stopAudion();
                    showTestListen();
                }
            }
        });
        startCountTime(tvCountTime, new CallBackimpl() {
            @Override
            public void confirmHandle() {
                if (null != dialog) {
                    dialog.dismiss();
                    //在这里再弹出试听弹出框
                    stopAudion();
                    showTestListen();
                }
            }
        });
    }

    /**
     * 显示试听窗口
     */
    private void showTestListen(){
        final Dialog dialog = new Dialog(context,R.style.Dialog_Fullscreen);
        dialog.setContentView(R.layout.testlisten);
        TextView tvConfirm = (TextView) dialog.findViewById(R.id.tv_record_finish);
        TextView tvReRecord = (TextView) dialog.findViewById(R.id.tv_record_repeate);
        final ImageView imageView = (ImageView) dialog.findViewById(R.id.iv_audio);
        RelativeLayout tvTestListen = (RelativeLayout) dialog.findViewById(R.id.tv_audio_play);
        final GifView gifView = (GifView) dialog.findViewById(R.id.mygif);

        if(null != dialog && !((Activity)context).isFinishing())  //在保证当前Activity存在的情况下弹出弹出框
            dialog.show();

        tvTestListen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File voiceFile = new File(filePath + fileAudioName);
                PlaySound.playRecordVoice(context, mediaPlayer, voiceFile, gifView, R.drawable.recordvoice_reading, R.drawable.recordvoice_reading, imageView);
            }
        });

        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null) {
                    dialog.dismiss();
                    QiniuUpLoadUtils.getUploadToken(filePath + fileAudioName,context);
                }
            }
        });

        tvReRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null) {
                    dialog.dismiss();
                    isPlaying = !isPlaying;
                    startAudio();
                    showDialog();
                }
            }
        });

    }

    /**
     * 停止录制
     */
    public void stopAudion() {
        isStopCount = true;
        resttime = COUNTTIME;
        isLuYin = false;
        if (null != mediaRecorder) {
            // 停止录音
            try {
                mediaRecorder.stop();
                mediaRecorder.release();
                mediaRecorder = null;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 录音倒计时
     */
    public void startCountTime(final TextView tvCountTime, final CallBackimpl callBackimpl) {
        new CountDownTimer(COUNTTIME, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvCountTime.setText(millisUntilFinished/1000 + " S");
            }
            @Override
            public void onFinish() {
                callBackimpl.confirmHandle();
            }
        }.start();
    }

}
