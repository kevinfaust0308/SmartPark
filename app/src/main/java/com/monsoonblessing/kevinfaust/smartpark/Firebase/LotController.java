package com.monsoonblessing.kevinfaust.smartpark.Firebase;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.monsoonblessing.kevinfaust.smartpark.MainActivity;
import com.monsoonblessing.kevinfaust.smartpark.R;

/**
 * Created by Kevin on 2017-07-02.
 */

public class LotController {

    private DatabaseReference lotReference;
    private DatabaseReference vehiclesReference;
    private DatabaseReference vehicleLogReference;


    public LotController(Context context, String ownerNumber, String lotNumber) {

        lotReference = FirebaseDatabase.getInstance().getReference()
                .child(context.getString(R.string.UserData))
                .child(ownerNumber)
                .child(context.getString(R.string.ParkingLots))
                .child(lotNumber);

        vehiclesReference = lotReference.child("Vehicles");
        vehicleLogReference = lotReference.child("VehicleLog");

    }


    public DatabaseReference getLotReference() {
        return lotReference;
    }


    public DatabaseReference getVehiclesReference() {
        return vehiclesReference;
    }


    public DatabaseReference getVehicleLogReference() {
        return vehicleLogReference;
    }


    private void decreaseSpaceAvailability(int currAvailableSpots) {
        lotReference.child("availableSpots").setValue(currAvailableSpots - 1);
    }


    private void increaseSpaceAvailability(int currAvailableSpots) {
        lotReference.child("availableSpots").setValue(currAvailableSpots + 1);
    }


    public void addVehicle(String licensePlate, Double ocrAccuracy, int currAvailableSpots) {

        // create new vehicle object and store in database
        VehicleObject v = new VehicleObject(licensePlate, ocrAccuracy);

        // add vehicle under license plate to current vehicles list
        vehiclesReference.child(licensePlate).setValue(v);

        // subtract an available spot
        decreaseSpaceAvailability(currAvailableSpots);
    }


    public void removeVehicle(VehicleObject vehicle, int currAvailableSpots) {

        // remove vehicle from database
        vehiclesReference.child(vehicle.getPlateNumber()).setValue(null);

        // mark vehicle out time for log
        vehicle.setTimeOut(System.currentTimeMillis());
        vehicleLogReference.push().setValue(vehicle);

        // free a space in the lot
        increaseSpaceAvailability(currAvailableSpots);

    }

    public void reaverageLotAccuracy(LotObject lot, Double justScannedAccuracy) {

        // re-average the accuracy of this lot
        int scans = lot.getLifetimeScans();
        double curr_acc = lot.getAccuracy();
        double new_acc = ((curr_acc * scans) + justScannedAccuracy) / (scans + 1);

        lotReference.child("lifetimeScans").setValue(scans + 1);
        lotReference.child("accuracy").setValue(new_acc);
    }

}
