package com.ronaldbarrera.nine11.utils;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.telephony.SmsManager;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.ronaldbarrera.nine11.R;
import com.ronaldbarrera.nine11.database.UserEntry;
import com.ronaldbarrera.nine11.viewmodel.ProfileViewModel;

public class MessageBuilder {

    private UserEntry currentUser;
    private Context mContext;
    private boolean isShowAlert;

    public MessageBuilder(Context context) {
        this.mContext = context;
        ProfileViewModel profileViewModel = new ViewModelProvider((ViewModelStoreOwner) mContext).get(ProfileViewModel.class);
        profileViewModel.getUser().observe((LifecycleOwner) mContext, user -> {
            currentUser = user;
        });
    }

    public void buildSMS(boolean isShowAlert) {
        this.isShowAlert = isShowAlert;
        getLocation();
    }

    public void getLocation() {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext);
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener((Activity) mContext, location -> sendSMS(location));
    }

    public void sendSMS(Location location) {
        String mPhoneNumber = "5102095955";
        SmsManager smsManager = SmsManager.getDefault();

        StringBuilder sb = new StringBuilder();
        sb.append("Nine11 Emergency SMS").append("\n");
        String name = currentUser.getPersonalName();
        String dob = currentUser.getDob();

        if(name != null && !name.equals(""))
            sb.append("Name: ").append(name).append("\n");
        if(dob != null && !dob.equals(""))
            sb.append("DOB: ").append(dob).append("\n");

        if (location != null) {
            // Logic to handle location object
            double lat = location.getLatitude();
            double lng = location.getLongitude();
            String geoPoint = "https://www.google.com/maps/place/" + lat + "," + lng;
            sb.append("My Location: ").append(geoPoint);
        }

        if(isShowAlert) {
            new MaterialAlertDialogBuilder(mContext)
                    .setTitle(mContext.getString(R.string.alert_message_title))
                    .setMessage(mContext.getString(R.string.alert_message_message))
                    .setPositiveButton(mContext.getString(R.string.alert_message_positive), (dialog, which) -> smsManager.sendTextMessage(mPhoneNumber, null, sb.toString(), null, null))
                    .setNegativeButton(mContext.getString(R.string.alert_message_negative), null)
                    .show();
        } else {
            smsManager.sendTextMessage(mPhoneNumber, null, sb.toString(), null, null);
        }
    }
}
