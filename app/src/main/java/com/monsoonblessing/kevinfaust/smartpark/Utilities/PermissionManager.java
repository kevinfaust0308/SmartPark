package com.monsoonblessing.kevinfaust.smartpark.Utilities;

/**
 * Created by Kevin Faust
 */

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

/**
 * Created by Kevin Faust on 12/9/2016.
 */

public class PermissionManager {
    /*
    Utility class to help with any permissions
     */

    // permission codes
    public static final int PERMISSION_STORAGE_CODE = 1;
    public static final int PERMISSION_CAMERA_CODE = 2;

    // permission names
    public static final String STORAGE_PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    public static final String CAMERA_PERMISSION = Manifest.permission.CAMERA;


    public static boolean hasStoragePermission(Context context) {
        /*
        Returns true if we have the storage permission
         */
        return hasPermission(context, STORAGE_PERMISSION);
    }

    public static boolean hasCameraPermission(Context context) {
        /*
        Returns true if we have the storage permission
         */
        return hasPermission(context, CAMERA_PERMISSION);
    }

    private static boolean hasPermission(Context context, String permission) {
        /*
        Returns true if we have the specified permission
         */
        return (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED);
    }


    public static void requestPermission(final Activity activity, final String permission, final int permissionCode) {

        // if user denied permission, show them a popup with an explanation before re-prompting permission
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {

            String message = "";

            switch (permission) {
                case STORAGE_PERMISSION:
                    message = "Storage permission required";
                    break;
                case CAMERA_PERMISSION:
                    message = "Camera permission required";
                    break;
                // other permissions can have different messages
            }

            new AlertDialog.Builder(activity)
                    .setMessage(message)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestPermissionPopup(activity, permission, permissionCode);
                        }
                    })
                    .create()
                    .show();

        } else {
            // prompt user to enable permission
            requestPermissionPopup(activity, permission, permissionCode);
        }


    }

    private static void requestPermissionPopup(Activity activity, String permission, int permissionCode) {
        /*
        Prompts user to enable permission using android's built in permission popup
         */
        ActivityCompat.requestPermissions(activity,
                new String[]{permission},
                permissionCode);

    }


}
