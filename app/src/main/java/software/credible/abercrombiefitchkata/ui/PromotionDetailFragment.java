package software.credible.abercrombiefitchkata.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
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

            setupTitle();
            setupDescription();
            setupFooter();
            setupImage();
            setupButton();
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        realm.close();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.promotion_detail, container, false);
    }


    private void setupButton() {
        final Button buttonData = promotion.getButtons().get(0);
        android.widget.Button buttonView = getActivity().findViewById(R.id.promotion_button);
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

    private void setupImage() {
        ImageView promoDetailImage = getView().findViewById(R.id.promotion_detail_image);
        Picasso.with(getActivity()).load(promotion.getImageUrl()).into(promoDetailImage);
    }

    private void setupFooter() {
        if(promotion.getFooter() != null) {
            TextView footer = getActivity().findViewById(R.id.promotion_footer);
            if(footer != null) {
                footer.setVisibility(View.VISIBLE);
                footer.setText(Html.fromHtml(promotion.getFooter()));
                footer.setMovementMethod(LinkMovementMethod.getInstance());
            }
        }
    }

    private void setupDescription() {
        ((TextView)getView().findViewById((R.id.promotion_detail))).setText(promotion.getDescription());
    }

    private void setupTitle() {
        Activity activity = getActivity();
        CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
        Toolbar toolbar = activity.findViewById(R.id.toolbar);
        if (appBarLayout != null) {
            appBarLayout.setTitle(promotion.getTitle());
        } else if(toolbar != null) { // if in split pane mode, the tool bar will be present
            // in the appBarLayouts absense.
            toolbar.setTitle(promotion.getTitle());
        }
    }
}
