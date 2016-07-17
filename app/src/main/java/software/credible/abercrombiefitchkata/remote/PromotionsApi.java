package software.credible.abercrombiefitchkata.remote;

import retrofit.http.GET;
import software.credible.abercrombiefitchkata.dto.PromotionsResponseDto;

public interface PromotionsApi {

    @GET("/promotions.json")
    PromotionsResponseDto getPromotions();

}
