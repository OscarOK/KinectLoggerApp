package mx.uach.hcilab.kinectlogger;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mx.uach.hcilab.kinectlogger.util.BitmapHelper;
import mx.uach.hcilab.kinectlogger.util.CameraIntentHelper;
import mx.uach.hcilab.kinectlogger.util.CameraIntentHelperCallback;
import mx.uach.hcilab.kinectlogger.util.FirestoreHelper;
import mx.uach.hcilab.kinectlogger.util.PermissionsHelper;

public class TherapistManagerActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    CameraIntentHelper mCameraIntentHelper;
    PermissionsHelper mPermissionHelper;

    Uri photoPath = null;

    private static final String TAG = "TherapistManagerActiv";


    @BindView(R.id.button_take_photo)
    FloatingActionButton mTakePhotoButton;
    @BindView(R.id.image_view_therapist)
    ImageView mPatientImageView;

    @BindView(R.id.text_therapist_name) EditText nameEditText;
    @BindView(R.id.text_therapist_lastname) EditText paternalEditText;
    @BindView(R.id.text_therapist_lastname_2) EditText maternalEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_therapists_manager);
        ButterKnife.bind(this);
        setTitle(getString(R.string.therapist_activity_title));
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
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (mPermissionHelper != null) {
            mPermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @OnClick(R.id.button_save)
    protected void clickedSaveButton(){
        if(nameEditText.getText().toString().isEmpty() ||
                paternalEditText.getText().toString().isEmpty() ||
                maternalEditText.getText().toString().isEmpty() ||
                photoPath == null){
            Toast.makeText(getApplicationContext(), R.string.warning_fields_incomplete, Toast.LENGTH_SHORT).show();
            return;
        }

        final Therapist therapist = new Therapist(nameEditText.getText().toString(),
                paternalEditText.getText().toString(),
                maternalEditText.getText().toString(),
                photoPath);
        FirestoreHelper.uploadTherapist(getApplicationContext(), therapist);
        finish();

    }

    @OnClick(R.id.button_take_photo)
    protected void takePhoto(){

        if(mCameraIntentHelper !=null)
            mCameraIntentHelper.startCameraIntent();
    }
    private void setupCameraIntentHelper() {
        mCameraIntentHelper = new CameraIntentHelper(this, new CameraIntentHelperCallback() {
            @Override
            public void onPhotoUriFound(Date dateCameraIntentStarted, Uri photoUri, int rotateXDegrees) {
                //Toast.makeText(getApplicationContext(), getString(R.string.activity_camera_intent_photo_uri_found) + photoUri.toString(),Toast.LENGTH_LONG).show();

                photoPath = photoUri;
                Bitmap photo = BitmapHelper.readBitmap(TherapistManagerActivity.this, photoUri);
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
                    if(cropImg != null) mPatientImageView.setImageBitmap(cropImg);
                }
            }

            @Override
            public void deletePhotoWithUri(Uri photoUri) {
                BitmapHelper.deleteImageWithUriIfExists(photoUri, TherapistManagerActivity.this);
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
}
