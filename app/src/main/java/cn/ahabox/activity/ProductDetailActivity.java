package cn.ahabox.activity;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.ahabox.adapter.JuheAdapter;
import cn.ahabox.adapter.ProductViewpagerAdapter;
import cn.ahabox.application.MyApp;
import cn.ahabox.config.App;
import cn.ahabox.config.Config;
import cn.ahabox.feiliwu_help.R;
import cn.ahabox.fragment.CommentFragment;
import cn.ahabox.fragment.DescriptFragment;
import cn.ahabox.fragment.PicDetailFragment;
import cn.ahabox.fragment.ServiceFragment;
import cn.ahabox.interfaces.CallBackimpl;
import cn.ahabox.model.ProductDetailEntity;
import cn.ahabox.utils.CollectionUtils;
import cn.ahabox.utils.StrUtils;
import cn.ahabox.widget.AdaptiveImageView;
import cn.ahabox.widget.BannerView;
import cn.ahabox.widget.CustomActionBar;
import cn.ahabox.widget.CustomBuyStatus;
import cn.ahabox.widget.ProgressVoice;
import cn.ahabox.widget.ShipTimeDialog;

/**
 * Created by libo on 2015/1/26.
 * 商品详情
 */
public class ProductDetailActivity extends BaseActivity {
    private RecyclerView horizonlvDetail;
    private RadioGroup rgDetail;
    private LinearLayout llDetailBottomline;
    private TextView tvDetailCollnum;
    private TextView tvDetailName;
    private TextView tvDetailPrice;
    private TextView tvAddexplain;
    private TextView tvDetailShiptime;
    private ImageView ivDetailDesigneravater;
    private TextView tvDetailDesingername;
    private TextView tvDetailDesignerintro;
    private LinearLayout llUnion;
    private RelativeLayout rlDetailPlayer;
    private ImageView cbDetailColl;
    private ImageButton cbProductdetailLove;
    private LinearLayout llProductdetailColl;
    private TextView tvAddtoShopcart;
    private AdaptiveImageView ivProductdetailShowpic;
    private RelativeLayout rlProductColl;
    private LinearLayout llProductChoosenum;
    private LinearLayout llProductDetailDesigner;
    private CustomActionBar customActionBar;
    /* 商品详情轮播介绍图 */
    private BannerView bannerView;
    private LinearLayout linearLayout;

    /** 所有点击事件控件 */
    private TextView tvDetailMore;
    private TextView tvDetailProductnum;
    private TextView tvReduce;
    private TextView tvPlus;
    private Button btnBuy;
    private LinearLayout llKefu;
    private LinearLayout llcoll;
    private Button btnDonetion;
    private Button btnContact;

    private FragmentManager fragmentManager;
    private PicDetailFragment detailFragmentone;
    private CommentFragment commentFragment;
    private DescriptFragment descriptFragment;
    private ServiceFragment serviceFragment;
    private final String BUY_DONATIONS = "donations";
    /**
     * 选择商品id
     */
    private int productId;

    /**
     * 特有服务数据
     */
    private ArrayList<String> servicePics = new ArrayList<>();
    /**
     * 商品描述数据
     */
    private ArrayList giftDesript = new ArrayList();
    private int productNum = 1;
    private ProductDetailEntity entity;
    private ProgressVoice progressVoice;
    private ShipTimeDialog shipTimeDialog;
    /**
     * 客服电话
     */
    private final String KEFU_PHONE = "4009696530";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        initView();
        initData();
        event();
    }

    private void initView() {
        productId = getIntent().getIntExtra(Config.PRODUCT_ID_KEY, 1);
        customActionBar = (CustomActionBar) findViewById(R.id.product_detail_header);
        customActionBar.initStyle(CustomActionBar.HeaderStyle.DEFAULT_TITLE);
        customActionBar.setTitle(getResources().getString(R.string.title_giftdetail));

        bindView();

        fragmentManager = getSupportFragmentManager();
        linearLayout = (LinearLayout) findViewById(R.id.ll_productdetail_banner);
        progressVoice = (ProgressVoice) findViewById(R.id.progress_voice);

        //图文详情，礼物描述，特有服务
        llDetailBottomline = (LinearLayout) findViewById(R.id.ll_detail_bottomline);
    }

    private void bindView(){
        horizonlvDetail = (RecyclerView) findViewById(R.id.horizonlv_detail);
        llDetailBottomline = (LinearLayout) findViewById(R.id.ll_detail_bottomline);
        tvDetailCollnum = (TextView) findViewById(R.id.tv_detail_collnum);
        tvDetailName = (TextView) findViewById(R.id.tv_detail_name);
        tvDetailPrice = (TextView) findViewById(R.id.tv_detail_price);
        tvAddexplain = (TextView) findViewById(R.id.tv_addexplain);
        tvDetailShiptime = (TextView) findViewById(R.id.tv_detail_shiptime);
        ivDetailDesigneravater = (ImageView) findViewById(R.id.iv_detail_designeravater);
        tvDetailDesingername = (TextView) findViewById(R.id.tv_detail_desingername);
        tvDetailDesignerintro = (TextView)findViewById(R.id.tv_detail_designerintro);
        llUnion = (LinearLayout) findViewById(R.id.ll_union);
        cbDetailColl = (ImageView) findViewById(R.id.cb_detail_coll);
        ivProductdetailShowpic = (AdaptiveImageView) findViewById(R.id.iv_productdetail_showpic);
        rlProductColl = (RelativeLayout) findViewById(R.id.rl_product_coll);
        llProductChoosenum = (LinearLayout) findViewById(R.id.ll_product_choosenum);
        llProductDetailDesigner = (LinearLayout) findViewById(R.id.ll_product_detail_designer);
        tvDetailMore = (TextView) findViewById(R.id.tv_detail_more);
        rlDetailPlayer = (RelativeLayout) findViewById(R.id.rl_detail_player);
        tvDetailProductnum = (TextView) findViewById(R.id.tv_detail_productnum);
        tvReduce = (TextView) findViewById(R.id.tv_detail_reduce);
        tvPlus = (TextView) findViewById(R.id.tv_detail_plus);

        rgDetail = (RadioGroup) findViewById(R.id.rg_detail);

        llProductdetailColl = (LinearLayout) findViewById(R.id.ll_productdetail_coll);
        cbProductdetailLove = (ImageButton) findViewById(R.id.iv_productdetail_love);
        btnBuy = (Button) findViewById(R.id.btn_detail_buy);
        llKefu = (LinearLayout) findViewById(R.id.ll_productdetail_kefu);
        llcoll = (LinearLayout) findViewById(R.id.ll_productdetail_coll);
        tvAddtoShopcart = (TextView) findViewById(R.id.tv_addto_shopcart);
        btnDonetion = (Button) findViewById(R.id.btn_detail_donations);
        btnContact = (Button) findViewById(R.id.btn_detail_contact);

        MyOnclickListner listner = new MyOnclickListner();
        tvDetailMore.setOnClickListener(listner);
        rlDetailPlayer.setOnClickListener(listner);
        tvReduce.setOnClickListener(listner);
        tvPlus.setOnClickListener(listner);
        btnBuy.setOnClickListener(listner);
        llKefu.setOnClickListener(listner);
        llcoll.setOnClickListener(listner);
        tvAddtoShopcart.setOnClickListener(listner);
        btnDonetion.setOnClickListener(listner);
        btnContact.setOnClickListener(listner);
    }

    private void initData() {
        dialogUtils.progressDialog();
        dataParser.setCallBack(getApplicationContext(), new CallBackimpl() {
            @Override
            public void callBack(JSONObject obj) {
                if (null != dialogUtils.getProgressDialog()) {
                    dialogUtils.getProgressDialog().dismiss();
                }
                entity = JSON.parseObject(obj.toString(), ProductDetailEntity.class);
                setData();
            }
        });
        dataParser.parseProductDetail(productId);

    }

    private void setData() {
        setBanner();
        setProductStatus();

        servicePics = (ArrayList<String>) entity.getSpecial_service();
        giftDesript = (ArrayList) entity.getIntro();

        //当图文详情数据加载完成，再切换到图文详情页面
        ((RadioButton) rgDetail.getChildAt(0)).setChecked(true);

        tvDetailCollnum.setText("" + entity.getFavorites());
        tvDetailName.setText(entity.getName());
        tvDetailPrice.setText(entity.getPrice());
        tvAddexplain.setText(entity.getAdded_explain());

        if (null != entity.getShip_time())
            tvDetailShiptime.setText((CharSequence) entity.getShip_time());

        //设计师信息展示
        if (null == entity.getDesigner_thumbnail()) {
            llProductDetailDesigner.setVisibility(View.GONE);
        } else {
            ImageLoader.getInstance().displayImage(entity.getDesigner_thumbnail(), ivDetailDesigneravater, MyApp.getInstance().options);
            tvDetailDesingername.setText(entity.getDesigner_name());
            tvDetailDesignerintro.setText(entity.getDesigner_describe());
        }

        progressVoice.init(entity.getVoice_url());  //语音播放准备

        //收藏功能
        CollectionUtils collectionUtils = new CollectionUtils(this, cbDetailColl, dialogUtils, entity.getFavorites(), entity.getIs_favorited());
        collectionUtils.showStatus(entity.getIs_favorited(), cbDetailColl);
        collectionUtils.setEvent(productId, tvDetailCollnum, rlProductColl);
        collectionUtils.gotoShopcart(llProductdetailColl);

        setJuhe();

        if (entity.getVoice_url().isEmpty()) {
            rlDetailPlayer.setVisibility(View.GONE);
        }

        shipTimeDialog = new ShipTimeDialog(ProductDetailActivity.this,entity,productId);
        shipTimeDialog.setNumChangeListener(new CallBackimpl() {    //保持商品数量改变的一致
            @Override
            public void callBack(int quantity) {
                productNum = quantity;
                tvDetailProductnum.setText("" + productNum);
            }
        });

    }

    /**
     * 展示图设置
     */
    private void setBanner() {
        List list = entity.getCover_pictures();
        if (list.size() != 1) {
            ProductViewpagerAdapter viewpagerAdapter = new ProductViewpagerAdapter(getApplicationContext(), list);
            bannerView = new BannerView(getApplicationContext(), list, viewpagerAdapter, R.layout.customviewpager);
            bannerView.linearLayout.setHorizontalGravity(Gravity.RIGHT);
            moveViewpager();
            bannerView.linearLayout.setPadding(0, 0, 60, 0);
            linearLayout.removeAllViews();
            linearLayout.addView(bannerView.bannerView);
        } else {
            ivProductdetailShowpic.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(entity.getCover_pictures().get(0), ivProductdetailShowpic, MyApp.getInstance().options);
        }

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
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 设置聚合商品数据
     */
    private void setJuhe() {
        if (!entity.getUnion_hash().isEmpty()) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            horizonlvDetail.setLayoutManager(linearLayoutManager);
            JuheAdapter juheAdapter = new JuheAdapter(getApplicationContext(), (ArrayList<ProductDetailEntity.UnionHashEntity>) entity.getUnion_hash());
            horizonlvDetail.setAdapter(juheAdapter);
            juheAdapter.setOnItemClickListner(new JuheAdapter.OnItemClickListner() {
                @Override
                public void onItemClick(View view, int position) {
                    try {
                        String newProductId = entity.getUnion_hash().get(position).getId();
                        if (!newProductId.equals("self")) {
                            Intent intent = new Intent(getApplicationContext(), ProductDetailActivity.class);
                            intent.putExtra(Config.PRODUCT_ID_KEY, Integer.parseInt(newProductId));
                            startActivity(intent);
                            finish();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            llUnion.setVisibility(View.GONE);
        }
    }

    private void setProductStatus() {
        CustomBuyStatus customBuyStatus = (CustomBuyStatus) findViewById(R.id.product_detail_buy);
        customBuyStatus.setVisibility(View.VISIBLE);
        if (entity.getIs_presale()) {             //如果是预售商品，根据预售时间来判断当前预售状态
            long currentTime = entity.getTime_now().getTime();
            long startTime = entity.getPresale_start().getTime();
            long endTime = entity.getPresale_end().getTime();
            if (startTime - currentTime > 0) {
                customBuyStatus.initStyle("presale");    //预售前
                customBuyStatus.setPresaleTime(StrUtils.getDateTime(String.valueOf(startTime)) + " 开始预售");
                llProductChoosenum.setVisibility(View.GONE);
            } else if (currentTime - startTime > 0 && currentTime - endTime < 0) {
                customBuyStatus.initStyle("presaleing");  //预售中
            } else if (currentTime - endTime > 0) {
                customBuyStatus.initStyle("presale_end");  //预售结束
                llProductChoosenum.setVisibility(View.GONE);
            }
        } else if (entity.getQuantity() == 0) {    //售罄
            customBuyStatus.initStyle("sold_out");
            llProductChoosenum.setVisibility(View.GONE);
        } else {                                 //普通商品
            customBuyStatus.initStyle(entity.getNature());
        }

    }

    @TargetApi(Build.VERSION_CODES.M)
    private void event() {
        rgDetail.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
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

    }

    private void showKefu(int layout, int contactId, int closeId) {
        final Dialog dialog = new Dialog(this, R.style.Dialog_Fullscreen);
        dialog.setContentView(layout);
        dialog.setCanceledOnTouchOutside(true);
        ImageButton ibContact = (ImageButton) dialog.findViewById(contactId);
        ImageButton ibClose = (ImageButton) dialog.findViewById(closeId);
        dialog.show();

        ibContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.CALL");
                intent.setData(Uri.parse("tel:" + KEFU_PHONE));
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

    class MyOnclickListner implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tv_detail_more:
                    Intent intent = new Intent(getApplicationContext(), DesignerDetailActivity.class);
                    intent.putExtra(Config.DESIGNER_ID_KEY, entity.getChild_category_id());
                    startActivity(intent);
                    break;
                case R.id.tv_detail_reduce:
                    if (productNum > 1) {
                        productNum--;
                        tvDetailProductnum.setText("" + productNum);
                    }
                    break;
                case R.id.tv_detail_plus:
                    if (productNum < entity.getQuantity()) {
                        productNum++;
                        tvDetailProductnum.setText("" + productNum);
                    } else {
                        Toast.makeText(getApplicationContext(), "超出库存量", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.ll_productdetail_kefu:
                    showKefu(R.layout.toast_kefu, R.id.ib_kefu_contact, R.id.ib_kefu_close);
                    break;
                case R.id.ll_productdetail_coll:
                    cbProductdetailLove.setBackgroundResource(R.mipmap.menu_bottom_love_pressed);
                    break;
                case R.id.btn_detail_donations:
                    App.BUY_WAY = App.BUY_DONATIONS;
                    App.gotoLogin(ProductDetailActivity.this, new CallBackimpl() {
                        @Override
                        public void confirmHandle() {
                            shipTimeDialog.buy(BUY_DONATIONS, WXpayActivity.class);
                        }
                    });
                    break;
                case R.id.btn_detail_contact:  //联系礼物管家
                    showKefu(R.layout.toast_contactbutler, R.id.ib_butler_contact, R.id.ib_butler_close);
                    break;
                case R.id.tv_addto_shopcart:
                    App.gotoLogin(ProductDetailActivity.this, new CallBackimpl() {
                        @Override
                        public void confirmHandle() {
                            shipTimeDialog.showshipTime(0,productNum);
                        }
                    });
                    break;
                case R.id.btn_detail_buy:
                    App.gotoLogin(ProductDetailActivity.this, new CallBackimpl() {
                        @Override
                        public void confirmHandle() {
                            shipTimeDialog.showshipTime(1,productNum);
                        }
                    });
                    break;
            }
        }
    }


    /**
     * 将其他fragment隐藏
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (null != detailFragmentone) transaction.hide(detailFragmentone);
        if (null != descriptFragment) transaction.hide(descriptFragment);
        if (null != commentFragment) transaction.hide(commentFragment);
        if (null != serviceFragment) transaction.hide(serviceFragment);
    }

    /**
     * 设置切换页面
     *
     * @param page
     */
    private void setFragment(int page) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        //fragment切换渐变效果
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        hideFragments(transaction);
        switch (page) {
            case 0:
                if (null == detailFragmentone) {
                    detailFragmentone = PicDetailFragment.newInstance(entity);
                    transaction.add(R.id.fl_picdetail, detailFragmentone);
                } else {
                    transaction.show(detailFragmentone);
                }
                break;
            case 1:
                if (null == descriptFragment) {
                    descriptFragment = DescriptFragment.newInstance(giftDesript);
                    transaction.add(R.id.fl_picdetail, descriptFragment);
                } else {
                    transaction.show(descriptFragment);
                }
                break;
            case 2:
                if (null == commentFragment) {
                    commentFragment = CommentFragment.newInstance(productId);
                    transaction.add(R.id.fl_picdetail, commentFragment);
                } else {
                    transaction.show(commentFragment);
                }
                break;
            case 3:
                if (null == serviceFragment) {
                    serviceFragment = ServiceFragment.newInstance(servicePics);
                    transaction.add(R.id.fl_picdetail, serviceFragment);
                } else {
                    transaction.show(serviceFragment);
                }
                break;
        }
        transaction.commit();
    }

}
