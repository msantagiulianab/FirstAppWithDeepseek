package com.example.firstappwithdeepseek

import com.example.firstappwithdeepseek.model.Article
import com.example.firstappwithdeepseek.model.NewsResponse
import com.example.firstappwithdeepseek.network.NewsApiService
import com.example.firstappwithdeepseek.repository.NewsRepository
import com.example.firstappwithdeepseek.ui.viewmodel.NewsUiState
import com.example.firstappwithdeepseek.ui.viewmodel.NewsViewModel
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.serialization.json.Json
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for the NewsViewModel.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class NewsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val json = Json { ignoreUnknownKeys = true }

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `viewModel loads news and emits Success state`() = runTest {
        // Arrange: create mock response
        val mockArticles = listOf(
            Article(
                title = "Breaking News",
                description = "Something happened",
                urlToImage = "https://example.com/img.jpg",
                content = "Full story here..."
            )
        )
        val mockResponse = NewsResponse(status = "ok", totalResults = 1, articles = mockArticles)
        val mockResponseBody = json.encodeToString(mockResponse)

        val mockEngine = MockEngine { _ ->
            respond(
                content = ByteReadChannel(mockResponseBody),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }

        val mockRepository = object : NewsRepository() {
            override suspend fun getTopHeadlines(country: String): List<Article> {
                return mockArticles
            }
        }

        val viewModel = NewsViewModel(repository = mockRepository)

        // Need to collect the state after init
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value
        assertTrue(state is NewsUiState.Success)
        assertEquals(1, (state as NewsUiState.Success).articles.size)
        assertEquals("Breaking News", state.articles[0].title)
    }

    @Test
    fun `viewModel handles error state`() = runTest {
        // Arrange: create a repository that throws an exception
        val failingRepository = object : NewsRepository() {
            override suspend fun getTopHeadlines(country: String): List<Article> {
                throw RuntimeException("Network error")
            }
        }

        val viewModel = NewsViewModel(repository = failingRepository)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value
        assertTrue(state is NewsUiState.Error)
        assertTrue((state as NewsUiState.Error).message.contains("Network error"))
    }

    @Test
    fun `selectArticle and clearSelection work correctly`() {
        val viewModel = NewsViewModel(
            repository = object : NewsRepository() {
                override suspend fun getTopHeadlines(country: String): List<Article> = emptyList()
            }
        )
        val article = Article(title = "Test", description = "Desc")

        viewModel.selectArticle(article)
        assertNotNull(viewModel.selectedArticle.value)
        assertEquals("Test", viewModel.selectedArticle.value?.title)

        viewModel.clearSelection()
        assertEquals(null, viewModel.selectedArticle.value)
    }
}
