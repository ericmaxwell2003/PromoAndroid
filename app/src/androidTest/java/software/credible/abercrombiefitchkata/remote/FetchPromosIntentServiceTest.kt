package software.credible.abercrombiefitchkata.remote

import android.content.Intent
import android.support.test.filters.SmallTest
import android.support.test.runner.AndroidJUnit4
import io.reactivex.Single
import io.realm.Realm
import io.realm.RealmList
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import software.credible.abercrombiefitchkata.domain.Button
import software.credible.abercrombiefitchkata.domain.Promotion
import software.credible.abercrombiefitchkata.dto.ButtonDto
import software.credible.abercrombiefitchkata.dto.PromotionDto
import software.credible.abercrombiefitchkata.dto.PromotionsResponseDto


@RunWith(AndroidJUnit4::class)
@SmallTest
class FetchPromosIntentServiceTest {

    private lateinit var intentService: FetchPromosIntentService
    private lateinit var fakePromotionsApi: FakePromotionsApi

    @Before
    @Throws(Exception::class)
    fun setUp() {
        fakePromotionsApi = FakePromotionsApi()
        intentService = FetchPromosIntentService()
        intentService.setPromotionsApi(fakePromotionsApi)
    }


    @Test
    fun testAnExceptionIsThrownDuringFetchThenNoChangesHappenToData() {
        setupDatabaseWithNumberOfPromotions(2)
        fakePromotionsApi.onGetPromotionsThrowException(RuntimeException("Pretending an error occurred"))

        intentService.onHandleIntent(Intent())

        assertPromotionCountInDatabase(2)
    }

    @Test
    fun testWhenRemoteDataIsFoundTheyAreReplacedInTheDatabase() {
        setupDatabaseWithNumberOfPromotions(2)
        fakePromotionsApi.onGetPromotionsReturn(promotionReponseWithPromoCount(3))

        intentService.onHandleIntent(Intent())

        assertPromotionCountInDatabase(3)
    }

    @Test
    fun testTheFetchReturnsEmptyDataThenTheResultIsAnEmptyDatabase() {
        setupDatabaseWithNumberOfPromotions(3)
        fakePromotionsApi.onGetPromotionsReturn(promotionReponseWithPromoCount(0))

        intentService.onHandleIntent(Intent())

        assertPromotionCountInDatabase(0)
    }

    private fun promotionReponseWithPromoCount(promoCount: Int): PromotionsResponseDto {
        val promotionsResponseDto = PromotionsResponseDto()
        val promotionStubs = mutableListOf<PromotionDto>()

        for (i in 0 until promoCount) {
            val promotion = PromotionDto()
            promotion.imageUrl = "url"
            promotion.title = "title"
            promotion.footer = "footer"
            promotion.description = "description"
            promotion.buttons = listOf(createButtonDto())
            promotionStubs.add(promotion)
        }
        promotionsResponseDto.promotions = promotionStubs
        return promotionsResponseDto
    }

    private fun assertPromotionCountInDatabase(expectedCount: Int) {
        Realm.getDefaultInstance().use { r -> assertEquals(expectedCount.toLong(), r.where(Promotion::class.java).count()) }
    }

    private fun setupDatabaseWithNumberOfPromotions(promoCount: Int) {
        Realm.getDefaultInstance().use { r ->
            r.beginTransaction()
            r.deleteAll()
            for (i in 0 until promoCount) {
                val promotion = Promotion()
                promotion.id = i.toString()
                promotion.imageUrl = "url"
                promotion.title = "title"
                promotion.footer = "footer"
                promotion.description = "description"
                promotion.buttons = RealmList()
                promotion.buttons?.add(createButton())
                r.copyToRealm(promotion)
            }
            r.commitTransaction()
        }
    }

    private fun createButtonDto(): ButtonDto {
        val button = ButtonDto()
        button.targetUrl = "URL"
        button.title = "Title"
        return button
    }

    private fun createButton(): Button {
        val button = Button()
        button.id = "Button ID"
        button.targetUrl = "URL"
        button.title = "Title"
        return button
    }

    private class FakePromotionsApi : PromotionsApi {

        private var exception: RuntimeException? = null
        private var promotionsResponseDto: PromotionsResponseDto? = null

        fun onGetPromotionsReturn(promotionsResponseDto: PromotionsResponseDto) {
            this.promotionsResponseDto = promotionsResponseDto
        }

        fun onGetPromotionsThrowException(exception: RuntimeException) {
            this.exception = exception
        }

        override val promotions: Single<PromotionsResponseDto>
            get() {
                return if (this.exception != null) {
                    throw exception!!
                } else {
                    Single.just(promotionsResponseDto)
                }
            }
    }

}
