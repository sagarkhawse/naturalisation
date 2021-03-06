package com.theapp.naturalisation.adapters.viewholders;

import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.theapp.naturalisation.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdItemViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.card_ad)
    protected MaterialCardView cardAdView;
    @BindView(R.id.ad_image)
    public ImageView adImage;

    public AdItemViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setAdCard(MaterialCardView cardAdView) {
        this.cardAdView = cardAdView;
    }

}
