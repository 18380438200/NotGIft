package cn.ahabox.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ahabox.activity.MainActivity;
import cn.ahabox.activity.ProductDetailActivity;
import cn.ahabox.activity.WebViewActivity;
import cn.ahabox.adapter.BannerViewpagerAdapter;
import cn.ahabox.adapter.BaseViewHolder;
import cn.ahabox.adapter.CommonAdapter;
import cn.ahabox.adapter.GiftRecommendAdapter;
import cn.ahabox.adapter.QualityRecommendAdapter;
import cn.ahabox.application.MyApp;
import cn.ahabox.config.Config;
import cn.ahabox.feiliwu_help.R;
import cn.ahabox.interfaces.CallBackimpl;
import cn.ahabox.model.FirstPageBannerEntity;
import cn.ahabox.model.FirstPageQualityEntity;
import cn.ahabox.model.RecommendEntity;
import cn.ahabox.network.DataParserImpl;
import cn.ahabox.utils.DialogUtils;
import cn.ahabox.utils.NetWorkUtils;
import cn.ahabox.utils.SDcardTools;
import cn.ahabox.utils.SharedPrefUtils;
import cn.ahabox.utils.StrUtils;
import cn.ahabox.utils.UpdateUtil;
import cn.ahabox.widget.BannerView;
import cn.ahabox.widget.CustomMainBar;
import cn.ahabox.widget.ListViewForScrollView;
import cn.ahabox.widget.TopFloatScrollView;

/**
 * Created by libo on 2016/1/19.
 * <p/>
 * 首页
 */
public class FirstPageFragment extends Fragment implements TopFloatScrollView.OnScrollListener {
    @Bind(R.id.rl_firstpage_banner)
    LinearLayout rlFirstpageBanner;
    @Bind(R.id.lv_firstpage_catergery)
    ListViewForScrollView lvFirstpageCatergery;
    private BannerView bannerView;
    private ArrayList<RecommendEntity> giftDatas = new ArrayList<>();
    private ArrayList<FirstPageQualityEntity> qualityDatas = new ArrayList<>();
    private GiftRecommendAdapter giftRecommendAdapter;
    private QualityRecommendAdapter qualityRecommendAdapter;
    @Bind(R.id.gv_firstpage_gift)
    GridView gvGift;
    @Bind(R.id.lv_firstpage_quality)
    ListView lvQuality;
    @Bind(R.id.ib_firstpage_up)
    ImageButton toTopBtn;
    /**
     * 浮动菜单栏的显示隐藏高度
     */
    private int topLayoutHeight;
    @Bind(R.id.scroll_firstpage)
    TopFloatScrollView scrollView;
    @Bind(R.id.ll_firstpage_stick)
    LinearLayout floatBarContainer;
    @Bind(R.id.rg_menubar)
    RadioGroup rgStick;
    @Bind(R.id.srl_firstpage)
    SwipeRefreshLayout swipeRefreshLayout;
    private RadioGroup fixStick;
    private boolean isShow = false;
    /**
     * 跳转到设计师、艺术家页面记录
     */
    public static String currentPageKey = "intentPage";
    private DataParserImpl dataParser;
    private DialogUtils dialogUtils;
    /**
     * 轮播图的高度，单位为dp
     */
    private int bannerHeight = 250;
    private View view;
    private UpdateUtil updateUtil;
    private int[] catePics = {R.mipmap.ic_one,R.mipmap.ic_two,R.mipmap.ic_three,R.mipmap.ic_four,R.mipmap.ic_five,R.mipmap.ic_six,R.mipmap.ic_seven};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_first_page, container, false);
        ButterKnife.bind(this, view);
        initView();
        initToTop();
        event();
        return view;
    }

    private void initView() {
        CustomMainBar customMainBar = (CustomMainBar) view.findViewById(R.id.firstpage_header);
        customMainBar.setNoHome();
        initCatergery();
        //礼物推荐
        giftRecommendAdapter = new GiftRecommendAdapter(getActivity(), giftDatas);
        gvGift.setAdapter(giftRecommendAdapter.getAdapter());

        qualityRecommendAdapter = new QualityRecommendAdapter(getActivity(), qualityDatas);
        lvQuality.setAdapter(qualityRecommendAdapter.getAdapter());

        //网络判断
        if (!NetWorkUtils.isNetWorkAvailable(getActivity())) {
            Toast.makeText(getActivity(), "当前网络不可用", Toast.LENGTH_SHORT).show();
            String json = SDcardTools.getFirstPageData("firstpagedata.txt");
            try {
                scrollView.setVisibility(View.VISIBLE);
                setDatas(new JSONObject(json));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            dataParser = new DataParserImpl();
            dialogUtils = new DialogUtils(getActivity());
            initData();

            new CountDownTimer(1200, 1200) {
                @Override
                public void onTick(long millisUntilFinished) {
                }

                @Override
                public void onFinish() {
                    doubleInitData();
                }
            }.start();

            tipUpdate();
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });

    }

    private void initToTop() {
        RelativeLayout rl = (RelativeLayout) floatBarContainer.findViewById(R.id.fixbar);
        fixStick = (RadioGroup) rl.findViewById(R.id.rg_menubar);

        scrollView.setOnScrollListener(this);
    }

    /**
     * 商品分类
     */
    private void initCatergery(){
        ArrayList catelist = new ArrayList();
        for(int i=0;i<catePics.length;i++){
            catelist.add(catePics[i]);
        }
        CommonAdapter cateAdapter = new CommonAdapter(getActivity(), catelist,R.layout.iv_firstpage_cate) {
            @Override
            public void convert(BaseViewHolder baseViewHolder, Object o) {
                ImageView imageView = baseViewHolder.getView(R.id.iv_firstpage_cate);
                imageView.setBackgroundResource((Integer) o);
            }
        };
        lvFirstpageCatergery.setAdapter(cateAdapter);
        lvFirstpageCatergery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    MainActivity.menuCallBack.callBack(3, 0);
                } else {
                    MainActivity.menuCallBack.callBack(2, position - 1);
                }
            }
        });
    }

    private void initData() {
        dialogUtils.progressDialog();
        dataParser.setCallBack(getActivity(), new CallBackimpl() {
            @Override
            public void callBack(JSONObject obj) {
                if (null != dialogUtils.getProgressDialog()) {
                    dialogUtils.getProgressDialog().dismiss();
                }
                swipeRefreshLayout.setRefreshing(false);
                if (null != obj) {
                    scrollView.setVisibility(View.VISIBLE);
                    setDatas(obj);
                    SDcardTools.noNetWorkCache(obj.toString(), "firstpagedata.txt");
                }
            }
        });
        dataParser.parseFirstPage();
    }

    /**
     * 再一次刷新数据处理
     */
    private void doubleInitData() {
        dataParser.setCallBack(getActivity(), new CallBackimpl() {
            @Override
            public void callBack(JSONObject obj) {
                if (null != obj)
                    setDatas(obj);
            }
        });
        dataParser.parseFirstPage();
    }

    /**
     * 设置首页数据
     */
    private void setDatas(JSONObject obj) {
        try {
            if(obj.getBoolean("have_cart_product")){
                CustomMainBar.haveCartProduct = true;
                CustomMainBar.shopcartNotice.confirmHandle();
            }

            //滚动条数据
            JSONArray bannerArray = obj.getJSONArray("banner");
            ArrayList bannerlist = (ArrayList) JSON.parseArray(bannerArray.toString(), FirstPageBannerEntity.class);
            BannerViewpagerAdapter viewpagerAdapter = new BannerViewpagerAdapter(getActivity(), bannerlist);
            bannerView = new BannerView(getActivity(), bannerlist, viewpagerAdapter, R.layout.customviewpager);
            bannerView.linearLayout.setHorizontalGravity(Gravity.CENTER);
            moveViewpager();

            rlFirstpageBanner.removeAllViews();    //刷新前清除旧数据
            rlFirstpageBanner.addView(bannerView.bannerView);
            //让scrollView顶部聚焦，首页默认到顶部
            rlFirstpageBanner.setFocusable(true);
            rlFirstpageBanner.setFocusableInTouchMode(true);
            rlFirstpageBanner.requestFocus();

            //获取悬浮框的顶部位置
            topLayoutHeight = StrUtils.dpTopx(getActivity(), bannerHeight);

            setRecommendData(obj);

            activityDialog(obj);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置礼物、设计师、精品推荐数据
     */
    private void setRecommendData(JSONObject obj) throws JSONException {

        //礼物推荐数据
        JSONArray recommendArray = obj.getJSONArray("recommend");
        ArrayList recommendList = (ArrayList) JSON.parseArray(recommendArray.toString(), RecommendEntity.class);
        giftRecommendAdapter.clearData();  //刷新前清除旧数据
        giftRecommendAdapter.addAll(recommendList);

        //精品推荐数据
        JSONArray qualityArray = obj.getJSONArray("quality");
        ArrayList qualityList = (ArrayList) JSON.parseArray(qualityArray.toString(), FirstPageQualityEntity.class);
        qualityRecommendAdapter.clearData();   //刷新前清除旧数据
        qualityRecommendAdapter.addAll(qualityList);
        lvQuality.setSelection(ListView.FOCUS_DOWN);    //防止精品推荐列表最后一项高度显示不全

    }

    /**
     * 弹出框时间间隔判断
     */
    private void activityDialog(JSONObject obj) {
        try {
            JSONObject toastObj = obj.getJSONObject("website").getJSONObject("pop_page");
            boolean isShow = toastObj.getBoolean("switch");
            if (isShow) {
                String lastToastTime = (String) SharedPrefUtils.getParams(getActivity(), "toasttime", "");
                String currentTime = (String) StrUtils.currentTime("yyyy-MM-dd hh:mm:ss");
                String toastTime = StrUtils.getTimeStamp(currentTime, "yyyy-MM-dd hh:mm:ss");
                if (lastToastTime.equals("") || (Long.parseLong(toastTime) - Long.parseLong(lastToastTime)) / (60 * 60) > 12) {   //时间间隔12个小时会重新弹出
                    Dialog dialog = new Dialog(getActivity(), R.style.Dialog_Fullscreen);
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.setContentView(R.layout.firstpage_toast);
                    dialog.show();
                    dialogSetting(dialog, toastObj, toastTime);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 首页弹出框活动
     *
     * @param dialog
     * @param toastObj
     * @param toastTime
     * @throws JSONException
     */
    private void dialogSetting(final Dialog dialog, JSONObject toastObj, String toastTime) throws JSONException {
        ImageView imageView = (ImageView) dialog.findViewById(R.id.iv_firstpage_toast);
        ImageButton ibClose = (ImageButton) dialog.findViewById(R.id.ib_firstpage_close);
        //每当弹出框一弹出，就会重新记录弹出时间
        SharedPrefUtils.setParams(getActivity(), "toasttime", toastTime);
        final String imageUrl = toastObj.getJSONObject("content").getString("image_url");
        final String intentUrl = toastObj.getString("url");
        ImageLoader.getInstance().displayImage(imageUrl, imageView, MyApp.options);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra(Config.WEBVIEW_URL, intentUrl);
                startActivity(intent);
            }
        });

        ibClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    /**
     * 进入首页是否需要提示更新
     */
    private void tipUpdate() {
        updateUtil = new UpdateUtil(getActivity());
        updateUtil.firstPageCheckUpdate();
    }

    @OnClick(R.id.ib_firstpage_up)
    void onClick(View view) {
        //使用接口回调切换页面
        switch (view.getId()) {
            case R.id.ib_firstpage_up:
                scrollView.smoothScrollTo(0, 1);
                floatBarContainer.setVisibility(View.GONE);
                toTopBtn.setVisibility(View.GONE);
                break;
        }
    }

    //监听滚动Y值变化，通过addView和removeView来实现悬停效果
    @Override
    public void onScroll(int scrollY) {
        if (scrollY >= topLayoutHeight) {
            topBtnAnim(isShow, true);
            isShow = true;
        } else {
            topBtnAnim(isShow, false);
            isShow = false;
        }
    }

    /**
     * 回顶按钮效果
     *
     * @param isShow     当前是否显示
     * @param shouldShow 当前应该显示
     */
    private void topBtnAnim(boolean isShow, boolean shouldShow) {
        if (isShow) {
            if (!shouldShow) {
                toTopBtn.setVisibility(View.GONE);
            }
        } else {
            if (shouldShow) {
                toTopBtn.setVisibility(View.VISIBLE);
            }
        }
    }

    private void switchMenu(final RadioGroup radioGroup) {

        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            RadioButton button = (RadioButton) radioGroup.getChildAt(i);
            final int finalI = i;
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ClassifyFragment.class);
                    intent.putExtra("page", finalI);
                    startActivity(intent);
                }
            });
        }
    }

    private void event() {
        switchMenu(rgStick);
        switchMenu(fixStick);

        gvGift.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RecommendEntity giftEntity = giftDatas.get(position);
                if (giftEntity.getType() == 2) {
                    //商品详情页
                    Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
                    intent.putExtra(Config.PRODUCT_ID_KEY, Integer.parseInt(giftEntity.getProduct_id()));
                    startActivity(intent);
                } else if (giftEntity.getType() == 1) {
                    //webview页
                    Intent intent = new Intent(getActivity(), WebViewActivity.class);
                    intent.putExtra(Config.WEBVIEW_URL, giftEntity.getUrl());
                    startActivity(intent);
                }
            }
        });

        lvQuality.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FirstPageQualityEntity entity = qualityDatas.get(position);
                if (entity.getType() == 2) {
                    Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
                    intent.putExtra(Config.PRODUCT_ID_KEY, Integer.parseInt(entity.getProduct_id()));
                    startActivity(intent);
                }
            }
        });

    }

    /**
     * 滑动viewpager时禁用swipeRefreshLayout功能
     */
    private void moveViewpager() {
        bannerView.getViewPager().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        swipeRefreshLayout.setEnabled(false);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        swipeRefreshLayout.setEnabled(true);
                        break;
                }
                return false;
            }
        });
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
