package cn.ahabox.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 *  Created by libo on 2015/11/15.
 *
 *  adapter抽象基类，带有position
 * @param <T> 
 */
public abstract class CommonAdapterPosition<T> extends CommonAdapter {

	public CommonAdapterPosition(Context context, List<T> data, int layoutId) {
		super(context, data, layoutId);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		BaseViewHolder baseViewHolder = BaseViewHolder.get(context, convertView, parent, layoutId, position);
		convert(baseViewHolder, data.get(position),position);
		return baseViewHolder.getConvertView();
	}

	public abstract void convert(BaseViewHolder baseViewHolder, Object o,int position);
}
