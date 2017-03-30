package com.monsoonblessing.kevinfaust.smartpark.Popups;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.monsoonblessing.kevinfaust.smartpark.R;

/**
 * Created by Kevin Faust on 12/10/2016.
 */

public class WarningPopup extends DialogFragment {

    public WarningPopup newInstance(String message) {
        Bundle b = new Bundle();
        b.putString("WarningMessage", message);
        WarningPopup wp = new WarningPopup();
        wp.setArguments(b);
        return wp;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        /* if for some reason we didn't create popup using newInstance (with a message),
        set a default message as the warning message
        */
        String warningMessage;
        if (getArguments() != null) {
            warningMessage = getArguments().getString("WarningMessage");
        } else {
            warningMessage = "Warning!";
        }

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.warning_popup, null);
        TextView warningTextView = (TextView) v.findViewById(R.id.warning_text);
        warningTextView.setText(warningMessage);

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setPositiveButton("OK", null)
                .create();
    }

}
