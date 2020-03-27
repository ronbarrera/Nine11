package com.ronaldbarrera.nine11.ui.center;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.ronaldbarrera.nine11.utils.AppExecutors;
import com.ronaldbarrera.nine11.R;
import com.ronaldbarrera.nine11.database.AppDatabase;

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
        View view = LayoutInflater.from(mContext).inflate(R.layout.saved_center_list_item, parent, false);
        return new SavedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedViewHolder holder, int position) {
        holder.mTextViewPsapName.setText(mSavedCenters.get(position).getPsap_name());
        holder.mImageButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialAlertDialogBuilder(mContext)
                        .setTitle(mContext.getString(R.string.alert_delete_title))
                        .setMessage(mContext.getString(R.string.alert_delete_message))
                        .setPositiveButton(mContext.getString(R.string.alert_delete_positive), (dialog, which) -> {
                            AppDatabase mDb = AppDatabase.getInstance(mContext);
                            AppExecutors.getInstance().diskIO().execute(() -> mDb.centerDao().deleteCenter(mSavedCenters.get(position)));
                        })
                        .setNegativeButton(mContext.getString(R.string.alert_delete_negative), null)
                        .show();
            }
        });
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

     static class SavedViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_saved_center_psap_name)
        TextView mTextViewPsapName;

        @BindView(R.id.button_delete)
         ImageButton mImageButtonDelete;

        public SavedViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
     }
}
