package com.monsoonblessing.kevinfaust.smartpark.Firebase;

import java.util.ArrayList;

/**
 * Created by Kevin Faust on 3/28/2017.
 */

public class LotObject {

    private String name;
    private int availableSpots;
    private int maxSpots;
    private double hourlyCharge;
    private String qrCodeUrl;
    private double accuracy;
    private int lifetimeScans;
    private ArrayList<VehicleObject> vehicles;
    private ArrayList<VehicleObject> vehicleLog;

    public LotObject() {
    }

    public LotObject(String name, int availableSpots, int maxSpots, double hourlyCharge, String qrCodeUrl) {
        this.name = name;
        this.availableSpots = availableSpots;
        this.maxSpots = maxSpots;
        this.hourlyCharge = hourlyCharge;
        this.qrCodeUrl = qrCodeUrl;
        this.accuracy = 100.00;
        this.lifetimeScans = 1;
    }

    public String getQrCodeUrl() {
        return qrCodeUrl;
    }

    public void setQrCodeUrl(String qrCodeUrl) {
        this.qrCodeUrl = qrCodeUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAvailableSpots() {
        return availableSpots;
    }

    public void setAvailableSpots(int availableSpots) {
        this.availableSpots = availableSpots;
    }

    public int getMaxSpots() {
        return maxSpots;
    }

    public void setMaxSpots(int maxSpots) {
        this.maxSpots = maxSpots;
    }


    public double getHourlyCharge() {
        return hourlyCharge;
    }

    public void setHourlyCharge(double hourlyCharge) {
        this.hourlyCharge = hourlyCharge;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    public int getLifetimeScans() {
        return lifetimeScans;
    }

    public void setLifetimeScans(int lifetimeScans) {
        this.lifetimeScans = lifetimeScans;
    }

    public ArrayList<VehicleObject> getVehicles() {
        return vehicles;
    }

    public void setVehicles(ArrayList<VehicleObject> vehicles) {
        this.vehicles = vehicles;
    }

    public ArrayList<VehicleObject> getVehicleLog() {
        return vehicleLog;
    }

    public void setVehicleLog(ArrayList<VehicleObject> vehicleLog) {
        this.vehicleLog = vehicleLog;
    }
}



