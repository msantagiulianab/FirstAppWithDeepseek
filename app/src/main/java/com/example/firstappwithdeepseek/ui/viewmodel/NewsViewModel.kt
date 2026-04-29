package com.example.firstappwithdeepseek.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firstappwithdeepseek.model.Article
import com.example.firstappwithdeepseek.repository.NewsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * UI state for the news list screen.
 */
sealed class NewsUiState {
    data object Loading : NewsUiState()
    data class Success(val articles: List<Article>) : NewsUiState()
    data class Error(val message: String) : NewsUiState()
}

/**
 * ViewModel that manages the news feed UI state.
 */
class NewsViewModel(
    private val repository: NewsRepository = NewsRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<NewsUiState>(NewsUiState.Loading)
    val uiState: StateFlow<NewsUiState> = _uiState.asStateFlow()

    private val _selectedArticle = MutableStateFlow<Article?>(null)
    val selectedArticle: StateFlow<Article?> = _selectedArticle.asStateFlow()

    init {
        loadNews()
    }

    /**
     * Loads top headlines from the repository.
     */
    fun loadNews(country: String = "us") {
        viewModelScope.launch {
            _uiState.value = NewsUiState.Loading
            try {
                val articles = repository.getTopHeadlines(country)
                _uiState.value = NewsUiState.Success(articles)
            } catch (e: Exception) {
                _uiState.value = NewsUiState.Error(
                    e.message ?: "Failed to load news. Check your API key in Constants.kt"
                )
            }
        }
    }

    /**
     * Sets the article to display in the detail screen.
     */
    fun selectArticle(article: Article) {
        _selectedArticle.value = article
    }

    /**
     * Clears the selected article.
     */
    fun clearSelection() {
        _selectedArticle.value = null
    }
}
