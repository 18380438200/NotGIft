package cn.ahabox.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.ahabox.adapter.ImageViewAdapter;
import cn.ahabox.feiliwu_help.R;

/**
 * Created by libo on 2015/1/22.
 * <p/>
 * 特有服务fragment
 */
public class ServiceFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    @Bind(R.id.rcv_service)
    RecyclerView rcvService;
    private ArrayList<String> datas;
    private ImageViewAdapter imageViewAdapter;

    public static ServiceFragment newInstance(ArrayList<String> datas) {
        ServiceFragment fragment = new ServiceFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_PARAM1, datas);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            datas = getArguments().getStringArrayList(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service, container, false);
        ButterKnife.bind(this, view);
        intiView();
        return view;
    }

    private void intiView() {
        imageViewAdapter = new ImageViewAdapter(getActivity(),datas);
        rcvService.setLayoutManager(new LinearLayoutManager(getActivity()));
        rcvService.setAdapter(imageViewAdapter);
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
