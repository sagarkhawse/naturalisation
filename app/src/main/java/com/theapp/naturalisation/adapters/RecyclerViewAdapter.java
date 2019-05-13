package com.theapp.naturalisation.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdView;
import com.theapp.naturalisation.R;
import com.theapp.naturalisation.adapters.viewholders.AdItemViewHolder;
import com.theapp.naturalisation.adapters.viewholders.QuestionsItemViewHolder;
import com.theapp.naturalisation.fragments.QuestionsFragment;
import com.theapp.naturalisation.helpers.CategorySelector;
import com.theapp.naturalisation.helpers.CommonTools;
import com.theapp.naturalisation.models.Item;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    // A menu item view type.
    private static final int MENU_ITEM_VIEW_TYPE = 0;

    // The banner ad view type.
    private static final int BANNER_AD_VIEW_TYPE = 1;

    // An Activity's Context.
    private final Context context;

    private boolean mIsExpanded;

    // The list of banner ads and menu items.
    private List<Object> recyclerViewItems;


    public RecyclerViewAdapter(Context context, List<Object> recyclerViewItems) {
        this.context = context;
        this.recyclerViewItems = recyclerViewItems;
        mIsExpanded = false;
    }

    @Override
    public int getItemCount() {
        return recyclerViewItems.size();
    }

    /**
     * Determines the view type for the given position.
     */
    @Override
    public int getItemViewType(int position) {
        return (position % QuestionsFragment.ITEMS_PER_AD == 0) ? BANNER_AD_VIEW_TYPE
                : MENU_ITEM_VIEW_TYPE;
    }

    /**
     * Creates a new view for a menu item view or a banner ad view
     * based on the viewType. This method is invoked by the layout manager.
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case MENU_ITEM_VIEW_TYPE:
                View menuItemLayoutView = LayoutInflater.from(viewGroup.getContext()).inflate(
                        R.layout.card_item, viewGroup, false);
                return new QuestionsItemViewHolder(menuItemLayoutView, context);
            case BANNER_AD_VIEW_TYPE:
                // fall through
            default:
                View bannerLayoutView = LayoutInflater.from(
                        viewGroup.getContext()).inflate(R.layout.card_ad,
                        viewGroup, false);
                return new AdItemViewHolder(bannerLayoutView, context);
        }
    }


    /**
     * Replaces the content in the views that make up the menu item view and the
     * banner ad view. This method is invoked by the layout manager.
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case MENU_ITEM_VIEW_TYPE:
                QuestionsItemViewHolder itemHolder = (QuestionsItemViewHolder) holder;
                Item menuItem = (Item) recyclerViewItems.get(position);

                // Get the menu item image resource ID.
                itemHolder.setItemCategory(CategorySelector.getName(menuItem.getCategory()));
                itemHolder.setItemQuestion(menuItem.getQuestion());
                itemHolder.setItemResponse(CommonTools.formatResponse(menuItem.getResponse()));

                itemHolder.itemResponse.setVisibility(mIsExpanded ? View.VISIBLE : View.GONE);
                itemHolder.itemView.setActivated(mIsExpanded);
                itemHolder.mButtonResponse.setOnClickListener(v -> {
                    mIsExpanded = !mIsExpanded;
                    itemHolder.mButtonResponse.setImageResource(!mIsExpanded ? R.drawable.ic_keyboard_arrow_down_black_24dp : R.drawable.ic_keyboard_arrow_up_black_24dp);
                    itemHolder.itemResponse.setVisibility(mIsExpanded ? View.VISIBLE : View.GONE);
                });
                break;
            case BANNER_AD_VIEW_TYPE:
                // fall through
            default:
                AdItemViewHolder bannerHolder = (AdItemViewHolder) holder;
                AdView adView = (AdView) recyclerViewItems.get(position);
                ViewGroup adCardView = (ViewGroup) bannerHolder.itemView;
                // The AdViewHolder recycled by the RecyclerView may be a different
                // instance than the one used previously for this position. Clear the
                // AdViewHolder of any subviews in case it has a different
                // AdView associated with it, and make sure the AdView for this position doesn't
                // already have a parent of a different recycled AdViewHolder.
                if (adCardView.getChildCount() > 0) {
                    adCardView.removeAllViews();
                }
                if (adView.getParent() != null) {
                    ((ViewGroup) adView.getParent()).removeView(adView);
                }

                // Add the banner ad to the ad view.
                adCardView.addView(adView);
        }
    }


    // Clean all elements of the recycler
    public void clear() {
        recyclerViewItems.clear();
        notifyDataSetChanged();
    }

}
