package com.ronaldbarrera.nine11.ui.profile;

import android.graphics.drawable.Drawable;
import android.icu.text.SimpleDateFormat;
import android.icu.util.TimeZone;
import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.ronaldbarrera.nine11.R;

import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileFragment extends Fragment {

    @BindView(R.id.button_edit)
    FloatingActionButton buttonEdit;

    @BindView(R.id.button_change_photo)
    Button buttonChangePhoto;

    @BindView(R.id.edittext_personal_name) TextInputEditText editTextPersonalName;
    @BindView(R.id.edittext_personal_phone) TextInputEditText editTextPersonalPhone;
    @BindView(R.id.edittext_address) TextInputEditText editTextAddress;
    @BindView(R.id.edittext_dob) TextInputEditText editTextDob;
    @BindView(R.id.autocomplete_blood_type) AutoCompleteTextView autoCompleteBloodType;

    @BindView(R.id.inputlayout_personal_name) TextInputLayout layoutPersonalName;
    @BindView(R.id.inputlayout_personal_phone) TextInputLayout layoutPersonalPhone;
    @BindView(R.id.inputlayout_address) TextInputLayout layoutAddress;
    @BindView(R.id.inputlayout_dob) TextInputLayout layoutDob;
    @BindView(R.id.inputlayout_blood_type) TextInputLayout layoutBloodType;
    @BindView(R.id.inputlayout_contact_name) TextInputLayout layoutContactName;
    @BindView(R.id.inputlayout_contact_phone) TextInputLayout layoutContactPhone;





    private boolean isEdit = true;


    private ProfileViewModel profileViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        ButterKnife.bind(this, root);

//        final TextView textView = root.findViewById(R.id.text_notifications);
//        profileViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });


        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        getContext(),
                        R.layout.blood_type_menu_popup_item,
                        getResources().getStringArray(R.array.types_blood_list));


        autoCompleteBloodType.setAdapter(adapter);


        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Select Date of Birth");


        Calendar c = Calendar.getInstance();
        long todayDate = c.getTimeInMillis();
        int month = c.get(Calendar.MONTH);

        CalendarConstraints.DateValidator dateValidator = new CalendarConstraints.DateValidator() {
            @Override
            public boolean isValid(long date) {
                if(date < todayDate)
                    return true;
                return false;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {

            }
        };
        CalendarConstraints constraints = new CalendarConstraints.Builder().setStart(month).setValidator(dateValidator).build();
        builder.setCalendarConstraints(constraints);
        MaterialDatePicker materialDatePicker = builder.build();


        editTextDob.setClickable(true);
        editTextDob.setOnClickListener(v -> {
            if(!materialDatePicker.isAdded())
                materialDatePicker.show(getParentFragmentManager(), "DATE_PICKER");
        });

        materialDatePicker.addOnPositiveButtonClickListener(selection -> {
            editTextDob.setText(materialDatePicker.getHeaderText());
        });




        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable drawable;
                if(isEdit) {
                    drawable = ContextCompat.getDrawable(getContext(),R.drawable.ic_done);
                    isEdit = false;
                    enagleTextViews();
                } else {
                    drawable = ContextCompat.getDrawable(getContext(),R.drawable.ic_edit);
                    isEdit = true;
                    disableTextViews();
                }

                buttonEdit.setImageDrawable(drawable);

            }
        });

        return root;
    }

    private void disableTextViews() {
        buttonChangePhoto.setVisibility(View.INVISIBLE);

        layoutPersonalName.setEnabled(false);
        layoutPersonalPhone.setEnabled(false);
        layoutAddress.setEnabled(false);
        layoutDob.setEnabled(false);
        layoutBloodType.setEnabled(false);
        layoutContactName.setEnabled(false);
        layoutContactPhone.setEnabled(false);
    }

    private void enagleTextViews() {
        buttonChangePhoto.setVisibility(View.VISIBLE);

        layoutPersonalName.setEnabled(true);
        layoutPersonalPhone.setEnabled(true);
        layoutAddress.setEnabled(true);
        layoutDob.setEnabled(true);
        layoutBloodType.setEnabled(true);
        layoutContactName.setEnabled(true);
        layoutContactPhone.setEnabled(true);
    }

}
