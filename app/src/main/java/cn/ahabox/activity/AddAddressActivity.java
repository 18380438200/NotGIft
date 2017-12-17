package cn.ahabox.activity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TableRow;

import com.alibaba.fastjson.JSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ahabox.feiliwu_help.R;
import cn.ahabox.fragment.MyAddressFragment;
import cn.ahabox.interfaces.CallBackimpl;
import cn.ahabox.model.CityEntity;
import cn.ahabox.model.CountyEntity;
import cn.ahabox.model.ProvinceEntity;
import cn.ahabox.utils.StrUtils;
import cn.ahabox.widget.AddressPopupWindow;
import cn.ahabox.widget.CustomActionBar;

/**
 * Created by libo on 2015/1/27.
 * <p/>
 * 添加地址、编辑地址
 */
public class AddAddressActivity extends BaseActivity {
    @Bind(R.id.et_addadd_name)
    EditText etAddaddName;
    @Bind(R.id.et_addadd_phone)
    EditText etAddaddPhone;
    @Bind(R.id.et_addadd_province)
    EditText etAddaddProvince;
    @Bind(R.id.et_addadd_mayer)
    EditText etAddaddMayer;
    @Bind(R.id.et_addadd_county)
    EditText etAddaddCounty;
    @Bind(R.id.et_addadd_zipcode)
    EditText etAddaddZipcode;
    @Bind(R.id.et_addadd_address)
    EditText etAddaddAddress;
    @Bind(R.id.tr_add_city)
    TableRow trAddCity;
    @Bind(R.id.tr_add_county)
    TableRow trAddCounty;
    @Bind(R.id.tr_add_zipcode)
    TableRow trAddZipcode;
    private View parentView;
    private CustomActionBar customActionBar;
    /**
     * 添加地址或修改地址的标志:0表示添加地址，1表示修改地址
     */
    private int addOrEdit;
    private AddressPopupWindow popupWindow;
    public static CallBackimpl finishChoosePlace;
    private String putProviceCoce, putCityCode, putCountyCode, putZipCode;
    /** 所有省 */
    public static List<ProvinceEntity> provinceDatas;
    /** 所有市 */
    public static List<CityEntity> cityDatas;
    /** 所有县 */
    public static List<CountyEntity> countyDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentView = getLayoutInflater().inflate(R.layout.activity_add_address, null);
        setContentView(parentView);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        addOrEdit = getIntent().getIntExtra("addOredit", 0);
        customActionBar = (CustomActionBar) findViewById(R.id.addaddress_header);
        customActionBar.initStyle(CustomActionBar.HeaderStyle.DEFAULT_TITLE);
        parseArray();
        if (addOrEdit == 1) {
            customActionBar.setTitle(getResources().getString(R.string.title_edit_address));
            etAddaddName.setText(MyAddressFragment.editAddress.getName());
            etAddaddAddress.setText(MyAddressFragment.editAddress.getStreet());
            etAddaddZipcode.setText(MyAddressFragment.editAddress.getPostalcode());
            etAddaddPhone.setText(MyAddressFragment.editAddress.getPhone());
            putProviceCoce = MyAddressFragment.editAddress.getProvince();
            putCityCode = MyAddressFragment.editAddress.getCity();
            putCountyCode = MyAddressFragment.editAddress.getCounty();
            putZipCode = MyAddressFragment.editAddress.getPostalcode();
            trAddCity.setVisibility(View.VISIBLE);
            trAddCounty.setVisibility(View.VISIBLE);
            trAddZipcode.setVisibility(View.VISIBLE);
            showAddress();
        } else {
            customActionBar.setTitle(getResources().getString(R.string.title_add_address));
        }

        finishChoosePlace = new CallBackimpl() {
            @Override
            public void callBack(String... args) {
                trAddCity.setVisibility(View.VISIBLE);
                trAddCounty.setVisibility(View.VISIBLE);
                trAddZipcode.setVisibility(View.VISIBLE);
                putProviceCoce = args[0];
                putCityCode = args[1];
                putCountyCode = args[2];
                putZipCode = args[3];
                etAddaddZipcode.setText(args[3]);
                etAddaddProvince.setText(args[4]);
                etAddaddMayer.setText(args[5]);
                etAddaddCounty.setText(args[6]);
            }
        };

    }

    private void showAddress() {
        for (int i = 0; i < provinceDatas.size(); i++) {
            if (MyAddressFragment.editAddress.getProvince().equals(provinceDatas.get(i).getId()))
                etAddaddProvince.setText(provinceDatas.get(i).getText());
        }
        for (int i = 0; i < cityDatas.size(); i++) {
            if (MyAddressFragment.editAddress.getCity().equals(cityDatas.get(i).getId()))
                etAddaddMayer.setText(cityDatas.get(i).getText());
        }
        for (int i = 0; i < countyDatas.size(); i++) {
            if (MyAddressFragment.editAddress.getCounty().equals(countyDatas.get(i).getId()))
                etAddaddCounty.setText(countyDatas.get(i).getText());
        }
    }

    /**
     * 生成地址请求参数
     */
    private HashMap getParams() {
        HashMap<String, String> params = new HashMap();
        params.put("name", etAddaddName.getText().toString().trim());
        params.put("province", putProviceCoce);
        params.put("city", putCityCode);
        params.put("county", putCountyCode);
        params.put("street", etAddaddAddress.getText().toString().trim());
        params.put("postalcode", putZipCode);
        params.put("phone", etAddaddPhone.getText().toString().trim());
        return params;
    }

    @OnClick({R.id.tv_addadd_confirm, R.id.rl_addadd_province})
    void onClick(View view) {

        switch (view.getId()) {
            case R.id.tv_addadd_confirm:
                addressConfirm();
                break;
            case R.id.rl_addadd_province:
                popupWindow = new AddressPopupWindow(this);
                popupWindow.showAtLocation(parentView, Gravity.TOP, 0, 0);
                break;
        }

    }

    /**
     * 验证并提交地址参数
     */
    private void addressConfirm(){
        if(StrUtils.isArrStr(etAddaddName.getText().toString().trim(),putProviceCoce,putCityCode,putCountyCode,etAddaddAddress.getText().toString().trim()
                ,putZipCode,etAddaddPhone.getText().toString().trim())){
            //正则验证手机号，条件：1开头11位数字
            if(StrUtils.checkPhone(etAddaddPhone.getText().toString().trim())){
                if (addOrEdit == 0) {
                    postRequest("添加成功");
                    dataParser.parseAddAdd(getParams());
                } else {
                    postRequest("修改成功");
                    dataParser.parseEditAdd(getParams(), MyAddressFragment.editAddress.getId());
                }
            }else{
                showshortText("请输入正确的手机号");
            }
        }else{
            showshortText("请填写完整的地址信息");
        }
    }

    private void postRequest(final String tip) {
        dataParser.setCallBack(getApplicationContext(), new CallBackimpl() {
            @Override
            public void callBack(String str) {
                StrUtils.handleStatus(str, tip, getApplicationContext());
                MyAddressFragment.icallBack.confirmHandle();
                finish();
            }
        });
    }

    /**
     * 从资源文件中获取json字符串
     *
     * @return
     */
    private String getAddress() {
        StringBuilder sb = new StringBuilder();
        try {
            //从areas文件中获取字节输入流
            InputStream inputStream = getResources().openRawResource(R.raw.area);
            byte[] buffer = new byte[1024];
            while (inputStream.read(buffer) != -1) {
                sb.append(new String(buffer, "UTF-8"));
            }
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * 解析出所有省市县
     */
    private void parseArray() {
        try {
            JSONObject object = new JSONObject(getAddress());
            JSONArray provinceArray = object.getJSONArray("province");
            JSONArray cityArray = object.getJSONArray("city");
            JSONArray countyArray = object.getJSONArray("district");
            provinceDatas = JSON.parseArray(provinceArray.toString(), ProvinceEntity.class);
            cityDatas = JSON.parseArray(cityArray.toString(), CityEntity.class);
            countyDatas = JSON.parseArray(countyArray.toString(), CountyEntity.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
