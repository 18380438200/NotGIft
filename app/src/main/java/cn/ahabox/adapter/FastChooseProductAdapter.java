package cn.ahabox.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import cn.ahabox.application.MyApp;
import cn.ahabox.feiliwu_help.R;
import cn.ahabox.model.DesignerDetailEntity;
import cn.ahabox.utils.CollectionUtils;
import cn.ahabox.utils.DialogUtils;

/**
 * Created by libo on 2016/3/4.
 *
 * 快速选择商品适配器
 */
public class FastChooseProductAdapter extends MyBaseAdapter<DesignerDetailEntity.ProductsEntity>{

    private DialogUtils dialogUtils;
    public FastChooseProductAdapter(Context context, List<DesignerDetailEntity.ProductsEntity> datas) {
        super(context, datas);
        dialogUtils = new DialogUtils(context);
    }

    @Override
    public CommonAdapter createAdapter() {
        return new CommonAdapter(context,datas, R.layout.item_productlist) {
            @Override
            public void convert(BaseViewHolder baseViewHolder, Object o) {
                final DesignerDetailEntity.ProductsEntity entity = (DesignerDetailEntity.ProductsEntity)o;
                ImageView ivCover = baseViewHolder.getView(R.id.iv_productlist);
                TextView tvName = baseViewHolder.getView(R.id.tv_product_name);
                TextView tvPrice = baseViewHolder.getView(R.id.tv_product_price);
                TextView tvBuying = baseViewHolder.getView(R.id.tv_product_buying);
                LinearLayout llBtn = baseViewHolder.getView(R.id.ll_product_coll);
                final TextView tvfavorite = baseViewHolder.getView(R.id.tv_product_favorite);
                TextView cbcoll = baseViewHolder.getView(R.id.cb_product_coll);

                ivCover.setBackgroundResource(R.mipmap.fastchoose_default);
                ImageLoader.getInstance().displayImage(entity.getCover(), ivCover, MyApp.getInstance().options);
                tvName.setText(entity.getName());
                tvPrice.setText("" + entity.getPrice());
                tvfavorite.setText("" + entity.getFavorites());
                CollectionUtils collectionUtils = new CollectionUtils(context,cbcoll,dialogUtils,entity.getFavorites(),entity.getIs_favorited());
                collectionUtils.showStatus(entity.getIs_favorited(), cbcoll);
                collectionUtils.setEvent(entity.getId(), tvfavorite,llBtn);
                setTag(entity.getStatus(), tvBuying);
            }
        };
    }

    /**
     * 设置购买状态
     * @param status
     * @param tvBuying
     */
    private void setTag(int status,TextView tvBuying){
        switch (status){
            case 0:   //普通商品
                tvBuying.setVisibility(View.GONE);
                break;
            case 1:   //售罄
                tvBuying.setVisibility(View.VISIBLE);
                tvBuying.setText(context.getResources().getString(R.string.sold_out));
                break;
            case 2:   //预售前
                tvBuying.setVisibility(View.VISIBLE);
                tvBuying.setText(context.getResources().getString(R.string.will_sold));
                break;
            case 3:   //预售结束
                tvBuying.setVisibility(View.VISIBLE);
                tvBuying.setText(context.getResources().getString(R.string.productdetail_buy_finish));
                break;
            case 4:   //预售中
                tvBuying.setVisibility(View.VISIBLE);
                tvBuying.setText(context.getResources().getString(R.string.panic_buying));
                break;
        }
    }

}
