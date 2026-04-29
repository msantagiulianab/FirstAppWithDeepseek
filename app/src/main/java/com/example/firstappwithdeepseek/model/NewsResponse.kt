package com.example.firstappwithdeepseek.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NewsResponse(
    val status: String? = null,
    @SerialName("totalResults")
    val totalResults: Int = 0,
    val articles: List<Article> = emptyList()
)
