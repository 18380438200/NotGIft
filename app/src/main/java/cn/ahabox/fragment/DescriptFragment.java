package cn.ahabox.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.ahabox.feiliwu_help.R;
import cn.ahabox.adapter.BaseViewHolder;
import cn.ahabox.adapter.CommonAdapter;
import cn.ahabox.model.ProductDetailEntity;

/**
 *  Created by libo on 2015/1/22.
 *
 * 礼物描述
 */
public class DescriptFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private ArrayList datas;
    @Bind(R.id.lv_descript) ListView listView;

    public static DescriptFragment newInstance(ArrayList param1) {
        DescriptFragment fragment = new DescriptFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM1,param1);
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
        View view = inflater.inflate(R.layout.fragment_descript, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        CommonAdapter adapter = new CommonAdapter(getActivity(),datas,R.layout.item_detail_descript) {
            @Override
            public void convert(BaseViewHolder baseViewHolder, Object o) {
                ProductDetailEntity.IntroEntity entity = (ProductDetailEntity.IntroEntity)o;
                TextView tvTitle = baseViewHolder.getView(R.id.tv_descript_title);
                TextView tvContent = baseViewHolder.getView(R.id.tv_descript_content);
                tvTitle.setText(entity.getName());
                tvContent.setText(entity.getSummary());
            }
        };
        listView.setAdapter(adapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

}
