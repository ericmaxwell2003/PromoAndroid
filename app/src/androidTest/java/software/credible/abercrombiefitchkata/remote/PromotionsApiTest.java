package software.credible.abercrombiefitchkata.remote;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;

import org.junit.After;
import org.junit.Before;

import java.io.IOException;

import software.credible.abercrombiefitchkata.dto.ButtonDto;
import software.credible.abercrombiefitchkata.dto.PromotionDto;
import software.credible.abercrombiefitchkata.dto.PromotionsResponseDto;

public class PromotionsApiTest extends ApplicationTestCase<Application> {

    private PromotionsApi promotionsApi;
    private MockWebServer mockWebServer;

    public PromotionsApiTest() {
        super(Application.class);
    }

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        createApplication();

        mockWebServer = new MockWebServer();
        mockWebServer.start();
        promotionsApi = PromotionsApiFactory.createService(mockWebServer.getUrl("/").toString());
    }

    @After
    @Override
    public void tearDown() throws Exception {
        shutdownMockWebServer();
        super.tearDown();
    }

    public void testAnfFullResponseParsesWithoutError() throws Exception {

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(TEST_ANF_FULL_RESPONSE));

        PromotionsResponseDto promotionsResponse = promotionsApi.getPromotions();
        assertEquals(2, promotionsResponse.getPromotions().size());
    }

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

        PromotionsResponseDto promotionsResponse = promotionsApi.getPromotions();
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

        PromotionDto promotion = promotionsApi.getPromotions().getPromotions().get(0);

        assertEquals(1, promotion.getButtons().size());
        ButtonDto button = promotion.getButtons().get(0);
        assertEquals("target", button.getTargetUrl());
        assertEquals("title", button.getTitle());
    }

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

        PromotionDto promotion = promotionsApi.getPromotions().getPromotions().get(0);

        assertEquals(1, promotion.getButtons().size());
        ButtonDto button = promotion.getButtons().get(0);
        assertEquals("target", button.getTargetUrl());
        assertEquals("title", button.getTitle());
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

    private static final String TEST_ANF_FULL_RESPONSE = "\n" +
            "{\n" +
            "  \"promotions\": [\n" +
            "    {\n" +
            "      \"button\": {\n" +
            "        \"target\": \"https://m.abercrombie.com\", \n" +
            "        \"title\": \"Shop Now\"\n" +
            "      }, \n" +
            "      \"description\": \"GET READY FOR SUMMER DAYS\", \n" +
            "      \"footer\": \"In stores & online. Exclusions apply. <a href=\\\"https://www.abercrombie.com/anf/media/legalText/viewDetailsText20150618_Shorts25_US.html\\\" class=\\\"legal promo-details\\\">See details</a>\", \n" +
            "      \"image\": \"http://anf.scene7.com/is/image/anf/anf-US-20150629-app-women-shorts\", \n" +
            "      \"title\": \"Shorts Starting at $25\"\n" +
            "    }, \n" +
            "    {\n" +
            "      \"button\": [\n" +
            "        {\n" +
            "          \"target\": \"https://m.hollisterco.com\", \n" +
            "          \"title\": \"Shop Now\"\n" +
            "        }\n" +
            "      ], \n" +
            "      \"description\": \"Our Favorite Brands\", \n" +
            "      \"image\": \"http://anf.scene7.com/is/image/anf/anf-US-20150629-app-women-brands\", \n" +
            "      \"title\": \"Dolce Vita\"\n" +
            "    }\n" +
            "  ]\n" +
            "}";
}
