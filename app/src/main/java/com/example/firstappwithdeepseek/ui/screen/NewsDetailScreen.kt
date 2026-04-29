package com.example.firstappwithdeepseek.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.firstappwithdeepseek.model.Article

/**
 * Detail screen showing the full article content with a large image at the top.
 * The entire screen is scrollable so users can read the full content without
 * opening an external browser.
 */
@Composable
fun NewsDetailScreen(article: Article) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Large image at the top
        AsyncImage(
            model = article.urlToImage,
            contentDescription = article.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .clipToBounds(),
            contentScale = ContentScale.Crop
        )

        Column(modifier = Modifier.padding(16.dp)) {
            // Title
            Text(
                text = article.title ?: "No Title",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            // Author and source
            val authorInfo = listOfNotNull(
                article.author,
                article.source?.name
            ).joinToString(" • ")

            if (authorInfo.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = authorInfo,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Published date
            article.publishedAt?.let { date ->
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Published: $date",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Full description / content
            Spacer(modifier = Modifier.height(16.dp))
            val fullContent = buildString {
                article.description?.let {
                    append(it)
                    append("\n\n")
                }
                article.content?.let {
                    append(it)
                }
            }.ifEmpty { "No additional content available." }

            Text(
                text = fullContent,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Note for demo
            Text(
                text = "This article is displayed in-app. Visit the original source for the complete story.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
