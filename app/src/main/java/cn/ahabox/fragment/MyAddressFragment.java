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
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.ahabox.activity.AddAddressActivity;
import cn.ahabox.adapter.AddressListAdapter;
import cn.ahabox.feiliwu_help.R;
import cn.ahabox.interfaces.CallBackimpl;
import cn.ahabox.interfaces.IcallBack;
import cn.ahabox.model.AddressEntity;
import cn.ahabox.network.DataParserImpl;
import cn.ahabox.utils.DialogUtils;

/**
 * Created by libo on 2015/1/27.
 *
 * 自填地址列表fragment
 */
public class MyAddressFragment extends Fragment {

    private ArrayList<AddressEntity> datas = new ArrayList<>();
    @Bind(R.id.gv_adress) GridView gvAddress;
    private AddressListAdapter adapter;
    public static IcallBack icallBack;
    private DialogUtils dialogUtils;
    public static AddressEntity editAddress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_adress_list, container, false);
        ButterKnife.bind(this, view);
        initView();
        event();
        initData();
        return view;
    }

    private void initView() {
        dialogUtils = new DialogUtils(getActivity());
        datas.add(new AddressEntity());
        adapter = new AddressListAdapter(getActivity(),datas);
        gvAddress.setAdapter(adapter);

        icallBack = new CallBackimpl() {
            @Override
            public void confirmHandle() {
                datas.clear();
                datas.add(new AddressEntity());
                initData();
            }
        };
    }

    private void event(){
        gvAddress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Intent intent = new Intent(getActivity(), AddAddressActivity.class);
                    intent.putExtra("addOredit", 0);
                    startActivity(intent);
                } else {
                    adapter.setSelection(position);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void initData(){
        dialogUtils.progressDialog();
        DataParserImpl dataParser = new DataParserImpl();
        dataParser.setCallBack(getActivity(), new CallBackimpl() {
            @Override
            public void callBack(List list) {
                if(null != dialogUtils.getProgressDialog()){
                    dialogUtils.getProgressDialog().dismiss();
                }
                if(null != list) {
                    adapter.addAll(list);
                    adapter.setSelection(1);
                }
            }
        });
        dataParser.parseMyAddress();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

}
