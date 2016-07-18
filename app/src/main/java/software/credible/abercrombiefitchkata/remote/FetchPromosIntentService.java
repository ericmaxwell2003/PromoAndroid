package software.credible.abercrombiefitchkata.remote;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import java.util.List;

import io.realm.Realm;
import software.credible.abercrombiefitchkata.domain.Promotion;
import software.credible.abercrombiefitchkata.dto.PromotionDto;
import software.credible.abercrombiefitchkata.dto.PromotionsResponseDto;

/**
 * Fetches new promos and updates the database.
 */
public class FetchPromosIntentService extends IntentService {

    public static final String TAG = FetchPromosIntentService.class.getName();

    public FetchPromosIntentService() {
        super(FetchPromosIntentService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "Fetching promos....");
        try(Realm r = Realm.getDefaultInstance()) {
            final List<PromotionDto> promotionDtos = fetchPromotions();
            r.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    r.deleteAll();
                    for(PromotionDto promotionDto : promotionDtos) {
                        Promotion promotion = Promotion.fromDto(promotionDto);
                        r.copyToRealm(promotion);
                    }
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Error fetching promos...", e);
        }
    }

    private List<PromotionDto> fetchPromotions() {
        PromotionsApi promotionsApi = PromotionsApiFactory.createService();
        PromotionsResponseDto promotionsResponseDto = promotionsApi.getPromotions();
        return promotionsResponseDto.getPromotions();
    }

}
