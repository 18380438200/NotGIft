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

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.ahabox.activity.ProductDetailActivity;
import cn.ahabox.adapter.ProductAdapter;
import cn.ahabox.config.Config;
import cn.ahabox.feiliwu_help.R;
import cn.ahabox.model.DesignerDetailEntity;
import cn.ahabox.utils.DialogUtils;

/**
 *  Created by libo on 2015/2/2.
 *
 * 商品列表
 */
public class ProductFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private ArrayList datas;
    @Bind(R.id.gv_product) GridView gvProduct;
    private ProductAdapter productAdapter;

    public static ProductFragment newInstance(ArrayList list) {
        ProductFragment fragment = new ProductFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1,list);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            datas = getArguments().getParcelableArrayList(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product, container, false);
        ButterKnife.bind(this,view);
        initView();
        return view;
    }

    private void initView() {
        productAdapter = new ProductAdapter(getActivity(),datas,new DialogUtils(getActivity()));
        gvProduct.setAdapter(productAdapter.getAdapter());

        gvProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
                intent.putExtra(Config.PRODUCT_ID_KEY, ((DesignerDetailEntity.ProductsEntity) datas.get(position)).getId());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

}
