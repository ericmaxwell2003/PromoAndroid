package software.credible.abercrombiefitchkata.ui;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.hamcrest.CoreMatchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.realm.Realm;
import software.credible.abercrombiefitchkata.R;
import software.credible.abercrombiefitchkata.domain.Promotion;
import software.credible.abercrombiefitchkata.dto.PromotionDto;
import software.credible.abercrombiefitchkata.dto.PromotionsResponseDto;
import software.credible.abercrombiefitchkata.remote.PromotionsApiFactory;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.allOf;
import static software.credible.abercrombiefitchkata.TestConstants.TEST_ANF_FULL_RESPONSE;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class PromotionListActivityTest {

    @Rule
    public ActivityTestRule<PromotionListActivity> mActivityTestRule = new ActivityTestRule<>(PromotionListActivity.class);


    @Test
    public void attractiveNoDataScreenPresence() {
        clearData();
        assertTheNoPromosViewIsShown();
    }

    @Test
    public void promotionListActivityContentsTest() {
        clearData();
        loadTestData();
        assertThePromoListViewIsShown();

        assertRefreshButtonShown();

        assertEntryWithTitleShown("Shorts Starting at $25");
        assertEntryWithTitleShown("Dolce Vita");
    }

    @Test
    public void promotionListActivityFirstEntryDetailTest() {
        clearData();
        loadTestData();
        goToDetailForItem(0);
        assertDetailScreenContents("GET READY FOR SUMMER DAYS", "Shop Now", "In stores & online. Exclusions apply. See details");
        goBackToListView();
        assertThePromoListViewIsShown();
    }

    @Test
    public void promotionListActivitySecondEntryDetailTest() {
        clearData();
        loadTestData();
        goToDetailForItem(1);
        assertDetailScreenContents("Our Favorite Brands", "Shop Now", null);
        goBackToListView();
        assertThePromoListViewIsShown();
    }

    private void assertDetailScreenContents(String description, String buttonText, String footerText) {

        // Check image element present
        ViewInteraction imageView3 = onView(
                allOf(withId(R.id.promotion_detail_image), withContentDescription("Promotional Image"), isDisplayed()));
        imageView3.check(matches(isDisplayed()));

        // Check promotion description contents
        ViewInteraction textView3 = onView(
                allOf(withId(R.id.promotion_detail), withText(description), isDisplayed()));
        textView3.check(matches(withText(description)));

        // Check button name.
        ViewInteraction button = onView(
                allOf(withId(R.id.promotion_button), withText(buttonText), isDisplayed()));
        button.check(matches(isDisplayed()));

        // Check footer contents or lack of presence if there isn't one.
        if(footerText == null) {
            onView(withId(R.id.promotion_footer)).check(matches(not(isDisplayed())));
        } else {
            // TODO : Check Orientation
            ViewInteraction textView4 = onView(
                    allOf(withId(R.id.promotion_footer), withText(footerText), isDisplayed()));
            textView4.check(matches(withText(footerText)));
        }

    }

    private void goToDetailForItem(int itemIdx) {
        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.promotion_list),
                        withParent(withId(R.id.frameLayout)),
                        isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(itemIdx, click()));
    }

    private void goBackToListView() {
        ViewInteraction imageButton2 = onView(
                allOf(withContentDescription("Navigate up"),
                        withParent(allOf(withId(R.id.detail_toolbar),
                                withParent(withId(R.id.toolbar_layout)))),
                        isDisplayed()));
        imageButton2.perform(click());
    }

    private void assertRefreshButtonShown() {
        ViewInteraction imageButton = onView(
                allOf(withId(R.id.fab), isDisplayed()));
        imageButton.check(matches(isDisplayed()));
    }

    private void assertEntryWithTitleShown(String titleText) {
        ViewInteraction textView = onView(
                allOf(withId(R.id.title), withText(titleText), isDisplayed()));
        textView.check(matches(withText(titleText)));
    }

    private void assertThePromoListViewIsShown() {
        onView(CoreMatchers.allOf(withId(R.id.empty_view), not(isDisplayed())));
        onView(CoreMatchers.allOf(withId(R.id.no_data_message), not(isDisplayed())));
        onView(CoreMatchers.allOf(withId(R.id.promotion_list), isDisplayed()));
    }

    private void assertTheNoPromosViewIsShown() {
        onView(CoreMatchers.allOf(withId(R.id.empty_view), isDisplayed()));
        onView(CoreMatchers.allOf(withId(R.id.no_data_message), isDisplayed()));
        onView(CoreMatchers.allOf(withId(R.id.promotion_list), not(isDisplayed())));
    }


    private void clearData() {
        try(Realm r = Realm.getDefaultInstance()) {
            r.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    r.deleteAll();
                }
            });
        }
    }

    /**
     * Choosing to load data from JSON, manually into the database.  I could actually execute
     * the FetchPromosIntentService to do this and go against an external URL, but decided not
     * to for this Kata for 2 reasons.
     *
     * <ol>
     * <li>I don't have control over the environment in which these tests run and cannot garantee
     * internet connectivity.</li>
     * <li>I don't have control over the test data, like I might if I worked for AnF.  I don't
     * know if it can change and if it does, it would break this test.  So in this way I have
     * control over the data.</li>
     * </ol>
     */
    private void loadTestData() {
        try(Realm r = Realm.getDefaultInstance()) {
            r.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    PromotionsResponseDto promotionsResponseDto =
                            PromotionsApiFactory.gson().fromJson(TEST_ANF_FULL_RESPONSE, PromotionsResponseDto.class);
                    for(PromotionDto promotionDto : promotionsResponseDto.getPromotions()) {
                        Promotion promotion = Promotion.fromDto(promotionDto);
                        r.copyToRealm(promotion);
                    }
                }
            });
        }
    }


}
