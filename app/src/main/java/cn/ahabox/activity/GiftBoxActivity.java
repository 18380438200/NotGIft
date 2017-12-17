package cn.ahabox.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.ahabox.feiliwu_help.R;
import cn.ahabox.fragment.GiftBoxNosendFragment;
import cn.ahabox.fragment.GiftboxNoreceiveFragment;
import cn.ahabox.widget.CustomActionBar;

/**
 * Created by libo on 2016/2/17.
 *
 * 礼物箱页面
 */
public class GiftBoxActivity extends BaseActivity {
    @Bind(R.id.rg_giftbox)
    RadioGroup rgGiftbox;
    @Bind(R.id.ll_detail_bottomline)
    LinearLayout llDetailBottomline;
    @Bind(R.id.giftbox_header) CustomActionBar customActionBar;
    private GiftBoxNosendFragment nosendFragment;
    private GiftboxNoreceiveFragment noreceiveFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_box);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        customActionBar.initStyle(CustomActionBar.HeaderStyle.DEFAULT_TITLE);
        customActionBar.setTitle(getResources().getString(R.string.title_giftbox));

        rgGiftbox.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i = 0; i < group.getChildCount(); i++) {
                    View view = llDetailBottomline.getChildAt(i);
                    if (checkedId == group.getChildAt(i).getId()) {
                        setFragment(i);
                        view.setBackgroundColor(getResources().getColor(R.color.text_red));
                    } else {
                        view.setBackgroundColor(getResources().getColor(R.color.transparent));
                    }
                }
            }
        });
        ((RadioButton) rgGiftbox.getChildAt(0)).setChecked(true);

    }

    /**
     * 设置切换页面
     *
     * @param page
     */
    private void setFragment(int page) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (null != nosendFragment) transaction.hide(nosendFragment);
        if (null != noreceiveFragment) transaction.hide(noreceiveFragment);
        switch (page) {
            case 0:
                if (null == nosendFragment) {
                    nosendFragment = new GiftBoxNosendFragment();
                    transaction.add(R.id.fl_giftbox, nosendFragment);
                } else {
                    transaction.show(nosendFragment);
                }
                break;
            case 1:
                if (null == noreceiveFragment) {
                    noreceiveFragment = new GiftboxNoreceiveFragment();
                    transaction.add(R.id.fl_giftbox, noreceiveFragment);
                } else {
                    transaction.show(noreceiveFragment);
                }
                break;
        }
        transaction.commit();
    }

}
