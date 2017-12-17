package cn.ahabox.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import java.util.List;
/**
 *  Created by libo on 2015/11/15.
 *
 *  adapter抽象基类，将数据设置交给具体子类实现
 * @param <T> 
 */
public abstract class CommonAdapter<T> extends BaseAdapter {

	protected Context context;
	protected List<T> data;
	protected int layoutId;

	public CommonAdapter(Context context, List<T> data,int layoutId) {
		super();
		this.context = context;
		this.data = data;
		this.layoutId  = layoutId;
	}

	@Override
	public int getCount() {
		return data==null?0:data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		BaseViewHolder baseViewHolder = BaseViewHolder.get(context, convertView, parent, layoutId, position);
		convert(baseViewHolder,data.get(position));
		return baseViewHolder.getConvertView();
	}

	public abstract void convert(BaseViewHolder baseViewHolder, T t) ;

}
