package cn.ahabox.activity;

import android.os.Bundle;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.ahabox.adapter.OrderListAdapter;
import cn.ahabox.feiliwu_help.R;
import cn.ahabox.interfaces.CallBackimpl;
import cn.ahabox.model.OrderEntity;
import cn.ahabox.widget.CustomActionBar;

/**
 *  Created by libo on 2015/1/20.
 *
 * 礼物订单
 */
public class TransactionOrderActivity extends BaseActivity {
    @Bind(R.id.lv_transaction)
    PullToRefreshListView lvTransaction;
    private CustomActionBar customActionBar;
    private ArrayList<OrderEntity> datas = new ArrayList<>();
    private OrderListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_order);
        ButterKnife.bind(this);

        initView();
        initData();
    }

    private void initView() {
        customActionBar = (CustomActionBar) findViewById(R.id.transactionorder_header);
        customActionBar.initStyle(CustomActionBar.HeaderStyle.DEFAULT_TITLE);
        customActionBar.setTitle(getResources().getString(R.string.title_transaction_order));
        adapter = new OrderListAdapter(this,datas,dataParser);
        lvTransaction.setAdapter(adapter.getAdapter());
        lvTransaction.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                initData();
            }
        });
    }

    private void initData(){
        dialogUtils.progressDialog();
        dataParser.setCallBack(getApplicationContext(), new CallBackimpl() {
            @Override
            public void callBack(List datas) {
                if (null != dialogUtils.getProgressDialog()) {
                    dialogUtils.getProgressDialog().dismiss();
                }
                lvTransaction.onRefreshComplete();
                adapter.clearData();
                adapter.addAll(datas);
            }
        });
        dataParser.parseOrders();
    }

//    private void event(){
//        lvTransaction.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                datas.get(position).getCode();
//                Intent intent = new Intent(getApplicationContext(), OrderDetailActivity.class);
//                intent.putExtra("ordercode", datas.get(position).getCode());
//                startActivity(intent);
//            }
//        });
//    }

}
