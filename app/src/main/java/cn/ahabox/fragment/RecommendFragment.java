package cn.ahabox.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.ahabox.feiliwu_help.R;
import cn.ahabox.adapter.BaseViewHolder;
import cn.ahabox.adapter.CommonAdapter;

/**
 * Created by libo on 2016/2/18.
 *
 * 商品推荐Fragment
 */
public class RecommendFragment extends Fragment {
    private ArrayList<String> datas = new ArrayList<>();
    @Bind(R.id.gv_recommend) GridView gridView;
    private CommonAdapter productAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recommend, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        //成交订单列表数据
        for (int i = 0; i < 8; i++) {
            datas.add("");
        }
        //成交订单
        productAdapter = new CommonAdapter(getActivity(),datas, R.layout.item_productlist) {
            @Override
            public void convert(BaseViewHolder baseViewHolder, Object o) {

            }
        };
        gridView.setAdapter(productAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

}
