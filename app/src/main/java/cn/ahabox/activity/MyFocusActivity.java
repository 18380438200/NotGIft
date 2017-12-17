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
import cn.ahabox.adapter.MyFocusAdapter;
import cn.ahabox.config.Config;
import cn.ahabox.feiliwu_help.R;
import cn.ahabox.interfaces.CallBackimpl;
import cn.ahabox.model.DesignerFocusEntity;
import cn.ahabox.widget.CustomActionBar;

/**
 * Created by libo on 2015/6/14.
 *
 * 我的关注
 */
public class MyFocusActivity extends BaseActivity {
    @Bind(R.id.ptr_myfocus)
    PullToRefreshGridView ptrMyfocus;
    @Bind(R.id.tv_focus_tip)
    TextView tvFocusTip;
    private CustomActionBar customActionBar;
    private MyFocusAdapter myFocusAdapter;
    private ArrayList<DesignerFocusEntity> datas = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_focus);
        ButterKnife.bind(this);

        init();
        initData();
        event();
    }

    private void init() {
        customActionBar = (CustomActionBar) findViewById(R.id.myfocus_header);
        customActionBar.initStyle(CustomActionBar.HeaderStyle.DEFAULT_TITLE);
        customActionBar.setTitle(getResources().getString(R.string.title_myfocus));

        myFocusAdapter = new MyFocusAdapter(getApplicationContext(), datas);
        ptrMyfocus.setAdapter(myFocusAdapter.getAdapter());
        ptrMyfocus.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<GridView>() {
            @Override
            public void onRefresh(PullToRefreshBase<GridView> refreshView) {
                initData();
            }
        });
    }

    private void initData() {
        dialogUtils.progressDialog();
        dataParser.setCallBack(getApplicationContext(), new CallBackimpl() {
            @Override
            public void callBack(List datas) {
                if (null != dialogUtils.getProgressDialog()) {
                    dialogUtils.getProgressDialog().dismiss();
                }
                ptrMyfocus.onRefreshComplete();
                myFocusAdapter.clearData();
                if (datas.size() == 0) {
                    tvFocusTip.setVisibility(View.VISIBLE);
                } else {
                    myFocusAdapter.addAll(datas);
                }
            }
        });
        dataParser.parseDesignerFocus();
    }

    private void event(){
        ptrMyfocus.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), DesignerDetailActivity.class);
                intent.putExtra(Config.DESIGNER_ID_KEY, datas.get(position).getId());
                startActivity(intent);
            }
        });
    }

}
