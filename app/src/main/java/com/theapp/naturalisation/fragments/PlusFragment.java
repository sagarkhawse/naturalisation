package com.theapp.naturalisation.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.button.MaterialButton;
import com.theapp.naturalisation.R;
import com.theapp.naturalisation.helpers.CommonTools;
import com.theapp.naturalisation.helpers.FullScreenDialog;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlusFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.button_add_question)
    MaterialButton mButtonAddQuestion;
    @BindView(R.id.button_facebook)
    MaterialButton mButtonFacebook;
    @BindView(R.id.button_exit)
    MaterialButton mButtonExit;

    private static final String TAG = "PlusFragment";

    public PlusFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_plus, container, false);
        ButterKnife.bind(this, view);

        mButtonAddQuestion.setOnClickListener(this);
        mButtonExit.setOnClickListener(this);
        mButtonFacebook.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v == mButtonAddQuestion) {
            if (CommonTools.isFullVersion()) {
                FullScreenDialog dialog = new FullScreenDialog();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                dialog.show(ft, "DIALOG");
            } else {
                Toast.makeText(getContext(), getResources().getString(R.string.toast_get_pro_version), Toast.LENGTH_LONG).show();
            }
        }
        if (v == mButtonFacebook) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW);
            browserIntent.setData(Uri.parse("https://www.facebook.com/Naturalisation-Fran%C3%A7aise-1272280599597926/"));
            startActivity(browserIntent);
        }
        if (v == mButtonExit) {
            getActivity().finish();
            System.exit(0);
        }
    }
}
