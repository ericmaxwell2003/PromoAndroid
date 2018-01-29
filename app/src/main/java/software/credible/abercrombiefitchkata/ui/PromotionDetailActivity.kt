package software.credible.abercrombiefitchkata.ui

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_promotion_detail.*
import kotlinx.android.synthetic.main.activity_promotion_list.*

import software.credible.abercrombiefitchkata.R

/**
 * An activity representing a single Promotion detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a [PromotionListActivity].
 */
class PromotionDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_promotion_detail)
        setSupportActionBar(detail_toolbar)

        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState == null) {
            val fragment = PromotionDetailFragment.newInstance(itemId = intent.getStringExtra(
                    PromotionDetailFragment.ARG_ITEM_ID))

            supportFragmentManager.beginTransaction()
                    .add(R.id.promotion_detail_container, fragment)
                    .commit()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            navigateUpTo(Intent(this, PromotionListActivity::class.java))
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
