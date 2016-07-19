package software.credible.abercrombiefitchkata.domain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Arrays;

import software.credible.abercrombiefitchkata.dto.ButtonDto;
import software.credible.abercrombiefitchkata.dto.PromotionDto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Unit tests for promotions.
 */
@RunWith(JUnit4.class)
public class PromotionUnitTest {

    @Test
    public void itTranslatesFromPromotionDtoToPromotionWithoutLosingData() {

        PromotionDto promotionDto = new PromotionDto();
        promotionDto.setTitle("Title");
        promotionDto.setImageUrl("ImageUrl");
        promotionDto.setDescription("Description");
        promotionDto.setFooter("Footer");

        ButtonDto buttonDto = new ButtonDto();
        buttonDto.setTitle("ButtonTitle");
        buttonDto.setTargetUrl("ButtonTargetUrl");
        promotionDto.setButtons(new ArrayList<>(Arrays.asList(buttonDto)));


        Promotion promotion = Promotion.fromDto(promotionDto);


        assertEquals("Title", promotion.getTitle());
        assertEquals("ImageUrl", promotion.getImageUrl());
        assertEquals("Description", promotion.getDescription());
        assertEquals("Footer", promotion.getFooter());
        assertEquals(1, promotion.getButtons().size());
        Button button = promotion.getButtons().get(0);
        assertEquals("ButtonTitle", button.getTitle());
        assertEquals("ButtonTargetUrl", button.getTargetUrl());
    }

    @Test
    public void itGeneratesAnIdWhenTranslatingFromPromotionDtoToPromotion() {
        PromotionDto dto = new PromotionDto();
        Promotion promotion = Promotion.fromDto(dto);
        assertNotNull(promotion.getId());
    }

    @Test
    public void itHandlesTranslatingFromPromotionDtoToPromotionGracefullyWhenThereAreNoButtonsSet() {
        PromotionDto dto = new PromotionDto();
        Promotion promotion = Promotion.fromDto(dto);
        assertNotNull(promotion.getId());
    }

}
