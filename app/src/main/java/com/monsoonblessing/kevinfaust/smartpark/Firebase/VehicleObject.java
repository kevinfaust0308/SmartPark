package com.monsoonblessing.kevinfaust.smartpark.Firebase;

/**
 * Created by Kevin Faust on 3/18/2017.
 */

public class VehicleObject {

    private String plateNumber;
    private Long timeIn;
    private Long timeOut; // Long object because we want it to have null value initially
    private Double ocrAccuracy;

    public VehicleObject() {
    }


    public VehicleObject(String plateNumber, Double ocrAccuracy) {
        this.plateNumber = plateNumber;
        this.timeIn = System.currentTimeMillis();
        this.timeOut = null;
        this.ocrAccuracy = ocrAccuracy;
    }


    public Long getTimeIn() {
        return timeIn;
    }

    public void setTimeIn(Long timeIn) {
        this.timeIn = timeIn;
    }


    public Long getTimeOut() {
        return timeOut;
    }


    public void setTimeOut(Long timeOut) {
        this.timeOut = timeOut;
    }


    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public Double getOcrAccuracy() {
        return ocrAccuracy;
    }

    public void setOcrAccuracy(Double ocrAccuracy) {
        this.ocrAccuracy = ocrAccuracy;
    }
}

