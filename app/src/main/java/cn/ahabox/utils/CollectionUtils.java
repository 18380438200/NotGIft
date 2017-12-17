package cn.ahabox.utils;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cn.ahabox.activity.ShopCartActivity;
import cn.ahabox.feiliwu_help.R;
import cn.ahabox.config.App;
import cn.ahabox.config.Config;
import cn.ahabox.interfaces.Api;
import cn.ahabox.interfaces.CallBackimpl;

/**
 * Created by libo on 2016/3/18.
 *
 * 用于收藏商品的功能
 */
public class CollectionUtils {
    private View checkBox;
    private DialogUtils dialogUtils;
    private Context context;
    private int favorites;
    private boolean isFavorite;
    /** 底部菜单收藏图标 */
    private View bottomLove;
    private TextView tvLove;

    public CollectionUtils(Context context,View checkBox,DialogUtils dialogUtils,int favorites,boolean isFavorite){
        this.checkBox = checkBox;
        this.dialogUtils = dialogUtils;
        this.favorites = favorites;
        this.context = context;
        this.isFavorite = isFavorite;
    }

    /**
     * checkBox当前应该显示的状态
     * @param isFavorited
     */
    public void showStatus(boolean isFavorited,View view){
        if(isFavorited) {
            view.setBackgroundResource(R.mipmap.display_love_pressed);
        }else{
            view.setBackgroundResource(R.mipmap.display_love_normal);
        }
    }

    public void setEvent(final int productId,final TextView tvfavorite,View view){

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                App.gotoLogin(context, new CallBackimpl() {
                    @Override
                    public void confirmHandle() {
                        collHandle(productId,tvfavorite);
                    }
                });
            }
        });

    }

    /**
     * 跳转购物车
     */
    public void gotoShopcart(LinearLayout linearLayout){
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                App.gotoLogin(context, new CallBackimpl() {
                    @Override
                    public void confirmHandle() {
                        context.startActivity(new Intent(context, ShopCartActivity.class));
                    }
                });
            }
        });
    }

    /**
     * 收藏或取消收藏操作
     */
    private void collHandle(int productId, final TextView tvfavorite){
        String url;
        if (!isFavorite) {
            url = Api.COLLECTION;
        } else {
            url = Api.REMOVE_COLL;
        }
        HashMap params = new HashMap();
        params.put("product_id", productId);
        Request.postAsync(url, params, new CallBackimpl() {
            @Override
            public void callBack(String str) {
                if (null != dialogUtils.getProgressDialog()) {
                    dialogUtils.getProgressDialog().dismiss();
                }
                handNum(str, tvfavorite);
            }
        });
    }

    /**
     * 根据点击对收藏数处理
     */
    private void handNum(String str,TextView tvfavorite){
        try {
            JSONObject obj = new JSONObject(str);
            if(obj.getInt("status") == Config.STATUS_SUCCESSED){
                isFavorite = !isFavorite;
                if(isFavorite){
                    favorites += 1;
                    tvfavorite.setText("" + favorites);
                    checkBox.startAnimation(MyAnimUtils.getHeartAnim());
                    Toast.makeText(context,"收藏成功",Toast.LENGTH_SHORT).show();
                    checkBox.setBackgroundResource(R.mipmap.display_love_pressed);
                    if(null != bottomLove){
                        bottomLove.setBackgroundResource(R.mipmap.menu_bottom_love_pressed);
                        tvLove.setText(R.string.productdetail_coll_already);
                    }
                } else{
                    favorites -= 1;
                    tvfavorite.setText("" + favorites);
                    Toast.makeText(context,"取消收藏成功",Toast.LENGTH_SHORT).show();
                    checkBox.setBackgroundResource(R.mipmap.display_love_normal);
                    if(null != bottomLove) {
                        bottomLove.setBackgroundResource(R.mipmap.menu_bottom_love_normal);
                        tvLove.setText(R.string.productdetail_coll);
                    }
                }
            }else{
                Toast.makeText(context, obj.getString("errmsg"), Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
