package software.credible.abercrombiefitchkata.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.squareup.picasso.Picasso

import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter
import kotlinx.android.synthetic.main.promotion_list_content.view.*
import software.credible.abercrombiefitchkata.R
import software.credible.abercrombiefitchkata.domain.Promotion

class PromotionRecyclerViewAdapter(private val mPromoListActivity: PromotionListActivity,
                                   promotions: OrderedRealmCollection<Promotion>) :
        RealmRecyclerViewAdapter<Promotion, PromotionRecyclerViewAdapter.ViewHolder>(
                promotions, true, true) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.promotion_list_content, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val promotion = data?.get(position)
        holder.promotionId = promotion?.id ?: ""
        Picasso.with(mPromoListActivity).load(promotion?.imageUrl).into(holder.imageView)
        holder.titleView?.text = promotion?.title ?: ""

        holder.view?.setOnClickListener { v ->
            if (mPromoListActivity.isTwoPane) {
                val fragment = PromotionDetailFragment.newInstance(itemId = holder.promotionId)

                mPromoListActivity.supportFragmentManager.beginTransaction()
                        .replace(R.id.promotion_detail_container, fragment)
                        .commit()
            } else {
                val context = v.context
                val intent = Intent(context, PromotionDetailActivity::class.java)
                intent.putExtra(PromotionDetailFragment.ARG_ITEM_ID, holder.promotionId)

                context.startActivity(intent)
            }
        }
    }

    inner class ViewHolder(val view: View?) : RecyclerView.ViewHolder(view) {
        val imageView = view?.image
        val titleView = view?.title
        var promotionId: String = ""

        override fun toString(): String {
            return super.toString() + " '" + titleView?.text + "'"
        }
    }
}