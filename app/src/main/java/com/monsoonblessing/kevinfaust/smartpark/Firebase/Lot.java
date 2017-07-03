package com.monsoonblessing.kevinfaust.smartpark.Firebase;

import com.monsoonblessing.kevinfaust.smartpark.Firebase.Vehicle;

import java.util.ArrayList;

/**
 * Created by Kevin Faust on 3/28/2017.
 */

public class Lot {

    private String name;
    private int availableSpots;
    private int maxSpots;
    private int maxTime;
    private double hourlyCharge;
    private String qrCodeUrl;
    private double accuracy;
    private int lifetimeScans;
    private ArrayList<Vehicle> vehicles;
    private ArrayList<Vehicle> vehicleLog;

    public Lot() {
    }

    public Lot(String name, int availableSpots, int maxSpots, int maxTime, double hourlyCharge, String qrCodeUrl) {
        this.name = name;
        this.availableSpots = availableSpots;
        this.maxSpots = maxSpots;
        this.maxTime = maxTime;
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

    public int getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(int maxTime) {
        this.maxTime = maxTime;
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

    public ArrayList<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(ArrayList<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public ArrayList<Vehicle> getVehicleLog() {
        return vehicleLog;
    }

    public void setVehicleLog(ArrayList<Vehicle> vehicleLog) {
        this.vehicleLog = vehicleLog;
    }
}



