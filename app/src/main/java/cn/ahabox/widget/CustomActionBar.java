package cn.ahabox.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.ahabox.feiliwu_help.R;

/**
 * Created by libo on 2015/11/14.
 *
 * 自定义标题栏
 */
public class CustomActionBar extends LinearLayout {
    private ImageButton headerBack;
    private TextView headerTitle,headerSearchBtn;
    private LinearLayout headerSearch,gotoSearch;
    private LayoutInflater mInflater;
    private View headView;

    public CustomActionBar(Context context) {
        super(context);
        init(context);
    }

    public CustomActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public enum HeaderStyle {
        ONLY_TITLE,DEFAULT_TITLE, EDIT_TEXT,GOTO_SEARCH
    }

    public void init(Context context) {
        mInflater = LayoutInflater.from(context);
        headView = mInflater.inflate(R.layout.customactionbar, null);
        addView(headView);
        initView();
    }

    private void initView() {
        headerBack = (ImageButton) headView.findViewById(R.id.header_back);
        headerTitle = (TextView) headView.findViewById(R.id.header_title);
        headerSearchBtn = (TextView) headView.findViewById(R.id.tv_header_searchbtn);
        headerSearch = (LinearLayout) headView.findViewById(R.id.header_search);
        gotoSearch = (LinearLayout) headView.findViewById(R.id.header_gotosearch);
        headerBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) getContext()).finish();
            }
        });
    }

    /**
     * 设置标题文字
     * @param title
     */
    public void setTitle(String title){
        if(title != null)
            headerTitle.setText(title);
    }

    /**
     * 将默认的返回按钮功能去掉
     */
    public void setNoBack(){
        headerBack.setVisibility(GONE);
    }

    /**
     * 按需求自定义样式
     *
     * @param style
     */
    public void initStyle(HeaderStyle style) {
        switch (style) {
            case ONLY_TITLE:
                headerBack.setVisibility(GONE);
                break;
            case DEFAULT_TITLE:

                break;
            case EDIT_TEXT:
                headerTitle.setVisibility(GONE);
                headerSearch.setVisibility(VISIBLE);
                headerSearchBtn.setVisibility(VISIBLE);
                break;
            case GOTO_SEARCH:
                headerTitle.setVisibility(GONE);
                gotoSearch.setVisibility(VISIBLE);
                break;
        }
    }

}
