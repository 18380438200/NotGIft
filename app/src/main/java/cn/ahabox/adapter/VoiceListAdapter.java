package cn.ahabox.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ant.liao.GifView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.ahabox.activity.VoiceChangenameActivity;
import cn.ahabox.activity.VoiceListActivity;
import cn.ahabox.config.Config;
import cn.ahabox.feiliwu_help.R;
import cn.ahabox.interfaces.CallBackimpl;
import cn.ahabox.model.VoiceEntiy;
import cn.ahabox.network.DataParserImpl;
import cn.ahabox.utils.DialogUtils;
import cn.ahabox.utils.PlaySound;
import cn.ahabox.utils.StrUtils;

/**
 * Created by libo on 2016/4/20.
 */
public class VoiceListAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<VoiceEntiy> datas;
    private DataParserImpl dataParser;
    private DialogUtils dialogUtils;
    /** 当前item点击位置 */
    private int clickPostion = -1;
    private MediaPlayer mediaPlayer;

    public VoiceListAdapter(Context context, ArrayList<VoiceEntiy> datas, MediaPlayer mediaPlayer){
        this.context = context;
        this.datas = datas;
        dialogUtils = new DialogUtils(context);
        dataParser = new DataParserImpl();
        this.mediaPlayer = mediaPlayer;
    }

    public void setSelection(int position){
        clickPostion = position;
    }

    @Override
    public int getCount() {
        return datas != null ? datas.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        final VoiceEntiy entity = datas.get(position);
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_audio,null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (clickPostion == position) {
            VoiceListActivity.message_id = entity.getId();
            convertView.setBackgroundResource(R.drawable.edittext_blackbg_shape);
            viewHolder.tvName.setTextColor(context.getResources().getColor(R.color.white));
            viewHolder.tvTime.setTextColor(context.getResources().getColor(R.color.white));
            viewHolder.tvDel.setTextColor(context.getResources().getColor(R.color.white));
            viewHolder.tvChangeName.setTextColor(context.getResources().getColor(R.color.white));
        } else {
            convertView.setBackgroundResource(R.drawable.edittext_bg_shape);
            viewHolder.tvName.setTextColor(context.getResources().getColor(R.color.voicelist_textcolor));
            viewHolder.tvTime.setTextColor(context.getResources().getColor(R.color.voicelist_textcolor));
            viewHolder.tvDel.setTextColor(context.getResources().getColor(R.color.voicelist_textcolor));
            viewHolder.tvChangeName.setTextColor(context.getResources().getColor(R.color.voicelist_textcolor));
        }

        viewHolder.tvName.setText((String) entity.getName());
        String time = String.valueOf(entity.getCreated_at());
        viewHolder.tvTime.setText(StrUtils.getTime(time));

        viewHolder.tvPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaySound.playRecordVoice(context, mediaPlayer, entity.getVoice_url(), viewHolder.gifView, R.drawable.recordvoice_reading,
                        R.drawable.recordvoice_reading, viewHolder.imageView);
            }
        });

        viewHolder.tvDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogUtils.confirmCancelDialog("是否删除该语音", "取消", "确认", new CallBackimpl() {
                    @Override
                    public void confirmHandle() {
                        dataParser.setCallBack(context, new CallBackimpl() {
                            @Override
                            public void callBack(String str) {
                                try {
                                    JSONObject obj = new JSONObject(str);
                                    int status = obj.getInt("status");
                                    if(status == Config.STATUS_SUCCESSED){
                                        Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                                        VoiceListActivity.voiceReload.confirmHandle();
                                    }else{
                                        Toast.makeText(context, obj.getString("errmsg"), Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                        dataParser.parseVoiceDel(entity.getId());
                    }
                });
            }
        });

        viewHolder.tvChangeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VoiceChangenameActivity.class);
                intent.putExtra("voiceid",entity.getId());
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    class ViewHolder{
        TextView tvName,tvTime,tvDel,tvChangeName;
        GifView gifView;
        RelativeLayout tvPlay;
        ImageView imageView;
        public ViewHolder(View convertView){
            tvName = (TextView) convertView.findViewById(R.id.audio_name);
            gifView = (GifView) convertView.findViewById(R.id.mygif);
            tvTime = (TextView) convertView.findViewById(R.id.tv_audio_time);
            tvDel = (TextView) convertView.findViewById(R.id.audio_del);
            tvChangeName = (TextView) convertView.findViewById(R.id.audio_changename);
            tvPlay = (RelativeLayout) convertView.findViewById(R.id.tv_audio_play);
            imageView = (ImageView) convertView.findViewById(R.id.iv_audio);
        }
    }

    public void addAll(List list){
        datas.addAll(list);
        notifyDataSetChanged();
    }
}
