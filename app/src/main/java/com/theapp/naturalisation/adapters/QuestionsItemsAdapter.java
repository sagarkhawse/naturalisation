package com.theapp.naturalisation.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.theapp.naturalisation.R;
import com.theapp.naturalisation.adapters.viewholders.QuestionsItemViewHolder;
import com.theapp.naturalisation.helpers.CategorySelector;
import com.theapp.naturalisation.models.Item;

public class QuestionsItemsAdapter extends FirestoreRecyclerAdapter<Item, QuestionsItemViewHolder> {

    private Context mContext;
    private boolean mIsExpanded;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public QuestionsItemsAdapter(@NonNull FirestoreRecyclerOptions<Item> options, Context context) {
        super(options);
        mContext = context;
        mIsExpanded = false;
    }

    @Override
    protected void onBindViewHolder(@NonNull QuestionsItemViewHolder holder, int i, @NonNull Item model) {

        holder.setItemCategory(CategorySelector.getName(model.getCategory()));
        holder.setItemQuestion(model.getQuestion());
        holder.setItemResponse(model.getResponse());

        holder.itemResponse.setVisibility(mIsExpanded ? View.VISIBLE : View.GONE);
        holder.itemView.setActivated(mIsExpanded);
        holder.mButtonResponse.setOnClickListener(v -> {
            mIsExpanded = !mIsExpanded;
            holder.mButtonResponse.setImageResource(!mIsExpanded ? R.drawable.ic_keyboard_arrow_down_black_24dp : R.drawable.ic_keyboard_arrow_up_black_24dp);
            holder.itemResponse.setVisibility(mIsExpanded ? View.VISIBLE : View.GONE);
        });
    }

    @NonNull
    @Override
    public QuestionsItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);

        return new QuestionsItemViewHolder(view, mContext);
    }
}
