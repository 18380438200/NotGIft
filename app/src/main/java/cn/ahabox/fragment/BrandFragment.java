package cn.ahabox.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ScrollView;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.ahabox.activity.DesignerDetailActivity;
import cn.ahabox.adapter.BrandAdapter;
import cn.ahabox.config.Config;
import cn.ahabox.feiliwu_help.R;
import cn.ahabox.interfaces.CallBackimpl;
import cn.ahabox.model.BrandEntity;
import cn.ahabox.network.DataParserImpl;
import cn.ahabox.utils.DialogUtils;
import cn.ahabox.utils.NetWorkUtils;
import cn.ahabox.utils.SDcardTools;

/**
 * Created by libo on 2015/1/20.
 * <p/>
 * 品牌汇
 */
public class BrandFragment extends Fragment {
    @Bind(R.id.gv_brand_designer)
    GridView gvDesigner;
    @Bind(R.id.scrollview_brand)
    PullToRefreshScrollView scrollviewBrand;
    /**
     * 设计师数据集合
     */
    private ArrayList<BrandEntity> designerDatas = new ArrayList();
    private BrandAdapter designerAdapter;
    private DataParserImpl dataParser;
    private DialogUtils dialogUtils;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_brand, container, false);
        ButterKnife.bind(this, view);
        initView();
        event();
        return view;
    }

    private void initView() {
        designerAdapter = new BrandAdapter(getActivity(), designerDatas);
        gvDesigner.setAdapter(designerAdapter.getAdapter());

        //网络判断
        if (!NetWorkUtils.isNetWorkAvailable(getActivity())) {
            String json = SDcardTools.getFirstPageData("brand.txt");
            try {
                setDatas(new JSONObject(json));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            initData();
            dialogUtils = new DialogUtils(getActivity());
            dialogUtils.progressDialog();
        }

        scrollviewBrand.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                initData();
            }
        });
    }

    private void event() {
        gvDesigner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), DesignerDetailActivity.class);
                intent.putExtra(Config.DESIGNER_ID_KEY, designerDatas.get(position).getId());
                startActivity(intent);
            }
        });

    }

    private void initData() {
        dataParser = new DataParserImpl();
        dataParser.setCallBack(getActivity(), new CallBackimpl() {
            @Override
            public void callBack(JSONObject obj) {
                if (null != dialogUtils.getProgressDialog()) {
                    dialogUtils.getProgressDialog().dismiss();
                }
                scrollviewBrand.onRefreshComplete();
                setDatas(obj);
                SDcardTools.noNetWorkCache(obj.toString(), "brand.txt");
            }
        });
        dataParser.parseBrand();
    }

    private void setDatas(JSONObject obj) {
        try {
            JSONArray designerArray = obj.getJSONArray("artists");
            List designerList = JSON.parseArray(designerArray.toString(), BrandEntity.class);
            designerAdapter.addAll(designerList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
