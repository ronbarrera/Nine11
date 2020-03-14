package com.ronaldbarrera.nine11.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ronaldbarrera.nine11.database.AppDatabase;
import com.ronaldbarrera.nine11.ui.center.Center;

import java.util.List;

public class CenterViewModel extends AndroidViewModel {

    private static final String TAG = CenterViewModel.class.getSimpleName();
    private LiveData<List<Center>> centers;

    public CenterViewModel(Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        Log.d(TAG, "Actively retrieving centers from Database");
        centers = database.centerDao().getAllCenters();

    }

    public LiveData<List<Center>> getCenters() {
        return centers;
    }
}