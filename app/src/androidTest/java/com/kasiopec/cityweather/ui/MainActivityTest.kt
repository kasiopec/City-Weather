package com.kasiopec.cityweather.ui

import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.kasiopec.cityweather.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest{

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)
    /**
     * Quick check if mainlayout is loaded
     * **/
    @Test
    fun isActivityInView(){
        onView(withId(R.id.mainCoordLayout)).check(matches(isDisplayed()))
    }
    /**
     * Quick check if fab is loaded
     * **/
    @Test
    fun isFabDisplayed(){
        onView(withId(R.id.fab)).check(matches(isDisplayed()))
    }
    /**
     * Test if Dialog box appears when fab is clicked
     * **/
    @Test
    fun isNewCityDialogDisplayed(){
        onView(withId(R.id.fab)).perform(click())
        onView(withId(R.id.customDialogLayout)).check(matches(isDisplayed()))
    }
    /**
     * Test fragment if MainFragment is added to the main view
     * **/
    @Test
    fun isMainFragmentAttached(){
        onView(withId(R.id.mainFragmentLayout)).check(matches(isDisplayed()))
        onView(withId(R.id.swipeContainer)).check(matches(isDisplayed()))
        onView(withId(R.id.weatherRecycler)).check(matches(isDisplayed()))
    }

    /**
     * Tests if a new card is added when a city name is entered into the dialog box
     * and action is confirmed. Sometimes test fails because it is not able to find the card at
     * specified positions, removing test app from the task manager fixes the issue.
     * **/
    @Test
    fun isCardAddedWithName(){
        val floatingActionButton = onView(
            Matchers.allOf(
                withId(R.id.fab),
                childAtPosition(
                    Matchers.allOf(
                        withId(R.id.mainCoordLayout),
                        childAtPosition(
                            withId(android.R.id.content),
                            0
                        )
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        floatingActionButton.perform(click())
        onView(withId(R.id.customDialogLayout)).check(matches(isDisplayed()))
        onView(withId(R.id.newCityEditText)).check(matches(isDisplayed()))
        onView(withId(R.id.newCityEditText)).perform(ViewActions.replaceText("Rīga"), ViewActions.closeSoftKeyboard())
        onView(withId(R.id.btnYes)).check(matches(isDisplayed())).perform(click())
        onView(withId(R.id.customDialogLayout)).check(doesNotExist())
        val viewGroup = onView(
            Matchers.allOf(
                childAtPosition(
                    Matchers.allOf(
                        withId(R.id.cardView),
                        childAtPosition(
                            IsInstanceOf.instanceOf(ViewGroup::class.java),
                            0
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        viewGroup.check(matches(isDisplayed()))

        val textView = onView(
            Matchers.allOf(
                withId(R.id.cityName), withText("Rīga"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.cardView),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textView.check(matches(withText("Rīga")))
    }
    /**
     * Tests if second fragment is loaded when "More details" is pressed.
     * Requires carditem to be present, you can run isCardAddedWithName test to add the card item
     * **/
    @Test
    fun testIfSecondFragmentIsLoadedWhenMoreDetailsIsPressed(){
        val weatherItemCard = onView(
            Matchers.allOf(
                childAtPosition(
                    Matchers.allOf(
                        withId(R.id.cardView),
                        childAtPosition(
                            IsInstanceOf.instanceOf(ViewGroup::class.java),
                            0
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        weatherItemCard.check(matches(isDisplayed()))

        val moreDetails = onView(Matchers.allOf(withId(R.id.moreDetails), withText("More details"), isDisplayed()))
        moreDetails.perform(click())
        onView(withId(R.id.detailsFragmentLayout)).check(matches(isDisplayed()))
    }
    /**
     * Tests if first fragment is loaded when back is pressed.
     * Requires carditem to be present, you can run isCardAddedWithName test to add the card item
     * **/
    @Test
    fun testIfFirstFragmentIsLoadedWhenBackPressed(){
        val weatherItemCard = onView(
            Matchers.allOf(
                childAtPosition(
                    Matchers.allOf(
                        withId(R.id.cardView),
                        childAtPosition(
                            IsInstanceOf.instanceOf(ViewGroup::class.java),
                            0
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        weatherItemCard.check(matches(isDisplayed()))

        val moreDetails = onView(Matchers.allOf(withId(R.id.moreDetails), withText("More details"), isDisplayed()))
        moreDetails.perform(click())
        onView(withId(R.id.detailsFragmentLayout)).check(matches(isDisplayed()))
        pressBack()
        onView(withId(R.id.mainFragmentLayout)).check(matches(isDisplayed()))
    }

    /**
     * Tests if Delete Dialog box is shown when delete icon is press.
     * Requires carditem to be present, you can run isCardAddedWithName test to add the card item
     * **/
    @Test
    fun testIfDeleteIconCallsDeleteDialogBox(){
        val deleteIcon = onView(
            Matchers.allOf(
                withId(R.id.delete), withContentDescription("Close icon"), isDisplayed()))
        deleteIcon.perform(click())
        val deleteButton = onView(
            Matchers.allOf(
                withId(android.R.id.button1), withText("Delete"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.buttonPanel),
                        0
                    ),
                    3
                ), isDisplayed()
            )
        )
    }

    /**
     * Tests if deletion of the item happens on the screen, when delete button is pressed inside
     * delete dialog box.
     * Requires carditem to be present, you can run isCardAddedWithName test to add the card item
     * **/
    @Test
    fun testIfDeleteDialogBoxDeletes(){
        val deleteIcon = onView(
            Matchers.allOf(
                withId(R.id.delete), withContentDescription("Close icon"), isDisplayed()))
        deleteIcon.perform(click())
        val deleteButton = onView(
            Matchers.allOf(
                withId(android.R.id.button1), withText("Delete"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.buttonPanel),
                        0
                    ),
                    3
                ), isDisplayed()
            )
        )
        deleteButton.perform(ViewActions.scrollTo(), click())
        val weatherItemCard = onView(
            Matchers.allOf(
                childAtPosition(
                    Matchers.allOf(
                        withId(R.id.cardView),
                        childAtPosition(
                            IsInstanceOf.instanceOf(ViewGroup::class.java),
                            0
                        )
                    ),
                    0
                )
            )
        )
        weatherItemCard.check(doesNotExist())
    }

    /**
     * Tests if edit box shows an error message when no city name was entered.
     * **/
    @Test
    fun testIfEmptyCityShowsAnError(){
        val floatingActionButton = onView(
            Matchers.allOf(
                withId(R.id.fab),
                childAtPosition(
                    Matchers.allOf(
                        withId(R.id.mainCoordLayout),
                        childAtPosition(
                            withId(android.R.id.content),
                            0
                        )
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        floatingActionButton.perform(click())
        onView(withId(R.id.customDialogLayout)).check(matches(isDisplayed()))
        onView(withId(R.id.newCityEditText)).check(matches(isDisplayed()))
        onView(withId(R.id.btnYes)).check(matches(isDisplayed())).perform(click())
        val textView = onView(
            Matchers.allOf(
                withId(R.id.textinput_error), withText("Please enter city name"),
                childAtPosition(
                    childAtPosition(
                        IsInstanceOf.instanceOf(LinearLayout::class.java),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textView.check(matches(isDisplayed()))
        textView.check(matches(withText("Please enter city name")))
    }


    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}