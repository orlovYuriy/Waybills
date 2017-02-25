package ru.ospin.waybills;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


/**
 * Created by Yuriy on 27.12.2016.
 */

public class MapYandex extends AppCompatActivity implements View.OnClickListener{

    private static final String EXTRA_WB_ID = "com.ospin.waybills.MapYandex";
    private Button btnCreateRoute;
    private Fragment fragment;



    interface BtnListener {
        void BtnClick();
    }

    public static Intent newIntent(Context packageContext, String waybillId) {
        Intent intent = new Intent(packageContext, MapYandex.class);
        intent.putExtra(EXTRA_WB_ID, waybillId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_yandex);

        String waybillId = getIntent().getStringExtra(EXTRA_WB_ID);

        btnCreateRoute = (Button) findViewById(R.id.btnCreateRoute);
        btnCreateRoute.setOnClickListener(this);
        //btnCreateRoute.getBackground().setAlpha(100);

        FragmentManager fm = getSupportFragmentManager();
        fragment = fm.findFragmentById(R.id.container);
        if (fragment == null) {
            fragment = createFragment(waybillId);
            fm.beginTransaction().add(R.id.container, fragment).commit();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId() /*to get clicked view id**/) {
            case R.id.btnCreateRoute:
                ((BtnListener)fragment).BtnClick();
                break;
            default:
                break;
        }

    }

    public Fragment createFragment(String waybillId) {
        return MapYandexFragment.newInstance(waybillId);
    }
}
