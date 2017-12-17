package cn.ahabox.activity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.ahabox.config.Config;
import cn.ahabox.feiliwu_help.R;

/**
 * Created by libo on 2015/3/2.
 *
 * 加载webview页面
 */
public class WebViewActivity extends BaseActivity {
    @Bind(R.id.webview)
    WebView webview;
    private String loadUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        ButterKnife.bind(this);

        initView();
    }

    private void initView(){
        loadUrl = getIntent().getStringExtra(Config.WEBVIEW_URL);
        //设置可自由缩放网页
        webview.loadUrl(loadUrl);
        webview.getSettings().setSupportZoom(true);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setBuiltInZoomControls(true);
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //监听所有网页链接，对需要的链接过滤得到商品id并跳转到商品详情
                //测试服地址: http://demo.feiliwu.com.cn
                //这是服地址: http://www.feiliwu.com.cn
                if(null != url && url.contains("http://www.feiliwu.com.cn") && url.contains("products")){
                    int id = Integer.parseInt(url.substring(url.lastIndexOf("/") + 1));
                    Intent intent = new Intent(getApplicationContext(),ProductDetailActivity.class);
                    intent.putExtra(Config.PRODUCT_ID_KEY,id);
                    startActivity(intent);
                }else{  //不是商品详情就直接加载网页
                    view.loadUrl(url);
                }
                return true;
            }
        });
    }

}
