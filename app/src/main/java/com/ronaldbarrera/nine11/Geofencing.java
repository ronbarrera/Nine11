package com.ronaldbarrera.nine11;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.ronaldbarrera.nine11.ui.center.Center;

import java.util.ArrayList;
import java.util.List;

public class Geofencing {

    public static final String TAG = Geofencing.class.getSimpleName();

    //private GoogleApiClient mGoogleApiClient;
    private GeofencingClient mGeofencingClient;
    private Context mContext;
    private PendingIntent mGeofencePendingIntent;
    private List<Geofence> mGeofenceList;

    private static final long GEOFENCE_TIMEOUT = 24 * 60 * 60 * 1000; // 24 hours


    public  Geofencing(Context context) {
        mContext = context;
//        mGoogleApiClient = client;
        mGeofencePendingIntent = null;
        mGeofenceList = new ArrayList<>();
    }

    public void registerAllGeofences() {
//        if(mGoogleApiClient == null || !mGoogleApiClient.isConnected() || mGeofenceList == null | mGeofenceList.size() == 0)
//            return;

        Log.d(TAG, "onRegisterAllGeofences called");

            mGeofencingClient = LocationServices.getGeofencingClient(mContext);
            mGeofencingClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent())
                    .addOnSuccessListener((Activity) mContext, new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "registerAllGeofences onSuccess");
                        }
                    })
                    .addOnFailureListener((Activity) mContext, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "registerAllGeofences onFailure");

                        }
                    });
    }

    public void unRegisterAllGeofences() {
//        if(mGoogleApiClient == null || !mGoogleApiClient.isConnected())
//            return;

        mGeofencingClient.removeGeofences(getGeofencePendingIntent())
                .addOnSuccessListener((Activity) mContext, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "unRegisterAllGeofences onSuccess");
                    }
                })
                .addOnFailureListener((Activity) mContext, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "unRegisterAllGeofences onFailure");
                    }
                });
    }

    public void updateGeofencesList(List<Center> centers) {
        mGeofenceList = new ArrayList<>();

        for(Center center : centers) {
            String centerUID = center.getId();
            double centerLat = center.getLat();
            double centerLng = center.getLng();

            Geofence geofence = new Geofence.Builder()
                    .setRequestId(centerUID)
                    .setExpirationDuration(GEOFENCE_TIMEOUT)
                    .setCircularRegion(centerLat, centerLng, 5500)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                    .build();
            mGeofenceList.add(geofence);
        }
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(mGeofenceList);
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent() {
        if(mGeofencePendingIntent != null)
            return mGeofencePendingIntent;
        Intent intent = new Intent(mContext, GeofenceBroadcastReceiver.class);
        mGeofencePendingIntent = PendingIntent.getBroadcast(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return mGeofencePendingIntent;
    }

}
