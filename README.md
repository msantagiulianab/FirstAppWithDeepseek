# World News

> **This is the first Android app I built using Deep Seek (via the Cline client).**

World News is an Android application built with **Kotlin** and **Jetpack Compose** that fetches and displays the latest top headlines from [NewsAPI.org](https://newsapi.org/). It presents a clean, scrollable list of news articles with images, and lets you tap any article to read the full content in-app.

---

## Tech Stack & Frameworks

| Technology | Purpose |
|---|---|
| **Kotlin** | Primary language |
| **Jetpack Compose (Material3)** | Declarative UI framework |
| **Ktor Client (CIO engine)** | Networking — fetches news from the API |
| **Kotlinx Serialization** | JSON deserialisation for API responses |
| **Jetpack Navigation Compose** | In-app screen navigation (list → detail) |
| **Coil (Compose)** | Asynchronous image loading for article thumbnails |
| **Jetpack ViewModel + StateFlow** | MVVM architecture and reactive UI state |
| **AndroidX Lifecycle** | Lifecycle-aware components |
| **JUnit + Ktor Mock + Compose UI Test** | Unit and UI testing |

## Architecture

The app follows the **MVVM (Model-View-ViewModel)** pattern with a **Repository** layer:

```
UI (Compose Screens) → ViewModel → Repository → NewsApiService (Ktor) → NewsAPI.org
```

- **Model** — `Article`, `NewsResponse` data classes with `@Serializable`
- **Network** — `NewsApiService` using Ktor with Content Negotiation + JSON
- **Repository** — `NewsRepository` as a single source of truth (with optional caching)
- **ViewModel** — `NewsViewModel` exposes a `StateFlow<NewsUiState>` to the UI (Loading / Success / Error)
- **UI** — `NewsListScreen` (scrollable card list), `NewsDetailScreen` (full article view)

## Features

- 📰 Fetches real-time top headlines from NewsAPI
- 🖼️ Displays article images, titles, descriptions, and source badges
- 🔍 Tap any article to read the full content in a scrollable detail screen
- ⏳ Loading spinner and error handling with user-friendly messages
- 📱 Material3 design with a dark/light theme

## Getting Started

1. **Get a free API key** from [https://newsapi.org/register](https://newsapi.org/register)
2. Open [`Constants.kt`](app/src/main/java/com/example/firstappwithdeepseek/Constants.kt) and replace the placeholder with your key:
   ```kotlin
   const val NEWS_API_KEY = "your_api_key_here"
   ```
3. Build and run the app on an emulator or device (min SDK 30).

## Project Structure

```
app/
├── src/
│   ├── main/
│   │   ├── java/com/example/firstappwithdeepseek/
│   │   │   ├── Constants.kt              # API config (base URL, key)
│   │   │   ├── MainActivity.kt           # Single activity entry point
│   │   │   ├── model/
│   │   │   │   ├── Article.kt            # Data model
│   │   │   │   └── NewsResponse.kt       # API response model
│   │   │   ├── network/
│   │   │   │   └── NewsApiService.kt     # Ktor HTTP client
│   │   │   ├── repository/
│   │   │   │   └── NewsRepository.kt     # Data repository
│   │   │   ├── ui/
│   │   │   │   ├── navigation/
│   │   │   │   │   └── AppNavigation.kt  # NavHost + routes
│   │   │   │   ├── screen/
│   │   │   │   │   ├── NewsListScreen.kt # Article list
│   │   │   │   │   └── NewsDetailScreen.kt # Article detail
│   │   │   │   ├── theme/                # Material3 theme
│   │   │   │   └── viewmodel/
│   │   │   │       └── NewsViewModel.kt  # UI state management
│   │   │   └── ...
│   │   └── res/                          # Resources (strings, etc.)
│   ├── test/                             # Unit tests (ViewModel + API)
│   └── androidTest/                      # UI tests (Compose)
```

## Notes

- The app uses the **free tier** of NewsAPI, which provides top headlines. For production use, consider upgrading or adding a caching layer.
- This project was built entirely with the assistance of **Deep Seek** through the **Cline** client — an AI-powered development workflow.
