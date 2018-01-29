package software.credible.abercrombiefitchkata.domain

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import java.util.ArrayList
import java.util.Arrays

import software.credible.abercrombiefitchkata.dto.ButtonDto
import software.credible.abercrombiefitchkata.dto.PromotionDto

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull

/**
 * Unit tests for promotions.
 */
@RunWith(JUnit4::class)
class PromotionUnitTest {

    @Test
    fun itTranslatesFromPromotionDtoToPromotionWithoutLosingData() {

        val promotionDto = PromotionDto()
        promotionDto.title = "Title"
        promotionDto.imageUrl = "ImageUrl"
        promotionDto.description = "Description"
        promotionDto.footer = "Footer"

        val buttonDto = ButtonDto()
        buttonDto.title = "ButtonTitle"
        buttonDto.targetUrl = "ButtonTargetUrl"
        promotionDto.buttons = ArrayList(Arrays.asList(buttonDto))


        val promotion = Promotion.fromDto(promotionDto)

        assertEquals("Title", promotion.title)
        assertEquals("ImageUrl", promotion.imageUrl)
        assertEquals("Description", promotion.description)
        assertEquals("Footer", promotion.footer)
        assertEquals(1, promotion.buttons?.size)
        val button = promotion.buttons?.get(0)
        assertEquals("ButtonTitle", button!!.title)
        assertEquals("ButtonTargetUrl", button.targetUrl)
    }

    @Test
    fun itGeneratesAnIdWhenTranslatingFromPromotionDtoToPromotion() {
        val dto = PromotionDto()
        val promotion = Promotion.fromDto(dto)
        assertNotNull(promotion.id)
    }

    @Test
    fun itHandlesTranslatingFromPromotionDtoToPromotionGracefullyWhenThereAreNoButtonsSet() {
        val dto = PromotionDto()
        val promotion = Promotion.fromDto(dto)
        assertNotNull(promotion.id)
    }

}
