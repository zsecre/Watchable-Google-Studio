# Kotlin Mobile App

This project has been converted from a web application to a native Android application using **Kotlin** and **Jetpack Compose**.

## Features
- **Modern UI**: Built with Jetpack Compose for a declarative UI experience.
- **CI/CD**: Integrated GitHub Actions workflow to automatically build your APK on push.
- **Gradle Setup**: Fully configured with Version Catalogs (`libs.versions.toml`).

## GitHub Actions
The project includes a workflow in `.github/workflows/android-build.yml`. 
- **Trigger**: Every push to the `main` branch.
- **Output**: An APK artifact named `app-debug` will be available in the Actions tab of your GitHub repository.

## How to Export to GitHub
To push this code to your own GitHub repository:
1. Open the **Settings** menu in the AI Studio editor.
2. Select **Export to GitHub**.
3. Follow the prompts to connect your account and create a new repository.

## Development instructions
- The entry point is located at `app/src/main/kotlin/com/example/app/MainActivity.kt`.
- UI components and logic should be added within the `app` module.
