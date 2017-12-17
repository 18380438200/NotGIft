package cn.ahabox.activity;

import android.content.Intent;
import android.os.Bundle;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ahabox.feiliwu_help.R;
import cn.ahabox.config.Config;
import cn.ahabox.fragment.MyAddressFragment;
import cn.ahabox.model.AddressEntity;
import cn.ahabox.widget.CustomActionBar;

/**
 * 我送过的地址
 */
public class BuymyselfAddressActivity extends BaseActivity {
    public static AddressEntity addressEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buymyself_address);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        CustomActionBar customActionBar = (CustomActionBar) findViewById(R.id.myaddress_header);
        customActionBar.initStyle(CustomActionBar.HeaderStyle.DEFAULT_TITLE);
        customActionBar.setTitle(getResources().getString(R.string.title_myaddress));

        getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, new MyAddressFragment()).commit();
    }

    @OnClick(R.id.tv_myaddress_next)
    void onClick() {
        Intent intent = new Intent(getApplicationContext(), RecipientInfoActivity.class);
        intent.putExtra(Config.RECIPENTINFO_INTENT,0);
        startActivity(intent);
        finish();
    }

}
