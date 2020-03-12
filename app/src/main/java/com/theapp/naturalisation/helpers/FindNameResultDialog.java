package com.theapp.naturalisation.helpers;

import android.app.Dialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.ads.InterstitialAd;
import com.theapp.naturalisation.R;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FindNameResultDialog extends DialogFragment {

    @BindView(R.id.progress_bar)
    protected ProgressBar mProgressBar;
    @BindView(R.id.result_text)
    protected TextView mResult;
    @BindView(R.id.result_mood)
    protected ImageView mResultMood;

    private static final String TAG = "FindNameResultDialog";

    private InterstitialAd mInterstitialAd;
    private String mName;

    public FindNameResultDialog(InterstitialAd mInterstitialAd, String mName) {
        this.mInterstitialAd = mInterstitialAd;
        this.mName = mName;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.dialog_find_name, container, false);
        ButterKnife.bind(this, view);

        mResult.setVisibility(View.GONE);
        mResultMood.setVisibility(View.GONE);

        Toolbar toolbar = view.findViewById(R.id.dialog_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_close_black_24dp);
        toolbar.setNavigationOnClickListener(v -> dismiss());
        toolbar.setTitle("Search Ongoing ...");

        new CountDownTimer(CommonTools.getJoTimeToWait(), 1000) {
            public void onTick(long millisUntilFinished) {
                Log.i(TAG, "time left : " + millisUntilFinished);
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d(TAG, "The interstitial wasn't loaded yet!!");
                }
            }

            public void onFinish() {
                boolean found = FindNameThread.isFound();
                String joNumber = FindNameThread.getJoNumber();

                if (found) {
                    toolbar.setTitle(getString(R.string.congratulations));
                    mResult.setText(String.format("%s %s ! %s %s",
                            getString(R.string.congratulations), StringUtils.capitalize(mName), getString(R.string.foundText), joNumber));
                    mResultMood.setImageDrawable(getResources().getDrawable(R.drawable.ic_mood_happy_black_24dp));
                    mResultMood.setColorFilter(getResources().getColor(R.color.colorGreen));
                } else {
                    toolbar.setTitle(getString(R.string.notyet));
                    mResult.setText(String.format("%s, %s", StringUtils.capitalize(mName), getString(R.string.notfoundText)));
                    mResultMood.setImageDrawable(getResources().getDrawable(R.drawable.ic_mood_bad_sad_black_24dp));
                    mResultMood.setColorFilter(getResources().getColor(R.color.colorPrimaryDark));
                }

                mProgressBar.setVisibility(View.GONE);
                mResult.setVisibility(View.VISIBLE);
                mResultMood.setVisibility(View.VISIBLE);

                Log.d(TAG, "FOUND : " + found);
            }
        }.start();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            Objects.requireNonNull(dialog.getWindow()).setLayout(width, height);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
