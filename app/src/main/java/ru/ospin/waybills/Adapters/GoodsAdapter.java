package ru.ospin.waybills.Adapters;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ru.ospin.waybills.GoodOfBill;
import ru.ospin.waybills.R;
import ru.ospin.waybills.Waybill;

/**
 * Created by Yuriy on 14.12.2016.
 */

public class GoodsAdapter extends RecyclerView.Adapter<GoodsAdapter.GoodViewHolder> {

    List<GoodOfBill> mGoods;

    public static class GoodViewHolder extends RecyclerView.ViewHolder {

        TextView mNumber;
        TextView mName;
        TextView mUnit;
        TextView mCount;
        GoodOfBill mGood;

        GoodViewHolder(View itemView) {
            super(itemView);

            mNumber = (TextView) itemView.findViewById(R.id.tVNumber);
            mName = (TextView) itemView.findViewById(R.id.tVName);
            mUnit = (TextView) itemView.findViewById(R.id.tVUnit);
            mCount = (TextView) itemView.findViewById(R.id.tVCount);

        }

        public void bindWaybill(GoodOfBill good) {
            this.mGood = good;
        }
    }

    public GoodsAdapter(List<GoodOfBill> mGoods){
        this.mGoods = mGoods;

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public GoodViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_good_of_bill, viewGroup, false);
        GoodViewHolder waybillViewHolder = new GoodViewHolder(v);
        return waybillViewHolder;
    }

    @Override
    public void onBindViewHolder(GoodViewHolder waybillViewHolder, final int position) {

        GoodOfBill goodOfBill = mGoods.get(position);

        waybillViewHolder.bindWaybill(goodOfBill);

        if (goodOfBill.getUnit().equals(Waybill.SERVICES)) {
            waybillViewHolder.mName.setTextSize(20);
            waybillViewHolder.mName.setPadding(0,16,0,0);
            waybillViewHolder.mName.setTypeface(null, Typeface.ITALIC);
        }
        else if (goodOfBill.getUnit().equals(Waybill.SERVICE)) {

            waybillViewHolder.mName.setTypeface(null, Typeface.ITALIC);
            waybillViewHolder.mNumber.setTypeface(null, Typeface.ITALIC);

        }
        else {
            waybillViewHolder.mUnit.setText(goodOfBill.getUnit());
        }

        waybillViewHolder.mNumber.setText(goodOfBill.getNumber());
        waybillViewHolder.mName.setText(goodOfBill.getName());

        waybillViewHolder.mCount.setText(goodOfBill.getCount());

    }

    @Override
    public int getItemCount() {
        return mGoods.size();
    }
}