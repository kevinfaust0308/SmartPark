package com.monsoonblessing.kevinfaust.smartpark;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Kevin Faust on 3/24/2017.
 */

public class PayFragment extends Fragment {

    private static final String TAG = "PayFragment";

    @BindView(R.id.verifyCreditCardBtn)
    Button verifyCreditCardBtn;

    /**
     * Callback interface to MainActivity
     */
    interface PayFragmentChoices {
        void onVerify();

        void onRetake();

        void onManual();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pay, container, false);
        ButterKnife.bind(this, view);

        // if we didnt recognize the license, remove "verify license" option
        boolean LicenseRecognized = getArguments().getBoolean("LicenseRecognized");
        if (!LicenseRecognized) {
            verifyCreditCardBtn.setVisibility(View.GONE);
        }

        return view;
    }


    @OnClick(R.id.verifyCreditCardBtn)
    void onVerifyCreditCard() {
        Log.d(TAG, "license is correct");
        ((MainActivity) getActivity()).onVerify();
    }

    @OnClick(R.id.retakeLicensePictureBtn)
    void onRetakeLicensePicture() {
        Log.d(TAG, "retaking license");
        ((MainActivity) getActivity()).onRetake();
    }

    @OnClick(R.id.enterLicenseManuallyBtn)
    void onEnterLicenseManually() {
        Log.d(TAG, "entering license manually");
        ((MainActivity) getActivity()).onManual();
    }


}
