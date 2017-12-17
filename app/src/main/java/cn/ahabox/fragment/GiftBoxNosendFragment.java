package cn.ahabox.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.ahabox.adapter.WaitSendAdapter;
import cn.ahabox.feiliwu_help.R;
import cn.ahabox.interfaces.CallBackimpl;
import cn.ahabox.model.WaitSendEntity;
import cn.ahabox.network.DataParserImpl;
import cn.ahabox.utils.DialogUtils;

/**
 * Created by libo on 2016/2/18.
 * <p/>
 * 礼物箱未送出礼物
 */
public class GiftBoxNosendFragment extends Fragment {
    @Bind(R.id.lv_giftbox)
    PullToRefreshListView listview;
    @Bind(R.id.tv_no_gift)
    TextView tvNoGift;
    private ArrayList<WaitSendEntity> datas = new ArrayList<>();
    private DialogUtils dialogUtils;
    private WaitSendAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_giftbox_list, container, false);
        ButterKnife.bind(this, view);
        initView();
        initData();
        return view;
    }

    private void initView() {
        dialogUtils = new DialogUtils(getActivity());
        adapter = new WaitSendAdapter(getActivity(), datas);
        listview.setAdapter(adapter.getAdapter());
        listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                initData();
            }
        });
    }

    private void initData() {
        dialogUtils.progressDialog();
        DataParserImpl dataParser = new DataParserImpl();
        dataParser.setCallBack(getActivity(), new CallBackimpl() {
            @Override
            public void callBack(List list) {
                if (null != dialogUtils.getProgressDialog()) {
                    dialogUtils.getProgressDialog().dismiss();
                }
                listview.onRefreshComplete();
                if(list.size() == 0){    //没有礼物提示
                    tvNoGift.setVisibility(View.VISIBLE);
                }else{
                    adapter.clearData();
                    adapter.addAll(list);
                }
            }
        });
        dataParser.parseWaitSend();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
