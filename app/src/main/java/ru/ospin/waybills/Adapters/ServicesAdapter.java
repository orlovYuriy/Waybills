package ru.ospin.waybills.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ru.ospin.waybills.R;
import ru.ospin.waybills.ServiceOfBill;

/**
 * Created by Yuriy on 03.01.2017.
 */

public class ServicesAdapter  extends RecyclerView.Adapter<ServicesAdapter.ServicViewHolder> {

    List<ServiceOfBill> mServices;

    public static class ServicViewHolder extends RecyclerView.ViewHolder {

        TextView mNumber;
        TextView mName;
        TextView mStatus;
        ServiceOfBill mServiceOfBill;

        ServicViewHolder(View itemView) {
            super(itemView);

            mNumber = (TextView) itemView.findViewById(R.id.serviceNumber);
            mName = (TextView) itemView.findViewById(R.id.serviceName);
            mStatus = (TextView) itemView.findViewById(R.id.serviceStatus);


        }

        public void bindWaybill(ServiceOfBill good) {
            this.mServiceOfBill = good;
        }
    }

    public ServicesAdapter(List<ServiceOfBill> mServices){
        this.mServices = mServices;

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public ServicesAdapter.ServicViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_service_of_bill, viewGroup, false);
        ServicesAdapter.ServicViewHolder servicViewHolder = new ServicesAdapter.ServicViewHolder(v);
        return servicViewHolder;
    }

    @Override
    public void onBindViewHolder(ServicesAdapter.ServicViewHolder servicViewHolder, final int position) {

        servicViewHolder.bindWaybill(mServices.get(position));

        servicViewHolder.mNumber.setText(mServices.get(position).getNumber());
        servicViewHolder.mName.setText(mServices.get(position).getName());
        servicViewHolder.mStatus.setText(mServices.get(position).getStatus());

    }

    @Override
    public int getItemCount() {
        return mServices.size();
    }
}
