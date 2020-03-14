package com.ronaldbarrera.nine11;

import android.app.PendingIntent;
import android.content.Context;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;

import java.util.ArrayList;
import java.util.List;

public class Geofencing {
    private GoogleApiClient mGoogleApiClient;
    private Context mContext;
    private PendingIntent mGeofencePendingIntent;
    private List<Geofence> mGeofenceList;

    private static final long GEOFENCE_TIMEOUT = 24 * 60 * 60 * 1000; // 24 hours


    public  Geofencing(Context context, GoogleApiClient client) {
        mContext = context;
        mGoogleApiClient = client;
        mGeofencePendingIntent = null;
        mGeofenceList = new ArrayList<>();
    }

    public void updateGeofencesList() {
        mGeofenceList = new ArrayList<>();


    }
}
