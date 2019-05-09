package com.theapp.naturalisation.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;
import com.theapp.naturalisation.R;
import com.theapp.naturalisation.adapters.viewholders.QuestionsItemViewHolder;
import com.theapp.naturalisation.adapters.QuestionsItemsAdapter;
import com.theapp.naturalisation.helpers.ItemHelper;
import com.theapp.naturalisation.models.Item;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QuestionsFragment extends Fragment {

    @BindView(R.id.items_list)
    RecyclerView mItemsList;

    private FirestoreRecyclerAdapter<Item, QuestionsItemViewHolder> firestoreRecyclerAdapter;

    public QuestionsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_questions, container, false);
        ButterKnife.bind(this, fragmentView);

        mItemsList.setLayoutManager(new LinearLayoutManager(getContext()));
        mItemsList.setHasFixedSize(true);

        Query queryFirestore = ItemHelper.getItemsCollection();
        FirestoreRecyclerOptions<Item> optionsFirestore = new FirestoreRecyclerOptions
                .Builder<Item>().setQuery(queryFirestore, Item.class).build();

        firestoreRecyclerAdapter = new QuestionsItemsAdapter(optionsFirestore, getContext());

        mItemsList.setAdapter(firestoreRecyclerAdapter);
        return fragmentView;
    }

    @Override
    public void onStart() {
        super.onStart();
        firestoreRecyclerAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        firestoreRecyclerAdapter.stopListening();
    }

}
