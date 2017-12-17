package cn.ahabox.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.ahabox.activity.ProductDetailActivity;
import cn.ahabox.adapter.FastChooseProductAdapter;
import cn.ahabox.adapter.ProductSubtagAdapter;
import cn.ahabox.config.Config;
import cn.ahabox.feiliwu_help.R;
import cn.ahabox.interfaces.CallBackimpl;
import cn.ahabox.model.DesignerDetailEntity;
import cn.ahabox.model.FastChooseTagEntity;
import cn.ahabox.network.DataParserImpl;
import cn.ahabox.widget.PullToRefreshView;

/**
 * Created by libo on 2015/1/19.
 * <p/>
 * 快速选择子标签商品列表
 */
public class FastChooseListFragment extends Fragment {
    @Bind(R.id.gv_fastchoose)
    GridView gvFastchoose;
    @Bind(R.id.gv_fastchoose_sub)
    GridView gvFastchooseSub;
    @Bind(R.id.main_pull_refresh_view)
    PullToRefreshView mainPullRefreshView;
    /**
     * 商品标签id标签名集合
     */
    private ArrayList<FastChooseTagEntity.SubtagsEntity> datas = new ArrayList<>();
    /**
     * 根据标签选择商品数据
     */
    private ArrayList tagProducts = new ArrayList();
    private FastChooseProductAdapter productAdapter;
    private static String ARG_PARAM1 = "params1";
    /**
     * 选择子类别标签
     */
    private int tagId;
    private ProductSubtagAdapter subtagAdapter;
    private int currentPage = 1;
    public static CallBackimpl endCallBack;

    public static FastChooseListFragment newInstance(ArrayList datas) {
        Bundle args = new Bundle();
        FastChooseListFragment fragment = new FastChooseListFragment();
        args.putParcelableArrayList(ARG_PARAM1, datas);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            datas = (ArrayList<FastChooseTagEntity.SubtagsEntity>) getArguments().getSerializable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fastchoose_list, container, false);
        ButterKnife.bind(this, view);
        initView();
        initData(0);
        event();
        return view;
    }

    private void initView() {
        productAdapter = new FastChooseProductAdapter(getActivity(), tagProducts);
        gvFastchoose.setAdapter(productAdapter.getAdapter());

        mainPullRefreshView.setOnFooterRefreshListener(new PullToRefreshView.OnFooterRefreshListener() {
            @Override
            public void onFooterRefresh(PullToRefreshView view) {
                initData(2);
            }
        });
        mainPullRefreshView.setLastUpdated(new Date().toLocaleString());

        subtagAdapter = new ProductSubtagAdapter(getActivity(), datas);
        gvFastchooseSub.setAdapter(subtagAdapter);

        //加载默认第一项数据
        subtagAdapter.setSelection(0);
        subtagAdapter.notifyDataSetChanged();
        tagId = datas.get(0).getTag_id();
        initData(0);

    }

    @Override
    public void onResume() {
        super.onResume();
        initData(0);   //当启动其它界面进行操作（如收藏）再次回到分类列表，刷新当前标签数据
    }

    private void initData(final int way) {
        DataParserImpl dataParser = new DataParserImpl();

        if (way == 0) currentPage = 1;   //重新加载数据将当前页初始为1

        dataParser.setCallBack(getActivity(), new CallBackimpl() {
            @Override
            public void callBack(List datas, boolean end) {
                switch (way) {
                    case 0:
                        tagProducts.clear();
                        break;
                    case 1:
                        tagProducts.clear();
                        break;
                    case 2:
                        mainPullRefreshView.onFooterRefreshComplete();
                        break;
                }
                productAdapter.addAll(datas);
                if(end) {
                    mainPullRefreshView.getChildAt(2).setVisibility(View.GONE);
                }
            }
        });
        dataParser.parseTagProducts(tagId, currentPage++);
    }

    private void event() {

        gvFastchooseSub.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                subtagAdapter.setSelection(position);
                subtagAdapter.notifyDataSetChanged();
                tagId = datas.get(position).getTag_id();
                initData(0);
            }
        });

        gvFastchoose.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DesignerDetailEntity.ProductsEntity entity = (DesignerDetailEntity.ProductsEntity) tagProducts.get(position);
                Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
                intent.putExtra(Config.PRODUCT_ID_KEY, entity.getId());
                startActivity(intent);
            }
        });

        endCallBack = new CallBackimpl() {
            @Override
            public void confirmHandle() {
                //if(isEnd) Toast.makeText(getActivity(), isEnd + "没有更多数据了", Toast.LENGTH_SHORT).show();
            }
        };

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
