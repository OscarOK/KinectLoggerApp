package mx.uach.hcilab.kinectlogger;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.graphics.Bitmap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import java.security.Permission;
import java.util.Calendar;
import java.util.Date;

import mx.uach.hcilab.kinectlogger.fragments.DatePickerFragment;
import mx.uach.hcilab.kinectlogger.util.*;

public class PatientManagerActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    static final int REQUEST_IMAGE_CAPTURE = 1;
    CameraIntentHelper mCameraIntentHelper;
    PermissionsHelper mPermissionHelper;

    Uri photoPath = null;

    private static final String TAG = "PatientManagerActivity";

    @BindView(R.id.button_take_photo)
    FloatingActionButton mTakePhotoButton;
    @BindView(R.id.image_view_patient)
    ImageView mPatientImageView;

    @BindView(R.id.text_patient_name)
    EditText nameEditText;
    @BindView(R.id.text_patient_lastname)
    EditText paternalEditText;
    @BindView(R.id.text_patient_lastname_2)
    EditText maternalEditText;
    @BindView(R.id.button_birthdate)
    Button mBirthdaButton;
    @BindView(R.id.text_birthday)
    TextInputEditText mBirthdayText;

    private Calendar mPatientBirthday;
    private DialogFragment mdatePickerFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patients_manager);
        ButterKnife.bind(this);
        setTitle(getString(R.string.patient_manager_activity_title));
        if(getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mPermissionHelper = new PermissionsHelper(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 100);

        mPermissionHelper.request(new PermissionsHelper.PermissionCallback() {
            @Override
            public void onPermissionGranted() {
                Log.d(TAG, "onPermissionGranted() called");
            }

            @Override
            public void onIndividualPermissionGranted(String[] grantedPermission) {
                Log.d(TAG, "onIndividualPermissionGranted() called with: grantedPermission = [" + TextUtils.join(",", grantedPermission) + "]");
            }

            @Override
            public void onPermissionDenied() {
                Log.d(TAG, "onPermissionDenied() called");
            }

            @Override
            public void onPermissionDeniedBySystem() {
                Log.d(TAG, "onPermissionDeniedBySystem() called");
                mPermissionHelper.openAppDetailsActivity();
            }
        });
        setupCameraIntentHelper();
        mdatePickerFragment = new DatePickerFragment();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (mPermissionHelper != null) {
            mPermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    @OnClick(R.id.button_birthdate)
    protected void clickedBirthdateButton(){

        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                mPatientBirthday = Calendar.getInstance();
                mPatientBirthday.set(year,month+1,day);
                mBirthdayText.setText(String.valueOf(day)+"-"+String.valueOf(month+1)+"-"+String.valueOf(year));
                // +1 because january is zero
                final String selectedDate = day + " / " + (month+1) + " / " + year;
                mBirthdayText.setText(selectedDate);
            }
        });
        newFragment.show(getFragmentManager(), "datePicker");
    }


    @OnClick(R.id.button_save)
    protected void clickedSaveButton(){
        if(nameEditText.getText().toString().isEmpty() ||
                paternalEditText.getText().toString().isEmpty() ||
                maternalEditText.getText().toString().isEmpty() ||
                mPatientBirthday == null ||
                photoPath == null){
            Toast.makeText(getApplicationContext(), R.string.warning_fields_incomplete, Toast.LENGTH_SHORT).show();
            return;
        }
        String birthdayString = String.valueOf(mPatientBirthday.get(Calendar.YEAR))+"-"+
                                String.valueOf(mPatientBirthday.get(Calendar.MONTH))+"-"+
                                String.valueOf(mPatientBirthday.get(Calendar.DATE));
        Patient patient = new Patient(nameEditText.getText().toString(),
                paternalEditText.getText().toString(),
                maternalEditText.getText().toString(),
                birthdayString,
                photoPath);
        FirestoreHelper.uploadPatient(getApplicationContext(), patient);
        finish();
    }


    @OnClick(R.id.button_take_photo)
    protected void takePhoto(){


        if(mCameraIntentHelper !=null)
            mCameraIntentHelper.startCameraIntent();
    }



    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void setupCameraIntentHelper() {
        mCameraIntentHelper = new CameraIntentHelper(this, new CameraIntentHelperCallback() {
            @Override
            public void onPhotoUriFound(Date dateCameraIntentStarted, Uri photoUri, int rotateXDegrees) {
                //Toast.makeText(getApplicationContext(), getString(R.string.activity_camera_intent_photo_uri_found) + photoUri.toString(),Toast.LENGTH_LONG).show();

                photoPath = photoUri;
                Bitmap photo = BitmapHelper.readBitmap(PatientManagerActivity.this, photoUri);
                if (photo != null) {
                    photo = BitmapHelper.shrinkBitmap(photo, 300, rotateXDegrees);

                    Bitmap cropImg;
                    int width = photo.getWidth();
                    int height = photo.getHeight();
                    if(width > height){
                        int crop = (width - height) / 2;
                        cropImg = Bitmap.createBitmap(photo, crop, 0, height, height);
                    } else {
                        int crop = (height - width) / 2;
                        cropImg = Bitmap.createBitmap(photo, 0, crop, width, width);
                    }
                    mPatientImageView.setImageBitmap(cropImg);
                }
            }

            @Override
            public void deletePhotoWithUri(Uri photoUri) {
                BitmapHelper.deleteImageWithUriIfExists(photoUri, PatientManagerActivity.this);
            }

            @Override
            public void onSdCardNotMounted() {
                Toast.makeText(getApplicationContext(), getString(R.string.error_sd_card_not_mounted), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCanceled() {
                Toast.makeText(getApplicationContext(), getString(R.string.warning_camera_intent_canceled), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCouldNotTakePhoto() {
                Toast.makeText(getApplicationContext(), getString(R.string.error_could_not_take_photo), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onPhotoUriNotFound() {
                Toast.makeText(getApplicationContext(), getString(R.string.activity_camera_intent_photo_uri_not_found), Toast.LENGTH_LONG).show();
            }

            @Override
            public void logException(Exception e) {
                Toast.makeText(getApplicationContext(), getString(R.string.error_sth_went_wrong), Toast.LENGTH_LONG).show();
                Log.d(getClass().getName(), e.getMessage());
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        mCameraIntentHelper.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mCameraIntentHelper.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        mCameraIntentHelper.onActivityResult(requestCode, resultCode, intent);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {

    }
}
