package com.gwi.selfplatform.ui.activity.expand;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.GlobalSettings;
import com.gwi.selfplatform.common.utils.Logger;
import com.gwi.selfplatform.config.HospitalParams;
import com.gwi.selfplatform.ui.base.HospBaseActivity;

import java.util.List;
import java.util.Map;

public class MapAroundActivity extends HospBaseActivity implements OnGetPoiSearchResultListener, OnMapClickListener {

    private static final String TAG = MapAroundActivity.class.getSimpleName();

    MapView mMapView;
    BaiduMap mBaiduMap;
    private GeoCoder mGeoSearch;
    private String mCity;

    //POI search
    private PoiSearch mPoiSearch = null;

    private LatLng mHospitalLocation = null;

    private TextView popupText;

    private OnGetGeoCoderResultListener mOnGetGeoCoderResultListener = new OnGetGeoCoderResultListener() {

        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
            //Do nothing.
            Logger.d(TAG, "onGetReverseGeoCodeResult");
        }

        @Override
        public void onGetGeoCodeResult(GeoCodeResult result) {
            Logger.d(TAG, "onGetGeoCodeResult");
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                showToast("抱歉，未能找到结果");
                return;
            }
            mBaiduMap.clear();
            if (!hasLatngPos()) {
                mHospitalLocation = result.getLocation();
            }
            mBaiduMap.addOverlay(new MarkerOptions().position(mHospitalLocation)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding)));
            mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(mHospitalLocation));
            Logger.d(TAG, mHospitalLocation.toString());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_around);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        Bundle b = getIntent().getExtras();
//        if(b!=null&&b.containsKey("KEY_CITY")) {
//            mCity = b.getString("KEY_CITY");
//            Logger.d(TAG, mCity);
//        }else {
//            showToast("未获取到位置信息，请稍后再试！");
//            finish();
//        }

        initViews();
        initEvents();
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }


    @Override
    protected void onDestroy() {
        mMapView.onDestroy();
        mPoiSearch.destroy();
        mGeoSearch.destroy();

        mPoiSearch = null;
        mGeoSearch = null;
        mMapView = null;
        super.onDestroy();
    }

    @Override
    protected void initViews() {
        mCity = GlobalSettings.INSTANCE.getCityOfHospital();
        FrameLayout mapLayout = (FrameLayout) findViewById(R.id.bmapView);
        BaiduMapOptions mapOptions = new BaiduMapOptions();
        mapOptions.scaleControlEnabled(false); // 隐藏比例尺控件
        mapOptions.zoomControlsEnabled(false);//隐藏缩放按钮
        mMapView = new MapView(this, mapOptions);
        mMapView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mapLayout.addView(mMapView);
        mBaiduMap = mMapView.getMap();
        mGeoSearch = GeoCoder.newInstance();
        mPoiSearch = PoiSearch.newInstance();

        mGeoSearch.setOnGetGeoCodeResultListener(mOnGetGeoCoderResultListener);
        mGeoSearch.geocode(new GeoCodeOption().city(mCity).address(GlobalSettings.INSTANCE.getHospitalName()));

        mPoiSearch.setOnGetPoiSearchResultListener(this);
        mBaiduMap.setOnMapClickListener(this);
    }

    /**
     * 邵阳市第一人民医院  添加
     * 获取地图参数：坐标
     */
    private boolean hasLatngPos() {
        try {
            Map<String, String> params = GlobalSettings.INSTANCE.getHospitalParams();
            List<String> mapParams = HospitalParams.getFields(params.get(HospitalParams.CODE_MAP_NAVIGATION));
            if (mapParams.size() > 2) {
                mHospitalLocation = new LatLng(Double.valueOf(mapParams.get(2)), Double.valueOf(mapParams.get(3)));
                return true;
            }
        } catch (Exception e) {
            Logger.e(TAG, "handleMapNavParams", e);
        }
        return false;
    }

    @Override
    protected void initEvents() {

    }

    public void onClick(View v) {
        String key = "";
        int id = v.getId();
        if (id == R.id.map_around_drug_store) {
            key = "药店";
        } else if (id == R.id.map_around_bank) {
            key = "银行";
        } else if (id == R.id.map_around_hotel) {
            key = "酒店";
        } else if (id == R.id.map_around_gas_station) {
            key = "加油站";
        }
        Logger.d(TAG, mHospitalLocation.toString());
        mPoiSearch.searchNearby(new PoiNearbySearchOption()
                .location(mHospitalLocation)
                .radius(1500)
                .keyword(key));
//        mPoiSearch.searchInBound(new PoiBoundSearchOption()
//        .bound(new LatLngBounds.Builder().include(mHospitalLocation).build())
//        .keyword(key));
//       mPoiSearch.searchInCity(new PoiCitySearchOption()
//       .city("长沙")
//       .keyword(key));

    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult result) {
        if (result.error != SearchResult.ERRORNO.NO_ERROR) {
            showToast("抱歉，未找到结果");
        } else {
            if (result.getLocation() != null) {
                popupText = new TextView(MapAroundActivity.this);
                popupText.setBackgroundResource(R.drawable.popup);
                popupText.setTextColor(0xFF000000);
                popupText.setText(result.getName() + ": " + result.getAddress());
                mBaiduMap.showInfoWindow(new InfoWindow(popupText, result.getLocation(), 0));
            } else {
                showToast(result.getName() + ": " + result.getAddress());
            }
        }
    }

    @Override
    public void onGetPoiResult(PoiResult result) {
        if (result == null
                || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            showToast("抱歉，未找到结果");
            return;
        }
        Logger.i(TAG, mCity + "  onGetPoiResult");
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            mBaiduMap.clear();

            mBaiduMap.addOverlay(new MarkerOptions().position(mHospitalLocation)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding)));
            mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(mHospitalLocation));

            PoiOverlay overlay = new MyPoiOverlay(mBaiduMap);
            mBaiduMap.setOnMarkerClickListener(overlay);
            overlay.setData(result);
            overlay.addToMap();
            overlay.zoomToSpan();
            return;
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {

            // 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
            String strInfo = "在";
            for (CityInfo cityInfo : result.getSuggestCityList()) {
                strInfo += cityInfo.city;
                strInfo += ",";
            }
            strInfo += "找到结果";
            showToast(strInfo);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish(R.anim.push_right_in, R.anim.push_right_out);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private class MyPoiOverlay extends PoiOverlay {

        public MyPoiOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public boolean onPoiClick(int index) {
            super.onPoiClick(index);
            PoiInfo poi = getPoiResult().getAllPoi().get(index);
            // if (poi.hasCaterDetails) {
            mPoiSearch.searchPoiDetail((new PoiDetailSearchOption())
                    .poiUid(poi.uid));
            // }
            return true;
        }
    }

    @Override
    public void onMapClick(LatLng arg0) {
        mBaiduMap.hideInfoWindow();
    }

    @Override
    public boolean onMapPoiClick(MapPoi arg0) {
        return false;
    }

}
