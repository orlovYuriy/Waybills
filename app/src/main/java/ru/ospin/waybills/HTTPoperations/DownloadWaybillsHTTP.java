package ru.ospin.waybills.HTTPoperations;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import cz.msebera.android.httpclient.Header;
import ru.ospin.waybills.R;
import ru.ospin.waybills.Waybill;

/**
 * Created by Yuriy on 18.12.2016.
 */

public class DownloadWaybillsHTTP {

    public interface HTTPListenerDownloadWaybills {
        void loadCompleteWaybills();
    }


    public final static String SAVED_IP = "saved_ip";
    public final static String SAVED_USER = "saved_user";
    public final static String SAVED_PASSWORD = "saved_password";
    public final static String SAVED_KEY = "saved_key";
    public final static String FILE_SAVED = "file_saved";


    // Используем AsyncHttpClient для обращения к базе по HTTP сервису.
    // для загрузки Waybills
    public static void downloadWaybills(final Activity activity, final HTTPListenerDownloadWaybills httpListenerDownloadWaybills) {

//      SharedPreferences mPref = activity.getSharedPreferences(FILE_SAVED, Context.MODE_PRIVATE);
        SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(activity);
        String savedIp = mPref.getString(DownloadWaybillsHTTP.SAVED_IP, "");
        String savedUser = mPref.getString(DownloadWaybillsHTTP.SAVED_USER, "");
        String savedPassword = mPref.getString(DownloadWaybillsHTTP.SAVED_PASSWORD, "");
        String savedKey = mPref.getString(DownloadWaybillsHTTP.SAVED_KEY, "");


        String mUrl = "http://MYIP/kuap/hs/med/info/KEY";
        mUrl = mUrl.replaceFirst("MYIP", savedIp);
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
                String jsonString = new String(response, StandardCharsets.UTF_8);

                JSONObject jsonBody = null;
                try {
                    jsonBody = new JSONObject(jsonString);
                    Waybill.parseItems(jsonBody);

                    httpListenerDownloadWaybills.loadCompleteWaybills();
                   ImageView favicon = (ImageView)  activity.findViewById(R.id.imVOsplog);
                   favicon.clearAnimation();
                   favicon.setVisibility(View.GONE);


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Toast.makeText(activity, R.string.errorGetData, Toast.LENGTH_LONG).show();
                httpListenerDownloadWaybills.loadCompleteWaybills();
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }


}
