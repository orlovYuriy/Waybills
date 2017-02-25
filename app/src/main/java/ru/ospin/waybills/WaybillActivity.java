package ru.ospin.waybills;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Yuriy on 17.12.2016.
 */

public class WaybillActivity extends AppCompatActivity {

    private static final String EXTRA_WB_ID = "com.ospin.waybills.WaybillActivity";

    public static Intent newIntent(Context packageContext, String waybillId) {
        Intent intent = new Intent(packageContext, WaybillActivity.class);
        intent.putExtra(EXTRA_WB_ID, waybillId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waybill);

        String waybillId = getIntent().getStringExtra(EXTRA_WB_ID);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainerWaybill);
        if (fragment == null) {
            fragment = createFragment(waybillId);
            fm.beginTransaction().add(R.id.fragmentContainerWaybill, fragment).commit();
        }

    }

    public Fragment createFragment(String waybillId) {
        return WaybillFragment.newInstance(waybillId);
    }

}
