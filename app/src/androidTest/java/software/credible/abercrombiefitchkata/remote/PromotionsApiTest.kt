package software.credible.abercrombiefitchkata.remote

import android.support.test.filters.SmallTest
import android.support.test.runner.AndroidJUnit4
import com.squareup.okhttp.mockwebserver.MockResponse
import com.squareup.okhttp.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.HttpException
import software.credible.abercrombiefitchkata.TestConstants.Companion.TEST_ANF_FULL_RESPONSE
import java.io.IOException

@RunWith(AndroidJUnit4::class)
@SmallTest
class PromotionsApiTest {

    private lateinit var promotionsApi: PromotionsApi
    private var mockWebServer: MockWebServer? = null

    @Before
    @Throws(Exception::class)
    fun setUp() {

        mockWebServer = MockWebServer()
        mockWebServer?.start()
        promotionsApi = PromotionsApiFactory.createService(mockWebServer?.getUrl("/").toString())
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        shutdownMockWebServer()
    }

    @Test
    @Throws(Exception::class)
    fun testParsesAnfFullResponseParsesWithoutError() {

        mockWebServer?.enqueue(MockResponse()
                .setResponseCode(200)
                .setBody(TEST_ANF_FULL_RESPONSE))

        val promotionsResponse = promotionsApi.promotions.blockingGet()
        assertEquals(2, promotionsResponse.promotions?.size)
    }

    @Test
    fun testPromotionContentsParse() {

        mockWebServer?.enqueue(MockResponse()
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
                        "}"))

        val promotionsResponse = promotionsApi.promotions.blockingGet()
        assertEquals(1, promotionsResponse.promotions?.size)
        val promotion = promotionsResponse.promotions?.get(0)

        assertEquals(1, promotion?.buttons?.size)
        val button = promotion?.buttons?.get(0)
        assertEquals("target", button?.targetUrl)
        assertEquals("title", button?.title)

        assertEquals("description", promotion?.description)
        assertEquals("title", promotion?.title)
        assertEquals("image", promotion?.imageUrl)
        assertEquals("footer", promotion?.footer)
    }

    @Test
    fun testPromotionButtonObjectParsesToButtonList() {

        mockWebServer?.enqueue(MockResponse()
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
                        "}"))

        val promotion = promotionsApi.promotions.blockingGet().promotions?.get(0)

        assertEquals(1, promotion?.buttons?.size)
        val button = promotion?.buttons?.get(0)
        assertEquals("target", button?.targetUrl)
        assertEquals("title", button?.title)
    }

    @Test
    fun testPromotionButtonListParsesToButtonList() {

        mockWebServer?.enqueue(MockResponse()
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
                        "}"))

        val promotion = promotionsApi.promotions.blockingGet().promotions?.get(0)

        assertEquals(1, promotion?.buttons?.size)
        val button = promotion?.buttons?.get(0)
        assertEquals("target", button?.targetUrl)
        assertEquals("title", button?.title)
    }

    @Test
    fun testNon200ReponseThrowsException() {

        mockWebServer?.enqueue(MockResponse().setResponseCode(404))

        var threwError = false
        try {
            promotionsApi.promotions.blockingGet()
        } catch (e: HttpException) {
            threwError = true
        }

        assertTrue(threwError)
    }


    private fun shutdownMockWebServer() {
        try {
            if (mockWebServer != null) {
                mockWebServer?.shutdown()
                mockWebServer = null
            }
        } catch (e: IOException) {
            fail(e.message)
        }

    }
}
