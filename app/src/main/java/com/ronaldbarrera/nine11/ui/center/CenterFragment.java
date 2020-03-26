package com.ronaldbarrera.nine11.ui.center;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.ronaldbarrera.nine11.R;
import com.ronaldbarrera.nine11.utils.MessageBuilder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CenterFragment extends Fragment {

    private static final int PERMISSIONS_REQUEST_APP = 1;
    private Context mContext;

    @BindView(R.id.fragment_center_layout)
    CoordinatorLayout mLayout;

    @BindView(R.id.fab_sms)
    FloatingActionButton mSMS;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_center, container, false);
        ButterKnife.bind(this, root);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getContext(), getChildFragmentManager());
        ViewPager viewPager = root.findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = root.findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        mSMS.setOnClickListener(v -> Snackbar.make(mLayout, getString(R.string.snackbar_single_click), Snackbar.LENGTH_SHORT).setAnchorView(mSMS).show());

        mSMS.setOnLongClickListener(v -> {
            checkPermissions();
            return true;
        });
        return root;
    }

    // Check and request permission for SEND_SMS and ACCESS_FINE_LOCATION
    public void checkPermissions() {

        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.SEND_SMS)) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.SEND_SMS, android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_APP);
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.SEND_SMS, android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_APP);
            }
        } else {
            new MessageBuilder(mContext).buildSMS(false);
            Snackbar.make(mLayout, getString(R.string.snackbar_sms_sent), Snackbar.LENGTH_SHORT).setAnchorView(mSMS).show();
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }
}