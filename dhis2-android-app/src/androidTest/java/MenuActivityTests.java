import android.support.test.filters.MediumTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.Gravity;

import org.dhis2.mobile.R;
import org.dhis2.mobile.ui.activities.MenuActivity;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerActions.open;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.contrib.NavigationViewActions.navigateTo;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by george on 9/8/16.
 */
@RunWith(AndroidJUnit4.class)
@MediumTest
public class MenuActivityTests {


    @Rule
    public ActivityTestRule<MenuActivity> mActivityRule = new ActivityTestRule(MenuActivity.class);

    @Test
    public void checkIfMenuIsDisplayed(){
        onView(withId(R.id.recyclerview_pickers_one)).check(matches(isDisplayed()));
    }

    @Test
    public void shouldOpenNavigation(){
        onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.LEFT))).perform(open());
    }
    @Test
    public void checkIfLetterAvatarIsDisplayed(){
        onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.LEFT))).perform(open());
        onView(withId(R.id.side_nav_photo)).check(matches(isDisplayed()));
    }
    @Test
    public void checkIfUsernameIsDisplayed(){
        onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.LEFT))).perform(open());
        onView(withId(R.id.side_nav_username)).check(matches(isDisplayed()));
    }
    @Test
    public void checkIfEmailIsDisplayed(){
        onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.LEFT))).perform(open());
        onView(withId(R.id.side_nav_email)).check(matches(isDisplayed()));
    }

    @Test
    public void shouldOpenProfileFragment(){
        onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.LEFT))).perform(open());
        onView(withId(R.id.navigation_view)).perform(navigateTo(R.id.drawer_item_profile));
        onView(withId(R.id.list_of_fields)).check(matches(isDisplayed()));
    }
    @Test
    public void shouldShowPopup(){
        onView(withId(R.id.recyclerview_pickers_one)).perform(click());
    }
}