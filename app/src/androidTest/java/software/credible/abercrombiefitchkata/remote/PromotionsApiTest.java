package software.credible.abercrombiefitchkata.remote;

import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import retrofit2.HttpException;
import software.credible.abercrombiefitchkata.dto.ButtonDto;
import software.credible.abercrombiefitchkata.dto.PromotionDto;
import software.credible.abercrombiefitchkata.dto.PromotionsResponseDto;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static software.credible.abercrombiefitchkata.TestConstants.TEST_ANF_FULL_RESPONSE;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class PromotionsApiTest {

    private PromotionsApi promotionsApi;
    private MockWebServer mockWebServer;

    @Before
    public void setUp() throws Exception {

        mockWebServer = new MockWebServer();
        mockWebServer.start();
        promotionsApi = PromotionsApiFactory.createService(mockWebServer.getUrl("/").toString());
    }

    @After
    public void tearDown() throws Exception {
        shutdownMockWebServer();
    }

    @Test
    public void testParsesAnfFullResponseParsesWithoutError() throws Exception {

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(TEST_ANF_FULL_RESPONSE));

        PromotionsResponseDto promotionsResponse = promotionsApi.getPromotions().blockingGet();
        assertEquals(2, promotionsResponse.getPromotions().size());
    }

    @Test
    public void testPromotionContentsParse() throws Exception {

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\n" +
                        "  \"promotions\": [\n" +
                        "    {\n" +
                        "      \"button\": {\n" +
                        "        \"target\": \"target\", \n" +
                        "        \"title\": \"title\"\n" +
                        "      }, \n" +
                        "      \"description\": \"description\", \n" +
                        "      \"footer\": \"footer\", \n" +
                        "      \"image\": \"image\", \n" +
                        "      \"title\": \"title\"\n" +
                        "    }\n" +
                        "  ]\n" +
                        "}"));

        PromotionsResponseDto promotionsResponse = promotionsApi.getPromotions().blockingGet();
        assertEquals(1, promotionsResponse.getPromotions().size());
        PromotionDto promotion = promotionsResponse.getPromotions().get(0);

        assertEquals(1, promotion.getButtons().size());
        ButtonDto button = promotion.getButtons().get(0);
        assertEquals("target", button.getTargetUrl());
        assertEquals("title", button.getTitle());

        assertEquals("description", promotion.getDescription());
        assertEquals("title", promotion.getTitle());
        assertEquals("image", promotion.getImageUrl());
        assertEquals("footer", promotion.getFooter());
    }

    @Test
    public void testPromotionButtonObjectParsesToButtonList() throws Exception {

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\n" +
                        "  \"promotions\": [\n" +
                        "    {\n" +
                        "      \"button\": {\n" +
                        "        \"target\": \"target\", \n" +
                        "        \"title\": \"title\"\n" +
                        "      }\n" +
                        "    }\n" +
                        "  ]\n" +
                        "}"));

        PromotionDto promotion = promotionsApi.getPromotions().blockingGet().getPromotions().get(0);

        assertEquals(1, promotion.getButtons().size());
        ButtonDto button = promotion.getButtons().get(0);
        assertEquals("target", button.getTargetUrl());
        assertEquals("title", button.getTitle());
    }

    @Test
    public void testPromotionButtonListParsesToButtonList() throws Exception {

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\n" +
                        "  \"promotions\": [\n" +
                        "    {\n" +
                        "      \"button\": [{\n" +
                        "        \"target\": \"target\", \n" +
                        "        \"title\": \"title\"\n" +
                        "      }]\n" +
                        "    }\n" +
                        "  ]\n" +
                        "}"));

        PromotionDto promotion = promotionsApi.getPromotions().blockingGet().getPromotions().get(0);

        assertEquals(1, promotion.getButtons().size());
        ButtonDto button = promotion.getButtons().get(0);
        assertEquals("target", button.getTargetUrl());
        assertEquals("title", button.getTitle());
    }

    @Test
    public void testNon200ReponseThrowsException() throws Exception {

        mockWebServer.enqueue(new MockResponse().setResponseCode(404));

        boolean threwError = false;
        try {
            promotionsApi.getPromotions().blockingGet();
        } catch (HttpException e) {
            threwError = true;
        }
        assertTrue(threwError);
    }


    private void shutdownMockWebServer() {
        try {
            if (mockWebServer != null) {
                mockWebServer.shutdown();
                mockWebServer = null;
            }
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }
}
