package cn.ahabox.widget;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.ant.liao.GifView;

import butterknife.Bind;
import cn.ahabox.feiliwu_help.R;
import cn.ahabox.activity.MainActivity;
import cn.ahabox.interfaces.CallBackimpl;
import cn.ahabox.utils.PlaySound;

/**
 * Created by libo on 2016/3/20.
 *
 * 带有进度条的语音播放器
 */
public class ProgressVoice extends RelativeLayout {
    @Bind(R.id.mygif)
    GifView mygif;
    @Bind(R.id.roundprogressbar)
    RoundProgressBar roundprogressbar;
    @Bind(R.id.view_play_finish)
    View viewPlayFinish;
    @Bind(R.id.rl_detail_player)
    RelativeLayout rlDetailPlayer;
    private View view;
    private CallBackimpl callBackimpl;

    public ProgressVoice(Context context) {
        super(context);
        view = LayoutInflater.from(getContext()).inflate(R.layout.progress_voice, null);
        addView(view);
    }

    public ProgressVoice(Context context, AttributeSet attrs) {
        super(context, attrs);
        view = LayoutInflater.from(getContext()).inflate(R.layout.progress_voice, null);
        addView(view);
    }

    public void init(final String voiceUrl) {
        mygif = (GifView) view.findViewById(R.id.mygif);
        roundprogressbar = (RoundProgressBar) view.findViewById(R.id.roundprogressbar);
        viewPlayFinish = view.findViewById(R.id.view_play_finish);
        rlDetailPlayer = (RelativeLayout) view.findViewById(R.id.rl_detail_player);
        mygif.setShowDimension(MainActivity.progressVoiceDimens,MainActivity.progressVoiceDimens);

        if(null != MainActivity.mediaPlayer){
            MainActivity.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mygif.setVisibility(View.GONE);
                    viewPlayFinish.setVisibility(View.VISIBLE);
                }
            });
        }

        callBackimpl = new CallBackimpl() {
            @Override
            public void callBack(int progress) {
                roundprogressbar.setProgress(progress);
            }
        };

        rlDetailPlayer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaySound.playVoice(getContext(), MainActivity.mediaPlayer, voiceUrl, mygif, viewPlayFinish, new CallBackimpl() {
                    @Override
                    public void confirmHandle() {
                        roundprogressbar.setMax(MainActivity.mediaPlayer.getDuration());
                        new ProgressThread().start();
                    }
                });
            }
        });
    }

    /**
     * 发送播放进度线程
     */
    class ProgressThread extends Thread {
        @Override
        public void run() {
            while (MainActivity.mediaPlayer.isPlaying()) {
                try {
                    sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                callBackimpl.callBack(MainActivity.mediaPlayer.getCurrentPosition());
            }
        }
    }

}
