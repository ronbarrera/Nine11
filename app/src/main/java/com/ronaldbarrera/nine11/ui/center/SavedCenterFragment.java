package com.ronaldbarrera.nine11.ui.center;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.ronaldbarrera.nine11.AppExecutors;
import com.ronaldbarrera.nine11.Geofencing;
import com.ronaldbarrera.nine11.R;
import com.ronaldbarrera.nine11.database.AppDatabase;
import com.ronaldbarrera.nine11.viewmodel.CenterViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SavedCenterFragment extends Fragment {

    private CenterViewModel savedViewModel;
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


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        savedViewModel = new ViewModelProvider(this).get(CenterViewModel.class);
        View root = inflater.inflate(R.layout.fragment_saved_center, container, false);
        Log.d("SavedFragment", "onCreateView called");
        ButterKnife.bind(this, root);


        //LinearLayoutManager layoutManager= new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new SavedAdapter(mContext);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);

//        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
//            @Override
//            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//                return false;
//            }
//
//            // Called when a user swipes left or right on a ViewHolder
//            @Override
//            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
//                // Here is where you'll implement swipe to delete
//                AppExecutors.getInstance().diskIO().execute(new Runnable() {
//                    @Override
//                    public void run() {
//                        int position = viewHolder.getAdapterPosition();
//                        List<Center> centers = mAdapter.getSavedCenters();
//                        mDb.centerDao().deleteCenter(centers.get(position));
//                        Snackbar.make(mFragmentSavedLayout, "Center Deleted!", Snackbar.LENGTH_LONG).show();
//
//                    }
//                });
//            }
//        }).attachToRecyclerView(mRecyclerView);

        mDb = AppDatabase.getInstance(mContext);
        setupCenterViewModel();

        mIsNotificationEnabled = this.getActivity().getPreferences(Context.MODE_PRIVATE).getBoolean(getString(R.string.setting_enabled), false);
        switchNotification.setChecked(mIsNotificationEnabled);
        setupNotificationIcon();
        switchNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d(TAG, "this is onCheckedChanged");
                SharedPreferences.Editor editor = getActivity().getPreferences(Context.MODE_PRIVATE).edit();
                editor.putBoolean(getString(R.string.setting_enabled), isChecked);
                mIsNotificationEnabled = isChecked;
                editor.apply();
                setupNotificationIcon();

//                if(mIsNotificationEnabled) mGeofencing.registerAllGeofences();
//                else mGeofencing.unRegisterAllGeofences();
            }
        });


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