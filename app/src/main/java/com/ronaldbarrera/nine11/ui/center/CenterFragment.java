package com.ronaldbarrera.nine11.ui.center;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.ronaldbarrera.nine11.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CenterFragment extends Fragment {

    @BindView(R.id.recyclerview_centers)
    RecyclerView mRecyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_center, container, false);
        ButterKnife.bind(this, root);


//        Center centerTest = new Center();
//        centerTest.setTitle("Public Safety Communication Manager");
//        centerTest.setLat(37.443688);
//        centerTest.setLng(-122.15071);
//        centerTest.setAddress("275 Forest Ave");
//        centerTest.setCity("Palo Alto");
//        centerTest.setState("CA");
//        centerTest.setZip("94301");
//        centerTest.setPhone("650-329-2114");
//        ImageButton test = root.findViewById(R.id.imageView);
//        test.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Class destinatinClass = CenterMapActivity.class;
//                Intent intentTostartLocationMapActivity = new Intent(getContext(), destinatinClass);
//                Gson gson = new Gson();
//                intentTostartLocationMapActivity.putExtra("center", gson.toJson(centerTest));
//                startActivity(intentTostartLocationMapActivity);
//
//            }
//        });

        new FirebaseDatabaseHelper().readCentersFirebase(new FirebaseDatabaseHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<Center> centers, List<String> keys) {
                new RecyclerViewConfig().setConfig(mRecyclerView, getContext(), centers, keys);
            }

            @Override
            public void DataIsInserted() {

            }

            @Override
            public void DataIsUpdated() {

            }

            @Override
            public void DataIsDeleted() {

            }
        });
        return root;
    }
}