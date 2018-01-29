package software.credible.abercrombiefitchkata.domain

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import software.credible.abercrombiefitchkata.dto.ButtonDto

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull

/**
 * Unit level test for button.
 */
@RunWith(JUnit4::class)
class ButtonUnitTest {

    @Test
    fun itTranslatesFromButtonDtoToButtonWithoutLosingData() {
        val buttonDto = ButtonDto()
        buttonDto.targetUrl = "SomeUrl"
        buttonDto.title = "SomeTitle"

        val button = Button.fromDto(buttonDto)

        assertEquals("SomeUrl", button.targetUrl)
        assertEquals("SomeTitle", button.title)
    }

    @Test
    fun itGeneratesAnIdWhenTranslatingFromButtonDtoToButton() {
        val buttonDto = ButtonDto()
        val button = Button.fromDto(buttonDto)
        assertNotNull(button.id)
    }

}
