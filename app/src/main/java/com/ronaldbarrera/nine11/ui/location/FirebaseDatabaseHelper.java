package com.ronaldbarrera.nine11.ui.location;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseDatabaseHelper {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mLocationsReference;
    private List<Location> locations = new ArrayList<>();

    public interface DataStatus {
        void DataIsLoaded(List<Location> locations, List<String> keys);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }

    public FirebaseDatabaseHelper() {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mLocationsReference = mFirebaseDatabase.getReference().child("locations");
    }

    public void readLocations(final DataStatus dataStatus) {
        mLocationsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                locations.clear();
                List<String> keys = new ArrayList<>();
                for(DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    Location location = keyNode.getValue(Location.class);
                    locations.add(location);
                }
                dataStatus.DataIsLoaded(locations, keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
