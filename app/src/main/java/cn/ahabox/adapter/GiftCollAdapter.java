package cn.ahabox.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import cn.ahabox.activity.GiftCollectionActivity;
import cn.ahabox.application.MyApp;
import cn.ahabox.config.Config;
import cn.ahabox.feiliwu_help.R;
import cn.ahabox.interfaces.Api;
import cn.ahabox.interfaces.CallBackimpl;
import cn.ahabox.model.GiftCollEntity;
import cn.ahabox.utils.DialogUtils;
import cn.ahabox.utils.Request;

/**
 * Created by libo on 2016/3/16.
 */
public class GiftCollAdapter extends MyBaseAdapter<GiftCollEntity>{
    private DialogUtils dialogUtils;
    public GiftCollAdapter(Context context, List<GiftCollEntity> datas) {
        super(context, datas);
        dialogUtils = new DialogUtils(context);
    }

    @Override
    public CommonAdapter createAdapter() {
        return new CommonAdapter(context, datas, R.layout.item_giftcoll) {
            @Override
            public void convert(BaseViewHolder baseViewHolder, Object o) {
                final GiftCollEntity entity = (GiftCollEntity) o;
                ImageView ivCover = baseViewHolder.getView(R.id.iv_coll_cover);
                TextView tvName = baseViewHolder.getView(R.id.tv_coll_name);
                TextView tvPrice = baseViewHolder.getView(R.id.tv_coll_price);
                ImageButton ibDel = baseViewHolder.getView(R.id.ib_giftcoll_del);
                ivCover.setBackgroundResource(R.mipmap.fastchoose_default);

                ImageLoader.getInstance().displayImage(entity.getCover(), ivCover, MyApp.getInstance().options);
                tvName.setText(entity.getName());
                tvPrice.setText(entity.getPrice());
                ibDel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogUtils.confirmCancelDialog("是否取消收藏", "取消", "确认", new CallBackimpl() {
                            @Override
                            public void confirmHandle() {
                                handleDel(entity);
                            }
                        });
                    }
                });

            }
        };
    }

    private void handleDel(GiftCollEntity entity){
        dialogUtils.progressDialog();
        HashMap params = new HashMap();
        params.put("product_id",entity.getId());
        Request.postAsync(Api.REMOVE_COLL, params, new CallBackimpl() {
            @Override
            public void callBack(String str) {
                try {
                    if (null != dialogUtils.getProgressDialog()) {
                        dialogUtils.getProgressDialog().dismiss();
                    }
                    JSONObject obj = new JSONObject(str);
                    int status = obj.getInt("status");
                    if (status == Config.STATUS_SUCCESSED) {
                        Toast.makeText(context, "取消成功", Toast.LENGTH_SHORT).show();
                        //用接口回调刷新列表数据
                        GiftCollectionActivity.callBackimpl.confirmHandle();
                    } else {
                        Toast.makeText(context, obj.getString("errmsg"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
