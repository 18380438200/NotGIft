package cn.ahabox.widget;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import cn.ahabox.activity.MainActivity;
import cn.ahabox.activity.SearchResultsActivity;
import cn.ahabox.activity.ShopCartActivity;
import cn.ahabox.config.App;
import cn.ahabox.feiliwu_help.R;
import cn.ahabox.interfaces.CallBackimpl;

/**
 * Created by libo on 2016/8/22.
 * <p/>
 * mainactivity顶部菜单栏
 */
public class CustomMainBar extends RelativeLayout implements View.OnClickListener {
    private View headView,viewShopcartNotice;
    private ImageButton ibSearch, ibHome, ibShopcart;
    private RelativeLayout rlShopcart;
    public static CallBackimpl shopcartNotice;
    public static boolean haveCartProduct;

    public CustomMainBar(Context context) {
        super(context);
        init(context);
    }

    public CustomMainBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        headView = LayoutInflater.from(context).inflate(R.layout.fix_actionbar, null);
        addView(headView);
        initView();

        if(CustomMainBar.haveCartProduct)
            viewShopcartNotice.setVisibility(VISIBLE);

        shopcartNotice = new CallBackimpl() {
            @Override
            public void confirmHandle() {
                viewShopcartNotice.setVisibility(VISIBLE);
            }
        };
    }

    private void initView() {
        ibSearch = (ImageButton) headView.findViewById(R.id.ib_mainbar_search);
        ibHome = (ImageButton) headView.findViewById(R.id.ib_mainbar_home);
        rlShopcart = (RelativeLayout) headView.findViewById(R.id.rl_shopcart);
        ibShopcart = (ImageButton) headView.findViewById(R.id.ib_shopcart);
        viewShopcartNotice = headView.findViewById(R.id.view_shopcart_notice);
        ibSearch.setOnClickListener(this);
        ibHome.setOnClickListener(this);
        ibShopcart.setOnClickListener(this);
        rlShopcart.setOnClickListener(this);
    }

    public void setNoHome() {
        ibHome.setVisibility(GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_mainbar_home:
                MainActivity.menuCallBack.callBack(0, 0);
                break;
            case R.id.ib_mainbar_search:
                getContext().startActivity(new Intent(getContext(), SearchResultsActivity.class));
                break;
            case R.id.rl_shopcart:
            case R.id.ib_shopcart:
                App.gotoLogin(getContext(), new CallBackimpl() {
                    @Override
                    public void confirmHandle() {
                        getContext().startActivity(new Intent(getContext(), ShopCartActivity.class));
                    }
                });
                break;
        }
    }
}
