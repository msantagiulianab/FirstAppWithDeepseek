package com.example.firstappwithdeepseek.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.firstappwithdeepseek.model.Article
import com.example.firstappwithdeepseek.ui.screen.NewsDetailScreen
import com.example.firstappwithdeepseek.ui.screen.NewsListScreen
import com.example.firstappwithdeepseek.ui.viewmodel.NewsViewModel
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Navigation routes for the app.
 */
object Routes {
    const val NEWS_LIST = "news_list"
    const val NEWS_DETAIL = "news_detail/{articleJson}"
}

/**
 * Main navigation graph for the app.
 * Uses serialized article JSON to pass data to the detail screen (no browser opening).
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
                    val articleJson = Json.encodeToString(article)
                    navController.navigate("news_detail/$articleJson")
                }
            )
        }

        composable(
            route = Routes.NEWS_DETAIL,
            arguments = listOf(
                navArgument("articleJson") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val articleJson = backStackEntry.arguments?.getString("articleJson") ?: ""
            val article = try {
                Json.decodeFromString<Article>(articleJson)
            } catch (e: Exception) {
                null
            }

            if (article != null) {
                NewsDetailScreen(article = article)
            } else {
                // Fallback: go back
                navController.popBackStack()
            }
        }
    }
}
