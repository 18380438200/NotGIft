package cn.ahabox.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import cn.ahabox.feiliwu_help.R;
import cn.ahabox.application.MyApp;
import cn.ahabox.model.ProductDetailEntity;

/**
 * Created by libo on 2016/5/9.
 *
 * 聚合商品适配器
 */
public class JuheAdapter extends RecyclerView.Adapter<JuheAdapter.ViewHolder>{
    private ArrayList<ProductDetailEntity.UnionHashEntity> datas;
    private LayoutInflater inflater;
    private OnItemClickListner clickListner;

    public JuheAdapter(Context context, ArrayList<ProductDetailEntity.UnionHashEntity> datas){
        this.datas = datas;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = inflater.inflate(R.layout.item_detail_juhe,parent,false);
        ViewHolder viewHolder = new ViewHolder(convertView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        if(datas.get(position).getId().equals("self")){
            viewHolder.rlFrame.setBackgroundResource(R.drawable.juhe_frame_red);
            viewHolder.productName.setBackgroundResource(R.color.text_red);
            viewHolder.productName.setTextColor(Color.WHITE);
        }else{
            viewHolder.rlFrame.setBackgroundResource(R.drawable.juhe_frame_grey);
            viewHolder.productName.setBackgroundResource(R.color.listview_bg);
            viewHolder.productName.setTextColor(Color.BLACK);
        }
        ImageLoader.getInstance().displayImage(datas.get(position).getFirst_cover_url(),viewHolder.productPic, MyApp.getInstance().options);
        viewHolder.productName.setText(datas.get(position).getNickname());

        if(null != clickListner){
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListner.onItemClick(viewHolder.itemView,position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productPic;
        TextView productName;
        RelativeLayout rlFrame;
        public ViewHolder(View convertView){
            super(convertView);
            productPic = (ImageView) convertView.findViewById(R.id.iv_detail_juhe);
            productName = (TextView) convertView.findViewById(R.id.tv_detail_juhe);
            rlFrame = (RelativeLayout) convertView.findViewById(R.id.rl_juhe_frame);
        }
    }

    public interface OnItemClickListner{
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListner(OnItemClickListner clickListner){
        this.clickListner = clickListner;
    }

}
