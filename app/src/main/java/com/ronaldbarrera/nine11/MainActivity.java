package com.ronaldbarrera.nine11;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.database.FirebaseDatabase;
import com.ronaldbarrera.nine11.database.AppDatabase;
import com.ronaldbarrera.nine11.database.UserEntry;
import com.ronaldbarrera.nine11.utils.MessageBuilder;
import com.ronaldbarrera.nine11.viewmodel.ProfileViewModel;
import com.ronaldbarrera.nine11.widget.Nine11AppWidget;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        if(getIntent().getAction().equals(Nine11AppWidget.ACTION_SEND_SMS)) {
            checkPermissions();
        }


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_center, R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

       // navView.setSelectedItemId(R.id.navigation_profile);

        //mMessage = new MessageBuilder(this);

        setupUserProfile();
    }

    public void setupUserProfile() {
        AppDatabase mDb;
        mDb = AppDatabase.getInstance(this);

        AppExecutors.getInstance().diskIO().execute(() -> {
            // If no profile, create user's profile with empty data
            if(mDb.userDao().getRowCount() == 0 ) {
                mDb.userDao().insertUser(new UserEntry("", "", "", "", "", "", ""));
            }
        });
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


//    public void sendSMS() {
//
//        new MaterialAlertDialogBuilder(this)
//                .setTitle("Confirm?")
//                .setMessage("Send only in case of an emergency")
//                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        String mPhoneNumber = "5102095955";
//                        SmsManager smsManager = SmsManager.getDefault();
//                        smsManager.sendTextMessage(mPhoneNumber, null, mMessage.getMessageString(), null, null);
//                    }
//                })
//                .setNegativeButton("Cancel", null)
//                .show();
//    }

    public void checkPermissions() {

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED
        && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                requestPermissions(new String[]{Manifest.permission.SEND_SMS, android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST);
            } else {
                // No explanation needed; request the permission
                requestPermissions(new String[]{Manifest.permission.SEND_SMS, android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            new MessageBuilder(this).buildSMS(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    new MessageBuilder(this).buildSMS(true);
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }
}
