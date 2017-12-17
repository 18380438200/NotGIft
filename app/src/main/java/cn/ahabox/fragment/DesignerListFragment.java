package cn.ahabox.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.ahabox.adapter.DesignerAdapter;
import cn.ahabox.feiliwu_help.R;
import cn.ahabox.interfaces.CallBackimpl;
import cn.ahabox.model.SubPageEntity;
import cn.ahabox.network.DataParserImpl;
import cn.ahabox.utils.DialogUtils;

/**
 * Created by libo on 2015/1/25.
 *
 * 设计师列表
 */
public class DesignerListFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";

    /** 链接地址中参数种类id */
    public int categoryId;
    @Bind(R.id.lv_designerlist)
    PullToRefreshListView listview;
    private ArrayList<SubPageEntity> designerdatas = new ArrayList<>();
    private DesignerAdapter designerAdapter;

    private DialogUtils dialogUtils;
    /** 当前加载页数 */
    private int currentPage = 1;

    public static DesignerListFragment newInstance(int param1) {
        DesignerListFragment fragment = new DesignerListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categoryId = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_designer_list, container, false);
        ButterKnife.bind(this,view);
        initView();
        initData(0);
        dialogUtils.progressDialog();
        return view;
    }

    private void initView() {
        dialogUtils = new DialogUtils(getActivity());

        designerAdapter = new DesignerAdapter(getActivity(),designerdatas);

        listview.setAdapter(designerAdapter.getAdapter());
        listview.setMode(PullToRefreshBase.Mode.BOTH);
        listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                currentPage = 1;  //下拉刷新，重新从第一页开始加载
                initData(1);
            }
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                initData(2);
            }
        });
    }

    private void initData(final int way){
        DataParserImpl dataParser = new DataParserImpl();
        dataParser.setCallBack(getActivity(), new CallBackimpl() {
            @Override
            public void callBack(List datas,boolean isEnd) {
                switch (way){
                    case 0:
                        if(null != dialogUtils.getProgressDialog()){
                            dialogUtils.getProgressDialog().dismiss();
                        }
                        designerAdapter.addAll(datas);
                        break;
                    case 1:
                        designerAdapter.clearData();
                        designerAdapter.addAll(datas);
                        listview.onRefreshComplete();
                        break;
                    case 2:
                        designerAdapter.addAll(datas);
                        listview.onRefreshComplete();
                        break;
                }

                if(isEnd){
                    listview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);  //如果是最后一页，停止上拉加载功能
                }else{
                    listview.setMode(PullToRefreshBase.Mode.BOTH);
                }

            }
        });
        dataParser.parseSubPage(categoryId, currentPage++);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
