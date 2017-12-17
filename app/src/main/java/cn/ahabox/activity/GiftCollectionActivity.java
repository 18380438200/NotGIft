package cn.ahabox.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.ahabox.adapter.GiftCollAdapter;
import cn.ahabox.config.Config;
import cn.ahabox.feiliwu_help.R;
import cn.ahabox.interfaces.CallBackimpl;
import cn.ahabox.model.GiftCollEntity;
import cn.ahabox.widget.CustomActionBar;

/**
 * Created by libo on 2015/1/20.
 * <p/>
 * 礼物收藏
 */
public class GiftCollectionActivity extends BaseActivity {
    @Bind(R.id.gv_collection)
    PullToRefreshGridView gvCollection;
    @Bind(R.id.collection_header)
    CustomActionBar customActionBar;
    @Bind(R.id.tv_coll_tip)
    TextView tvCollTip;
    private ArrayList<GiftCollEntity> datas = new ArrayList<>();
    private GiftCollAdapter adapter;
    public static CallBackimpl callBackimpl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_collection);
        ButterKnife.bind(this);

        intView();
        initData();
    }

    private void intView() {
        customActionBar.initStyle(CustomActionBar.HeaderStyle.DEFAULT_TITLE);
        customActionBar.setTitle(getResources().getString(R.string.title_giftcollection));

        adapter = new GiftCollAdapter(this, datas);
        gvCollection.setAdapter(adapter.getAdapter());
        gvCollection.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<GridView>() {
            @Override
            public void onRefresh(PullToRefreshBase<GridView> refreshView) {
                initData();
            }
        });

        callBackimpl = new CallBackimpl() {
            @Override
            public void confirmHandle() {
                datas.clear();
                initData();
            }
        };

        gvCollection.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ProductDetailActivity.class);
                intent.putExtra(Config.PRODUCT_ID_KEY, datas.get(position).getId());
                startActivity(intent);
            }
        });
    }

    private void initData() {
        dialogUtils.progressDialog();
        dataParser.setCallBack(getApplicationContext(), new CallBackimpl() {
            @Override
            public void callBack(List list) {
                if (null != dialogUtils.getProgressDialog()) {
                    dialogUtils.getProgressDialog().dismiss();
                }
                gvCollection.onRefreshComplete();
                if (list.size() == 0) {
                    tvCollTip.setVisibility(View.VISIBLE);
                } else {
                    adapter.clearData();
                    adapter.addAll(list);
                }
            }
        });
        dataParser.parseCollList();
    }

}
