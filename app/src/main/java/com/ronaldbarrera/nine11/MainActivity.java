package com.ronaldbarrera.nine11;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.FirebaseDatabase;
import com.ronaldbarrera.nine11.database.AppDatabase;
import com.ronaldbarrera.nine11.ui.center.CenterFragment;
import com.ronaldbarrera.nine11.ui.profile.ProfileFragment;
import com.ronaldbarrera.nine11.ui.saved.SavedFragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    // Constant for logging
    private static final String TAG = MainActivity.class.getSimpleName();

    final Fragment centerFragment = new CenterFragment();
    final Fragment savedFragment = new SavedFragment();
    final Fragment profileFragment = new ProfileFragment();
    final FragmentManager fragmentManager = getSupportFragmentManager();
    Fragment active = centerFragment;

    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        mDb = AppDatabase.getInstance(getApplicationContext());

//        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
//        fragmentManager.beginTransaction().add(R.id.nav_host_fragment, profileFragment, "3").hide(profileFragment).commit();
//        fragmentManager.beginTransaction().add(R.id.nav_host_fragment, savedFragment, "2").hide(savedFragment).commit();
//        fragmentManager.beginTransaction().add(R.id.nav_host_fragment,centerFragment, "1").commit();

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_center, R.id.navigation_saved, R.id.navigation_profile)
//                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);


        FirebaseDatabase.getInstance().setPersistenceEnabled(true);


        navView.setSelectedItemId(R.id.navigation_saved);

        //Set toolbar
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //setupViewModel();
    }
//    private void setupViewModel() {
//
//        AppExecutors.getInstance().diskIO().execute(new Runnable() {
//            @Override
//            public void run() {
//                // If no profile, create one
//                if(mDb.userDao().getRowCount() == 0 ) {
//                    UserEntry user = new UserEntry("", "", "", "", ",")
//                }
//
//            }
//        });
//
//
//        ProfileViewModel profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
//        profileViewModel.getUser().observe(this, new Observer<UserEntry>() {
//            @Override
//            public void onChanged(@Nullable UserEntry user) {
//                Log.d(TAG, "Updating user from LiveData in ViewModel");
////                Log.d(TAG, "user name = " + user.getPersonalName());
//
//                //mAdapter.setTasks(taskEntries);
//            }
//        });
//    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_center:
                    Log.d("MainActivity", "navigation_center case");
                    fragmentManager.beginTransaction().hide(active).show(centerFragment).commit();
                    active = centerFragment;
                    return true;

                case R.id.navigation_saved:
                    Log.d("MainActivity", "navigation_saved case");
                    fragmentManager.beginTransaction().hide(active).show(savedFragment).commit();
                    active = savedFragment;
                    return true;

                case R.id.navigation_profile:
                    Log.d("MainActivity", "navigation_profile case");
                    fragmentManager.beginTransaction().hide(active).show(profileFragment).commit();
                    active = profileFragment;
                    return true;
            }
            return false;
        }
    };
//    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
//        @Override
//        public boolean OnNavigationItemSelectedListener(@NonNull MenuItem item) {
//            Log.d("MainActivity", "onNavigationItemSelected called");
//            switch (item.getItemId()) {
//                case R.id.navigation_center:
//                    Log.d("MainActivity", "navigation_center case");
//
//                    fragmentManager.beginTransaction().hide(active).show(centerFragment).commit();
//                    active = centerFragment;
//                    break;
//
//
//                case R.id.navigation_saved:
//                    Log.d("MainActivity", "navigation_savedcase");
//
//                    fragmentManager.beginTransaction().hide(active).show(savedFragment).commit();
//                    active = savedFragment;
//                    break;
//
//                case R.id.navigation_profile:
//                    Log.d("MainActivity", "navigation_profile case");
//                    fragmentManager.beginTransaction().hide(active).show(profileFragment).commit();
//                    active = profileFragment;
//                    break;
//            }
//        }
//    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main_menu, menu);
//        return true;
//    }
}
