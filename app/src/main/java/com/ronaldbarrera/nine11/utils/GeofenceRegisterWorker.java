package com.ronaldbarrera.nine11.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.LocationServices;
import com.ronaldbarrera.nine11.Geofencing;
import com.ronaldbarrera.nine11.MainActivity;
import com.ronaldbarrera.nine11.database.AppDatabase;
import com.ronaldbarrera.nine11.ui.center.Center;

import java.util.List;

public class GeofenceRegisterWorker extends Worker {

    public GeofenceRegisterWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {

        try {
            AppDatabase database = AppDatabase.getInstance(getApplicationContext());
            List<Center> centers = database.centerDao().getAllCentersList();

            Geofencing mGeofencing = new Geofencing(getApplicationContext());
            mGeofencing.updateGeofencesList(centers);
            mGeofencing.registerAllGeofences();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Result.success();
    }
}
