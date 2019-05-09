package com.theapp.naturalisation.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.theapp.naturalisation.R;
import com.theapp.naturalisation.helpers.FullScreenDialog;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.button_add_question)
    MaterialButton mButtonAddQuestion;
    @BindView(R.id.button_exit)
    MaterialButton mButtonExit;


    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.bind(this, view);

        mButtonAddQuestion.setOnClickListener(this);
        mButtonExit.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v == mButtonAddQuestion) {
            FullScreenDialog dialog = new FullScreenDialog();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            dialog.show(ft, "DIALOG");
        }

        if (v == mButtonExit) {
            getActivity().finish();
            System.exit(0);
        }
    }


}
