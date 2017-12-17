package cn.ahabox.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.ahabox.feiliwu_help.R;
import cn.ahabox.model.FastChooseTagEntity;

/**
 * Created by libo on 2016/4/20.
 */
public class ProductSubtagAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<FastChooseTagEntity.SubtagsEntity> datas;
    /** 当前item点击位置 */
    private int clickPostion = -1;

    public ProductSubtagAdapter(Context context,ArrayList<FastChooseTagEntity.SubtagsEntity> datas){
        this.context = context;
        this.datas = datas;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_fastchoose_sublist,null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvTitle.setText(datas.get(position).getTag_name());
        if(clickPostion == position){
            viewHolder.tvTitle.setBackgroundResource(R.drawable.fastchoose_subtitle_shape);
        }else{
            viewHolder.tvTitle.setBackgroundResource(R.drawable.fastchoose_subtitle_shape2);
        }

        return convertView;
    }

    class ViewHolder{
        TextView tvTitle;
        public ViewHolder(View convertView){
            tvTitle = (TextView) convertView.findViewById(R.id.tv_sublist_title);
        }
    }

    public void addAll(List list){
        datas.addAll(list);
        notifyDataSetChanged();
    }

}
