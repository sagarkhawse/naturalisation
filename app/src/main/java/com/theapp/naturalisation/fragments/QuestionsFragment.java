package com.theapp.naturalisation.fragments;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.theapp.naturalisation.R;
import com.theapp.naturalisation.adapters.RecyclerViewAdapter;
import com.theapp.naturalisation.helpers.CommonTools;
import com.theapp.naturalisation.helpers.DbHelper;
import com.theapp.naturalisation.models.Item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QuestionsFragment extends Fragment {

    @BindView(R.id.items_list)
    RecyclerView mItemsList;
    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;
    @BindView(R.id.floatingActionButton)
    FloatingActionButton floatingActionButton;

    private static final String TAG = "QuestionsFragment";

    private static final String AD_UNIT_ID = "ca-app-pub-4315109878775682/2401097240";
    private static final String TEST_AD_UNIT_ID = "ca-app-pub-4315109878775682/2257988710";

    private List<Object> list = new ArrayList<>();

    private RecyclerViewAdapter adapter;

    private static int itemsPerAd;

    private static final int REQUEST = 112;

    public QuestionsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_questions, container, false);
        ButterKnife.bind(this, fragmentView);

        mItemsList.setLayoutManager(new LinearLayoutManager(getContext()));
        mItemsList.setHasFixedSize(true);

        swipeContainer.setOnRefreshListener(this::addItemsFromFirestore);
        swipeContainer.setColorSchemeResources(R.color.colorPrimaryDark);

        floatingActionButton.setOnClickListener(v -> mItemsList.smoothScrollToPosition(0));

        CommonTools.setupFirebaseRemoteConfig();

        addItemsFromFirestore();

        adapter = new RecyclerViewAdapter(getContext(), list);
        mItemsList.setAdapter(adapter);

        return fragmentView;
    }

    private void addItemsFromFirestore() {
        Query queryFirestore = DbHelper.getItemsCollection();

        queryFirestore.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Remember to CLEAR OUT old items before appending in the new ones
                adapter.clear();

                long counter = 0;
                long liteMaxItems = CommonTools.getLiteMaxItems();

                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    if (CommonTools.isLiteVersion() && counter++ == liteMaxItems) {
                        break;
                    }
                    list.add(document.toObject(Item.class));
                }

//                try {
//                    if (Build.VERSION.SDK_INT >= 23) {
//                        String[] PERMISSIONS = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
//                        if (!hasPermissions(requireContext(), PERMISSIONS)) {
//                            ActivityCompat.requestPermissions(requireActivity(), PERMISSIONS, REQUEST);
//                        } else {
//                            //do here
//                        }
//                    } else {
//                        //do here
//                    }
//
//                    DataExtractor.saveDataToFile(list);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

                try {
                    Collections.shuffle(list);
//                    if (CommonTools.isLiteVersion()) {
//                        addBannerAds();
//                        loadBannerAds();
//                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error adding banner ads to recycler view !", e);
                }

                swipeContainer.setRefreshing(false);
            } else {
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
        });
    }

//    private static boolean hasPermissions(Context context, String... permissions) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
//            for (String permission : permissions) {
//                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
//                    return false;
//                }
//            }
//        }
//        return true;
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case REQUEST: {
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    //do here
//                } else {
//                    Toast.makeText(requireContext(), "The app was not allowed to write in your storage", Toast.LENGTH_LONG).show();
//                }
//            }
//        }
//    }

    /**
     * Sets up and loads the banner ads.
     */
    private void loadBannerAds() {
        // Load the first banner ad in the items list (subsequent ads will be loaded automatically
        // in sequence).
        loadBannerAd(0);
    }

    /**
     * Loads the banner ads in the items list.
     */
    private void loadBannerAd(final int index) {

        if (index >= list.size()) {
            return;
        }

        Object item = list.get(index);
        if (!(item instanceof AdView)) {
            throw new ClassCastException("Expected item at index " + index + " to be a banner ad" + " ad.");
        }

        final AdView adView = (AdView) item;

        // Set an AdListener on the AdView to wait for the previous banner ad
        // to finish loading before loading the next ad in the items list.
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                // The previous banner ad loaded successfully, call this method again to
                // load the next ad in the items list.
                loadBannerAd(index + itemsPerAd);
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // The previous banner ad failed to load. Call this method again to load
                // the next ad in the items list.
                Log.e(TAG, "The previous banner ad failed to load. Attempting to"
                        + " load the next banner ad in the items list.");
                loadBannerAd(index + itemsPerAd);
            }
        });

        if (CommonTools.isDebug()) {
            adView.loadAd(new AdRequest.Builder().addTestDevice(CommonTools.DEVICE_ID).build());
        } else {
            adView.loadAd(new AdRequest.Builder().build());
        }
    }

    private void addBannerAds() {
        // Loop through the items array and place a new banner ad in every ith position in the items List.
        itemsPerAd = CommonTools.getItemsPerAd();
        for (int i = 0; i <= list.size(); i += itemsPerAd) {
            final AdView adView = new AdView(Objects.requireNonNull(getContext()));
            adView.setAdSize(AdSize.BANNER);
            if (CommonTools.isDebug()) {
                adView.setAdUnitId(TEST_AD_UNIT_ID);
            } else {
                adView.setAdUnitId(AD_UNIT_ID);
            }
            list.add(i, adView);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onResume() {
//        for (Object item : list) {
//            if (item instanceof AdView) {
//                AdView adView = (AdView) item;
//                adView.resume();
//            }
//        }
        super.onResume();
    }

    @Override
    public void onPause() {
//        for (Object item : list) {
//            if (item instanceof AdView) {
//                AdView adView = (AdView) item;
//                adView.pause();
//            }
//        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
//        for (Object item : list) {
//            if (item instanceof AdView) {
//                AdView adView = (AdView) item;
//                adView.destroy();
//            }
//        }
        super.onDestroy();
    }

}
