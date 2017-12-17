package cn.ahabox.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.ahabox.activity.WebViewActivity;
import cn.ahabox.adapter.SpecialTopicAdapter;
import cn.ahabox.config.Config;
import cn.ahabox.feiliwu_help.R;
import cn.ahabox.interfaces.CallBackimpl;
import cn.ahabox.model.SpecialTopicEntity;
import cn.ahabox.network.DataParserImpl;
import cn.ahabox.utils.DialogUtils;
import cn.ahabox.utils.NetWorkUtils;
import cn.ahabox.utils.SDcardTools;
import cn.ahabox.widget.CustomActionBar;

/**
 * Created by libo on 2015/2/17.
 *
 * 专题
 */
public class TopicFragment extends Fragment {
    @Bind(R.id.topic_header)
    CustomActionBar customActionBar;
    @Bind(R.id.lv_topic)
    PullToRefreshListView lvTopic;
    private ArrayList<SpecialTopicEntity> datas = new ArrayList();
    private SpecialTopicAdapter topicAdapter;
    private DataParserImpl dataParser;
    private DialogUtils dialogUtils;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_topic, container, false);
        ButterKnife.bind(this, view);
        initView();
        initData();
        return view;
    }

    private void initView() {
        customActionBar.initStyle(CustomActionBar.HeaderStyle.ONLY_TITLE);
        customActionBar.setTitle(getResources().getString(R.string.title_topic));

        topicAdapter = new SpecialTopicAdapter(getActivity(), datas);
        lvTopic.setAdapter(topicAdapter.getAdapter());

        //网络判断
        if(!NetWorkUtils.isNetWorkAvailable(getActivity())){
            String json = SDcardTools.getFirstPageData("topic.txt");
            Toast.makeText(getActivity(),"当前网络不可用",Toast.LENGTH_SHORT).show();
            try {
                setDatas(new JSONObject(json));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            initData();
            dialogUtils = new DialogUtils(getActivity());
            dialogUtils.progressDialog();
        }

        lvTopic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SpecialTopicEntity entity = datas.get(position-1);
                if (null != entity.getUrl() && entity.getUrl().length() != 0) {
                    Intent intent = new Intent(getActivity(), WebViewActivity.class);
                    intent.putExtra(Config.WEBVIEW_URL, entity.getUrl());
                    startActivity(intent);
                }
            }
        });

        lvTopic.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                initData();
            }
        });
    }

    private void initData() {
        dataParser = new DataParserImpl();
        dataParser.setCallBack(getActivity(), new CallBackimpl() {
            @Override
            public void callBack(JSONObject response) {
                if (null != dialogUtils.getProgressDialog()) {
                    dialogUtils.getProgressDialog().dismiss();
                }
                setDatas(response);
            }
        });
        dataParser.parseTopic();
    }

    private void setDatas(JSONObject response){
        try {
            JSONArray array = response.getJSONArray("data");
            List datas = JSON.parseArray(array.toString(), SpecialTopicEntity.class);
            lvTopic.onRefreshComplete();
            topicAdapter.clearData();
            topicAdapter.addAll(datas);
            SDcardTools.noNetWorkCache(response.toString(), "topic.txt");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
