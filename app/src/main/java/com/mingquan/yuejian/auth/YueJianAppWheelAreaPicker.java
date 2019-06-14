package com.mingquan.yuejian.auth;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.aigestudio.wheelpicker.WheelPicker;
import com.google.gson.Gson;
import com.mingquan.yuejian.R;
import com.mingquan.yuejian.utils.YueJianAppTLog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class YueJianAppWheelAreaPicker extends LinearLayout implements YueJianAppIWheelAreaPicker {
    private static final float ITEM_TEXT_SIZE = 18;
    private static final String SELECTED_ITEM_COLOR = "#353535";
    private static final int PROVINCE_INITIAL_INDEX = 0;

    private Context mContext;

    private List<YueJianAppCountryBean> mCountryBeanList;
    private List<YueJianAppCountryBean.ProvinceBean> mProvinceBeanList;
    private List<String> mProvinceName, mCityName;

    private LayoutParams mLayoutParams;
    private WheelPicker mWPProvince, mWPCity, mWPArea;

    public YueJianAppWheelAreaPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayoutParams();
        initView(context);
        mCountryBeanList = getJsonDataFromAssets();
        obtainProvinceData();
        addListenerToWheelPicker();
    }

    @SuppressWarnings("unchecked")
    private List<YueJianAppCountryBean> getJsonDataFromAssets() {
        List<YueJianAppCountryBean> countryBeanList = new ArrayList<>();
        try {
            Gson gson = new Gson();
            JSONObject countryJsonObject = new JSONObject(getContext().getResources().getString(R.string.country_province_city));
            JSONArray countryJsonArray = countryJsonObject.getJSONArray("country");
            if (countryJsonArray.length() > 0) {
                for (int i = 0; i < countryJsonArray.length(); i++) {
                    YueJianAppTLog.info(countryJsonArray.getString(i));
                    YueJianAppCountryBean countryBean = gson.fromJson(countryJsonArray.getString(i), YueJianAppCountryBean.class);
                    YueJianAppTLog.info("countryBean:%s", countryBean.toString());
                    countryBeanList.add(countryBean);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return countryBeanList;
    }

    private void initLayoutParams() {
        mLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mLayoutParams.setMargins(5, 5, 5, 5);
        mLayoutParams.width = 0;
    }

    private void initView(Context context) {
        setOrientation(HORIZONTAL);
        mContext = context;
        mProvinceName = new ArrayList<>();
        mCityName = new ArrayList<>();

        mWPProvince = new WheelPicker(context);
        mWPCity = new WheelPicker(context);
        mWPArea = new WheelPicker(context);

        initWheelPicker(mWPProvince, 1);
        initWheelPicker(mWPCity, 1.5f);
        initWheelPicker(mWPArea, 1.5f);
    }

    private void initWheelPicker(WheelPicker wheelPicker, float weight) {
        mLayoutParams.weight = weight;
        wheelPicker.setItemTextSize(dip2px(mContext, ITEM_TEXT_SIZE));
        wheelPicker.setSelectedItemTextColor(Color.parseColor(SELECTED_ITEM_COLOR));
        wheelPicker.setCurved(true);
        wheelPicker.setLayoutParams(mLayoutParams);
        addView(wheelPicker);
    }

    private void obtainProvinceData() {
        for (YueJianAppCountryBean countryBean : mCountryBeanList) {
            mProvinceName.add(countryBean.getName());
        }
        mWPProvince.setData(mProvinceName);
        setCityAndAreaData(PROVINCE_INITIAL_INDEX);
    }

    private void addListenerToWheelPicker() {
        //监听省份的滑轮,根据省份的滑轮滑动的数据来设置市跟地区的滑轮数据
        mWPProvince.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
            @Override
            public void onItemSelected(WheelPicker picker, Object data, int position) {
                //获得该省所有城市的集合
                mProvinceBeanList = mCountryBeanList.get(position).getProvince();
                setCityAndAreaData(position);
            }
        });

        mWPCity.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
            @Override
            public void onItemSelected(WheelPicker picker, Object data, int position) {
                //获取城市对应的城区的名字
                mWPArea.setData(mProvinceBeanList.get(position).getCity());
            }
        });
    }

    private void setCityAndAreaData(int position) {
        //获得该省所有城市的集合
        mProvinceBeanList = mCountryBeanList.get(position).getProvince();
        //获取所有city的名字
        //重置先前的城市集合数据
        mCityName.clear();
        for (YueJianAppCountryBean.ProvinceBean provinceBean : mProvinceBeanList)
            mCityName.add(provinceBean.getName());
        mWPCity.setData(mCityName);
        mWPCity.setSelectedItemPosition(0);
        //获取第一个城市对应的城区的名字
        //重置先前的城区集合的数据
        mWPArea.setData(mProvinceBeanList.get(0).getCity());
        mWPArea.setSelectedItemPosition(0);
    }

    @Override
    public String getCountry() {
        return mCountryBeanList.get(mWPProvince.getCurrentItemPosition()).getName();
    }

    @Override
    public String getProvince() {
        return mProvinceBeanList.get(mWPCity.getCurrentItemPosition()).getName();
    }

    @Override
    public String getCity() {
        return mProvinceBeanList.get(mWPCity.getCurrentItemPosition()).getCity().get(mWPArea.getCurrentItemPosition());
    }

    @Override
    public void hideArea() {
        this.removeViewAt(2);
    }

    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
