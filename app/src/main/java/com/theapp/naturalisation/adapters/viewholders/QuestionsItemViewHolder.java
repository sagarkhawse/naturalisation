package com.theapp.naturalisation.adapters.viewholders;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.theapp.naturalisation.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QuestionsItemViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.item_category)
    public TextView itemCategory;
    @BindView(R.id.item_question)
    public TextView itemQuestion;
    @BindView(R.id.item_response)
    public TextView itemResponse;
    @BindView(R.id.button_response)
    public ImageButton mButtonResponse;

    public QuestionsItemViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setItemCategory(String itemCategory) {
        this.itemCategory.setText(itemCategory);
    }

    public void setItemQuestion(String itemQuestion) {
        this.itemQuestion.setText(itemQuestion);
    }

    public void setItemResponse(String itemResponse) {
        this.itemResponse.setText(itemResponse);
    }

    public void setItemCategoryNotVisible() {
        itemCategory.setVisibility(View.GONE);
    }

}
