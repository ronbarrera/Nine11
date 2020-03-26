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
import android.text.Html;
import android.text.Spanned;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.ronaldbarrera.nine11.AppExecutors;
import com.ronaldbarrera.nine11.R;
import com.ronaldbarrera.nine11.database.AppDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CenterMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private AppDatabase mDb;
    private boolean mLocationPermissionGranted;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private Center center;
    private boolean isSaved;

    @BindView(R.id.text_center_psap_name)
    TextView mPsapName;
    @BindView(R.id.text_center_name)
    TextView mName;
    @BindView(R.id.text_center_title)
    TextView mTitle;
    @BindView(R.id.text_center_address)
    TextView mAddress;
    @BindView(R.id.text_center_phone)
    TextView mPhone;

    @BindView(R.id.button_save)
    FloatingActionButton mSave;

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
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mPsapName.setText(center.getPsap_name());
        mName.setText(center.getName());
        mTitle.setText(center.getTitle());
        mAddress.setText(center.getFullAddress());
        mPhone.setText(center.getPhone());

        AppExecutors.getInstance().diskIO().execute(() -> {
            Center mDbCenter = mDb.centerDao().getCenterById(center.getId());
            runOnUiThread(() -> {
                isSaved = (mDbCenter != null);
                onSaveUI();
            });
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        getLocationPermission();
        mMap = googleMap;
        LatLng geoPoint = new LatLng(center.getLat(), center.getLng());

        // Instantiates a new CircleOptions object and defines the center and radius
        CircleOptions circleOptions = new CircleOptions()
                .center(geoPoint)
                .fillColor(0x40BB86FC)
                .strokeColor(Color.TRANSPARENT)
                .strokeWidth(2)
                .radius(5500); // In meters

        // add Circle
        mMap.addCircle(circleOptions);

        // create marker
        MarkerOptions marker = new MarkerOptions().position(geoPoint).title(center.getTitle());

        // Changing marker icon
        marker.icon(bitmapDescriptorFromVector(this, R.drawable.ic_shield));

        // adding marker
        googleMap.addMarker(marker);
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
        // Check and request permission for ACCESS_FINE_LOCATION
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
            e.printStackTrace();
        }
    }

    public void onBackAction(View view) {
        onBackPressed();
    }

    public void onSaveAction(View view) {
        isSaved = !isSaved;

        if(isSaved) {
            AppExecutors.getInstance().diskIO().execute(() -> { mDb.centerDao().insertCenter(center); });
            Snackbar.make(mActivityCenterLayout, getString(R.string.snackbar_center_added), Snackbar.LENGTH_LONG).show();
        } else {
            AppExecutors.getInstance().diskIO().execute(() -> { mDb.centerDao().deleteCenter(center); });
            Snackbar.make(mActivityCenterLayout, getString(R.string.snackbar_center_deleted), Snackbar.LENGTH_LONG).show();
        }
        onSaveUI();
    }

    public void onSaveUI() {
        Drawable saveIcon = getDrawable((isSaved ? R.drawable.ic_favorite : R.drawable.ic_favorite_border));
        mSave.setImageDrawable(saveIcon);
    }
}
