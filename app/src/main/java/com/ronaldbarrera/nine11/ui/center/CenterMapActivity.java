package com.ronaldbarrera.nine11.ui.center;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.ronaldbarrera.nine11.AppExecutors;
import com.ronaldbarrera.nine11.R;
import com.ronaldbarrera.nine11.database.AppDatabase;
import com.ronaldbarrera.nine11.ui.center.Center;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CenterMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private AppDatabase mDb;
    private boolean mLocationPermissionGranted;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private Center center;
    private boolean isFavorite = false;


    @BindView(R.id.text_center_title)
    TextView mTitle;
    @BindView(R.id.text_center_address)
    TextView mAddress;
    @BindView(R.id.button_save)
    ImageButton mSave;

    @BindView(R.id.activity_center_layout)
    FrameLayout mActivityCenterLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center_map);
        ButterKnife.bind(this);
        mDb = AppDatabase.getInstance(getApplicationContext());


        Gson gson = new Gson();
        String strOjb = getIntent().getStringExtra("center");
        center = gson.fromJson(strOjb, Center.class);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mTitle.setText(center.getTitle());
        mAddress.setText(center.getFullAddress());
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng geoPoint = new LatLng(center.getLat(), center.getLng());


        // Instantiates a new CircleOptions object and defines the center and radius
        CircleOptions circleOptions = new CircleOptions()
                .center(geoPoint)
                .fillColor(0x40BB86FC)
                .strokeColor(Color.TRANSPARENT)
                .strokeWidth(2)
                .radius(5500); // In meters

        // Get back the mutable Circle
        Circle circle = mMap.addCircle(circleOptions);


        //LatLng scCamera = new LatLng(37.423688,-122.15071);

        getLocationPermission();

        // create marker
        MarkerOptions marker = new MarkerOptions().position(geoPoint).title(center.getTitle());

        // Changing marker icon
        //marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.my_marker_icon)));
        marker.icon(bitmapDescriptorFromVector(this, R.drawable.ic_shield));

        // adding marker
        googleMap.addMarker(marker);
        // Add a marker in Sydney and move the camera
        //mMap.addMarker(new MarkerOptions().position(geoPoint).title(center.getTitle()));
        mMap.addMarker(marker);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(geoPoint, 11));




    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private void getLocationPermission() {
        /*
         * Request center permission, so that we can get the center of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
            updateLocationUI();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    public void onBackAction(View view) {
        onBackPressed();
    }

    public void onSaveAction(View view) {
        if(isFavorite) {
            isFavorite = false;
            //unmarkAsFavorite();
            mSave.setImageDrawable(getDrawable(R.drawable.ic_favorite_border));
        } else {
            isFavorite = true;
            markAsFavorite();
            mSave.setImageDrawable(getDrawable(R.drawable.ic_favorite));
        }
    }

    private void markAsFavorite() {
        AppExecutors.getInstance().diskIO().execute(() -> { mDb.centerDao().insertCenter(center); });
        Snackbar.make(mActivityCenterLayout, "Center Added!", Snackbar.LENGTH_LONG).show();
    }
}
