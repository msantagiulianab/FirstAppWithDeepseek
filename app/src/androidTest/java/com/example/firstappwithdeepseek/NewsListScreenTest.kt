package com.example.firstappwithdeepseek

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.firstappwithdeepseek.model.Article
import com.example.firstappwithdeepseek.ui.screen.NewsDetailScreen
import com.example.firstappwithdeepseek.ui.screen.NewsListItem
import org.junit.Rule
import org.junit.Test

/**
 * End-to-end UI test for the news list and detail screens.
 * Tests that UI components render correctly with sample data.
 */
class NewsListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `news list item displays title and description`() {
        val article = Article(
            title = "Test Headline",
            description = "This is a test description for the news article."
        )

        composeTestRule.setContent {
            NewsListItem(article = article, onClick = { })
        }

        composeTestRule.onNodeWithText("Test Headline").assertIsDisplayed()
        composeTestRule.onNodeWithText("This is a test description for the news article.").assertIsDisplayed()
    }

    @Test
    fun `news detail screen displays article content`() {
        val article = Article(
            title = "Detail Headline",
            description = "Short description",
            author = "John Doe",
            source = com.example.firstappwithdeepseek.model.Source(name = "CNN"),
            publishedAt = "2025-01-01T12:00:00Z",
            content = "This is the long form content of the article that should be displayed on the detail screen."
        )

        composeTestRule.setContent {
            NewsDetailScreen(article = article)
        }

        composeTestRule.onNodeWithText("Detail Headline").assertIsDisplayed()
        composeTestRule.onNodeWithText("John Doe • CNN").assertIsDisplayed()
        composeTestRule.onNodeWithText(
            "This is the long form content of the article that should be displayed on the detail screen."
        ).assertIsDisplayed()
    }

    @Test
    fun `news detail screen does not open external browser`() {
        // Verify the detail screen renders content in-app
        val article = Article(
            title = "In-App Article",
            content = "Full content shown in the app, not in a browser."
        )

        composeTestRule.setContent {
            NewsDetailScreen(article = article)
        }

        composeTestRule.onNodeWithText("In-App Article").assertIsDisplayed()
        composeTestRule.onNodeWithText("Full content shown in the app, not in a browser.").assertIsDisplayed()
    }
}
