package com.example.firstappwithdeepseek.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.firstappwithdeepseek.ui.screen.NewsDetailScreen
import com.example.firstappwithdeepseek.ui.screen.NewsListScreen
import com.example.firstappwithdeepseek.ui.viewmodel.NewsViewModel

/**
 * Navigation routes for the app.
 */
object Routes {
    const val NEWS_LIST = "news_list"
    const val NEWS_DETAIL = "news_detail"
}

/**
 * Main navigation graph for the app.
 * Uses the ViewModel to share the selected article to the detail screen
 * (avoiding serialization issues from passing JSON in route URLs).
 */
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val newsViewModel: NewsViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Routes.NEWS_LIST
    ) {
        composable(Routes.NEWS_LIST) {
            NewsListScreen(
                viewModel = newsViewModel,
                onArticleClick = { article ->
                    newsViewModel.selectArticle(article)
                    navController.navigate(Routes.NEWS_DETAIL)
                }
            )
        }

        composable(Routes.NEWS_DETAIL) {
            val selectedArticle by newsViewModel.selectedArticle.collectAsState()
            selectedArticle?.let { article ->
                NewsDetailScreen(
                    article = article,
                    onBackClick = {
                        newsViewModel.clearSelection()
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}
