package ru.ospin.waybills;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import ru.ospin.waybills.Adapters.GoodsAdapter;
import ru.ospin.waybills.HTTPoperations.SendInfoShipTimeHTTP;

/**
 * Created by Yuriy on 16.12.2016.
 */

public class WaybillFragment extends Fragment implements View.OnClickListener, SendInfoShipTimeHTTP.HTTPListeneSendInfoShipTime{

    private static final String ARG_WB_ID = "waybill_id";
    private Waybill mWaybill;
    private RecyclerView rvt;

    TextView orderNumber;
    TextView customer;
    TextView customerAddress;
    TextView deliveryTime;
    TextView arrivalTime;
    TextView shipment;
    ImageButton imButBack;
    ImageButton imButShipment;
    ImageButton imButLocation;




    public static WaybillFragment newInstance(String waybillId) {
        Bundle args = new Bundle();
        args.putString(ARG_WB_ID, waybillId);
        WaybillFragment fragment = new WaybillFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        String waybillId = getArguments().getString(ARG_WB_ID);
        mWaybill = Waybill.getWaybill(waybillId);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_scrolling_waybill, parent, false);



        orderNumber = (TextView) v.findViewById(R.id.orderNumberS);
        customer = (TextView) v.findViewById(R.id.customerS);
        customerAddress = (TextView) v.findViewById(R.id.customerAddressS);
        deliveryTime = (TextView) v.findViewById(R.id.deliveryTimeS);
        arrivalTime = (TextView) v.findViewById(R.id.arrivalTimeS);
        shipment = (TextView) v.findViewById(R.id.shipmentS);
        imButBack = (ImageButton) v.findViewById(R.id.imButBack);
        imButShipment = (ImageButton) v.findViewById(R.id.imButShipment);
        imButLocation = (ImageButton) v.findViewById(R.id.imButLocation);

        orderNumber.setText(mWaybill.getOrderNumber());
        customer.setText(mWaybill.getCustomer());
        customerAddress.setText(mWaybill.getCustomerAddress());
        deliveryTime.setText(mWaybill.getDeliveryTime());
        arrivalTime.setText(mWaybill.getArrivalTime());
        shipment.setText(mWaybill.getShipment());

        rvt = (RecyclerView) v.findViewById(R.id.recycler_view_goods);


        imButBack.getBackground().setAlpha(0);
        imButBack.setOnClickListener(this);

        imButLocation.getBackground().setAlpha(0);
        imButLocation.setOnClickListener(this);

        imButShipment.getBackground().setAlpha(0);
        imButShipment.setOnClickListener(this);
        if (!mWaybill.getShipment().isEmpty()) {
            imButShipment.setClickable(false);
            imButShipment.setColorFilter(R.color.buttonPressedColorMenu);
        }


        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rvt.setLayoutManager(llm);
        rvt.setHasFixedSize(true);

        initializeAdapter();

        return v;
    }

    private void initializeAdapter() {
        GoodsAdapter adapter = new GoodsAdapter(mWaybill.getGoods());
        rvt.setAdapter(adapter);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId() /*to get clicked view id**/) {
            case R.id.imButBack:
                getActivity().finish();
                break;
            case R.id.imButShipment:

                showDilogShipment();
                break;
            case R.id.imButLocation:

                Intent intent = MapYandex.newIntent(v.getContext(), mWaybill.getOrderNumber());
                v.getContext().startActivity(intent);
                break;
            default:
                break;
        }
    }


    private void showDilogShipment(){

        AlertDialog.Builder ad = new AlertDialog.Builder(getActivity());
        ad.setTitle(getString(R.string.confirm_sipment));
        ad.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        sendConfirmShipment();
                        imButShipment.setClickable(false);
                        imButShipment.setColorFilter(R.color.buttonPressedColorMenu);
                        dialog.cancel();
                    }
                })
                .setNegativeButton(R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.cancel();
                            }
                        });

        AlertDialog alert = ad.create();
        alert.show();

    }

    @Override
    public void sendingComplete(String timeShipment) {
        mWaybill.setShipment(timeShipment);
        shipment.setText(timeShipment);
        Toast.makeText(getActivity(), R.string.confirm_time_shipment, Toast.LENGTH_SHORT).show();

    }

    private void sendConfirmShipment() {
        SendInfoShipTimeHTTP.sendInfoShipTime(this, mWaybill.getOrderNumber());

    }
}