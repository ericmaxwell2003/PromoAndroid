package software.credible.abercrombiefitchkata.ui

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View

import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_promotion_list.*
import kotlinx.android.synthetic.main.empty_list.*
import kotlinx.android.synthetic.main.promotion_list.*
import software.credible.abercrombiefitchkata.R
import software.credible.abercrombiefitchkata.domain.Promotion
import software.credible.abercrombiefitchkata.remote.FetchPromosIntentService

/**
 * An activity representing a list of Promotions. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [PromotionDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class PromotionListActivity : AppCompatActivity() {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    var isTwoPane: Boolean = false
        private set
    private var realm: Realm? = null
    private var promotionsList: RealmResults<Promotion>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_promotion_list)

        realm = Realm.getDefaultInstance()
        promotionsList = realm!!.where(Promotion::class.java).findAllAsync()
        promotionsList?.addChangeListener { updatedPromotions ->
            if (updatedPromotions.isEmpty()) {
                promotion_list.visibility = View.GONE
                empty_view.visibility = View.VISIBLE
            } else {
                promotion_list.visibility = View.VISIBLE
                empty_view.visibility = View.GONE
            }
        }

        setupToolbarWithDefaultTitle()

        setupRefreshFab()

        if (findViewById<View>(R.id.promotion_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            isTwoPane = true
        }

        setupRecyclerView()
    }

    override fun onDestroy() {
        super.onDestroy()
        realm!!.removeAllChangeListeners()
        realm!!.close()
    }

    private fun setupRefreshFab() {
        fab.setOnClickListener {
            val intent = Intent(baseContext, FetchPromosIntentService::class.java)
            startService(intent)
        }
        fab.setOnLongClickListener {
            realm!!.executeTransactionAsync { realm -> realm.deleteAll() }
            true
        }
    }

    private fun setupToolbarWithDefaultTitle() {
        if(toolbar != null) {
            setSupportActionBar(toolbar)
            toolbar.title = title
        }
    }

    private fun setupRecyclerView() {
        promotionsList?.let {
            promotion_list.adapter = PromotionRecyclerViewAdapter(this, it)
            promotion_list.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        }

    }

}
