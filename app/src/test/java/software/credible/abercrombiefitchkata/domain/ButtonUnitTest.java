package software.credible.abercrombiefitchkata.domain;

import org.junit.Test;

import software.credible.abercrombiefitchkata.dto.ButtonDto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Unit level test for button.
 */
public class ButtonUnitTest {

    @Test
    public void itTranslatesFromButtonDtoToButtonWithoutLosingData() {
        ButtonDto buttonDto = new ButtonDto();
        buttonDto.setTargetUrl("SomeUrl");
        buttonDto.setTitle("SomeTitle");

        Button button = Button.fromDto(buttonDto);

        assertEquals("SomeUrl", button.getTargetUrl());
        assertEquals("SomeTitle", button.getTitle());
    }

    @Test
    public void itGeneratesAnIdWhenTranslatingFromButtonDtoToButton() {
        ButtonDto buttonDto = new ButtonDto();
        Button button = Button.fromDto(buttonDto);
        assertNotNull(button.getId());
    }

}
