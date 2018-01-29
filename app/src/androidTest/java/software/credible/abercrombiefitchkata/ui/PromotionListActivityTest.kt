package software.credible.abercrombiefitchkata.ui


import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import io.realm.Realm
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matchers.allOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import software.credible.abercrombiefitchkata.R
import software.credible.abercrombiefitchkata.TestConstants.Companion.TEST_ANF_FULL_RESPONSE
import software.credible.abercrombiefitchkata.domain.Promotion
import software.credible.abercrombiefitchkata.dto.PromotionsResponseDto
import software.credible.abercrombiefitchkata.remote.PromotionsApiFactory

@RunWith(AndroidJUnit4::class)
class PromotionListActivityTest {

    @get:Rule
    public val mActivityTestRule = ActivityTestRule(PromotionListActivity::class.java)

    @Before
    fun setup() {
        clearData()
    }

    @Test
    fun attractiveNoDataScreenPresence() {
        assertTheNoPromosViewIsShown()
    }

    @Test
    fun promotionListActivityContentsTest() {
        loadTestData()
        waitForSeconds(1)
        assertThePromoListViewIsShown()
        assertRefreshButtonShown()
        assertEntryWithTitleShown("Shorts Starting at $25")
        assertEntryWithTitleShown("Dolce Vita")
    }

    @Test
    fun promotionListActivityFirstEntryDetailTest() {
        loadTestData()
        waitForSeconds(1)
        goToDetailForItem(0)
        waitForSeconds(1)
        assertDetailScreenContents("GET READY FOR SUMMER DAYS", "Shop Now", "In stores & online. Exclusions apply. See details")
        goBackToListView()
        waitForSeconds(1)
        assertThePromoListViewIsShown()
    }

    @Test
    fun promotionListActivitySecondEntryDetailTest() {
        loadTestData()
        waitForSeconds(1)
        goToDetailForItem(1)
        waitForSeconds(1)
        assertDetailScreenContents("Our Favorite Brands", "Shop Now", null)
        goBackToListView()
        waitForSeconds(1)
        assertThePromoListViewIsShown()
    }

    private fun waitForSeconds(sec: Int) {
        try {
            Thread.sleep((sec * 1000).toLong())
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

    }

    private fun assertDetailScreenContents(description: String, buttonText: String, footerText: String?) {

        // Check image element present
        val imageView3 = onView(
                allOf(withId(R.id.promotion_detail_image), withContentDescription("Promotional Image"), isDisplayed()))
        imageView3.check(matches(isDisplayed()))

        // Check promotion description contents
        val textView3 = onView(
                allOf(withId(R.id.promotion_detail), withText(description), isDisplayed()))
        textView3.check(matches(withText(description)))

        // Check button name.
        val button = onView(
                allOf(withId(R.id.promotion_button), withText(buttonText), isDisplayed()))
        button.check(matches(isDisplayed()))

        // Check footer contents or lack of presence if there isn't one.
        if (footerText == null) {
            onView(withId(R.id.promotion_footer)).check(matches(not(isDisplayed())))
        } else {
            val textView4 = onView(
                    allOf(withId(R.id.promotion_footer), withText(footerText), isDisplayed()))
            textView4.check(matches(withText(footerText)))
        }

    }

    private fun goToDetailForItem(itemIdx: Int) {
        val recyclerView = onView(
                allOf(withId(R.id.promotion_list),
                        withParent(withId(R.id.frameLayout)),
                        isDisplayed()))
        recyclerView.perform(actionOnItemAtPosition<PromotionRecyclerViewAdapter.ViewHolder>(itemIdx, click()))
    }

    private fun goBackToListView() {
        val imageButton2 = onView(
                allOf(withContentDescription("Navigate up"),
                        withParent(allOf(withId(R.id.detail_toolbar),
                                withParent(withId(R.id.toolbar_layout)))),
                        isDisplayed()))
        imageButton2.perform(click())
    }

    private fun assertRefreshButtonShown() {
        val imageButton = onView(
                allOf(withId(R.id.fab), isDisplayed()))
        imageButton.check(matches(isDisplayed()))
    }

    private fun assertEntryWithTitleShown(titleText: String) {
        val textView = onView(
                allOf(withId(R.id.title), withText(titleText), isDisplayed()))
        textView.check(matches(withText(titleText)))
    }

    private fun assertThePromoListViewIsShown() {
        onView(CoreMatchers.allOf(withId(R.id.empty_view), not(isDisplayed())))
        onView(CoreMatchers.allOf(withId(R.id.no_data_message), not(isDisplayed())))
        onView(CoreMatchers.allOf(withId(R.id.promotion_list), isDisplayed()))
    }

    private fun assertTheNoPromosViewIsShown() {
        onView(CoreMatchers.allOf(withId(R.id.empty_view), isDisplayed()))
        onView(CoreMatchers.allOf(withId(R.id.no_data_message), isDisplayed()))
        onView(CoreMatchers.allOf(withId(R.id.promotion_list), not(isDisplayed())))
    }


    private fun clearData() {
        Realm.getDefaultInstance().use { r ->
            r.executeTransaction { r.deleteAll() }
        }
    }

    /**
     * Choosing to load data from JSON, manually into the database.  I could actually execute
     * the FetchPromosIntentService to do this and go against an external URL, but decided not
     * to for this Kata for 2 reasons.
     *
     *
     *  1. I don't have control over the environment in which these tests run and cannot garantee
     * internet connectivity.
     *  1. I don't have control over the test data, like I might if I worked for AnF.  I don't
     * know if it can change and if it does, it would break this test.  So in this way I have
     * control over the data.
     *
     */
    private fun loadTestData() {
        Realm.getDefaultInstance().use { r ->
            r.executeTransaction {
                val promotionsResponseDto = PromotionsApiFactory.gson()
                        .fromJson(TEST_ANF_FULL_RESPONSE, PromotionsResponseDto::class.java)
                for (promotionDto in promotionsResponseDto?.promotions ?: emptyList()) {
                    val promotion = Promotion.fromDto(promotionDto)
                    r.copyToRealm(promotion)
                }
            }
        }
    }


}
