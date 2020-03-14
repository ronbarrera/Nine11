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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ronaldbarrera.nine11.R;
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

    @BindView(R.id.recyclerview_saved_centers)
    RecyclerView mRecyclerView;


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