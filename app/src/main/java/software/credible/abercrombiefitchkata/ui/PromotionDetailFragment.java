package software.credible.abercrombiefitchkata.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import io.realm.Realm;
import software.credible.abercrombiefitchkata.R;
import software.credible.abercrombiefitchkata.domain.Button;
import software.credible.abercrombiefitchkata.domain.Promotion;

/**
 * A fragment representing a single Promotion detail screen.
 * This fragment is either contained in a {@link PromotionListActivity}
 * in two-pane mode (on tablets) or a {@link PromotionDetailActivity}
 * on handsets.
 */
public class PromotionDetailFragment extends Fragment {

    public static final String ARG_ITEM_ID = "item_id";

    private Realm realm;
    private Promotion promotion;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PromotionDetailFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        realm = Realm.getDefaultInstance();

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            promotion = realm.where(Promotion.class).equalTo("id", getArguments().getString(ARG_ITEM_ID)).findFirst();

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(promotion.getTitle());
            }

            getViewOfType(R.id.promotion_detail, TextView.class).setText(promotion.getDescription());
            if(promotion.getFooter() != null) {
                getViewOfType(R.id.promotion_footer, TextView.class).setText(Html.fromHtml(promotion.getFooter()));
            }

            ImageView promoDetailImage = getViewOfType(R.id.promotion_detail_image, ImageView.class);
            Picasso.with(getActivity()).load(promotion.getImageUrl()).into(promoDetailImage);

            android.widget.Button buttonView = getViewOfType(R.id.promotion_button, android.widget.Button.class);
            final Button buttonData = promotion.getButtons().get(0);
            buttonView.setText(buttonData.getTitle());

            buttonView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(buttonData.getTargetUrl()));
                    startActivity(i);
                }
            });
        }

    }

    private <T> T getViewOfType(int resId, Class<T> ofType) {
        return ((T) getView().findViewById(resId));
    }

    @Override
    public void onStop() {
        super.onStop();
        realm.close();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.promotion_detail, container, false);
    }

}
