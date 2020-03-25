package com.ronaldbarrera.nine11.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.lifecycle.ViewModelStoreOwner;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.ronaldbarrera.nine11.MainActivity;
import com.ronaldbarrera.nine11.R;
import com.ronaldbarrera.nine11.database.UserEntry;
import com.ronaldbarrera.nine11.ui.center.CenterFragment;
import com.ronaldbarrera.nine11.viewmodel.ProfileViewModel;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static java.security.AccessController.getContext;

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
                .addOnSuccessListener((Activity) mContext, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        sendSMS(location);
                    }
                });
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
            Log.d("MessageBuilder", "current location is not null geo = " + location.getLatitude() + ", " + location.getLongitude());
            double lat = location.getLatitude();
            double lng = location.getLongitude();
            String geoPoint = "https://www.google.com/maps/place/" + lat + "," + lng;
            sb.append("My Location: ").append(geoPoint);
        }

        Log.d("MessageBuilder" , "isShowAlert = " + isShowAlert);
        if(isShowAlert) {
            new MaterialAlertDialogBuilder(mContext)
                    .setTitle("Confirm?")
                    .setMessage("Send only in case of an emergency")
                    .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            smsManager.sendTextMessage(mPhoneNumber, null, sb.toString(), null, null);
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        } else {
            smsManager.sendTextMessage(mPhoneNumber, null, sb.toString(), null, null);

        }
    }
}
