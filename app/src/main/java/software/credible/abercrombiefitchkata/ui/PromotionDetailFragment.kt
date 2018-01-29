package software.credible.abercrombiefitchkata.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.squareup.picasso.Picasso

import io.realm.Realm
import software.credible.abercrombiefitchkata.R
import software.credible.abercrombiefitchkata.domain.Button
import software.credible.abercrombiefitchkata.domain.Promotion

/**
 * A fragment representing a single Promotion detail screen.
 * This fragment is either contained in a [PromotionListActivity]
 * in two-pane mode (on tablets) or a [PromotionDetailActivity]
 * on handsets.
 */
class PromotionDetailFragment : Fragment() {

    private var realm: Realm? = null
    private var promotion: Promotion? = null

    override fun onStart() {
        super.onStart()
        realm = Realm.getDefaultInstance()

        if (arguments!!.containsKey(ARG_ITEM_ID)) {

            promotion = realm!!.where(Promotion::class.java).equalTo("id", arguments!!.getString(ARG_ITEM_ID)).findFirst()

            setupTitle()
            setupDescription()
            setupFooter()
            setupImage()
            setupButton()
        }

    }

    override fun onStop() {
        super.onStop()
        realm!!.close()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.promotion_detail, container, false)
    }


    private fun setupButton() {
        val buttonData = promotion!!.buttons!![0]
        val buttonView = activity!!.findViewById<android.widget.Button>(R.id.promotion_button)
        buttonView.text = buttonData!!.title
        buttonView.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(buttonData.targetUrl)
            startActivity(i)
        }
    }

    private fun setupImage() {
        val promoDetailImage = view!!.findViewById<ImageView>(R.id.promotion_detail_image)
        Picasso.with(activity).load(promotion!!.imageUrl).into(promoDetailImage)
    }

    private fun setupFooter() {
        if (promotion!!.footer != null) {
            val footer = activity!!.findViewById<TextView>(R.id.promotion_footer)
            if (footer != null) {
                footer.visibility = View.VISIBLE
                footer.text = Html.fromHtml(promotion!!.footer)
                footer.movementMethod = LinkMovementMethod.getInstance()
            }
        }
    }

    private fun setupDescription() {
        (view!!.findViewById<View>(R.id.promotion_detail) as TextView).text = promotion!!.description
    }

    private fun setupTitle() {
        val activity = activity
        val appBarLayout = activity!!.findViewById<CollapsingToolbarLayout>(R.id.toolbar_layout)
        val toolbar = activity.findViewById<Toolbar>(R.id.toolbar)
        if (appBarLayout != null) {
            appBarLayout.title = promotion!!.title
        } else if (toolbar != null) { // if in split pane mode, the tool bar will be present
            // in the appBarLayouts absense.
            toolbar.title = promotion!!.title
        }
    }

    companion object {

        val ARG_ITEM_ID = "item_id"

        fun newInstance(itemId: String) : PromotionDetailFragment {
            val fragment = PromotionDetailFragment()

            val arguments = Bundle()
            arguments.putString(ARG_ITEM_ID, itemId)
            fragment.arguments = arguments

            return fragment
        }

    }
}
