package software.credible.abercrombiefitchkata.remote

import io.reactivex.Single
import retrofit2.http.GET
import software.credible.abercrombiefitchkata.dto.PromotionsResponseDto

interface PromotionsApi {

    @get:GET("promotions.json")
    val promotions: Single<PromotionsResponseDto>

}
