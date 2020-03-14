package com.ronaldbarrera.nine11.ui.center;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
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
//        mCentersReference.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                Center center = dataSnapshot.getValue(Center.class);
//                Log.d("FirebaseDatabaseHelper", "center title = " + center.getTitle());
//                centers.add(center);
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
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
