# 📦 Module :app

## 📝 Overview

The main application module that serves as the entry point for the application.

### 🔧 Key Functionalities

- Serves as the main entry point for the Android application
- Configures Hilt dependency injection for the entire app
- Manages application lifecycle and initialization
- Provides the main activity that initializes the composable function `applicationScaffold`
- Handles CSV file export functionality for saved words
- Defines app permissions and manifest configuration

---

## 🧠 Class Responsibilities

### ``ApplicationClass``
The main application class annotated with `@HiltAndroidApp` that initializes Hilt dependency injection
for the entire application. Initializes the SavedWordsRepository on app startup.

### ``MainActivity``
The main activity of the application that serves as the entry point for user interaction. It's annotated
with `@AndroidEntryPoint` for Hilt integration and uses Jetpack Compose to render the UI through the
`applicationScaffold()` composable. Enables edge-to-edge display and handles CSV file export functionality
through the SaveFileEventBus.

### ``SaveFileContract``
An ActivityResultContract that handles the file save dialog for exporting saved words as CSV files.
Creates the appropriate intent for saving CSV files with the default filename "exported_words.csv".

---

## 🧬 Class dependency graph
```mermaid
flowchart TB

    subgraph ":localData"

        SavedWordsRepositoryLocalModule["<big>SavedWordsRepositoryLocalModule</big>"]
    end

    subgraph ":common"

        EventBusModule["<big>EventBusModule</big>"]
    end

    subgraph ":scaffold"

        ApplicationScaffold["<big>applicationScaffold()</big>"]
    end
    
    subgraph ":app"

        ApplicationClass["<big>ApplicationClass</big><div><small>SavedWordsRepository</small>"]
        MainActivity["<big>MainActivity</big>"]
    end

    SavedWordsRepositoryLocalModule -- SavedWordsRepositoryLocalImpl --> ApplicationClass
    EventBusModule -- EventBus.SaveFileEvent --> MainActivity
    ApplicationScaffold --> MainActivity
    
```

## 🧩 Module dependency graph
Shows which modules depend on `:app` and which modules `:app` itself depends on.

```mermaid
flowchart TD
    COMMON[:common]
    LOCAL_DATA[:localData]
    SCAFFOLD[:scaffold]
    APP[:app]
    
    
    COMMON --> LOCAL_DATA
    COMMON --> SCAFFOLD
    LOCAL_DATA --> APP
    COMMON --> APP
    SCAFFOLD --> APP
```
