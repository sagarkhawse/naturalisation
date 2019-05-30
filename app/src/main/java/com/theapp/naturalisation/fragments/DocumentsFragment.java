package com.theapp.naturalisation.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.theapp.naturalisation.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DocumentsFragment extends Fragment {

    @BindView(R.id.checkBox1)
    CheckBox checkBox1;
    @BindView(R.id.checkBox2)
    CheckBox checkBox2;
    @BindView(R.id.checkBox3)
    CheckBox checkBox3;
    @BindView(R.id.checkBox4)
    CheckBox checkBox4;
    @BindView(R.id.checkBox5)
    CheckBox checkBox5;
    @BindView(R.id.checkBox6)
    CheckBox checkBox6;
    @BindView(R.id.checkBox7)
    CheckBox checkBox7;
    @BindView(R.id.checkBox8)
    CheckBox checkBox8;
    @BindView(R.id.checkBox9)
    CheckBox checkBox9;
    @BindView(R.id.checkBox10)
    CheckBox checkBox10;
    @BindView(R.id.checkBox11)
    CheckBox checkBox11;
    @BindView(R.id.checkBox12)
    CheckBox checkBox12;
    @BindView(R.id.checkBox13)
    CheckBox checkBox13;
    @BindView(R.id.checkBox14)
    CheckBox checkBox14;
    @BindView(R.id.checkBox15)
    CheckBox checkBox15;
    @BindView(R.id.checkBox16)
    CheckBox checkBox16;
    @BindView(R.id.checkBox17)
    CheckBox checkBox17;
    @BindView(R.id.checkBox18)
    CheckBox checkBox18;
    @BindView(R.id.checkBox19)
    CheckBox checkBox19;
    @BindView(R.id.checkBox20)
    CheckBox checkBox20;
    @BindView(R.id.linkToOriginalDocuments)
    TextView linkToOriginalDocuments;

    private static final String TAG = "DocumentsFragment";

    public DocumentsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_documents, container, false);
        ButterKnife.bind(this, view);
        initViews();
        setupListeners();

        return view;
    }


    private void initViews() {
        boolean checked1 = PreferenceManager.getDefaultSharedPreferences(getContext())
                .getBoolean("checkBox1", false);
        checkBox1.setChecked(checked1);
        boolean checked2 = PreferenceManager.getDefaultSharedPreferences(getContext())
                .getBoolean("checkBox2", false);
        checkBox2.setChecked(checked2);
        boolean checked3 = PreferenceManager.getDefaultSharedPreferences(getContext())
                .getBoolean("checkBox3", false);
        checkBox3.setChecked(checked3);
        boolean checked4 = PreferenceManager.getDefaultSharedPreferences(getContext())
                .getBoolean("checkBox4", false);
        checkBox4.setChecked(checked4);
        boolean checked5 = PreferenceManager.getDefaultSharedPreferences(getContext())
                .getBoolean("checkBox5", false);
        checkBox5.setChecked(checked5);
        boolean checked6 = PreferenceManager.getDefaultSharedPreferences(getContext())
                .getBoolean("checkBox6", false);
        checkBox6.setChecked(checked6);
        boolean checked7 = PreferenceManager.getDefaultSharedPreferences(getContext())
                .getBoolean("checkBox7", false);
        checkBox7.setChecked(checked7);
        boolean checked8 = PreferenceManager.getDefaultSharedPreferences(getContext())
                .getBoolean("checkBox8", false);
        checkBox8.setChecked(checked8);
        boolean checked9 = PreferenceManager.getDefaultSharedPreferences(getContext())
                .getBoolean("checkBox9", false);
        checkBox9.setChecked(checked9);
        boolean checked10 = PreferenceManager.getDefaultSharedPreferences(getContext())
                .getBoolean("checkBox10", false);
        checkBox10.setChecked(checked10);
        boolean checked11 = PreferenceManager.getDefaultSharedPreferences(getContext())
                .getBoolean("checkBox11", false);
        checkBox11.setChecked(checked11);
        boolean checked12 = PreferenceManager.getDefaultSharedPreferences(getContext())
                .getBoolean("checkBox12", false);
        checkBox12.setChecked(checked12);
        boolean checked13 = PreferenceManager.getDefaultSharedPreferences(getContext())
                .getBoolean("checkBox13", false);
        checkBox13.setChecked(checked13);
        boolean checked14 = PreferenceManager.getDefaultSharedPreferences(getContext())
                .getBoolean("checkBox14", false);
        checkBox14.setChecked(checked14);
        boolean checked15 = PreferenceManager.getDefaultSharedPreferences(getContext())
                .getBoolean("checkBox15", false);
        checkBox15.setChecked(checked15);
        boolean checked16 = PreferenceManager.getDefaultSharedPreferences(getContext())
                .getBoolean("checkBox16", false);
        checkBox16.setChecked(checked16);
        boolean checked17 = PreferenceManager.getDefaultSharedPreferences(getContext())
                .getBoolean("checkBox17", false);
        checkBox17.setChecked(checked17);
        boolean checked18 = PreferenceManager.getDefaultSharedPreferences(getContext())
                .getBoolean("checkBox18", false);
        checkBox18.setChecked(checked18);
        boolean checked19 = PreferenceManager.getDefaultSharedPreferences(getContext())
                .getBoolean("checkBox19", false);
        checkBox19.setChecked(checked19);
        boolean checked20 = PreferenceManager.getDefaultSharedPreferences(getContext())
                .getBoolean("checkBox20", false);
        checkBox20.setChecked(checked20);
        linkToOriginalDocuments.setClickable(true);
        linkToOriginalDocuments.setMovementMethod(LinkMovementMethod.getInstance());
        linkToOriginalDocuments.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW);
            browserIntent.setData(Uri.parse("https://www.service-public.fr/particuliers/vosdroits/F2213"));
            startActivity(browserIntent);
        });
    }


    private void setupListeners() {
        checkBox1.setOnCheckedChangeListener((buttonView, isChecked) -> PreferenceManager.getDefaultSharedPreferences(getContext()).edit()
                .putBoolean("checkBox1", isChecked).apply());
        checkBox2.setOnCheckedChangeListener((buttonView, isChecked) -> PreferenceManager.getDefaultSharedPreferences(getContext()).edit()
                .putBoolean("checkBox2", isChecked).apply());
        checkBox3.setOnCheckedChangeListener((buttonView, isChecked) -> PreferenceManager.getDefaultSharedPreferences(getContext()).edit()
                .putBoolean("checkBox3", isChecked).apply());
        checkBox4.setOnCheckedChangeListener((buttonView, isChecked) -> PreferenceManager.getDefaultSharedPreferences(getContext()).edit()
                .putBoolean("checkBox4", isChecked).apply());
        checkBox5.setOnCheckedChangeListener((buttonView, isChecked) -> PreferenceManager.getDefaultSharedPreferences(getContext()).edit()
                .putBoolean("checkBox5", isChecked).apply());
        checkBox6.setOnCheckedChangeListener((buttonView, isChecked) -> PreferenceManager.getDefaultSharedPreferences(getContext()).edit()
                .putBoolean("checkBox6", isChecked).apply());
        checkBox7.setOnCheckedChangeListener((buttonView, isChecked) -> PreferenceManager.getDefaultSharedPreferences(getContext()).edit()
                .putBoolean("checkBox7", isChecked).apply());
        checkBox8.setOnCheckedChangeListener((buttonView, isChecked) -> PreferenceManager.getDefaultSharedPreferences(getContext()).edit()
                .putBoolean("checkBox8", isChecked).apply());
        checkBox9.setOnCheckedChangeListener((buttonView, isChecked) -> PreferenceManager.getDefaultSharedPreferences(getContext()).edit()
                .putBoolean("checkBox9", isChecked).apply());
        checkBox10.setOnCheckedChangeListener((buttonView, isChecked) -> PreferenceManager.getDefaultSharedPreferences(getContext()).edit()
                .putBoolean("checkBox10", isChecked).apply());
        checkBox11.setOnCheckedChangeListener((buttonView, isChecked) -> PreferenceManager.getDefaultSharedPreferences(getContext()).edit()
                .putBoolean("checkBox11", isChecked).apply());
        checkBox12.setOnCheckedChangeListener((buttonView, isChecked) -> PreferenceManager.getDefaultSharedPreferences(getContext()).edit()
                .putBoolean("checkBox12", isChecked).apply());
        checkBox13.setOnCheckedChangeListener((buttonView, isChecked) -> PreferenceManager.getDefaultSharedPreferences(getContext()).edit()
                .putBoolean("checkBox13", isChecked).apply());
        checkBox14.setOnCheckedChangeListener((buttonView, isChecked) -> PreferenceManager.getDefaultSharedPreferences(getContext()).edit()
                .putBoolean("checkBox14", isChecked).apply());
        checkBox15.setOnCheckedChangeListener((buttonView, isChecked) -> PreferenceManager.getDefaultSharedPreferences(getContext()).edit()
                .putBoolean("checkBox15", isChecked).apply());
        checkBox16.setOnCheckedChangeListener((buttonView, isChecked) -> PreferenceManager.getDefaultSharedPreferences(getContext()).edit()
                .putBoolean("checkBox16", isChecked).apply());
        checkBox17.setOnCheckedChangeListener((buttonView, isChecked) -> PreferenceManager.getDefaultSharedPreferences(getContext()).edit()
                .putBoolean("checkBox17", isChecked).apply());
        checkBox18.setOnCheckedChangeListener((buttonView, isChecked) -> PreferenceManager.getDefaultSharedPreferences(getContext()).edit()
                .putBoolean("checkBox18", isChecked).apply());
        checkBox19.setOnCheckedChangeListener((buttonView, isChecked) -> PreferenceManager.getDefaultSharedPreferences(getContext()).edit()
                .putBoolean("checkBox19", isChecked).apply());
        checkBox20.setOnCheckedChangeListener((buttonView, isChecked) -> PreferenceManager.getDefaultSharedPreferences(getContext()).edit()
                .putBoolean("checkBox20", isChecked).apply());
    }
}
