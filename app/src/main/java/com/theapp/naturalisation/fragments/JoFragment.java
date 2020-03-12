package com.theapp.naturalisation.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.theapp.naturalisation.R;
import com.theapp.naturalisation.helpers.CommonTools;
import com.theapp.naturalisation.helpers.FindNameResultDialog;
import com.theapp.naturalisation.helpers.FindNameThread;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;


public class JoFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.family_name_text_layout)
    TextInputLayout mFamilyNameLayout;
    @BindView(R.id.family_name_text)
    TextInputEditText mFamilyName;
    @BindView(R.id.first_name_text_layout)
    TextInputLayout mFirstNameLayout;
    @BindView(R.id.first_name_text)
    TextInputEditText mFirstName;
    @BindView(R.id.middle_name_text_layout)
    TextInputLayout mMiddleNameLayout;
    @BindView(R.id.middle_name_text)
    TextInputEditText mMiddleName;
    @BindView(R.id.button_search_jo)
    MaterialButton mButtonSearchJo;

    private static final String TAG = "JoFragment";

    private static final int JOS_NUMBER = 3;

    private InterstitialAd mInterstitialAd;

    public JoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_jo, container, false);
        ButterKnife.bind(this, view);

        mButtonSearchJo.setOnClickListener(this);
        mButtonSearchJo.setActivated(false);

        mInterstitialAd = new InterstitialAd(requireContext());
        mInterstitialAd.setAdUnitId("ca-app-pub-4315109878775682/2101714341");
        loadAd();

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                Log.i(TAG, "Ad closed");
                loadAd();
            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v == mButtonSearchJo) {
            if (StringUtils.isEmpty(mFamilyName.getText())) {
                mFamilyName.setError("Need to provide your family name please !");
            } else if (StringUtils.isEmpty(mFirstName.getText())) {
                mFirstName.setError("Need to provide your first name please !");
            } else {
                closeKeyboard();
                if (CommonTools.isFullVersion()) {
                    String endName = ")";
                    if (StringUtils.isNotBlank(mMiddleName.getText())) {
                        endName = ", " + StringUtils.capitalize(mMiddleName.getText().toString().toLowerCase()).trim() + ")";
                    }

                    String nameToFind = StringUtils.upperCase(mFamilyName.getText().toString()).trim()
                            + " ("
                            + StringUtils.capitalize(mFirstName.getText().toString().toLowerCase()).trim()
                            + endName;

                    List<String> refList = new ArrayList<>();
//
                    for (int i = 0; i < JOS_NUMBER; i++) {
                        String name = "joe" + i + ".pdf";
                        refList.add(name);
                    }

                    FindNameThread findNameThread = new FindNameThread();
                    findNameThread.execute(nameToFind, refList.get(0), refList.get(1), refList.get(2));

                    FindNameResultDialog dialog = new FindNameResultDialog(mInterstitialAd, mFirstName.getText().toString());
                    FragmentTransaction ft = requireFragmentManager().beginTransaction();
                    dialog.show(ft, "DIALOG_FIND_NAME");

                    clearNames();
                } else {
                    Toast.makeText(getContext(), getResources().getString(R.string.toast_get_pro_version_jo), Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void clearNames() {
        // clear first and last names
        Objects.requireNonNull(mFamilyName.getText()).clear();
        Objects.requireNonNull(mFirstName.getText()).clear();
        Objects.requireNonNull(mMiddleName.getText()).clear();
        mFamilyName.requestFocus();
    }

    private void closeKeyboard() {
        View view = this.requireActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void loadAd() {
        if (CommonTools.isDebug()) {
            mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice(CommonTools.DEVICE_ID).build());
        } else {
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
        }
    }
}
