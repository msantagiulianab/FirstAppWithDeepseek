package com.example.firstappwithdeepseek.repository

import com.example.firstappwithdeepseek.model.Article
import com.example.firstappwithdeepseek.network.NewsApiService

/**
 * Repository that acts as a single source of truth for news data.
 * Currently fetches from the network; in a production app this could cache results.
 */
open class NewsRepository(private val newsApiService: NewsApiService = NewsApiService()) {

    private var cachedArticles: List<Article> = emptyList()

    /**
     * Fetches top news headlines from the API.
     *
     * @param country Two-letter country code for the news source.
     * @return List of [Article] objects.
     */
    open suspend fun getTopHeadlines(country: String = "us"): List<Article> {
        val response = newsApiService.fetchTopHeadlines(country)
        cachedArticles = response.articles
        return cachedArticles
    }

    /**
     * Returns the currently cached articles, if any.
     */
    fun getCachedArticles(): List<Article> = cachedArticles
}
