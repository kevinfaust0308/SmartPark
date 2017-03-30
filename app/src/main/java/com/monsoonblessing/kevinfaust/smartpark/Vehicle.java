package com.monsoonblessing.kevinfaust.smartpark;

/**
 * Created by Kevin Faust on 3/18/2017.
 */

public class Vehicle {

    private String plateNumber;
    private long timeIn;
    private Long timeOut;
    private Double ocrAccuracy;

    public Vehicle() {
    }

    public long getTimeIn() {
        return timeIn;
    }

    public void setTimeIn(long timeIn) {
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

