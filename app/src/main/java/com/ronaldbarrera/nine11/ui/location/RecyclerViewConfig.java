package com.ronaldbarrera.nine11.ui.location;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ronaldbarrera.nine11.R;

import java.util.List;

public class RecyclerViewConfig {
    private Context mContext;
    private LocationsAdapter mLocationsAdapter;

    public void setConfig(RecyclerView recyclerView, Context context, List<Location> locations, List<String> keys) {
        mContext = context;
        mLocationsAdapter = new LocationsAdapter(locations, keys);
        LinearLayoutManager layoutManager= new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mLocationsAdapter);
    }

    class LocationItemView extends RecyclerView.ViewHolder {
        private TextView mTitle;

        private String key;

        public LocationItemView(ViewGroup parent) {
            super(LayoutInflater.from(mContext).inflate(R.layout.location_list_item, parent, false));

            mTitle = itemView.findViewById(R.id.text_location_title);
        }

        public void bind(Location location, String key) {
            mTitle.setText(location.getTitle());
            this.key = key;
        }

    }

    class LocationsAdapter extends RecyclerView.Adapter<LocationItemView> {
        private List<Location> mLocationList;
        private List<String> mKeys;

        public LocationsAdapter(List<Location> mLocationList, List<String> mKeys) {
            this.mLocationList = mLocationList;
            this.mKeys = mKeys;
        }


        @NonNull
        @Override
        public LocationItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new LocationItemView(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull LocationItemView holder, int position) {
            holder.bind(mLocationList.get(position), mKeys.get(position));
        }

        @Override
        public int getItemCount() {
            return mLocationList.size();
        }
    }
}
