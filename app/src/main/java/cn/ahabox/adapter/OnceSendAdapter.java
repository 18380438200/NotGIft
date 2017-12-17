package cn.ahabox.adapter;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cn.ahabox.activity.RecipientAddressActivity;
import cn.ahabox.config.Config;
import cn.ahabox.feiliwu_help.R;
import cn.ahabox.interfaces.CallBackimpl;
import cn.ahabox.model.AddressEntity;
import cn.ahabox.model.OtherAddressEntity;
import cn.ahabox.network.DataParserImpl;
import cn.ahabox.utils.DialogUtils;

/**
 * Created by libo on 2016/3/16.
 *
 * 曾经送过适配器
 */
public class OnceSendAdapter extends MyBaseAdapter<OtherAddressEntity>{
    private DataParserImpl dataParser;
    /** 当前选择位置 */
    private int clickPosition;

    public OnceSendAdapter(Context context, List<OtherAddressEntity> datas) {
        super(context, datas);
        dataParser = new DataParserImpl();
    }

    public void setClickPosition(int position){
        clickPosition = position;
    }

    @Override
    public CommonAdapterPosition createAdapter() {
        return new CommonAdapterPosition(context,datas, R.layout.item_once_send) {
            @Override
            public void convert(BaseViewHolder baseViewHolder, Object o) {}

            @Override
            public void convert(BaseViewHolder baseViewHolder, final Object o, final int position) {
                final OtherAddressEntity entity = (OtherAddressEntity) o;
                LinearLayout llContainer = baseViewHolder.getView(R.id.ll_address_container);
                TextView tvName = baseViewHolder.getView(R.id.tv_address_name);
                TextView tvPhone = baseViewHolder.getView(R.id.tv_address_phone);
                TextView tvAddress = baseViewHolder.getView(R.id.tv_address_address);
                TextView tvDel = baseViewHolder.getView(R.id.tv_address_del);

                    if (clickPosition == position) {
                        RecipientAddressActivity.addressEntity = chooseAddress(position);  //选择曾经送过地址
                        llContainer.setBackgroundResource(R.drawable.edittext_redbg_shape);
                        setColor(tvName, tvPhone, tvAddress, tvDel, R.color.white);
                    } else {
                        llContainer.setBackgroundResource(R.drawable.edittext_bg_shape);
                        tvName.setTextColor(context.getResources().getColor(R.color.black));
                        tvPhone.setTextColor(context.getResources().getColor(R.color.black));
                        tvAddress.setTextColor(context.getResources().getColor(R.color.voicelist_textcolor));
                        tvDel.setTextColor(context.getResources().getColor(R.color.voicelist_textcolor));
                    }

                tvName.setText(entity.getName());
                tvPhone.setText(entity.getPhone());
                tvAddress.setText(entity.getDisplay_str());

                tvDel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        delAddress(entity.getId(),position);
                    }
                });
            }
        };
    }

    private AddressEntity chooseAddress(int position){
        OtherAddressEntity otherAddressEntity = datas.get(position);
        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setId(otherAddressEntity.getId());
        addressEntity.setName(otherAddressEntity.getName());
        addressEntity.setPostalcode(otherAddressEntity.getPostalcode());
        addressEntity.setPhone(otherAddressEntity.getPhone());
        addressEntity.setDisplay_str(otherAddressEntity.getDisplay_str());
        return addressEntity;
    }

    /**
     * 设置textview颜色
     */
    private void setColor(TextView tvName,TextView tvPhone,TextView tvAddress,TextView tvDel,int color){
        tvName.setTextColor(context.getResources().getColor(color));
        tvPhone.setTextColor(context.getResources().getColor(color));
        tvAddress.setTextColor(context.getResources().getColor(color));
        tvDel.setTextColor(context.getResources().getColor(color));
    }

    /**
     * 删除地址
     * @param addressId
     * @param position
     */
    private void delAddress(final int addressId, final int position){
        new DialogUtils(context).confirmCancelDialog("是否删除", "否", "是", new CallBackimpl() {
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
                                datas.remove(position);
                                getAdapter().notifyDataSetChanged();
                            }else{
                                Toast.makeText(context, obj.getString("errmsg"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                dataParser.parseDelAddress(addressId);
            }
        });
    }

}
