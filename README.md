# Say It Again

Native Android application for audio transcription with real-time playback sync and vocabulary management.

The idea to create this application emerged one day when the author of this project ([me](https://www.linkedin.com/in/marcel-milka)) was listening to a podcast to practice listening skills and couldn't fully understand the topic. The lack of podcast audio transcription and podcast/transcription-related applications not meeting expectations contributed to the idea of creating software that allows users to transcribe anything by uploading their own audio/video, creating a transcription, and following along with the transcript while listening to the audio with the ability to save unknown words to learn.

**Note:** The application is still under development and lacks several key features.

## Screenshots

![Screenshots of the production-ready features](/docs/Screenshots.png)

## Design

View the UI design on [Figma](https://www.figma.com/design/inkoCgXPCNKRNAPuOllk6P/Say-It-Again-UI?node-id=0-1&p=f&t=SopfZCzkgSRhCpeL-0).

## Features

* Picking audio to transcribe (planned)
* Audio transcription with a speech-to-text API (planned)
* Displaying transcription while playing audio with the ability to save vocabulary (planned)
* CRUD Room database for saved vocabulary (production-ready)
* Internet connectivity tracker (production-ready)
* Bulk-exporting saved vocabulary by generating a CSV file with a backend service (production-ready, see [Say It Again Backend repository](https://github.com/MarcelMilka/SayItAgainBackend))
* Saving a CSV file directly to the device (production-ready)
* Sending a CSV file to user's email address (planned)

**Minor improvements in the future:**
* Splash screen with animated content
* Custom illustrations (the current ones come from [undraw.co](https://undraw.co))

## Modularization

The project was divided into Android modules to improve the overall structure.

* **app** - Main application module with MainActivity and ApplicationClass
* **common** - Shared models, interfaces, and utilities used across all modules
* **scaffold** - Application-level navigation and main UI structure
* **feature:home** - Home screen functionality and UI
* **feature:saved** - Saved words management, export functionality, and related screens
* **feature:transcribe** - Audio transcription functionality (currently in development)
* **localData** - Local database operations using Room, DAOs, and data sources
* **remoteData** - Network operations, API endpoints, and remote data handling
* **connectivity** - Network connectivity monitoring and status management
* **ui** - Shared UI components, themes, and design system elements

Module-level dependency graph:

![Module-level dependency graph](/docs/Module-level%20dependency%20graph.png)

## Technologies Used

**Android:**
* SAF (Storage Access Framework)
* ContentResolver
* ConnectivityManager

**Kotlin:**
* Kotlin Flow and Coroutines
* Kotlin Serialization
* Extension Functions
* OOP (data, sealed, interface, abstract)
* Generics

**Jetpack Components:**
* Jetpack Compose
* Navigation Compose
* Room Database
* ViewModel
* Hilt

**Networking:**
* Retrofit
* OkHttp
* Gson
* JSON Serialization/Deserialization

**Testing:**
* JUnit
* MockK
* Turbine (Flow testing)
* MockWebServer

## Programming Methodologies

* Multi-module architecture
* Single-activity pattern
* Dependency Injection (DI)
* MVVM (Model-View-ViewModel)
* MVI (Model-View-Intent)
* Event-Driven Architecture (EDA)
* SOLID principles
* Repository pattern