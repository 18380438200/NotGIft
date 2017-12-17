package cn.ahabox.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.ant.liao.GifView;

import cn.ahabox.feiliwu_help.R;
import cn.ahabox.activity.MainActivity;
import cn.ahabox.utils.PlaySound;

/**
 * Created by libo on 2016/4/25.
 *
 * 列表音频播放器
 */
public class VoicePlayer extends RelativeLayout {
    private View view;

    public VoicePlayer(Context context) {
        super(context);
        view = LayoutInflater.from(context).inflate(R.layout.voice_player, null);
        addView(view);
    }

    public VoicePlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        view = LayoutInflater.from(context).inflate(R.layout.voice_player, null);
        addView(view);
    }

    public void init(final Context context, final String voiceUrl) {
        ImageButton ibVoice = (ImageButton) view.findViewById(R.id.ib_quality_voice);
        final GifView mygif = (GifView) view.findViewById(R.id.mygif);

        mygif.setShowDimension(MainActivity.voiceDimens,MainActivity.voiceDimens);
        ibVoice.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaySound.playVoice(context, MainActivity.mediaPlayer, voiceUrl, mygif, R.drawable.designer_loading, R.drawable.designer_reading);
            }
        });
    }


}
