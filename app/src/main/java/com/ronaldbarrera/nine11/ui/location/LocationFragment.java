package com.ronaldbarrera.nine11.ui.location;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.ronaldbarrera.nine11.R;
import com.ronaldbarrera.nine11.ui.LocationMapActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LocationFragment extends Fragment {

    private LocationViewModel locationViewModel;

//    @BindView(R.id.recyclerview_locations)
//    RecyclerView mRecyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        locationViewModel = new ViewModelProvider(this).get(LocationViewModel.class);
        View root = inflater.inflate(R.layout.location_list_item, container, false);
        ButterKnife.bind(this, root);


        ImageButton test = root.findViewById(R.id.imageView);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Class destinatinClass = LocationMapActivity.class;
                Intent intentTostartLocationMapActivity = new Intent(getContext(), destinatinClass);
                startActivity(intentTostartLocationMapActivity);

            }
        });

//        final TextView textView = root.findViewById(R.id.text_home);
//        locationViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });



//        new FirebaseDatabaseHelper().readLocations(new FirebaseDatabaseHelper.DataStatus() {
//            @Override
//            public void DataIsLoaded(List<Location> locations, List<String> keys) {
//                new RecyclerViewConfig().setConfig(mRecyclerView, getContext(), locations, keys);
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