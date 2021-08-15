package com.demo.composetest

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import com.demo.composetest.ui.components.RallyTopAppBar
import com.demo.composetest.ui.overview.OverviewBody
import org.junit.Rule
import org.junit.Test

/**
 *
 * @author Randall.
 * @Date 2021-08-15.
 * @Time 22:36.
 */
class AnimatingCircleTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule(RallyActivity::class.java)

    @Test
    fun rallyTopAppBarTest() {
        composeTestRule.setContent {
            RallyTopAppBar(
                allScreens = RallyScreen.values().toList(),
                onTabSelected = {},
                currentScreen = RallyScreen.Accounts
            )
        }

        composeTestRule
            .onNodeWithContentDescription(RallyScreen.Accounts.name)
            .assertIsSelected()
    }

    @Test
    fun rallyTopAppBarTest_currentLabelExists() {
        composeTestRule.setContent {
            RallyTopAppBar(
                allScreens = RallyScreen.values().toList(),
                onTabSelected = {},
                currentScreen = RallyScreen.Accounts
            )
        }

        composeTestRule.onRoot(useUnmergedTree = true).printToLog("currentLabelExists")

        composeTestRule
            .onNode(
                hasText(RallyScreen.Accounts.name.toUpperCase(Locale.current)) and
                        hasParent(
                            hasContentDescription(RallyScreen.Accounts.name)
                        ),
                useUnmergedTree = true
            )
            .assertExists()
    }

    @Test
    fun overviewScreen_alertDisplayed() {
        composeTestRule.setContent {
            OverviewBody()
        }

        composeTestRule
            .onNodeWithText("Alerts")
            .assertIsDisplayed()
    }
}
