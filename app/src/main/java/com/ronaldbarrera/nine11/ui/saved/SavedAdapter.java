package com.ronaldbarrera.nine11.ui.saved;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ronaldbarrera.nine11.R;
import com.ronaldbarrera.nine11.ui.center.Center;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SavedAdapter extends RecyclerView.Adapter<SavedAdapter.SavedViewHolder> {

    private Context mContext;
    private List<Center> mSavedCenters;

    public SavedAdapter(Context context) {
        mContext = context;
    }

    @Override
    public SavedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.saved_center_list_item, parent, false);
        return new SavedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedViewHolder holder, int position) {

        holder.mTitle.setText(mSavedCenters.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return (mSavedCenters == null ? 0 : mSavedCenters.size());
    }

    public List<Center> getSavedCenters() {return mSavedCenters;}

    public void setSavedCenters(List<Center> centers) {
        mSavedCenters = centers;
        notifyDataSetChanged();
    }

     class SavedViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_center_title)
        TextView mTitle;

        public SavedViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
     }
}
