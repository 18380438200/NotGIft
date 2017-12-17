package cn.ahabox.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.Arrays;

import cn.ahabox.feiliwu_help.R;
import cn.ahabox.adapter.BaseViewHolder;
import cn.ahabox.adapter.CommonAdapter;
import cn.ahabox.config.Config;

/**
 * Created by libo on 2016/5/18.
 *
 * 分享弹出窗
 */
public class SharePopupWindow extends PopupWindow{
    private Context context;
    private View view;
    private String[] shareWay = {"分享给朋友","分享到朋友圈"};
    private ListView listView;
    private IWXAPI iwxapi;
    private TextView tvCancel;

    public SharePopupWindow(Context context){
        this.context = context;
        view = ((Activity)context).getLayoutInflater().from(context).inflate(R.layout.share_popupwindow,null);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setBackgroundDrawable(new BitmapDrawable());
        setFocusable(true);
        setOutsideTouchable(true);
        setAnimationStyle(R.style.PopupAnimation);
        setContentView(view);

    }

    public void init(final String url, final String title, final String content) {
        iwxapi = WXAPIFactory.createWXAPI(context, Config.APP_ID, true);
        iwxapi.registerApp(Config.APP_ID);

        tvCancel = (TextView) view.findViewById(R.id.tv_popup_cancel);
        listView = (ListView) view.findViewById(R.id.lv_popup);
        CommonAdapter commonAdapter = new CommonAdapter(context, Arrays.asList(shareWay),R.layout.item_address_textview) {
            @Override
            public void convert(BaseViewHolder baseViewHolder, Object o) {
                TextView textView = baseViewHolder.getView(R.id.tv_popup_place);
                textView.setText((String)o);
            }
        };
        listView.setAdapter(commonAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                wechatShare(position,url,title,content);
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    /**
     * 微信分享
     *
     * @param flag 0,分享给朋友，1分享给朋友圈
     */
    private void wechatShare(int flag,String url,String title,String content) {
        if (!iwxapi.isWXAppInstalled()) {
            Toast.makeText(context,"您还未安装微信客户端",Toast.LENGTH_SHORT).show();
            return;
        }

        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = content;
        //分享链接图片资源
        Bitmap thumb = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        msg.setThumbImage(thumb);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = flag==0?SendMessageToWX.Req.WXSceneSession:SendMessageToWX.Req.WXSceneTimeline;
        iwxapi.sendReq(req);
    }

}
