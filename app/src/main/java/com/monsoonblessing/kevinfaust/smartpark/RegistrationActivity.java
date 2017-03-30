package com.monsoonblessing.kevinfaust.smartpark;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.monsoonblessing.kevinfaust.smartpark.Popups.InternetRequiredPopup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegistrationActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener {

    private static final String TAG = "RegistrationActivity";

    private static final int QR_DECODE_CODE = 1;

    @BindView(R.id.lot_id_text)
    TextView lotIDTextView;
    @BindView(R.id.continue_btn)
    Button continueButton;

    private ProgressDialog pd;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor mEditor;

    private String lotID;
    private String ownerID;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        ButterKnife.bind(this);

        // check if we have storage permission and proceed to take picture
        if (!PermissionManager.hasStoragePermission(RegistrationActivity.this)) {
            PermissionManager.requestPermission(RegistrationActivity.this, PermissionManager.STORAGE_PERMISSION, PermissionManager.PERMISSION_STORAGE_CODE);
            finish();
            // check if we have camera permission
        } else if (!PermissionManager.hasCameraPermission(RegistrationActivity.this)) {
            PermissionManager.requestPermission(RegistrationActivity.this, PermissionManager.CAMERA_PERMISSION, PermissionManager.PERMISSION_CAMERA_CODE);
            finish();
        }

        // make sure we got internet
        if (!InternetConnectivityUtils.isConnectedToInternet(this)) {
            InternetRequiredPopup p = new InternetRequiredPopup();
            p.show(getSupportFragmentManager(), "InternetRequiredPopup");
            finish();
        } else {
            // create and show a progress dialog
            pd = new ProgressDialog(this);
            pd.setTitle("Loading");
            pd.show();

            // need to login user to access stuff in firebase datbase. so bs
            // can either set rules to == null or != null but in owner app they are logged in
            mAuth = FirebaseAuth.getInstance();
            mAuthStateListener = this;

            sharedPreferences = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
            mEditor = sharedPreferences.edit();

            // do an anonymous signin so we can access database in main activity
            mAuth.signInAnonymously();
        }
    }


    @OnClick(R.id.continue_btn)
    void onContinue() {
        // save parking lot id and owner id in this device and launch main activity
        mEditor.putString(getString(R.string.lotNumber), lotID);
        mEditor.putString(getString(R.string.ownerNumber), ownerID);
        mEditor.apply();
        launchMain();
    }


    public void launchMain() {
        Intent i = new Intent(RegistrationActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }

    @OnClick(R.id.join_lot_btn)
    void onJoinLotButton() {

        // check if we scanned a parking lot id
        if (lotIDTextView.getText().toString().length() == 0) {
            // launch QR code reader activity
            startActivityForResult(new Intent(this, QRDecodeActivity.class), QR_DECODE_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == QR_DECODE_CODE) {
            if (resultCode == RESULT_OK) {
                String returnedResult = data.getData().toString();

                // result looks like: ownerid lotid
                // split on space
                String[] splitResult = returnedResult.split(" ");
                ownerID = splitResult[0];
                lotID = splitResult[1];

                // display parking lot id
                lotIDTextView.setText(lotID.substring(1));
                // show 'continue' button
                continueButton.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        if (firebaseAuth.getCurrentUser() != null) {
            if (sharedPreferences.getString(getString(R.string.lotNumber), null) != null) {
                launchMain();
            }
            pd.dismiss();
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthStateListener != null) {
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

}
