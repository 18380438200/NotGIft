package cn.ahabox.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.ahabox.feiliwu_help.R;

/**
 * Created by libo on 2016/7/22.
 *
 * 购买流程的当前状态
 */
public class ShipStatusAdapter extends MyBaseAdapter<String>{
    private int step;
    private ArrayList<String> dates;

    public ShipStatusAdapter(Context context, List<String> datas,ArrayList<String> dates,int step) {
        super(context, datas);
        this.dates = dates;
        this.step = step;
    }

    @Override
    public CommonAdapterPosition createAdapter() {
        return new CommonAdapterPosition(context,datas, R.layout.item_shipstatus) {
            @Override
            public void convert(BaseViewHolder baseViewHolder, Object o) {}

            @Override
            public void convert(BaseViewHolder baseViewHolder, Object o, int position) {
                View view = baseViewHolder.getView(R.id.rl_shipstatus_container);
                View upLine = baseViewHolder.getView(R.id.view_shipstatus_up);
                View downLine = baseViewHolder.getView(R.id.view_shipstatus_down);
                ImageView ivIcon = baseViewHolder.getView(R.id.iv_shipstatus_icon);
                TextView tvStatus = baseViewHolder.getView(R.id.tv_shipstatus_status);
                TextView tvDate = baseViewHolder.getView(R.id.tv_shipstatus_time);
                tvStatus.setText(datas.get(position));
                tvDate.setText(dates.get(position));

                if(position %2 != 0){    //基数位置背景为灰色
                    view.setBackgroundColor(context.getResources().getColor(R.color.seperater_bg));
                }

                //列表首末位置竖线不同
                if(position == datas.size()-1){
                    downLine.setVisibility(View.INVISIBLE);
                }

                if(position == 0){
                    upLine.setVisibility(View.INVISIBLE);
                    downLine.setVisibility(View.VISIBLE);
                    view.setBackgroundColor(context.getResources().getColor(R.color.white));
                }

                //当前步骤以前标签为红色
                if(position <= step){
                    ivIcon.setBackgroundResource(R.mipmap.orderinformation_ok);
                    tvStatus.setTextColor(context.getResources().getColor(R.color.text_red));
                }

            }

        };
    }

}
