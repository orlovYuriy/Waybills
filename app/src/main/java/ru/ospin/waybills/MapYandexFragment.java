package ru.ospin.waybills;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.widget.LinearLayout;


/**
 * Created by Yuriy on 28.12.2016.
 */

public class MapYandexFragment extends Fragment implements MapYandex.BtnListener {

    private static final String MYF_WB_ID = "waybill_id";
    private static final String TAG = "BOOMBOOMTESTGPS";
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 100;
    private static final float LOCATION_DISTANCE = 2f;


    private Waybill mWaybill;
    private  String geoPosition;

    WebView webMap;
    View v;



    public static MapYandexFragment newInstance(String waybillId) {
        Bundle args = new Bundle();
        args.putString(MYF_WB_ID, waybillId);
        MapYandexFragment fragment = new MapYandexFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String waybillId = getArguments().getString(MYF_WB_ID);
        mWaybill = Waybill.getWaybill(waybillId);

        setRetainInstance(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ((LinearLayout)v.findViewById(R.id.webviewplace)).removeAllViews();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_map_yandex, container, false);

        ((LinearLayout)v.findViewById(R.id.webviewplace)).removeAllViewsInLayout();

        if (webMap == null) {
            webMap = new WebView(getActivity());
            webMap.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            webMap.getSettings().setJavaScriptEnabled(true);
            webMap.loadUrl("file:///android_asset/yandex_map/index.html");

        }

        ((LinearLayout) v.findViewById(R.id.webviewplace)).addView(webMap);

//        webMap = (WebView) v.findViewById(R.id.webMap);



        initializeLocationManager();
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }

        return v;
    }

    @Override
    public void BtnClick() {
        String geoPositionDeliveryAddress = "[{ type: 'wayPoint', point: "+geoPosition+" },'"+mWaybill.getCustomerAddress()+"']";
        webMap.loadUrl("javascript:calRoute("+geoPositionDeliveryAddress+")");

    }


    private class LocationListener implements android.location.LocationListener {
        Location mLastLocation;

        public LocationListener(String provider) {
            Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.e(TAG, "onLocationChanged: " + location);

            mLastLocation.set(location);
            geoPosition = "[" + mLastLocation.getLatitude() + "," + mLastLocation.getLongitude() + "]";

            webMap.loadUrl("javascript:addCurrent(" + geoPosition + ")");


        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.e(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.e(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.e(TAG, "onStatusChanged: " + provider);
        }
    }

    MapYandexFragment.LocationListener[] mLocationListeners = new MapYandexFragment.LocationListener[]{
            new MapYandexFragment.LocationListener(LocationManager.GPS_PROVIDER),
            new MapYandexFragment.LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listners, ignore", ex);
                }
            }
        }
    }

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getActivity().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }


}
