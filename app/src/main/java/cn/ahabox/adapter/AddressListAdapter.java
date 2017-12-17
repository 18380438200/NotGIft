package cn.ahabox.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.ahabox.activity.AddAddressActivity;
import cn.ahabox.activity.BuymyselfAddressActivity;
import cn.ahabox.activity.RecipientAddressActivity;
import cn.ahabox.config.App;
import cn.ahabox.feiliwu_help.R;
import cn.ahabox.fragment.MyAddressFragment;
import cn.ahabox.interfaces.CallBackimpl;
import cn.ahabox.model.AddressEntity;
import cn.ahabox.network.DataParserImpl;
import cn.ahabox.utils.DialogUtils;
import cn.ahabox.utils.StrUtils;

/**
 * Created by libo on 2016/2/2.
 *
 * 地址列表adapter
 */
public class AddressListAdapter extends BaseAdapter{

    private Context context;
    private ArrayList<AddressEntity> datas;
    private DataParserImpl dataParser;
    private DialogUtils dialogUtils;
    /** 当前item点击位置 */
    private int clickPostion = -1;

    public AddressListAdapter(Context context, ArrayList<AddressEntity> datas){
        this.context = context;
        this.datas = datas;
        dialogUtils = new DialogUtils(context);
        dataParser = new DataParserImpl();
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
        //注意：在这个适配器以及gridView操作时，position是从1开始的
        if(position == 0){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_add_address,null);
        }else{
            final AddressEntity entity = datas.get(position);
            convertView = LayoutInflater.from(context).inflate(R.layout.item_myadress,null);
            TextView tvName = (TextView) convertView.findViewById(R.id.tv_address_name);
            TextView tvPhone = (TextView) convertView.findViewById(R.id.tv_address_phone);
            TextView tvAddress = (TextView) convertView.findViewById(R.id.tv_address_address);
            TextView tvDel = (TextView) convertView.findViewById(R.id.tv_address_del);
            TextView tvEdit = (TextView) convertView.findViewById(R.id.tv_address_edit);

            if(clickPostion == position){ //选中当前项
                convertView.setBackgroundResource(R.drawable.edittext_redbg_shape);
                setColor(tvName, tvPhone, tvAddress, tvDel, tvEdit, R.color.white);
                if (App.BUY_WAY == App.BUY_MYSELF) {
                    BuymyselfAddressActivity.addressEntity = entity;  //送自己地址
                } else if (App.BUY_WAY == App.BUY_OTHER_HAVEADD) {
                    RecipientAddressActivity.addressEntity = entity;  //送给Ta地址
                }
            }else{
                convertView.setBackgroundResource(R.drawable.edittext_bg_shape);
                tvName.setTextColor(context.getResources().getColor(R.color.black));
                tvPhone.setTextColor(context.getResources().getColor(R.color.black));
                tvAddress.setTextColor(context.getResources().getColor(R.color.voicelist_textcolor));
                tvDel.setTextColor(context.getResources().getColor(R.color.voicelist_textcolor));
                tvEdit.setTextColor(context.getResources().getColor(R.color.voicelist_textcolor));
            }

            tvName.setText(entity.getName());
            tvPhone.setText(entity.getPhone());
            tvAddress.setText(entity.getDisplay_str());

            delAddress(tvDel,tvEdit,position);
        }

        return convertView;
    }

    /**
     * 设置textview颜色
     */
    private void setColor(TextView tvName,TextView tvPhone,TextView tvAddress,TextView tvDel,TextView tvEdit,int color){
        tvName.setTextColor(context.getResources().getColor(color));
        tvPhone.setTextColor(context.getResources().getColor(color));
        tvAddress.setTextColor(context.getResources().getColor(color));
        tvDel.setTextColor(context.getResources().getColor(color));
        tvEdit.setTextColor(context.getResources().getColor(color));
    }

    /**
     * 删除和修改地址功能
     * @param tvDel
     * @param tvEdit
     * @param position
     */
    private void delAddress(TextView tvDel,TextView tvEdit, final int position){

        tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyAddressFragment.editAddress = datas.get(position);
                Intent intent = new Intent(context, AddAddressActivity.class);
                intent.putExtra("addOredit",1);
                context.startActivity(intent);
            }
        });

        tvDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogUtils.confirmCancelDialog("是否删除", " 否 ", " 是 ", new CallBackimpl() {
                    @Override
                    public void confirmHandle() {
                        dataParser.setCallBack(context, new CallBackimpl() {
                            @Override
                            public void callBack(String str) {
                                StrUtils.handleStatus(str, "删除成功", context);
                                MyAddressFragment.icallBack.confirmHandle();
                            }
                        });
                        dataParser.parseDelAddress(datas.get(position).getId());
                    }
                });
            }
        });
    }

    public void addAll(List list){
        datas.addAll(list);
        notifyDataSetChanged();
    }

}
