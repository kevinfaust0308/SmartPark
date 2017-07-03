package com.monsoonblessing.kevinfaust.smartpark;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.monsoonblessing.kevinfaust.smartpark.Firebase.LotController;
import com.monsoonblessing.kevinfaust.smartpark.Firebase.LotObject;
import com.monsoonblessing.kevinfaust.smartpark.Firebase.VehicleObject;
import com.monsoonblessing.kevinfaust.smartpark.Fragments.ExitLotFragment;
import com.monsoonblessing.kevinfaust.smartpark.Fragments.PayFragment;
import com.monsoonblessing.kevinfaust.smartpark.Utilities.PermissionManager;
import com.squareup.picasso.Picasso;

import org.openalpr.OpenALPR;
import org.openalpr.model.Results;
import org.openalpr.model.ResultsError;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements PayFragment.PayFragmentChoices {

    private final int SPEECH_RECOGNITION_CODE = 10;
    private static final int REQUEST_IMAGE = 100;

    private static final String TAG = MainActivity.class.getSimpleName();
    private String ANDROID_DATA_DIR;
    private String openAlprConfFile;
    private static File destination;

    @BindView(R.id.lot_name_text)
    TextView lotNameTextView;
    @BindView(R.id.license_image)
    ImageView licensePlateImageView;
    @BindView(R.id.license_plate_text)
    TextView licensePlateTextView;
    @BindView(R.id.lot_availability_text)
    TextView lotAvailabilityTextView;


    private String licensePlate;
    private Double ocrAccuracy;

    // contains stuff related to parking lot like available spots and price
    // made it protected so we can access it from our fragment
    protected LotObject lot;


    private LotController lotController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // check if we have storage permission and proceed to take picture
        if (!PermissionManager.hasStoragePermission(MainActivity.this)) {
            PermissionManager.requestPermission(MainActivity.this, PermissionManager.STORAGE_PERMISSION, PermissionManager.PERMISSION_STORAGE_CODE);
            finish();
            // check if we have camera permission
        } else if (!PermissionManager.hasCameraPermission(MainActivity.this)) {
            PermissionManager.requestPermission(MainActivity.this, PermissionManager.CAMERA_PERMISSION, PermissionManager.PERMISSION_CAMERA_CODE);
            finish();
        }

        ButterKnife.bind(this);

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
        // get parking lot number associated with this android device
        String lotNumber = sharedPreferences.getString(getString(R.string.lotNumber), null);
        String ownerNumber = sharedPreferences.getString(getString(R.string.ownerNumber), null);
        Log.d(TAG, "lot number: " + lotNumber);
        Log.d(TAG, "owner number: " + ownerNumber);
        Log.d(TAG, "anonymous user: " + FirebaseAuth.getInstance().getCurrentUser().isAnonymous());

        // stuff for license recognition
        ANDROID_DATA_DIR = this.getApplicationInfo().dataDir;
        openAlprConfFile = ANDROID_DATA_DIR + File.separatorChar + "runtime_data" + File.separatorChar + "openalpr.conf";


        // initiate control over the associated parking lot
        lotController = new LotController(this, ownerNumber, lotNumber);


        // dynamic updating lot space text
        lotController.getLotReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                updateLotDataAndUI(dataSnapshot);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    private void updateLotDataAndUI(DataSnapshot dataSnapshot) {
        lot = dataSnapshot.getValue(LotObject.class);

        int availableSpots = lot.getAvailableSpots();
        int maximumSpots = lot.getMaxSpots();

        // lot availability text with color
        lotAvailabilityTextView.setText("Availability: " + availableSpots + " / " + maximumSpots);
        if (((float) availableSpots / maximumSpots) >= 0.60) {
            lotAvailabilityTextView.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.md_green_200));
        } else if (((float) availableSpots / maximumSpots) >= 0.30) {
            lotAvailabilityTextView.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.md_orange_200));
        } else {
            lotAvailabilityTextView.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.md_red_500));
        }

        // display lot name
        lotNameTextView.setText(String.format("Name: %s", lot.getName()));
    }


    // PROCESS LICENSE PLATE BUTTON
    @OnClick(R.id.process_license_plate)
    void onProcessLicensePlate() {
        // take picture of license plate and see whether car is leaving or entering
        takePicture();
    }


    private String dateToString(Date date, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format, Locale.getDefault());
        return df.format(date);
    }


    // using in pay fragment
    private void takePicture() {
        // Use a folder to store all results
        File folder = new File(Environment.getExternalStorageDirectory() + "/OpenALPR/");
        if (!folder.exists()) {
            folder.mkdir();
        }

        // Generate the path for the next photo
        String name = dateToString(new Date(), "yyyy-MM-dd-hh-mm-ss");
        destination = new File(folder, name + ".jpg");

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(destination));
        startActivityForResult(intent, REQUEST_IMAGE);
    }


    private void showPayFragment(boolean licenseRecognized) {
        FragmentManager fragmentManager = getFragmentManager();

        Bundle bundle = new Bundle();
        bundle.putBoolean("LicenseRecognized", licenseRecognized);

        PayFragment payFragment = new PayFragment();
        payFragment.setArguments(bundle);

        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, payFragment).commit();
    }


    private void showRecognizedLicensePayFragment() {
        showPayFragment(true);
    }


    private void showUnrecognizedLicensePayFragment() {
        showPayFragment(false);
    }


    private void showExitLotFragment(double amountcharged, double timespent) {
        FragmentManager fragmentManager = getFragmentManager();

        Bundle bundle = new Bundle();
        bundle.putDouble(getString(R.string.amountCharged), amountcharged);
        bundle.putDouble(getString(R.string.timeSpent), timespent);

        ExitLotFragment exitLotFragment = new ExitLotFragment();
        exitLotFragment.setArguments(bundle);

        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, exitLotFragment).commit();
    }


    // using in fragment too
    private void emptyFragmentContainer() {
        licensePlateTextView.setText("WAITING");
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().remove(fragmentManager.findFragmentById(R.id.fragment_container)).commit();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {

            /**
             * Callback for speech recognition activity
             * */
            case SPEECH_RECOGNITION_CODE: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String manual_license = result.get(0);
                    Log.d(TAG, "Using speech to text, detected the following: " + manual_license);

                    // after using speech to text, display the license plate that we heard
                    licensePlateTextView.setText(manual_license);

                    // show payment fragment
                    showRecognizedLicensePayFragment();
                } else {
                    Toast.makeText(this, "Please try again", Toast.LENGTH_LONG).show();
                }
                break;
            }

            /**
             * Callback from license plate recognition
             */
            case REQUEST_IMAGE: {
                if (resultCode == Activity.RESULT_OK) {
                    final ProgressDialog progress = ProgressDialog.show(this, "Loading", "Processing license...", true);

                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 10;

                    // show license plate image and insert license picture
                    licensePlateImageView.setVisibility(View.VISIBLE);

                    // Picasso requires permission.WRITE_EXTERNAL_STORAGE
                    Picasso.with(MainActivity.this).load(destination).fit().centerCrop().into(licensePlateImageView);
                    licensePlateTextView.setText("Processing");

                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            String result = OpenALPR.Factory.create(MainActivity.this, ANDROID_DATA_DIR).recognizeWithCountryRegionNConfig("us", "", destination.getAbsolutePath(), openAlprConfFile, 10);

                            try {
                                final Results results = new Gson().fromJson(result, Results.class);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (results == null || results.getResults() == null || results.getResults().size() == 0) {
                                            Toast.makeText(MainActivity.this, "It was not possible to detect the licence plate.", Toast.LENGTH_LONG).show();
                                            // resultTextView.setVisibility(View.VISIBLE);
                                            licensePlateTextView.setText("LICENSE NOT RECOGNIZED");

                                            // if cant read it, then clearly accuracy failed
                                            ocrAccuracy = 0.00;

                                            // let user either retake license picture or do manually
                                            showUnrecognizedLicensePayFragment();
                                        } else {

                                            // get license plate and accuracy
                                            licensePlate = results.getResults().get(0).getPlate();
                                            ocrAccuracy = results.getResults().get(0).getConfidence();

                                            // display the license plate we detected
                                            licensePlateTextView.setText(licensePlate);

                                            // check if we have this license in our system or not
                                            lotController.getVehiclesReference().child(licensePlate).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {

                                                    // check if license doesn't exist
                                                    if (dataSnapshot.getValue() == null) {
                                                        Log.d(TAG, "New vehicle. Adding to system");
                                                        // its a new vehicle. get credit card info before letting them in
                                                        showRecognizedLicensePayFragment();

                                                    } else {
                                                        Log.d(TAG, "VehicleObject already in system. VehicleObject leaving");
                                                        // we will show their stats (see how long they were in the parking lot for)
                                                        VehicleObject vehicle = dataSnapshot.getValue(VehicleObject.class);

                                                        // remove the vehicle from our lot
                                                        lotController.removeVehicle(vehicle, lot.getAvailableSpots());

                                                        // calculate time inside (in seconds)
                                                        long timeSpent = (System.currentTimeMillis() - vehicle.getTimeIn()) / 1000;
                                                        double amountCharged = ((double) timeSpent / 60) / 60 * lot.getHourlyCharge();


                                                        // display to screen
                                                        Log.d(TAG, "Time spent: " + timeSpent);
                                                        Log.d(TAG, "Amount charged: " + amountCharged);


                                                        // show exit message for 5 seconds then revert back
                                                        // to license "waiting" screen
                                                        showExitLotFragment(amountCharged, timeSpent);
                                                        final Handler handler = new Handler();
                                                        handler.postDelayed(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                //Do something after 5s
                                                                emptyFragmentContainer();
                                                            }
                                                        }, 10000);

                                                    }

                                                }


                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });


                                        }

                                        // reaverage lot accuracy with the just-scanned accuracy
                                        lotController.reaverageLotAccuracy(lot, ocrAccuracy);

                                    }
                                });

                            } catch (JsonSyntaxException exception) {
                                final ResultsError resultsError = new Gson().fromJson(result, ResultsError.class);

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        licensePlateTextView.setText(resultsError.getMsg());
                                    }
                                });
                            }

                            progress.dismiss();
                        }
                    });
                }
                break;
            }


        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (destination != null) {// Picasso does not seem to have an issue with a null value, but to be safe
            Picasso.with(MainActivity.this).load(destination).fit().centerCrop().into(licensePlateImageView);
        }
    }


    /**
     * Choices user can choose after license plate is analyzed
     */

    @Override
    public void onVerify() {
        // check if spots open
        if (lot.getAvailableSpots() == 0) {
            Toast.makeText(this, "No spots available", Toast.LENGTH_SHORT).show();
        } else {

            // add to lot
            lotController.addVehicle(licensePlate, ocrAccuracy, lot.getAvailableSpots());

            // remove "pay" screen
            emptyFragmentContainer();

            Toast.makeText(this, "Successfully registered license in system", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onRetake() {
        emptyFragmentContainer();
        takePicture();
    }


    @Override
    public void onManual() {
        startSpeechToText();
    }


    /**
     * Start speech to text intent. This opens up Google Speech Recognition API dialog box to listen the speech input.
     */
    private void startSpeechToText() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Please read out your license");
        try {
            startActivityForResult(intent, SPEECH_RECOGNITION_CODE);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! Speech recognition is not supported in this device.",
                    Toast.LENGTH_SHORT).show();
        }
    }

}
