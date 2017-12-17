package cn.ahabox.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ahabox.adapter.BaseViewHolder;
import cn.ahabox.adapter.CommonAdapter;
import cn.ahabox.adapter.ProductAdapter;
import cn.ahabox.config.Config;
import cn.ahabox.feiliwu_help.R;
import cn.ahabox.interfaces.CallBackimpl;
import cn.ahabox.model.DesignerDetailEntity;
import cn.ahabox.utils.DialogUtils;
import cn.ahabox.utils.SDcardTools;
import cn.ahabox.utils.StrUtils;
import cn.ahabox.widget.CustomActionBar;
import cn.ahabox.widget.FlowLayout;

/**
 * Created by libo on 2016/2/26.
 * 商品搜索结果
 */
public class SearchResultsActivity extends BaseActivity implements TextView.OnEditorActionListener {
    @Bind(R.id.rl_search_noproducts)
    RelativeLayout rlSearchNoproducts;
    @Bind(R.id.lv_search_record)
    ListView lvSearchRecord;
    @Bind(R.id.rl_search_recordbox)
    RelativeLayout rlSearchRecordbox;
    @Bind(R.id.gv_product)
    PullToRefreshGridView gvProduct;
    @Bind(R.id.fl_search_hot)
    FlowLayout flSearchHot;
    @Bind(R.id.rl_search_record)
    RelativeLayout rlSearchRecord;
    private CustomActionBar customActionBar;
    private EditText etSearchBox;
    private TextView headerSearch;
    private String recordFileName = "records.txt";
    /**
     * 搜索关键字
     */
    private String keyWords;
    private ArrayList<String> recordDatas = new ArrayList<>();
    private ArrayList<String> tempDatas;
    private CommonAdapter recordAdapter;
    private ProductAdapter productAdapter;
    private int currentPage = 1;
    private ArrayList datas = new ArrayList();
    private ArrayList<ArrayList<String>> keyWordsArray = new ArrayList<>();
    /**
     * 当前标签的组数，默认第一组
     */
    private int currentGroupNum = 0;
    /**
     * 标签长度总共组数
     */
    private int totalGroupNum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        ButterKnife.bind(this);

        initView();
        initKeyWords();
        event();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData(0);
    }

    private void initView() {
        customActionBar = (CustomActionBar) findViewById(R.id.search_header);
        customActionBar.initStyle(CustomActionBar.HeaderStyle.EDIT_TEXT);
        etSearchBox = (EditText) customActionBar.findViewById(R.id.et_header_search);
        etSearchBox.setOnEditorActionListener(this);
        headerSearch = (TextView) customActionBar.findViewById(R.id.tv_header_searchbtn);

        productAdapter = new ProductAdapter(this, datas, new DialogUtils(this));
        gvProduct.setAdapter(productAdapter.getAdapter());
        gvProduct.setMode(PullToRefreshBase.Mode.BOTH);
        gvProduct.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
                currentPage = 1;  //下拉刷新，重新从第一页开始加载
                initData(1);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
                initData(2);
            }
        });
    }

    /**
     * 获取热搜关键词
     */
    private void initKeyWords() {
        dataParser.setCallBack(getApplicationContext(), new CallBackimpl() {
            @Override
            public void callBack(JSONObject obj) {
                try {
                    JSONArray array = obj.getJSONArray("data");
                    totalGroupNum = array.length();   //标签总组数
                    for (int i = 0; i < array.length(); i++) {
                        JSONArray subArray = (JSONArray) array.get(i);
                        ArrayList<String> names = new ArrayList();
                        JSONObject subObj;
                        for (int j = 0; j < subArray.length(); j++) {
                            subObj = (JSONObject) subArray.get(j);
                            names.add(subObj.getString("name"));
                        }
                        keyWordsArray.add(names);
                    }
                    initLable(keyWordsArray.get(0));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        dataParser.parseKeyWords();
    }

    /**
     * 初始化热门搜索标签,以组为单位
     */
    private void initLable(ArrayList<String> names) {
        ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(30, 30, 10, 10);
        for (int i = 0; i < names.size(); i++) {
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.hot_search_tv, null);
            final TextView tvLable = (TextView) view.findViewById(R.id.tv_search_lable);
            tvLable.setBackgroundResource(R.drawable.hot_search_shape);
            tvLable.setTextColor(getResources().getColor(R.color.voicelist_textcolor));
            tvLable.setText(names.get(i));
            flSearchHot.addView(tvLable, layoutParams);
            tvLable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    keyWords = tvLable.getText().toString();
                    etSearchBox.setText(keyWords);
                    rlSearchRecordbox.setVisibility(View.GONE);
                    initData(0);
                }
            });
        }

        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.hot_search_tv, null);
        TextView tvNextGroup = (TextView) view.findViewById(R.id.tv_search_lable);
        tvNextGroup.setText(getResources().getString(R.string.change_group));
        tvNextGroup.setBackgroundResource(R.drawable.hot_searchnext_shape);
        tvNextGroup.setTextColor(getResources().getColor(R.color.white));
        flSearchHot.addView(tvNextGroup, layoutParams);
        tvNextGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flSearchHot.removeAllViews();
                currentGroupNum++;
                if (currentGroupNum > totalGroupNum - 1) {
                    currentGroupNum = 0;
                }
                initLable(keyWordsArray.get(currentGroupNum));
            }
        });
    }

    private void event() {
        recordDatas.addAll(SDcardTools.getStrArray(recordFileName));
        recordAdapter = new CommonAdapter(getApplicationContext(), recordDatas, R.layout.item_search_record) {
            @Override
            public void convert(BaseViewHolder baseViewHolder, Object o) {
                TextView textView = baseViewHolder.getView(R.id.tv_record_title);
                textView.setText((String) o);
            }
        };

        lvSearchRecord.setAdapter(recordAdapter);
        lvSearchRecord.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                etSearchBox.setText(recordDatas.get(position));
                rlSearchRecordbox.setVisibility(View.GONE);
                keyWords = recordDatas.get(position);
                //设置光标位置
                etSearchBox.setSelection(keyWords.length());
                initData(0);
            }
        });

        etSearchBox.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                searchBoxFocus();
            }
        });

        etSearchBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchBoxFocus();
            }
        });

        gvProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ProductDetailActivity.class);
                intent.putExtra(Config.PRODUCT_ID_KEY, ((DesignerDetailEntity.ProductsEntity) datas.get(position)).getId());
                startActivity(intent);
            }
        });
    }

    private void searchBoxFocus() {
        tempDatas = (ArrayList<String>) SDcardTools.getStrArray(recordFileName);
        rlSearchRecordbox.setVisibility(View.VISIBLE);
        if (!tempDatas.isEmpty()) {
            recordDatas.clear();
            recordDatas.addAll(tempDatas);
            recordAdapter.notifyDataSetChanged();
            rlSearchRecord.setVisibility(View.VISIBLE);
        } else {
            rlSearchRecord.setVisibility(View.GONE);
            gvProduct.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.tv_search_del, R.id.tv_header_searchbtn})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_search_del:
                SDcardTools.delFile(new File(Config.ROOT + recordFileName));
                Toast.makeText(getApplicationContext(), "清除成功", Toast.LENGTH_SHORT).show();
                rlSearchRecord.setVisibility(View.GONE);
                break;
            case R.id.tv_header_searchbtn:
                rlSearchRecordbox.setVisibility(View.GONE);
                keyWords = etSearchBox.getText().toString().trim();
                if (!TextUtils.isEmpty(keyWords)) {
                    initData(0);
                } else {
                    Toast.makeText(getApplicationContext(), "输入内容为空", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void initData(final int way) {
        if (StrUtils.isStr(keyWords)) {
            dialogUtils.progressDialog();

            if (way == 0) currentPage = 1;   //重新搜索数据将当前页初始为1

            dataParser.setCallBack(getApplicationContext(), new CallBackimpl() {
                @Override
                public void callBack(List datas, boolean isEnd) {
                    if (null != dialogUtils.getProgressDialog()) {
                        dialogUtils.getProgressDialog().dismiss();
                    }

                    switch (way) {
                        case 0:
                            productAdapter.clearData();
                            break;
                        case 1:
                            productAdapter.clearData();
                            gvProduct.onRefreshComplete();
                            break;
                        case 2:
                            gvProduct.onRefreshComplete();
                            break;
                    }
                    setProducts(datas);

                    if (isEnd) {
                        gvProduct.setMode(PullToRefreshBase.Mode.PULL_FROM_START);  //如果是最后一页，停止上拉加载功能
                    } else {
                        gvProduct.setMode(PullToRefreshBase.Mode.BOTH);
                    }

                }
            });
            dataParser.parseSearch(keyWords, currentPage++);
        }

    }

    private void setProducts(List datas) {
        //只要搜索过，就保存搜索记录
        SDcardTools.saveStrArray(keyWords, recordFileName);
        if (!datas.isEmpty()) {
            productAdapter.addAll(datas);
            gvProduct.setVisibility(View.VISIBLE);
            rlSearchNoproducts.setVisibility(View.GONE);
        } else {
            //无数据
            gvProduct.setVisibility(View.GONE);
            rlSearchNoproducts.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH)
            headerSearch.performClick();
        return true;
    }
}
