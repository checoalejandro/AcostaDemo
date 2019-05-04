package com.acostadev.acostademo.main


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.acostadev.acostademo.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun englishLanguageDetection() {
        val appCompatEditText = onView(
                allOf(withId(R.id.textInput),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`("androidx.cardview.widget.CardView")),
                                        0),
                                0),
                        isDisplayed()))
        appCompatEditText.perform(replaceText("This is an example with one language"), closeSoftKeyboard())
        Thread.sleep(1000)
        val textView = onView(
                allOf(withId(R.id.txtResult), withText("English"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java),
                                        0),
                                2),
                        isDisplayed()))

        textView.check(matches(withText("English")))
    }

    @Test
    fun spanishLanguageDetection() {
        val appCompatEditText = onView(
                allOf(withId(R.id.textInput),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`("androidx.cardview.widget.CardView")),
                                        0),
                                0),
                        isDisplayed()))
        appCompatEditText.perform(replaceText("Este es un ejemplo de otro idioma."), closeSoftKeyboard())
        Thread.sleep(1000)
        val textView = onView(
                allOf(withId(R.id.txtResult), withText("Spanish"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java),
                                        0),
                                2),
                        isDisplayed()))
        textView.check(matches(withText("Spanish")))
    }

    private fun childAtPosition(
            parentMatcher: Matcher<View>, position: Int): Matcher<View> {

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
