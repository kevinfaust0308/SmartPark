package com.monsoonblessing.kevinfaust.smartpark.Popups;

/**
 * Created by Kevin Faust on 11/20/2016.
 */

public class InternetRequiredPopup extends WarningPopup {

    public WarningPopup newInstance() {
        return super.newInstance("Please make sure you are connected to the internet");
    }
}


