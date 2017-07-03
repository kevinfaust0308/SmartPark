package com.monsoonblessing.kevinfaust.smartpark.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.monsoonblessing.kevinfaust.smartpark.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Kevin Faust on 3/24/2017.
 */

public class ExitLotFragment extends Fragment {

    @BindView(R.id.timeSpent)
    TextView timeSpentText;
    @BindView(R.id.costCharged)
    TextView costChargedText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exit_lot, container, false);
        ButterKnife.bind(this, view);

        Bundle b = getArguments();
        double timeSpent = b.getDouble(getString(R.string.timeSpent));
        double amountCharged = b.getDouble(getString(R.string.amountCharged));

        timeSpentText.setText(String.format("%.2f", (double) timeSpent) + " seconds");
        costChargedText.setText("$" + String.format("%.2f", amountCharged));

        return view;
    }
}
