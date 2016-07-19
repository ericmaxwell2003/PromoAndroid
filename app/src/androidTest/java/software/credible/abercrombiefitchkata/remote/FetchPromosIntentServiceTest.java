package software.credible.abercrombiefitchkata.remote;

import android.content.Intent;
import android.support.test.runner.AndroidJUnit4;
import android.test.ApplicationTestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmList;
import software.credible.abercrombiefitchkata.AnfPromoApplication;
import software.credible.abercrombiefitchkata.domain.Button;
import software.credible.abercrombiefitchkata.domain.Promotion;
import software.credible.abercrombiefitchkata.dto.ButtonDto;
import software.credible.abercrombiefitchkata.dto.PromotionDto;
import software.credible.abercrombiefitchkata.dto.PromotionsResponseDto;

public class FetchPromosIntentServiceTest extends ApplicationTestCase<AnfPromoApplication> {

    private FetchPromosIntentService intentService;

    private FakePromotionsApi fakePromotionsApi;

    public FetchPromosIntentServiceTest() {
        super(AnfPromoApplication.class);
    }

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        createApplication();
        fakePromotionsApi = new FakePromotionsApi();
        intentService = new FetchPromosIntentService();
        intentService.setPromotionsApi(fakePromotionsApi);
    }

    @Test
    public void testAnExceptionIsThrownDuringFetchThenNoChangesHappenToData() {
        setupDatabaseWithNumberOfPromotions(0);
        fakePromotionsApi.onGetPromotionsReturn(promotionReponseWithPromoCount(2));

        intentService.onHandleIntent(new Intent());

        assertPromotionCountInDatabase(2);
    }

    @Test
    public void testTheFetchReturnsEmptyDataThenTheResultIsAnEmptyDatabase() {
        setupDatabaseWithNumberOfPromotions(3);
        fakePromotionsApi.onGetPromotionsReturn(promotionReponseWithPromoCount(0));

        intentService.onHandleIntent(new Intent());

        assertPromotionCountInDatabase(0);
    }

    private PromotionsResponseDto promotionReponseWithPromoCount(int promoCount) {
        PromotionsResponseDto promotionsResponseDto = new PromotionsResponseDto();
        promotionsResponseDto.setPromotions(new ArrayList<PromotionDto>());
        for(int i = 0; i < promoCount; i++) {
            PromotionDto promotion = new PromotionDto();
            promotion.setImageUrl("url");
            promotion.setTitle("title");
            promotion.setFooter("footer");
            promotion.setDescription("description");
            promotion.setButtons(new ArrayList<ButtonDto>());
            promotion.getButtons().add(createButtonDto());
            promotionsResponseDto.getPromotions().add(promotion);
        }
        return promotionsResponseDto;
    }

    private void assertPromotionCountInDatabase(int expectedCount) {
        try(Realm r = Realm.getDefaultInstance()) {
            assertEquals(expectedCount, r.where(Promotion.class).count());
        }
    }

    private void setupDatabaseWithNumberOfPromotions(int promoCount) {
        try(Realm r = Realm.getDefaultInstance()) {
            r.beginTransaction();
            r.deleteAll();
            for(int i = 0; i < promoCount; i++) {
                Promotion promotion = new Promotion();
                promotion.setId(String.valueOf(i));
                promotion.setImageUrl("url");
                promotion.setTitle("title");
                promotion.setFooter("footer");
                promotion.setDescription("description");
                promotion.setButtons(new RealmList<Button>());
                promotion.getButtons().add(createButton());
                r.copyToRealm(promotion);
            }
            r.commitTransaction();
        }
    }

    private ButtonDto createButtonDto() {
        ButtonDto button = new ButtonDto();
        button.setTargetUrl("URL");
        button.setTitle("Title");
        return button;
    }

    private Button createButton() {
        Button button = new Button();
        button.setId("Button ID");
        button.setTargetUrl("URL");
        button.setTitle("Title");
        return button;
    }

    private static class FakePromotionsApi implements PromotionsApi {

        private PromotionsResponseDto promotionsResponseDto;

        public void onGetPromotionsReturn(PromotionsResponseDto promotionsResponseDto) {
            this.promotionsResponseDto = promotionsResponseDto;
        }

        @Override
        public PromotionsResponseDto getPromotions() {
            return promotionsResponseDto;
        }

    }

}
