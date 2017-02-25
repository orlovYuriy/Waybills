package ru.ospin.waybills.HTTPoperations;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.nio.charset.StandardCharsets;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Yuriy on 19.12.2016.
 */

public class SendInfoShipTimeHTTP {

    public interface HTTPListeneSendInfoShipTime {
        void sendingComplete(String respons);
    }

    // Используем AsyncHttpClient для обращения к базе по HTTP сервису.
    // для отправки о отгрузки товара
    public static void sendInfoShipTime (final Fragment fragment, String orderNumber) {

        SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(fragment.getActivity());
        String savedIp = mPref.getString(DownloadWaybillsHTTP.SAVED_IP, "");
        String savedUser = mPref.getString(DownloadWaybillsHTTP.SAVED_USER, "");
        String savedPassword = mPref.getString(DownloadWaybillsHTTP.SAVED_PASSWORD, "");
        String savedKey = mPref.getString(DownloadWaybillsHTTP.SAVED_KEY, "");

        String mUrl = "http://MYIP/kuap/hs/med/shipment/KEY/ORDERNUMBER";
        mUrl = mUrl.replaceFirst("MYIP", savedIp);
        mUrl = mUrl.replaceFirst("ORDERNUMBER", orderNumber);
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
                String timeShipment = new String(response, StandardCharsets.UTF_8);
                ((HTTPListeneSendInfoShipTime)fragment).sendingComplete(timeShipment);

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
