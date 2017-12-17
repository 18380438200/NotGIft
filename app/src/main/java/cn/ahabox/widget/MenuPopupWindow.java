package cn.ahabox.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TableRow;
import android.widget.TextView;

import cn.ahabox.activity.MainActivity;
import cn.ahabox.activity.SearchResultsActivity;
import cn.ahabox.feiliwu_help.R;

/**
 * Created by libo on 2016/8/16.
 *
 * 菜单弹出页面
 */
public class MenuPopupWindow extends PopupWindow{
    private Context context;
    private View view;
    private TextView tvsearchBox;
    private TableRow trCate,trMe,trDesigner,trAbout;
    private FlowMenu flowMenu;

    public MenuPopupWindow(Context context,FlowMenu flowMenu){
        this.context = context;
        this.flowMenu = flowMenu;
        view = LayoutInflater.from(context).inflate(R.layout.popup_menu,null);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setContentView(view);
        setBackgroundDrawable(new BitmapDrawable());
        setFocusable(true);
        initView();
    }

    private void initView() {
        tvsearchBox = (TextView) view.findViewById(R.id.tv_menu_searchbox);
        trCate = (TableRow) view.findViewById(R.id.tr_menu_cate);
        trMe = (TableRow) view.findViewById(R.id.tr_menu_me);
        trDesigner = (TableRow) view.findViewById(R.id.tr_menu_designer);
        trAbout = (TableRow) view.findViewById(R.id.tr_menu_about);

        MyOnclickListener listener = new MyOnclickListener();
        tvsearchBox.setOnClickListener(listener);
        trCate.setOnClickListener(listener);
        trMe.setOnClickListener(listener);
        trDesigner.setOnClickListener(listener);
        trAbout.setOnClickListener(listener);
    }

    class MyOnclickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            dismiss();
            flowMenu.getMenuBtn().toggle();
            switch (v.getId()){
                case R.id.tv_menu_searchbox:
                    context.startActivity(new Intent(context, SearchResultsActivity.class));
                    break;
                case R.id.tr_menu_about:
                    MainActivity.menuCallBack.callBack(1,0);
                    break;
                case R.id.tr_menu_cate:
                    MainActivity.menuCallBack.callBack(2,0);
                    break;
                case R.id.tr_menu_designer:
                    MainActivity.menuCallBack.callBack(3,0);
                    break;
                case R.id.tr_menu_me:
                    MainActivity.menuCallBack.callBack(4,0);
                    break;
            }
        }
    }

}
