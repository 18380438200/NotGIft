package cn.ahabox.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ahabox.feiliwu_help.R;
import cn.ahabox.adapter.FragmentAdapter;
import cn.ahabox.config.Config;
import cn.ahabox.fragment.MyAddressFragment;
import cn.ahabox.fragment.OnceSendFragment;
import cn.ahabox.model.AddressEntity;
import cn.ahabox.model.TabInfo;
import cn.ahabox.widget.CustomActionBar;
import cn.ahabox.widget.indicator.TitleIndicator;

/**
 * Created by libo on 2015/1/27.
 *
 * 收礼人地址
 */
public class RecipientAddressActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    @Bind(R.id.tv_address_confirm)
    TextView tvAddressConfirm;
    @Bind(R.id.view_address_bottom)
    View viewAddressBottom;
    private CustomActionBar customActionBar;
    protected int mCurrentTab = 0;
    protected int mLastTab = -1;
    private String[] titles = {"自填地址", "曾经送过"};
    /**
     * 存放选项卡信息的列表
     */
    protected ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();
    protected FragmentAdapter myAdapter = null;
    protected ViewPager mPager;
    /**
     * 选项卡控件
     */
    protected TitleIndicator mIndicator;
    /** 预支付地址实体类 */
    public static AddressEntity addressEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipient_address);
        ButterKnife.bind(this);

        initView();
        initTab();
    }

    private void initView() {
        customActionBar = (CustomActionBar) findViewById(R.id.addredss_header);
        customActionBar.initStyle(CustomActionBar.HeaderStyle.DEFAULT_TITLE);
        customActionBar.setTitle(getResources().getString(R.string.title_recipient_address));

        //如果是购买流程，则显示下一步；0表示地址管理，1表示购买选择地址
        int tag = getIntent().getIntExtra(Config.ADDRESS_PARAMS, 0);
        if (tag == 1) {
            tvAddressConfirm.setVisibility(View.VISIBLE);
            viewAddressBottom.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.tv_address_confirm)
    void onClick() {
        startActivity(new Intent(getApplicationContext(), RecipientInfoActivity.class));
    }

    private void initTab() {
        // 这里初始化界面
        mCurrentTab = supplyTabs(mTabs);
        myAdapter = new FragmentAdapter(getApplicationContext(), getSupportFragmentManager(), mTabs);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(myAdapter);
        mPager.setOnPageChangeListener(this);
        mPager.setOffscreenPageLimit(mTabs.size());

        mIndicator = (TitleIndicator) findViewById(R.id.pagerindicator);
        mIndicator.init(mCurrentTab, mTabs, mPager);

        mPager.setCurrentItem(mCurrentTab);
        mLastTab = mCurrentTab;
    }

    /**
     * 在这里提供要显示的选项卡数据
     */
    protected int supplyTabs(List<TabInfo> tabs) {
        tabs.add(new TabInfo(0, titles[0], new MyAddressFragment()));
        tabs.add(new TabInfo(1, titles[1], new OnceSendFragment()));
        return 0;
    }

    @Override
    public void onDestroy() {
        mTabs.clear();
        mTabs = null;
        myAdapter.notifyDataSetChanged();
        myAdapter = null;
        mPager.setAdapter(null);
        mPager = null;
        mIndicator = null;

        super.onDestroy();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        mIndicator.onScrolled((mPager.getWidth() + mPager.getPageMargin()) * position + positionOffsetPixels);
    }

    @Override
    public void onPageSelected(int position) {
        mIndicator.onSwitched(position);
        mCurrentTab = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == ViewPager.SCROLL_STATE_IDLE) {
            mLastTab = mCurrentTab;
        }
    }

}
