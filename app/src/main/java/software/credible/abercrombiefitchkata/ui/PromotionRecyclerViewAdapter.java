package software.credible.abercrombiefitchkata.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;
import software.credible.abercrombiefitchkata.R;
import software.credible.abercrombiefitchkata.domain.Promotion;

public class PromotionRecyclerViewAdapter extends RealmRecyclerViewAdapter<Promotion, PromotionRecyclerViewAdapter.ViewHolder> {

    private final PromotionListActivity mPromoListActivity;

    public PromotionRecyclerViewAdapter(@NonNull PromotionListActivity promoListActivity, @Nullable OrderedRealmCollection<Promotion> promotions) {
        super(promotions, true, true);
        this.mPromoListActivity = promoListActivity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.promotion_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Promotion promotion = getData().get(position);
        holder.promotionId = promotion.getId();
        Picasso.with(mPromoListActivity).load(promotion.getImageUrl()).into(holder.imageView);
        holder.titleView.setText(promotion.getTitle());

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPromoListActivity.isTwoPane()) {
                    Bundle arguments = new Bundle();
                    arguments.putString(PromotionDetailFragment.ARG_ITEM_ID, holder.promotionId);
                    PromotionDetailFragment fragment = new PromotionDetailFragment();
                    fragment.setArguments(arguments);
                    mPromoListActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.promotion_detail_container, fragment)
                            .commit();
                } else {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, PromotionDetailActivity.class);
                    intent.putExtra(PromotionDetailFragment.ARG_ITEM_ID, holder.promotionId);

                    context.startActivity(intent);
                }
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final ImageView imageView;
        public final TextView titleView;
        public String promotionId;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            imageView = (ImageView) view.findViewById(R.id.image);
            titleView = (TextView) view.findViewById(R.id.title);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + titleView.getText() + "'";
        }
    }
}