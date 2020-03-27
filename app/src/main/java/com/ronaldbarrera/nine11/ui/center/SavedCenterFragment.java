package com.ronaldbarrera.nine11.ui.center;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.ronaldbarrera.nine11.Geofencing;
import com.ronaldbarrera.nine11.R;
import com.ronaldbarrera.nine11.database.AppDatabase;
import com.ronaldbarrera.nine11.utils.GeofenceRegisterWorker;
import com.ronaldbarrera.nine11.viewmodel.CenterViewModel;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SavedCenterFragment extends Fragment {

    private static final String TAG = SavedCenterFragment.class.getSimpleName();
    private Context mContext;
    private SavedAdapter mAdapter;
    private AppDatabase mDb;
    private boolean mIsNotificationEnabled;
    private Geofencing mGeofencing;

    @BindView(R.id.recyclerview_saved_centers)
    RecyclerView mRecyclerView;

    @BindView(R.id.fragment_saved_layout)
    ConstraintLayout mFragmentSavedLayout;

    @BindView(R.id.text_no_saved)
    TextView mTextViewNoSaved;

    @BindView(R.id.switch_notification)
    Switch switchNotification;

    @BindView(R.id.text_switch_label)
    TextView swithTextLabel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_saved_center, container, false);
        ButterKnife.bind(this, root);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new SavedAdapter(mContext);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);

        mDb = AppDatabase.getInstance(mContext);
        setupCenterViewModel();

        mIsNotificationEnabled = this.getActivity().getPreferences(Context.MODE_PRIVATE).getBoolean(getString(R.string.setting_enabled), false);
        switchNotification.setChecked(mIsNotificationEnabled);
        setupNotificationIcon();
        switchNotification.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = getActivity().getPreferences(Context.MODE_PRIVATE).edit();
            editor.putBoolean(getString(R.string.setting_enabled), isChecked);
            mIsNotificationEnabled = isChecked;
            editor.apply();
            setupNotificationIcon();

            if(mIsNotificationEnabled) {
                // WorkManager to re-register geofences every 24 hours
                PeriodicWorkRequest registerGeofenceRequest = new PeriodicWorkRequest.Builder(GeofenceRegisterWorker.class, 24, TimeUnit.HOURS).build();
                WorkManager.getInstance(mContext).enqueue(registerGeofenceRequest);

            } else {
                mGeofencing.unRegisterAllGeofences();
                WorkManager.getInstance(mContext).cancelAllWork();
            }
        });
        mGeofencing = new Geofencing(mContext);

        return root;
    }

    private void setupNotificationIcon(){
        if(mIsNotificationEnabled)
            swithTextLabel.setCompoundDrawablesWithIntrinsicBounds(mContext.getDrawable(R.drawable.ic_notifications_active), null, null, null);
        else
            swithTextLabel.setCompoundDrawablesWithIntrinsicBounds(mContext.getDrawable(R.drawable.ic_notifications_off), null, null, null);
    }

    private void setupCenterViewModel() {
        CenterViewModel centerViewModel = new ViewModelProvider(this).get(CenterViewModel.class);
        centerViewModel.getCenters().observe(getViewLifecycleOwner(), new Observer<List<Center>>() {
            @Override
            public void onChanged(@Nullable List<Center> centers) {
                mAdapter.setSavedCenters(centers);
                mGeofencing.updateGeofencesList(centers);
                if(centers.size() == 0) {
                    mTextViewNoSaved.setVisibility(View.VISIBLE);
                    switchNotification.setChecked(false);
                    switchNotification.setEnabled(false);
                }
                else {
                    mTextViewNoSaved.setVisibility(View.INVISIBLE);
                    switchNotification.setEnabled(true);
                }
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }
}