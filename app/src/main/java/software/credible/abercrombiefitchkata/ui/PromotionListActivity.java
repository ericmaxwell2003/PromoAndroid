package software.credible.abercrombiefitchkata.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import io.realm.Realm;
import software.credible.abercrombiefitchkata.R;
import software.credible.abercrombiefitchkata.domain.Promotion;
import software.credible.abercrombiefitchkata.remote.FetchPromosIntentService;

/**
 * An activity representing a list of Promotions. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link PromotionDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class PromotionListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private Realm realm;
    private RecyclerView recyclerView;

    public boolean isTwoPane() {
        return mTwoPane;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion_list);

        realm = Realm.getDefaultInstance();

        setupToolbar();

        if (findViewById(R.id.promotion_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        setupRecyclerView();
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(PromotionListActivity.this, FetchPromosIntentService.class);
                startService(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    private void setupRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.promotion_list);
        recyclerView.setAdapter(new PromotionRecyclerViewAdapter(this, realm.where(Promotion.class).findAllAsync()));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
    }

}
