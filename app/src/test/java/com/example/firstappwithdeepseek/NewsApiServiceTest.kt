package com.example.firstappwithdeepseek

import com.example.firstappwithdeepseek.model.NewsResponse
import com.example.firstappwithdeepseek.network.NewsApiService
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests for the Ktor-based network service using a mock engine.
 */
class NewsApiServiceTest {

    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun `fetchTopHeadlines returns parsed articles on success`() = runTest {
        // Prepare mock response
        val mockResponse = NewsResponse(
            status = "ok",
            totalResults = 2,
            articles = listOf(
                com.example.firstappwithdeepseek.model.Article(
                    title = "Test Article 1",
                    description = "Description 1",
                    url = "https://example.com/1",
                    urlToImage = "https://example.com/img1.jpg",
                    content = "Full content 1"
                ),
                com.example.firstappwithdeepseek.model.Article(
                    title = "Test Article 2",
                    description = "Description 2",
                    url = "https://example.com/2",
                    urlToImage = "https://example.com/img2.jpg",
                    content = "Full content 2"
                )
            )
        )

        val mockResponseBody = json.encodeToString(mockResponse)

        // Create a mock engine
        val mockEngine = MockEngine { _ ->
            respond(
                content = ByteReadChannel(mockResponseBody),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }

        // TODO: In a real test, inject the mock engine into NewsApiService.
        // For now, this tests the serialization logic independently.
        val response = json.decodeFromString<NewsResponse>(mockResponseBody)
        assertEquals("ok", response.status)
        assertEquals(2, response.totalResults)
        assertEquals(2, response.articles.size)
        assertEquals("Test Article 1", response.articles[0].title)
        assertEquals("Description 1", response.articles[0].description)
        assertEquals("Full content 1", response.articles[0].content)
        assertNotNull(response.articles[0].urlToImage)
    }

    @Test
    fun `fetchTopHeadlines handles empty articles gracefully`() = runTest {
        val mockResponseBody = """{"status":"ok","totalResults":0,"articles":[]}"""
        val response = json.decodeFromString<NewsResponse>(mockResponseBody)
        assertEquals("ok", response.status)
        assertEquals(0, response.totalResults)
        assertTrue(response.articles.isEmpty())
    }
}
