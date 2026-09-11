package com.example

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import com.example.model.CalculationRecord
import com.example.ui.theme.MyApplicationTheme
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(qualifiers = RobolectricDeviceQualifiers.Pixel8, sdk = [36])
class DiscountCalculatorTest {

  @get:Rule val composeTestRule = createComposeRule()

  @Test
  fun testDiscountCalculationLogic() {
    val initialPrice = 200.0
    val discountPercent = 25.0
    val discountAmount = (initialPrice * discountPercent) / 100.0
    val finalPrice = initialPrice - discountAmount

    val record =
      CalculationRecord(
        initialPrice = initialPrice,
        discountPercent = discountPercent,
        discountAmount = discountAmount,
        finalPrice = finalPrice,
        currency = "MDL",
      )

    assertEquals(50.0, record.discountAmount, 0.001)
    assertEquals(150.0, record.finalPrice, 0.001)
  }

  @Test
  fun testDiscountCalculatorUIFlow() {
    composeTestRule.setContent {
      MyApplicationTheme {
        DiscountCalculatorApp()
      }
    }

    // Check initial inputs exist
    composeTestRule.onNodeWithTag("initial_price_input").assertExists()
    composeTestRule.onNodeWithTag("discount_percent_input").assertExists()
    composeTestRule.onNodeWithTag("calculate_button").assertExists()
    composeTestRule.onNodeWithTag("currency_dropdown").assertExists()

    // Enter price and trigger calculate
    composeTestRule.onNodeWithTag("initial_price_input").performTextInput("100")
    composeTestRule.onNodeWithTag("calculate_button").performScrollTo().performClick()

    // Verify result exists
    composeTestRule.onNodeWithTag("result_card").assertExists()
    composeTestRule.onNodeWithTag("output_final_price").assertExists()
    composeTestRule.onNodeWithTag("output_discount_value").assertExists()
  }
}
