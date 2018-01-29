package software.credible.abercrombiefitchkata.remote

import android.app.IntentService
import android.content.Intent
import android.util.Log

import io.realm.Realm
import software.credible.abercrombiefitchkata.domain.Promotion
import software.credible.abercrombiefitchkata.dto.PromotionDto
import software.credible.abercrombiefitchkata.dto.PromotionsResponseDto

/**
 * Fetches new promos and updates the database.
 */
class FetchPromosIntentService : IntentService(FetchPromosIntentService::class.java.name) {

    private var promotionsApi: PromotionsApi? = null
    private val tag = "FETCH_PROMOS_INTENT_SVC"

    fun setPromotionsApi(promotionsApi: PromotionsApi) {
        this.promotionsApi = promotionsApi
    }

    override fun onCreate() {
        super.onCreate()
        if (promotionsApi == null) {
            promotionsApi = PromotionsApiFactory.createService()
        }
    }

    public override fun onHandleIntent(intent: Intent?) {
        Log.i(tag, "Fetching promos....")
        try {
            Realm.getDefaultInstance().use { r ->
                val promotionDtos = fetchPromotions()
                r.executeTransaction {
                    r.deleteAll()
                    if(promotionDtos != null) {
                        for (promotionDto in promotionDtos) {
                            val promotion = Promotion.fromDto(promotionDto)
                            r.copyToRealm(promotion)
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(tag, "Error fetching promos...", e)
        }

    }

    private fun fetchPromotions(): List<PromotionDto>? {
        val promotionsResponseDto = promotionsApi?.promotions?.blockingGet()
        return promotionsResponseDto?.promotions
    }

}
