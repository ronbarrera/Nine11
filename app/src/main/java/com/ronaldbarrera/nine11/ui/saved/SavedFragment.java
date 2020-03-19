package com.ronaldbarrera.nine11.ui.saved;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.ronaldbarrera.nine11.AppExecutors;
import com.ronaldbarrera.nine11.R;
import com.ronaldbarrera.nine11.database.AppDatabase;
import com.ronaldbarrera.nine11.ui.center.Center;
import com.ronaldbarrera.nine11.viewmodel.CenterViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SavedFragment extends Fragment {

    private CenterViewModel savedViewModel;
    private static final String TAG = SavedFragment.class.getSimpleName();
    private Context mContext;
    private SavedAdapter mAdapter;
    private AppDatabase mDb;

    @BindView(R.id.recyclerview_saved_centers)
    RecyclerView mRecyclerView;

    @BindView(R.id.fragment_saved_layout)
    ConstraintLayout mFragmentSavedLayout;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        savedViewModel = new ViewModelProvider(this).get(CenterViewModel.class);
        View root = inflater.inflate(R.layout.fragment_saved, container, false);
        Log.d("SavedFragment", "onCreateView called");
        ButterKnife.bind(this, root);


        LinearLayoutManager layoutManager= new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new SavedAdapter(mContext);
        mRecyclerView.setAdapter(mAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // Here is where you'll implement swipe to delete
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        int position = viewHolder.getAdapterPosition();
                        List<Center> centers = mAdapter.getSavedCenters();
                        mDb.centerDao().deleteCenter(centers.get(position));
                        Snackbar.make(mFragmentSavedLayout, "Center Deleted!", Snackbar.LENGTH_LONG).show();

                    }
                });
            }
        }).attachToRecyclerView(mRecyclerView);

        mDb = AppDatabase.getInstance(mContext);
        setupCenterViewModel();

        return root;
    }

    private void setupCenterViewModel() {
        CenterViewModel centerViewModel = new ViewModelProvider(this).get(CenterViewModel.class);
        centerViewModel.getCenters().observe(getViewLifecycleOwner(), new Observer<List<Center>>() {
            @Override
            public void onChanged(@Nullable List<Center> centers) {
                Log.d(TAG, "Updating list of centers from LiveData in ViewModel");
                Log.d(TAG, "Size of centers = " + centers.size());
                mAdapter.setSavedCenters(centers);
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }
}