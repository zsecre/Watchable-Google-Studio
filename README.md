# 🎬 Watchable — Android

A premium movie, TV series, and anime discovery app built with **Kotlin + Jetpack Compose**.

> Converted from the original React/TypeScript web app.

---

## ✨ Features

| Feature | Details |
|---|---|
| 🏠 Home feed | Hero slideshow + trending, popular, now-playing rows |
| 🎬 Movies tab | Grid of trending & popular movies from TMDB |
| 📺 TV & Anime tab | TV shows from TMDB + anime from Jikan (MyAnimeList) |
| 🔍 Search | Live search across movies, TV, and anime |
| 🔖 Watchlist | Save titles locally via Room |
| 🕐 History | Auto-tracks every title you open |
| 👤 Profile | Edit name, toggle dark/light mode, stats |
| 🌙 Dark / Light theme | Cyan brand colour on dark or light background |

---

## 🏗️ Tech Stack

- **Kotlin** + **Jetpack Compose** (Material 3)
- **Hilt** — dependency injection
- **Retrofit + OkHttp** — REST API calls (TMDB + Jikan)
- **Room** — local persistence (watchlist / history)
- **DataStore** — user preferences (name, theme)
- **Coil** — async image loading
- **Firebase Firestore + Auth** — anonymous auth, comments
- **Coroutines + StateFlow** — reactive state management

---

## 🚀 Getting Started

### Prerequisites
- Android Studio Ladybug (2024.2) or newer
- JDK 17
- Android SDK 35

### 1 — Clone
```bash
git clone https://github.com/YOUR_USERNAME/watchable-android.git
cd watchable-android
```

### 2 — API Keys

Copy the template and fill it in:
```bash
cp local.properties.template local.properties
```

Edit `local.properties`:
```
sdk.dir=/path/to/Android/Sdk
TMDB_TOKEN=your_tmdb_bearer_token_here
```

Get a free TMDB token at **https://developer.themoviedb.org**.

### 3 — Firebase

- Go to [console.firebase.google.com](https://console.firebase.google.com)
- Create a project, add an Android app (`com.watchable.app`)
- Download `google-services.json` and place it in `app/`
- Enable **Anonymous Authentication** under Build → Authentication

### 4 — Run
Open in Android Studio and press **▶️ Run**, or:
```bash
./gradlew assembleDebug
adb install app/build/outputs/apk/debug/app-debug.apk
```

---

## ⚙️ CI/CD — GitHub Actions

The workflow at `.github/workflows/build.yml` has three jobs:

| Job | Trigger | Output |
|---|---|---|
| 🔨 Build & Lint | every push/PR | `watchable-debug.apk` artifact |
| 🧪 Unit Tests | after build | test HTML report artifact |
| 🚀 Release | push to `main` only | signed APK + AAB artifacts |

### Required GitHub Secrets

Go to **Settings → Secrets and variables → Actions** and add:

| Secret | Description |
|---|---|
| `TMDB_TOKEN` | Your TMDB v4 read-access bearer token |
| `GOOGLE_SERVICES_JSON` | Full contents of your `google-services.json` |
| `KEYSTORE_BASE64` | `base64 -i release.keystore` output |
| `STORE_PASSWORD` | Keystore password |
| `KEY_ALIAS` | Key alias inside the keystore |
| `KEY_PASSWORD` | Key password |

### Generating a release keystore
```bash
keytool -genkeypair -v \
  -keystore release.keystore \
  -alias watchable \
  -keyalg RSA -keysize 2048 \
  -validity 10000

# Encode for the GitHub secret:
base64 -i release.keystore | pbcopy   # macOS
base64 release.keystore | xclip       # Linux
```

---

## 📁 Project Structure

```
app/src/main/java/com/watchable/app/
├── WatchableApp.kt          # Hilt application
├── MainActivity.kt          # Entry point, navigation
├── data/
│   ├── api/
│   │   ├── TmdbApi.kt       # TMDB Retrofit interface
│   │   └── JikanApi.kt      # Jikan/MAL Retrofit interface
│   ├── local/
│   │   └── Database.kt      # Room DB, DAOs
│   ├── model/
│   │   └── Models.kt        # Data classes + converters
│   └── repository/
│       └── MediaRepository.kt
├── di/
│   └── AppModule.kt         # Hilt providers
├── ui/
│   ├── components/
│   │   └── Components.kt    # MediaCard, SearchBar, etc.
│   ├── screens/
│   │   ├── HomeScreen.kt
│   │   ├── MediaGridScreen.kt
│   │   ├── LibraryScreen.kt
│   │   ├── ProfileScreen.kt
│   │   ├── DetailScreen.kt
│   │   └── SearchScreen.kt
│   └── theme/
│       ├── Theme.kt
│       └── Typography.kt
└── viewmodel/
    └── MainViewModel.kt
```

---

## 📜 License

Apache 2.0 — same as the original web app.
