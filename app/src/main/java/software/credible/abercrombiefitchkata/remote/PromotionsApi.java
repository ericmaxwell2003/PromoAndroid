package software.credible.abercrombiefitchkata.remote;

import io.reactivex.Single;
import retrofit2.http.GET;
import software.credible.abercrombiefitchkata.dto.PromotionsResponseDto;

public interface PromotionsApi {

    @GET("promotions.json")
    Single<PromotionsResponseDto> getPromotions();

}
