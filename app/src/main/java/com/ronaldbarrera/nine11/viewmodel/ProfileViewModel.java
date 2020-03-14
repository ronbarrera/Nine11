package com.ronaldbarrera.nine11.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.ronaldbarrera.nine11.database.AppDatabase;
import com.ronaldbarrera.nine11.database.UserEntry;

public class ProfileViewModel extends AndroidViewModel {

    private static final String TAG = ProfileViewModel.class.getSimpleName();
    private LiveData<UserEntry> user;

    public ProfileViewModel(Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        Log.d(TAG, "Actively retrieving the user profile from the Database");
        user = database.userDao().getUser();
    }

    public LiveData<UserEntry> getUser() {return user;}
}

