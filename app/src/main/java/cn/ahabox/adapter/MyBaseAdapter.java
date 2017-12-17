package cn.ahabox.adapter;

import android.content.Context;
import java.util.List;

/**
 * Created by libo on 2015/11/30.
 *
 * 适配器Adapter的基类
 */
public abstract class MyBaseAdapter<T>{

    protected CommonAdapter adapter;
    protected List<T> datas;
    protected Context context;

    public MyBaseAdapter(Context context, List<T> datas){
        this.context = context;
        this.datas = datas;
        adapter = createAdapter();
    }

    public abstract CommonAdapter createAdapter();

    public CommonAdapter getAdapter() {
        return adapter;
    }

    /**
     * 添加数据源并通知刷新
     * @param list
     */
    public void addAll(List list) {
        datas.addAll(list);
        adapter.notifyDataSetChanged();
    }

    /**
     * 清空数据
     */
    public void clearData(){
        datas.clear();
    }

}
