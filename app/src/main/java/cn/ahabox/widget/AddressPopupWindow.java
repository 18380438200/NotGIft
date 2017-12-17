package cn.ahabox.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.ahabox.feiliwu_help.R;
import cn.ahabox.activity.AddAddressActivity;
import cn.ahabox.adapter.BaseViewHolder;
import cn.ahabox.adapter.CommonAdapter;
import cn.ahabox.model.CityEntity;
import cn.ahabox.model.CountyEntity;
import cn.ahabox.model.ProvinceEntity;

/**
 * Created by libo on 2016/3/23.
 *
 * 地址弹出框选择
 */
public class AddressPopupWindow extends PopupWindow{
    private View view;
    private Context context;
    private ListView listView;
    /** 当前省下显示市 */
    private List<CityEntity> currentCitiesDatas = new ArrayList<>();
    /** 当前市下显示县 */
    private List<CountyEntity> currentCountiesDatas = new ArrayList<>();
    /** 省截取2位代码 */
    private String provinceChargeCode;
    /** 市截取2位代码 */
    private String citiesChargeCode;
    /** 最终确认的地址提交参数 */
    private String saveProvinceCode,saveCityCode,saveCountyCode,saveZipCode;
    private String saveProvinceName,saveCityName,saveCountyName;

    public AddressPopupWindow(Context context){
        this.context = context;
        view = ((Activity)context).getLayoutInflater().from(context).inflate(R.layout.address_popupwindow,null);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setBackgroundDrawable(new BitmapDrawable());
        setFocusable(true);
        setOutsideTouchable(true);
        setAnimationStyle(R.style.PopupAnimation);
        setContentView(view);

        init();
    }

    private void init() {

        listView = (ListView) view.findViewById(R.id.lv_popup_add);
        CommonAdapter adapter = new CommonAdapter(context,AddAddressActivity.provinceDatas,R.layout.item_address_textview) {
            @Override
            public void convert(BaseViewHolder baseViewHolder, Object o) {
                ProvinceEntity entity = (ProvinceEntity)o;
                TextView tvItem = baseViewHolder.getView(R.id.tv_popup_place);
                tvItem.setText(entity.getText());
            }
        };
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                saveProvinceCode = AddAddressActivity.provinceDatas.get(position).getId();
                saveProvinceName = AddAddressActivity.provinceDatas.get(position).getText();
                provinceChargeCode = saveProvinceCode.substring(0, 2);
                String cityCode;
                for (int i = 0; i < AddAddressActivity.cityDatas.size(); i++) {
                    cityCode = AddAddressActivity.cityDatas.get(i).getId();
                    if (provinceChargeCode.equals(cityCode.substring(0, 2))) {
                        currentCitiesDatas.add(AddAddressActivity.cityDatas.get(i));
                    }
                }
                selectCites();
            }
        });

        view.findViewById(R.id.tv_popup_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                view.clearAnimation();
            }
        });
    }

    /**
     * 根据当前省选择市
     */
    private void selectCites(){
        CommonAdapter adapter = new CommonAdapter(context,currentCitiesDatas,R.layout.item_address_textview) {
            @Override
            public void convert(BaseViewHolder baseViewHolder, Object o) {
                CityEntity entity = (CityEntity)o;
                TextView tvItem = baseViewHolder.getView(R.id.tv_popup_place);
                tvItem.setText(entity.getText());
            }
        };
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                saveCityCode = currentCitiesDatas.get(position).getId();
                saveCityName = currentCitiesDatas.get(position).getText();
                citiesChargeCode = saveCityCode.substring(2, 4);
                String countyCode;
                for (int i = 0; i < AddAddressActivity.countyDatas.size(); i++) {
                    countyCode = AddAddressActivity.countyDatas.get(i).getId();
                    if (countyCode.substring(0, 2).equals(provinceChargeCode) && countyCode.substring(2,4).equals(citiesChargeCode)) {
                        currentCountiesDatas.add(AddAddressActivity.countyDatas.get(i));
                    }
                }
                selectCounties();
            }
        });
    }

    /**
     * 根据当前市选择县
     */
    private void selectCounties(){
        CommonAdapter adapter = new CommonAdapter(context,currentCountiesDatas,R.layout.item_address_textview) {
            @Override
            public void convert(BaseViewHolder baseViewHolder, Object o) {
                CountyEntity entity = (CountyEntity)o;
                TextView tvItem = baseViewHolder.getView(R.id.tv_popup_place);
                tvItem.setText(entity.getText());
            }
        };
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                saveCountyCode = currentCountiesDatas.get(position).getId();
                saveCountyName = currentCountiesDatas.get(position).getText();
                saveZipCode = currentCountiesDatas.get(position).getZipcode();
                AddAddressActivity.finishChoosePlace.callBack(saveProvinceCode,saveCityCode,saveCountyCode,saveZipCode,saveProvinceName,saveCityName,saveCountyName);

                dismiss();
                view.clearAnimation();
            }
        });
    }

}
