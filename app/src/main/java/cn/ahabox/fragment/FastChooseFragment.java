package cn.ahabox.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.ahabox.adapter.BaseViewHolder;
import cn.ahabox.adapter.CommonAdapterPosition;
import cn.ahabox.adapter.FmPagerAdapter;
import cn.ahabox.feiliwu_help.R;
import cn.ahabox.interfaces.CallBackimpl;
import cn.ahabox.model.FastChooseTagEntity;
import cn.ahabox.network.DataParserImpl;
import cn.ahabox.utils.DialogUtils;

public class FastChooseFragment extends Fragment {
    @Bind(R.id.viewPagerId)
    ViewPager viewPager;
    @Bind(R.id.gv_fastchoose_classify)
    GridView gvFastchooseClassify;
    private List<Fragment> fragments;
    private FmPagerAdapter adapter;
    /**
     * 商品标签数据
     */
    private List<FastChooseTagEntity> groupList;
    /**
     * 登录成功重新加载快速选择数据
     */
    public static CallBackimpl loginCallBack;
    /** 从首页菜单跳转回调当前选项 */
    public static CallBackimpl cateCallBack;
    private DialogUtils dialogUtils;
    private int[] pics = {R.mipmap.classify_home_unchecked,R.mipmap.classify_access_unchecked,R.mipmap.classify_beauty_unchecked,
            R.mipmap.classify_kids_unchecked,R.mipmap.classify_office_unchecked,R.mipmap.classify_sience_unchecked};
    private int[] picsChecked = {R.mipmap.classify_home_checked,R.mipmap.classify_access_checked,R.mipmap.classify_beauty_checked,
            R.mipmap.classify_kids_checked,R.mipmap.classify_office_checked,R.mipmap.classify_sience_checked};
    private static String params = "params";
    private int currentPage;

    public static FastChooseFragment newInstance(int page){
        FastChooseFragment fragment = new FastChooseFragment();
        Bundle args = new Bundle();
        args.putInt(params, page);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentPage = getArguments().getInt(params);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fast_choose_two, container, false);
        ButterKnife.bind(this, view);
        initView();
        initData();
        setClassify();
        return view;
    }

    private void initView() {
        loginCallBack = new CallBackimpl() {
            @Override
            public void confirmHandle() {
                initData();
            }
        };
    }

    private void setClassify(){
        ArrayList piclist = new ArrayList();
        for(int i=0;i<pics.length;i++){
            piclist.add(pics[i]);
        }
        CommonAdapterPosition classifyAdapter = new CommonAdapterPosition(getActivity(),piclist,R.layout.item_classify) {
            @Override
            public void convert(BaseViewHolder baseViewHolder, Object o, final int position) {
                ImageView imageView = baseViewHolder.getView(R.id.iv_classify);
                imageView.setBackgroundResource((Integer) o);
            }
            @Override
            public void convert(BaseViewHolder baseViewHolder, Object o) {
            }
        };
        gvFastchooseClassify.setAdapter(classifyAdapter);
        gvFastchooseClassify.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                viewPager.setCurrentItem(position);
                menuStateChange(position);
            }
        });
        //初始点击的分类样式
        cateCallBack = new CallBackimpl() {
            @Override
            public void callBack(int position) {
                //点击RadioGroup为position项
                gvFastchooseClassify.performItemClick(null,position,0);
            }
        };
    }

    private void menuStateChange(int position){
        ImageView imageView;
        for(int i=0;i<pics.length;i++){
            if(i==position){
                View bgView = gvFastchooseClassify.getChildAt(position);
                imageView = (ImageView) bgView.findViewById(R.id.iv_classify);
                bgView.setBackgroundColor(getResources().getColor(R.color.black));
                imageView.setBackgroundResource(picsChecked[position]);
            }else{
                View bgView = gvFastchooseClassify.getChildAt(i);
                imageView = (ImageView) bgView.findViewById(R.id.iv_classify);
                bgView.setBackgroundResource(R.drawable.classify_box_shape);
                imageView.setBackgroundResource(pics[i]);
            }
        }
    }

    private void initData() {
        dialogUtils = new DialogUtils(getActivity());
        dialogUtils.progressDialog();
        DataParserImpl dataParser = new DataParserImpl();
        dataParser.setCallBack(getActivity(), new CallBackimpl() {
            @Override
            public void callBack(JSONObject obj) {
                try {
                    if (null != dialogUtils.getProgressDialog()) {
                        dialogUtils.getProgressDialog().dismiss();
                    }
                    JSONArray array = obj.getJSONArray("groups");
                    groupList = JSON.parseArray(array.toString(), FastChooseTagEntity.class);
                    initViewsAndAdapter();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        dataParser.parseProductTag();
    }

    private void initViewsAndAdapter() {
        fragments = new ArrayList();
        for (int i = 0; i < pics.length; i++) {
            fragments.add(FastChooseListFragment.newInstance((ArrayList) groupList.get(i).getSubtags()));
        }
        adapter = new FmPagerAdapter(fragments, getChildFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(fragments.size());
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                menuStateChange(position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        gvFastchooseClassify.performItemClick(null,currentPage,0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
