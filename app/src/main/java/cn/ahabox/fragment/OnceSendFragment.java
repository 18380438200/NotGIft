package cn.ahabox.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.ahabox.adapter.OnceSendAdapter;
import cn.ahabox.feiliwu_help.R;
import cn.ahabox.interfaces.CallBackimpl;
import cn.ahabox.model.OtherAddressEntity;
import cn.ahabox.network.DataParserImpl;

/**
 * Created by libo on 2015/1/27.
 *
 * 曾近送过地址列表fragment
 */
public class OnceSendFragment extends Fragment {

    @Bind(R.id.gv_adress)
    GridView gvAddress;
    private ArrayList<OtherAddressEntity> datas = new ArrayList<>();
    private OnceSendAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_adress_list, container, false);
        ButterKnife.bind(this, view);
        initView();
        initData();
        return view;
    }

    private void initView() {
        adapter = new OnceSendAdapter(getActivity(),datas);
        gvAddress.setAdapter(adapter.getAdapter());
        gvAddress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.setClickPosition(position);
                adapter.getAdapter().notifyDataSetChanged();
            }
        });
    }

    private void initData(){
        DataParserImpl dataParser = new DataParserImpl();
        dataParser.setCallBack(getActivity(), new CallBackimpl() {
            @Override
            public void callBack(List list) {
                adapter.addAll(list);
            }
        });
        dataParser.parseOnceSend();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
