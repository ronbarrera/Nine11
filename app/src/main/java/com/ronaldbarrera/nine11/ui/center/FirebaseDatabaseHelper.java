package com.ronaldbarrera.nine11.ui.center;

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
    private DatabaseReference mCentersReference;
    private List<Center> centers = new ArrayList<>();

    public interface DataStatus {
        void DataIsLoaded(List<Center> centers, List<String> keys);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }

    public FirebaseDatabaseHelper() {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mCentersReference = mFirebaseDatabase.getReference().child("centers");
    }

    public void readCentersFirebase(final DataStatus dataStatus) {
        mCentersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                centers.clear();
                List<String> keys = new ArrayList<>();
                for(DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    Center center = keyNode.getValue(Center.class);
                    center.setId(keyNode.getKey());
                    centers.add(center);
                }
                dataStatus.DataIsLoaded(centers, keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
