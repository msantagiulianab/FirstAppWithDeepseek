package com.example.firstappwithdeepseek.network

import com.example.firstappwithdeepseek.Constants
import com.example.firstappwithdeepseek.model.NewsResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * Network service class that fetches news from NewsAPI.org using Ktor.
 */
class NewsApiService(
    private val httpClient: HttpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
                prettyPrint = false
            })
        }
    }
) {

    /**
     * Fetches top headline news for the given country.
     *
     * @param country Two-letter country code (e.g., "us", "gb").
     * @return [NewsResponse] containing the list of articles.
     */
    suspend fun fetchTopHeadlines(country: String = Constants.DEFAULT_COUNTRY): NewsResponse {
        return httpClient.get("${Constants.NEWS_API_BASE_URL}v2/top-headlines") {
            parameter("country", country)
            parameter("apiKey", Constants.NEWS_API_KEY)
        }.body()
    }
}
