package cn.ahabox.widget;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import cn.ahabox.feiliwu_help.R;
import cn.ahabox.utils.StrUtils;

/**
 * Created by libo on 2016/8/15.
 *
 * 啊哈浮动菜单
 */
public class FlowMenu {
    private WindowManager wm;
    private WindowManager.LayoutParams wmParams;
    private ToggleButton menuBtn;
    private Context context;
    private View parentView;
    private MenuPopupWindow menuPopupWindow;
    public FlowMenu(Context context,View parentView){
        this.context = context;
        this.parentView = parentView;
        menuPopupWindow = new MenuPopupWindow(context,this);
        initFloatView();
    }

    /**
     * 初始化浮动按钮
     */
    private void initFloatView() {
        // 获取WindowManager
        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        // 设置LayoutParams(全局变量）相关参数
        wmParams = new WindowManager.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT);

        // 以屏幕左上角为原点，设置x、y初始值
        wmParams.x = StrUtils.dpTopx(context, 20);
        wmParams.y = StrUtils.dpTopx(context,30);
        // 设置悬浮窗口长宽数据
        wmParams.width = 130;
        wmParams.height = 130;

        createLeftFloatView();
        menuBtn.invalidate();
    }

    public ToggleButton getMenuBtn(){
        return menuBtn;
    }

    /**
     * 创建悬浮按钮
     */
    private void createLeftFloatView() {
        menuBtn = new ToggleButton(context);
        menuBtn.setBackgroundResource(R.mipmap.ic_menu_open);
        menuBtn.setText("");
        menuBtn.setTextOn("");
        menuBtn.setTextOff("");
        menuBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (menuBtn.isChecked()) {
                    menuPopupWindow.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
                    menuBtn.setBackgroundResource(R.mipmap.ic_menu_close);
                } else {
                    menuPopupWindow.dismiss();
                    menuBtn.setBackgroundResource(R.mipmap.ic_menu_open);
                }
            }
        });
        // 调整悬浮窗口
        wmParams.gravity = Gravity.RIGHT | Gravity.BOTTOM;
        // 显示myFloatView图像
        wm.addView(menuBtn, wmParams);
    }
}
