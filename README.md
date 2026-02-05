# Petnames Android

Android versie van Petnames by Kinder - een app om samen huisdiernamen te kiezen met je household.

## Tech Stack

- **Kotlin** - Main language
- **Jetpack Compose** - Modern UI toolkit
- **Hilt** - Dependency injection
- **Supabase** - Backend (auth, database)
- **Firebase** - Analytics & Push notifications
- **DataStore** - Local preferences
- **Ktor** - HTTP client

## Project Structure

```
app/src/main/java/com/kinder/petnames/
├── core/                    # Core utilities
│   ├── AppConfig.kt         # Supabase configuration
│   ├── AnalyticsManager.kt  # Firebase Analytics
│   ├── PreferencesManager.kt# DataStore preferences
│   └── SupabaseModule.kt    # Hilt DI module
├── data/                    # Repositories
│   ├── HouseholdRepository.kt
│   ├── MatchesRepository.kt
│   ├── NamesRepository.kt
│   ├── SwipesRepository.kt
│   └── LocalNamesProvider.kt
├── domain/                  # Models
│   └── Models.kt
├── ui/
│   ├── components/          # Reusable components
│   │   ├── SwipeableCard.kt
│   │   ├── ActionButtons.kt
│   │   └── MatchPopup.kt
│   ├── screens/             # Screen composables + ViewModels
│   │   ├── HomeScreen.kt
│   │   ├── OnboardingScreen.kt
│   │   ├── MatchesScreen.kt
│   │   ├── LikesScreen.kt
│   │   ├── ProfileScreen.kt
│   │   └── FiltersScreen.kt
│   └── theme/               # Theming
│       ├── Color.kt
│       └── Theme.kt
├── MainActivity.kt          # Main entry point
└── PetnamesApplication.kt   # Application class
```

## Setup

### 1. Clone & Open
Open the project in Android Studio (Hedgehog or newer).

### 2. Firebase Setup
1. Create a Firebase project at [console.firebase.google.com](https://console.firebase.google.com)
2. Add an Android app with package name `com.kinder.petnames`
3. Download `google-services.json` and place it in `app/`
4. Enable Analytics and Cloud Messaging

### 3. Supabase
The Supabase credentials are already configured in `build.gradle.kts`.
They connect to the same backend as the iOS app.

### 4. Build & Run
```bash
./gradlew assembleDebug
```

Or just click Run in Android Studio.

## Features

- ✅ Tinder-style swipe cards
- ✅ Offline-first (bundled 7000+ names)
- ✅ Multi-language support (NL, EN, DE, FR, ES, IT, etc.)
- ✅ Style filtering (cute, strong, classic, etc.)
- ✅ Household matching
- ✅ Firebase Analytics
- ✅ Push notifications (via FCM)

## Notes

- The app uses the same Supabase backend as the iOS version
- Households are cross-platform compatible
- Names data is bundled in `assets/bundled_names.json`
