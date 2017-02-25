package ru.ospin.waybills.HTTPoperations;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;


import cz.msebera.android.httpclient.Header;

/**
 * Created by Yuriy on 19.12.2016.
 */

public class SendCurrentLocationHTTP {

    // Используем AsyncHttpClient для обращения к базе по HTTP сервису
    // и отправки данных о геолокации пользователя
    public static void sendCurrentLocation(Service service, String longitude, String latitude) {

//
        SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(service);
        String savedIp = mPref.getString(DownloadWaybillsHTTP.SAVED_IP, "");
        String savedUser = mPref.getString(DownloadWaybillsHTTP.SAVED_USER, "");
        String savedPassword = mPref.getString(DownloadWaybillsHTTP.SAVED_PASSWORD, "");
        String savedKey = mPref.getString(DownloadWaybillsHTTP.SAVED_KEY, "");

        String mUrl = "http://MYIP/kuap/hs/med/currentlocation/KEY/LONGITUDE/LATITUDE";
        mUrl = mUrl.replaceFirst("MYIP", savedIp);
        mUrl = mUrl.replaceFirst("LONGITUDE", longitude);
        mUrl = mUrl.replaceFirst("LATITUDE", latitude);
        mUrl = mUrl.replaceFirst("KEY", savedKey);

        AsyncHttpClient client = new AsyncHttpClient();
        if (!savedUser.isEmpty())
            client.setBasicAuth(savedUser, savedPassword);
        client.get(mUrl, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }

            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // called when response HTTP status is "200 OK"

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)

            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }


}
