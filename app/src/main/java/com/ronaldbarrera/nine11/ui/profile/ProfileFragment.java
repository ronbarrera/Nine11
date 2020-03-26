package com.ronaldbarrera.nine11.ui.profile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.preference.Preference;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.ronaldbarrera.nine11.AppExecutors;
import com.ronaldbarrera.nine11.Geofencing;
import com.ronaldbarrera.nine11.R;
import com.ronaldbarrera.nine11.database.AppDatabase;
import com.ronaldbarrera.nine11.database.UserEntry;
import com.ronaldbarrera.nine11.ui.center.Center;
import com.ronaldbarrera.nine11.viewmodel.CenterViewModel;
import com.ronaldbarrera.nine11.viewmodel.ProfileViewModel;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    @BindView(R.id.button_edit)
    FloatingActionButton buttonEdit;

    @BindView(R.id.image_profile)
    CircleImageView imageProfile;

    @BindView(R.id.button_change_photo)
    Button buttonChangePhoto;

    @BindView(R.id.edittext_personal_name) TextInputEditText editTextPersonalName;
    @BindView(R.id.edittext_personal_phone) TextInputEditText editTextPersonalPhone;
    @BindView(R.id.edittext_address) TextInputEditText editTextAddress;
    @BindView(R.id.edittext_dob) TextInputEditText editTextDob;
    @BindView(R.id.autocomplete_blood_type) AutoCompleteTextView autoCompleteBloodType;
    @BindView(R.id.edittext_contact_name) TextInputEditText editTextContactName;
    @BindView(R.id.edittext_contact_phone) TextInputEditText editTextContactPhone;

    @BindView(R.id.inputlayout_personal_name) TextInputLayout layoutPersonalName;
    @BindView(R.id.inputlayout_personal_phone) TextInputLayout layoutPersonalPhone;
    @BindView(R.id.inputlayout_address) TextInputLayout layoutAddress;
    @BindView(R.id.inputlayout_dob) TextInputLayout layoutDob;
    @BindView(R.id.inputlayout_blood_type) TextInputLayout layoutBloodType;
    @BindView(R.id.inputlayout_contact_name) TextInputLayout layoutContactName;
    @BindView(R.id.inputlayout_contact_phone) TextInputLayout layoutContactPhone;

    private static final String TAG = ProfileFragment.class.getSimpleName();
    private AppDatabase mDb;

    private boolean editMode = false;
    private UserEntry userProfile;
    private Context mContext;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        ButterKnife.bind(this, root);

        mDb = AppDatabase.getInstance(getContext());
        setupProfileViewModel();
        setupUI();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, R.layout.blood_type_menu_popup_item, getResources().getStringArray(R.array.types_blood_list));

        autoCompleteBloodType.setAdapter(adapter);

        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText(mContext.getString(R.string.picker_dob_text));

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

        buttonEdit.setOnClickListener(v -> {
            if(editMode)
                updateUserDB();
            editMode = !editMode;
            setupUI();
        });

        buttonChangePhoto.setOnClickListener(v -> selectImage(getContext()));

        return root;
    }

    private void selectImage(Context context) {
        final CharSequence[] options = {mContext.getString(R.string.picker_image_select), mContext.getString(R.string.picker_image_cancel)};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(mContext.getString(R.string.alert_image_title));
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if(options[item].equals(mContext.getString(R.string.picker_image_select))) {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, 0);
                } else if (options[item].equals(mContext.getString(R.string.picker_image_cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_CANCELED) {
            if(requestCode == 0) {
                if (resultCode == RESULT_OK && data != null) {
                    Uri contentURI = data.getData();
                    AppExecutors.getInstance().diskIO().execute(() -> mDb.userDao().updatePhotoUri(contentURI.toString(), userProfile.getId()));
                }
            }
        }
    }

    private void setupProfileViewModel() {
        ProfileViewModel profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        profileViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            if(user != null) {
                userProfile = user;
                editTextPersonalName.setText(userProfile.getPersonalName());
                editTextPersonalPhone.setText(userProfile.getPersonalPhone());
                editTextAddress.setText(userProfile.getPersonalAddress());
                editTextDob.setText(userProfile.getDob());
                autoCompleteBloodType.setText(userProfile.getBloodtype());
                editTextContactName.setText(userProfile.getContactName());
                editTextContactPhone.setText(userProfile.getContactPhone());

                if (user.getPictureUri() != null) {
                    Glide.with(mContext)
                            .load(Uri.parse(userProfile.getPictureUri()))
                            .into(imageProfile);
                }
            }
        });
    }

    public void updateUserDB(){
        userProfile.setPersonalName(editTextPersonalName.getText().toString());
        userProfile.setPersonalPhone(editTextPersonalPhone.getText().toString());
        userProfile.setPersonalAddress(editTextAddress.getText().toString());
        userProfile.setDob(editTextDob.getText().toString());
        userProfile.setBloodtype(autoCompleteBloodType.getText().toString());
        userProfile.setContactName(editTextContactName.getText().toString());
        userProfile.setContactPhone(editTextContactPhone.getText().toString());

        AppExecutors.getInstance().diskIO().execute(() -> mDb.userDao().updateUser(userProfile));
    }

    public void setupUI() {
        setEditDrawable();
        setTextViews();
    }

    private void setTextViews() {
        buttonChangePhoto.setVisibility((editMode ? View.VISIBLE : View.INVISIBLE));
        layoutPersonalName.setEnabled(editMode);
        layoutPersonalPhone.setEnabled(editMode);
        layoutAddress.setEnabled(editMode);
        layoutDob.setEnabled(editMode);
        layoutBloodType.setEnabled(editMode);
        layoutContactName.setEnabled(editMode);
        layoutContactPhone.setEnabled(editMode);
    }

    private void setEditDrawable() {
        Drawable drawable;
        drawable = ContextCompat.getDrawable(mContext,(editMode ? R.drawable.ic_done : R.drawable.ic_edit));
        buttonEdit.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(editMode ? "#06DAC6" : "#F5F5F5")));
        buttonEdit.setImageTintList(ColorStateList.valueOf(Color.parseColor(editMode ? "#000000" : "#747474")));
        buttonEdit.setImageDrawable(drawable);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }
}
