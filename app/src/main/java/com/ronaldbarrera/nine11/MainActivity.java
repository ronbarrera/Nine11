package com.ronaldbarrera.nine11;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.FirebaseDatabase;
import com.ronaldbarrera.nine11.database.AppDatabase;
import com.ronaldbarrera.nine11.database.UserEntry;
import com.ronaldbarrera.nine11.utils.AppExecutors;
import com.ronaldbarrera.nine11.utils.MessageBuilder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_WIDGET = 0;
    private static final int PERMISSIONS_REQUEST_APP = 1;
    public static String ACTION_SEND_SMS = "send_sms_action";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        if(getIntent().getAction() != null && getIntent().getAction().equals(ACTION_SEND_SMS)) {
            checkPermissions();
        }

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

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

    public void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED
        && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)) {
                requestPermissions(new String[]{Manifest.permission.SEND_SMS, android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_WIDGET);
            } else {
                requestPermissions(new String[]{Manifest.permission.SEND_SMS, android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_WIDGET);
            }
        } else {
            new MessageBuilder(this).buildSMS(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_WIDGET: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    new MessageBuilder(this).buildSMS(true);
                } else {
                    // permission denied
                }
                return;
            }
            case PERMISSIONS_REQUEST_APP: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    new MessageBuilder(this).buildSMS(false);
                    Snackbar.make(findViewById(R.id.fragment_center_layout), getString(R.string.snackbar_sms_sent), Snackbar.LENGTH_SHORT).setAnchorView(findViewById(R.id.fab_sms)).show();

                } else {
                    // permission denied
                }
                return;
            }
        }
    }
}
