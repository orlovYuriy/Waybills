package ru.ospin.waybills;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import ru.ospin.waybills.HTTPoperations.DownloadWaybillsHTTP;
import ru.ospin.waybills.Services.ServiceCurrentLocation;

public class WaybillsActivity extends AppCompatActivity {


    private AlertDialog.Builder mBuilder;
    private SharedPreferences mPref;
    private EditText mIP;
    private EditText mUser;
    private EditText mPassword;
    private TextView mKey;
    private Fragment fragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waybills);

        mBuilder = new AlertDialog.Builder(WaybillsActivity.this);

        // Достаем настройки из файла IP, User, Password
        mPref = PreferenceManager.getDefaultSharedPreferences(this);
        String savedIp = mPref.getString(DownloadWaybillsHTTP.SAVED_IP, "");
//        String savedUser = mPref.getString(DownloadWaybillsHTTP.SAVED_USER, "");
//        String savedPassword = mPref.getString(DownloadWaybillsHTTP.SAVED_PASSWORD, "");

        FragmentManager fm = getSupportFragmentManager();
        fragment = fm.findFragmentById(R.id.fragmentContainer);


        if (fragment == null) // иначе подключаемся к центральной базе и Загружаем заказы в Waybill если фрагмет еще не создан
        {
            fragment = createFragment();
            fm.beginTransaction().add(R.id.fragmentContainer, fragment).commit();
            DownloadWaybillsHTTP.downloadWaybills(this, (DownloadWaybillsHTTP.HTTPListenerDownloadWaybills) fragment);
        }



        if (savedIp.isEmpty()) // Если IP не задан открываем меню настроек
            openIpDialog();



        // Устанавливаем Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        ImageView favicon = (ImageView) findViewById(R.id.imVOsplog);

        if (Waybill.isStart) {

            Animation animation = AnimationUtils.loadAnimation(this, R.anim.rotate_icon);
            favicon.startAnimation(animation);
            Waybill.isStart = false;
        }
        else
            favicon.setVisibility(View.GONE);

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
    }

    public Fragment createFragment() {
        return WaybillListFragment.newInstance();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            openIpDialog();

            return true;
        }
        if (id == R.id.start_service){
            startService(new Intent(this, ServiceCurrentLocation.class));
            return true;
        }
        if (id == R.id.stop_service){
            stopService(new Intent(this, ServiceCurrentLocation.class));

//            Intent intent = MapYandex.newIntent(this, "123");
//            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openIpDialog() {

        LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final ViewGroup viewGroup = (ViewGroup) mInflater.inflate(R.layout.ip_dialog, null);

        mIP = (EditText) viewGroup.findViewById(R.id.input_ip);
        mUser = (EditText) viewGroup.findViewById(R.id.input_user);
        mPassword = (EditText) viewGroup.findViewById(R.id.input_password);
        mKey = (EditText) viewGroup.findViewById(R.id.input_key);

//        // Получаем тел. водителя для первой симкарты.
//        TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//        String mPhoneNumber = "0000000000";
//        try
//        {
//            mPhoneNumber =tMgr.getLine1Number();
//        }
//        catch(NullPointerException ex)
//        {
//        }
//        if(mPhoneNumber.equals("")){
//            mPhoneNumber = tMgr.getSimSerialNumber();
//        }
//        mKey.setText(mPhoneNumber);

        // Устанавливаем фильтр для IP адреса
        filtreIpAddress(mIP);
        // Загружаем из файла IP, User, Password, key
        loadText();

        // Создаем диалог настроек
        mBuilder.setView(viewGroup)
                .setCancelable(false)
                .setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                saveText();
                                dialog.cancel();
                            }
                        })
                .setNegativeButton(R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.cancel();

                            }
                        });

        AlertDialog alert = mBuilder.create();
        alert.show();

    }

    // Устанавливаем фильтр для IP адреса
    private void filtreIpAddress(EditText ipAddress) {

        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       android.text.Spanned dest, int dstart, int dend) {
                if (end > start) {
                    String destTxt = dest.toString();
                    String resultingTxt = destTxt.substring(0, dstart) + source.subSequence(start, end) + destTxt.substring(dend);
                    if (!resultingTxt.matches("^\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3})?)?)?)?)?)?")) {
                        return "";
                    } else {
                        String[] splits = resultingTxt.split("\\.");
                        for (int i = 0; i < splits.length; i++) {
                            if (Integer.valueOf(splits[i]) > 255) {
                                return "";
                            }
                        }
                    }
                }
                return null;
            }

        };
        ipAddress.setFilters(filters);
    }


    void saveText() {

        SharedPreferences.Editor ed = mPref.edit();
        ed.putString(DownloadWaybillsHTTP.SAVED_IP, mIP.getText().toString());
        ed.putString(DownloadWaybillsHTTP.SAVED_USER, mUser.getText().toString());
        ed.putString(DownloadWaybillsHTTP.SAVED_PASSWORD, mPassword.getText().toString());
        ed.putString(DownloadWaybillsHTTP.SAVED_KEY, mKey.getText().toString());
        ed.commit();

        // Подключаемся к центральной базе и Загружаем заказы в Waybill
        DownloadWaybillsHTTP.downloadWaybills(this, (DownloadWaybillsHTTP.HTTPListenerDownloadWaybills)fragment);

    }

    void loadText() {

        String savedIp = mPref.getString(DownloadWaybillsHTTP.SAVED_IP, "");
        String savedUser = mPref.getString(DownloadWaybillsHTTP.SAVED_USER, "");
        String savedPassword = mPref.getString(DownloadWaybillsHTTP.SAVED_PASSWORD, "");
        String savedKey = mPref.getString(DownloadWaybillsHTTP.SAVED_KEY, "");
        mIP.setText(savedIp);
        mUser.setText(savedUser);
        mPassword.setText(savedPassword);
        mKey.setText(savedKey);

    }
}
