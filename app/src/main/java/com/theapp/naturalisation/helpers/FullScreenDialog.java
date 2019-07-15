package com.theapp.naturalisation.helpers;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.theapp.naturalisation.R;
import com.theapp.naturalisation.models.Item;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;

public class FullScreenDialog extends DialogFragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private static final String DATABASE_ROOT = "items";
    private static final String HISTORY = "Histoire";
    private static final String PERSONAL = "Personnel";
    private static final String REPUBLIC = "République";
    private static final String GENERAL = "Général";

    @BindView(R.id.category_spinner)
    Spinner mSpinner;
    @BindView(R.id.dialog_question_text)
    TextInputEditText mQuestion;
    @BindView(R.id.dialog_response_text)
    TextInputEditText mResponse;
    @BindView(R.id.button_post_question)
    MaterialButton mButtonPostQuestion;

    private CollectionReference collection;
    private String category;
    private InterstitialAd mInterstitialAd;

    public FullScreenDialog() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);

        FirebaseFirestore mFirestoreDb = FirebaseFirestore.getInstance();
        collection = mFirestoreDb.collection(DATABASE_ROOT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.dialog_add_question, container, false);
        ButterKnife.bind(this, view);
        mButtonPostQuestion.setEnabled(false);
        mQuestion.addTextChangedListener(new GenericTextWatcher(mQuestion));

        loadAd(view);

        mInterstitialAd = new InterstitialAd(getContext());
        mInterstitialAd.setAdUnitId("ca-app-pub-4315109878775682/2101714341");

        if (CommonTools.isDebug()) {
            mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build());
        } else {
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
        }

        mSpinner.setOnItemSelectedListener(this);
        mButtonPostQuestion.setOnClickListener(this);

        Toolbar toolbar = view.findViewById(R.id.dialog_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_close_black_24dp);
        toolbar.setNavigationOnClickListener(v -> dismiss());
        toolbar.setTitle(getResources().getString(R.string.your_question));

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the interstitial ad is closed.
                dismiss();
            }
        });


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getItemAtPosition(position).toString()) {
            case HISTORY:
                category = "history";
                break;
            case REPUBLIC:
                category = "republic";
                break;
            case PERSONAL:
                category = "perso";
                break;
            case GENERAL:
                category = "general";
            default:
                category = "perso";
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        if (v == mButtonPostQuestion) {
            Item item = new Item();
            item.setCategory(category);
            item.setQuestion(mQuestion.getText().toString());
            item.setResponse(mResponse.getText().toString());

            startPosting(item);
//            mQuestion.getText().clear();
            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            } else {
                Log.d("TAG", "The interstitial wasn't loaded yet.");
                mQuestion.getText().clear();
                mResponse.getText().clear();
            }
        }
    }

    private void startPosting(Item item) {
        if (item != null) {
            collection.add(item).addOnSuccessListener(documentReference -> {
                Log.d(TAG, "The new item added with ID: " + documentReference.getId());
            }).addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
        }
    }

//    private void openMainPage() {
//        FragmentActivity activity = getActivity();
//        if (!(activity instanceof MainActivity)) {
//            return;
//        }
//        MainActivity homeActivity = (MainActivity) activity;
//        homeActivity.showPage(R.id.navigation_questions);
//    }

    private void loadAd(View view) {
        AdView mAdView;
        mAdView = view.findViewById(R.id.adView_add_question);

        if (CommonTools.isDebug()) {
            mAdView.loadAd(new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build());
        } else {
            mAdView.loadAd(new AdRequest.Builder().build());
        }
    }

    private class GenericTextWatcher implements TextWatcher {

        private View view;

        private GenericTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        public void afterTextChanged(Editable editable) {
            if (mQuestion.getText().toString().length() == 0) {
                mButtonPostQuestion.setEnabled(false);
            } else {
                mButtonPostQuestion.setEnabled(true);
            }
        }
    }
}
