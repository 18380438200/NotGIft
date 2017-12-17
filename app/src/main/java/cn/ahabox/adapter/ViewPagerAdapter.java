package cn.ahabox.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;

/**
 * Created by libo on 2015/11/28.
 *
 * ViewPager适配器
 */
public class ViewPagerAdapter extends PagerAdapter {

	private Context context;
	private List<? extends View> views;
	
	public ViewPagerAdapter(Context context,List<? extends View> views){
		this.views = views;
		this.context = context;
	}
	
	@Override
	public int getCount() {
		return views != null && !views.isEmpty() ? views.size() : 0;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(views.get(position));
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		container.addView(views.get(position));
		return views.get(position);
	}

}
