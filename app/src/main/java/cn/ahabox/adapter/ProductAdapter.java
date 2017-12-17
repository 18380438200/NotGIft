package cn.ahabox.adapter;

import android.content.Context;
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
 * Created by libo on 2016/3/10.
 *
 * 商品适配器
 */
public class ProductAdapter extends MyBaseAdapter<DesignerDetailEntity.ProductsEntity>{
    private DialogUtils dialogUtils;

    public ProductAdapter(Context context, List<DesignerDetailEntity.ProductsEntity> datas,DialogUtils dialogUtils) {
        super(context, datas);
        this.dialogUtils = dialogUtils;
    }

    @Override
    public CommonAdapter createAdapter() {
        return new CommonAdapter(context,datas, R.layout.item_only_productlist) {
            @Override
            public void convert(BaseViewHolder baseViewHolder, Object o) {
                DesignerDetailEntity.ProductsEntity entity = (DesignerDetailEntity.ProductsEntity) o;
                ImageView ivCover = baseViewHolder.getView(R.id.iv_product_cover);
                TextView tvName = baseViewHolder.getView(R.id.tv_product_name);
                TextView tvPrice = baseViewHolder.getView(R.id.tv_product_price);
                TextView tvFavorite = baseViewHolder.getView(R.id.tv_product_favorite);
                LinearLayout llBtn = baseViewHolder.getView(R.id.ll_product_coll);
                ImageView cbColl = baseViewHolder.getView(R.id.cb_product_coll);
                ivCover.setBackgroundResource(R.mipmap.fastchoose_default);

                ImageLoader.getInstance().displayImage(entity.getCover(),ivCover, MyApp.getInstance().options);
                tvName.setText(entity.getName());
                tvPrice.setText(entity.getPrice());
                tvFavorite.setText("" + entity.getFavorites());
                CollectionUtils collectionUtils = new CollectionUtils(context, cbColl, dialogUtils, entity.getFavorites(),entity.getIs_favorited());
                collectionUtils.showStatus(entity.getIs_favorited(),cbColl);
                collectionUtils.setEvent(entity.getId(), tvFavorite,llBtn);
            }
        };
    }
}
