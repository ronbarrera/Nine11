package com.ronaldbarrera.nine11.ui.center;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ronaldbarrera.nine11.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AllCenterFragment extends Fragment {

    @BindView(R.id.recyclerview_centers)
    RecyclerView mRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_all_center, container, false);
        ButterKnife.bind(this, root);


//        new FirebaseDatabaseHelper().readCentersFirebase(new FirebaseDatabaseHelper.DataStatus() {
//            @Override
//            public void DataIsLoaded(List<Center> centers, List<String> keys) {
//                new RecyclerViewConfig().setConfig(mRecyclerView, getContext(), centers, keys);
//            }
//
//            @Override
//            public void DataIsInserted() {
//
//            }
//
//            @Override
//            public void DataIsUpdated() {
//
//            }
//
//            @Override
//            public void DataIsDeleted() {
//
//            }
//        });

        return root;

    }
}
