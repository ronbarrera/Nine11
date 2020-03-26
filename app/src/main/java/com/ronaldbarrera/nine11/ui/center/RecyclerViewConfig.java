package com.ronaldbarrera.nine11.ui.center;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.ronaldbarrera.nine11.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerViewConfig {
    private Context mContext;
    private CentersAdapter mCentersAdapter;

    public void setConfig(RecyclerView recyclerView, Context context, List<Center> centers, List<String> keys) {
        mContext = context;
        mCentersAdapter = new CentersAdapter(centers, keys);
        LinearLayoutManager layoutManager= new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mCentersAdapter);
    }

    class CenterItemView extends RecyclerView.ViewHolder  implements View.OnClickListener {
        @BindView(R.id.text_center_psap_name) TextView mPsapName;
        @BindView(R.id.text_center_address) TextView mAddress;
        private String key;
        private Center center;

        public CenterItemView(ViewGroup parent) {
            super(LayoutInflater.from(mContext).inflate(R.layout.center_list_item, parent, false));
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void bind(Center center, String key) {
            this.center = center;
            mPsapName.setText(center.getPsap_name());
            mAddress.setText(center.getFullAddress());
            this.key = key;
        }

        @Override
        public void onClick(View v) {
            Class destinatinClass = CenterMapActivity.class;
            Intent intentTostartLocationMapActivity = new Intent(mContext, destinatinClass);
            Gson gson = new Gson();
            intentTostartLocationMapActivity.putExtra("center", gson.toJson(center));
            mContext.startActivity(intentTostartLocationMapActivity);
        }
    }

    class CentersAdapter extends RecyclerView.Adapter<CenterItemView> {
        private List<Center> mCenterList;
        private List<String> mKeys;

        public CentersAdapter(List<Center> mCenterList, List<String> mKeys) {
            this.mCenterList = mCenterList;
            this.mKeys = mKeys;
        }

        @NonNull
        @Override
        public CenterItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new CenterItemView(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull CenterItemView holder, int position) {
            holder.bind(mCenterList.get(position), mKeys.get(position));
        }

        @Override
        public int getItemCount() {
            return mCenterList.size();
        }
    }
}
