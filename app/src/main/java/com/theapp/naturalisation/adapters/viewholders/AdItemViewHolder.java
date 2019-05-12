package com.theapp.naturalisation.adapters.viewholders;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.theapp.naturalisation.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdItemViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.card_ad)
    MaterialCardView cardAdView;


    private Context mContext;

    public AdItemViewHolder(View itemView, Context context) {
        super(itemView);
        this.mContext = context;
        ButterKnife.bind(this, itemView);
    }

    public void setAdCard(MaterialCardView cardAdView) {
        this.cardAdView = cardAdView;
    }

}
