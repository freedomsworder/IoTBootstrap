package edu.columbia.cs.androidiotcomissioner;

import android.net.nsd.NsdServiceInfo;
import android.net.wifi.p2p.WifiP2pDevice;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ZeroConfFragment extends Fragment {

    public static final String TAG = "ZeroConfFragment";

    private MainActivity callingActivity;

    private SwitchCompat mServerSwitch;
    private TextView mTextView;
    private RecyclerView mRecyclerView;
    private ZeroConfServiceAdaptor mServiceAdaptor;

    public static ZeroConfFragment newInstance(){
        return new ZeroConfFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callingActivity = (MainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_zeroconf, container, false);

        mServerSwitch = (SwitchCompat) rootView.findViewById(R.id.fragment_zeroconf_switch);
        mTextView = (TextView) rootView.findViewById(R.id.fragment_zeroconf_text);

        mServerSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mServerSwitch.isChecked()){
                    callingActivity.setServerOn();
                }
                else
                    callingActivity.setServerOff();
            }

        });
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.fragment_zeroconf_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()) {
            @Override
            public boolean canScrollVertically() {
                return true;
            }
        });
        mServiceAdaptor = new ZeroConfServiceAdaptor(callingActivity.getZeroConfServices());
        mRecyclerView.setAdapter(mServiceAdaptor);
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public class ZeroConfServiceAdaptor extends RecyclerView.Adapter<ZeroConfServiceHolder> {

        private List<NsdServiceInfo>  mZeroConfServices;

        public ZeroConfServiceAdaptor (List<NsdServiceInfo> services)
        {
            mZeroConfServices = services;
            Log.d(TAG, "Run ZeroAdaptor constructor");
        }

        @Override
        public ZeroConfServiceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View v = inflater.inflate(R.layout.list_item_service, parent, false);
            return new ZeroConfServiceHolder(v);
        }

        @Override
        public void onBindViewHolder(ZeroConfServiceHolder holder, int position) {
            NsdServiceInfo thisService = mZeroConfServices.get(position);
            holder.bindService(thisService);
        }

        @Override
        public int getItemCount() {
            return mZeroConfServices.size();
        }

    }

    public class ZeroConfServiceHolder extends RecyclerView.ViewHolder{
        TextView mServiceNameTextView;
        TextView mServiceAddressTextView;
        NsdServiceInfo mNsdServiceInfo;

        public ZeroConfServiceHolder(View itemView) {
            super(itemView);
            mServiceNameTextView = (TextView) itemView.findViewById(R.id.list_item_service_name);
            mServiceAddressTextView = (TextView) itemView.findViewById(R.id.list_item_service_address);
            /* TODO
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogFragment connectDialog = new ConnectDialogFragment();
                    connectDialog.show(getFragmentManager(),mDevice.deviceAddress);
                }
            });
            */
        }

        public void bindService(NsdServiceInfo service){
            mNsdServiceInfo = service;
            mServiceNameTextView.setText(service.getServiceName());
            if(service.getHost() != null)
                mServiceAddressTextView.setText(service.getHost().toString()+":"+service.getPort());
        }

    }
    // additional functions

    public void setTextView(String content){
        mTextView.setText(content);
    }

    public ZeroConfServiceAdaptor getServiceAdaptor(){
        return mServiceAdaptor;
    }

    public void updateView(){
        // mServiceAdaptor.notifyDataSetChanged();
    }
}
