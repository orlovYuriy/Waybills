package ru.ospin.waybills.Adapters;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import ru.ospin.waybills.R;
import ru.ospin.waybills.Waybill;
import ru.ospin.waybills.WaybillActivity;

/**
 * Created by Yuriy on 14.12.2016.
 */

public class WBAdapter extends RecyclerView.Adapter<WBAdapter.WaybillViewHolder> {

    List<Waybill> waybills;

    public static class WaybillViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView orderNumber;
        TextView customer;
        TextView customerAddress;
        TextView deliveryTime;
        TextView arrivalTime;
        TextView shipment;
        Waybill waybill;


        WaybillViewHolder(View itemView) {
            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.cv);
            orderNumber = (TextView) itemView.findViewById(R.id.orderNumberL);
            customer = (TextView) itemView.findViewById(R.id.customerL);
            customerAddress = (TextView) itemView.findViewById(R.id.customerAddressL);
            deliveryTime = (TextView) itemView.findViewById(R.id.deliveryTimeL);
            arrivalTime = (TextView) itemView.findViewById(R.id.arrivalTimeL);
            shipment = (TextView) itemView.findViewById(R.id.shipmentL);

        }

        public void bindWaybill(Waybill waybill) {
            this.waybill = waybill;
        }
    }

    public WBAdapter(List<Waybill> waybills){
        this.waybills = waybills;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public WaybillViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_waybill, viewGroup, false);
        WaybillViewHolder waybillViewHolder = new WaybillViewHolder(v);
        return waybillViewHolder;
    }

    @Override
    public void onBindViewHolder(WaybillViewHolder waybillViewHolder, final int position) {

        waybillViewHolder.bindWaybill(waybills.get(position));


        waybillViewHolder.orderNumber.setText(waybills.get(position).getOrderNumber());
        waybillViewHolder.customer.setText(waybills.get(position).getCustomer());
        waybillViewHolder.customerAddress.setText(waybills.get(position).getCustomerAddress());
        waybillViewHolder.deliveryTime.setText(waybills.get(position).getDeliveryTime());
        waybillViewHolder.arrivalTime.setText(waybills.get(position).getArrivalTime());
        waybillViewHolder.shipment.setText(waybills.get(position).getShipment());


        waybillViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //implement onClick
                Intent intent = WaybillActivity.newIntent(v.getContext(), waybills.get(position).getOrderNumber());
                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return waybills.size();
    }
}