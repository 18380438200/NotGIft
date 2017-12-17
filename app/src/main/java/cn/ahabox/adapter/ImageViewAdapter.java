package cn.ahabox.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import cn.ahabox.application.MyApp;
import cn.ahabox.feiliwu_help.R;

/**
 * Created by libo on 2016/6/13.
 *
 * 图片列表适配器
 */
public class ImageViewAdapter extends RecyclerView.Adapter<ImageViewAdapter.ViewHolder> {
    private ArrayList<String> datas;
    private LayoutInflater inflater;

    public ImageViewAdapter(Context context, ArrayList<String> datas) {
        this.datas = datas;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = inflater.inflate(R.layout.item_adapte_imageview, parent, false);
        ViewHolder viewHolder = new ViewHolder(convertView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        ImageLoader.getInstance().displayImage(datas.get(position),viewHolder.imageView, MyApp.getInstance().options);
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(View convertView) {
            super(convertView);
            imageView = (ImageView) convertView.findViewById(R.id.imageview);
        }
    }

}
