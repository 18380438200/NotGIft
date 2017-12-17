package cn.ahabox.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import cn.ahabox.feiliwu_help.R;
import cn.ahabox.activity.DesignerDetailActivity;
import cn.ahabox.activity.ProductDetailActivity;
import cn.ahabox.application.MyApp;
import cn.ahabox.config.Config;
import cn.ahabox.model.SubPageEntity;
import cn.ahabox.utils.StrUtils;
import cn.ahabox.widget.VoicePlayer;

/**
 * Created by libo on 2016/3/1.
 *
 * 设计师、艺术家、心享物、定制适配器
 */
public class DesignerAdapter extends MyBaseAdapter<SubPageEntity> {

    public DesignerAdapter(Context context, List<SubPageEntity> datas) {
        super(context, datas);
    }

    @Override
    public CommonAdapter createAdapter() {
        return new CommonAdapter(context, datas, R.layout.item_designer) {
            @Override
            public void convert(BaseViewHolder baseViewHolder, Object o) {
                final SubPageEntity entity = (SubPageEntity) o;
                ImageView ivPic = baseViewHolder.getView(R.id.iv_designer_pic);
                TextView tvName = baseViewHolder.getView(R.id.tv_designer_name);
                TextView tvContent = baseViewHolder.getView(R.id.tv_designer_content);
                TextView detailMore = baseViewHolder.getView(R.id.tv_designer_more);
                VoicePlayer voicePlayer = baseViewHolder.getView(R.id.rl_player);

                if(!StrUtils.isStr(entity.getVoice_url())){
                    voicePlayer.setVisibility(View.GONE);
                }else {
                    voicePlayer.setVisibility(View.VISIBLE);
                    voicePlayer.init(context, entity.getVoice_url());
                }

                if (TextUtils.isEmpty(entity.getCover())) {
                    ivPic.setVisibility(View.GONE);
                }else{
                    ivPic.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage(entity.getCover(), ivPic, MyApp.getInstance().options);
                }

                if (TextUtils.isEmpty(entity.getDescribe())) {
                    tvContent.setVisibility(View.GONE);
                }

                tvName.setText(entity.getName());
                tvContent.setText(entity.getDescribe());

                detailMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, DesignerDetailActivity.class);
                        intent.putExtra(Config.DESIGNER_ID_KEY,entity.getId());
                        context.startActivity(intent);
                    }
                });

                GridView gridView = baseViewHolder.getView(R.id.gv_designerlist_product);
                CommonAdapter picAdapter = new CommonAdapter(context, entity.getProducts(), R.layout.item_designerlist_pic) {
                    @Override
                    public void convert(BaseViewHolder baseViewHolder, Object o) {
                        SubPageEntity.ProductsEntity productsEntity = (SubPageEntity.ProductsEntity) o;
                        ImageView imageView = baseViewHolder.getView(R.id.imageview);
                        imageView.setBackgroundResource(R.mipmap.fastchoose_default);

                        ImageLoader.getInstance().displayImage(productsEntity.getCover(), imageView, MyApp.getInstance().options);
                    }
                };
                gridView.setAdapter(picAdapter);

                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(context, ProductDetailActivity.class);
                        intent.putExtra(Config.PRODUCT_ID_KEY, entity.getProducts().get(position).getId());
                        context.startActivity(intent);
                    }
                });
            }
        };
    }

}
