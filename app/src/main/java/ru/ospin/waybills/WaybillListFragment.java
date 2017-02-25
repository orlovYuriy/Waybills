package ru.ospin.waybills;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.ospin.waybills.Adapters.WBAdapter;
import ru.ospin.waybills.HTTPoperations.DownloadWaybillsHTTP;

/**
 * Created by Yuriy on 14.12.2016.
 */

public class WaybillListFragment extends Fragment implements DownloadWaybillsHTTP.HTTPListenerDownloadWaybills {

    private View v;
    private RecyclerView rv;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private WBAdapter adapter;

    public static WaybillListFragment newInstance() {
        return new WaybillListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_waybill_list, container, false);

        rv = (RecyclerView) v.findViewById(R.id.recycler_view_wayBill);
        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.srl_wayBill);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                refreshItems();
            }
        });

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        initializeAdapter();

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        adapter.notifyDataSetChanged();

    }

    private void initializeAdapter() {
        adapter = new WBAdapter(Waybill.getItems());
        rv.setAdapter(adapter);
    }

    void refreshItems() {
        // Load items


        DownloadWaybillsHTTP.downloadWaybills(getActivity(), this);
    }

    @Override
    public void loadCompleteWaybills() {
        initializeAdapter();
        // Stop refresh animation
        mSwipeRefreshLayout.setRefreshing(false);
    }
}
