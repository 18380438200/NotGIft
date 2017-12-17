package cn.ahabox.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import cn.ahabox.feiliwu_help.R;
import cn.ahabox.model.ShipEntity;

/**
 * Created by libo on 2016/8/3.
 *
 * 物流信息适配器
 */
public class MyShipAdapter extends RecyclerView.Adapter<MyShipAdapter.ViewHolder>{
    private ArrayList<ShipEntity> datas;
    private LayoutInflater inflater;

    public MyShipAdapter(Context context,ArrayList<ShipEntity> datas) {
        this.datas = datas;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = inflater.inflate(R.layout.item_logistice,null);
        return new ViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvShipPlace.setText(datas.get(position).getRemark());
        holder.tvShipTime.setText(datas.get(position).getAccept_time());
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvShipTime;
        TextView tvShipPlace;
        public ViewHolder(final View itemView) {
            super(itemView);
            tvShipTime = (TextView) itemView.findViewById(R.id.tv_logistice_time);
            tvShipPlace = (TextView) itemView.findViewById(R.id.tv_logistice_place);
        }
    }
}
