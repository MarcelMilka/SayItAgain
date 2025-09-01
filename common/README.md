# 📦 Module :common

## 📝 Overview

A shared module containing pure Kotlin code used throughout the app, such as sealed classes, data classes, and
interfaces. It serves as a central location for reusable, platform-independent logic. Also includes Hilt modules
and coroutine qualifiers for dependency injection, navigation-related classes, and a centralized object
for storing UI component test tags to simplify their management.

### 🔧 Key Functionalities

- Provides non-API-specific Kotlin files that allow modules to communicate without depending on concrete implementations
- Manages test tags for UI elements in a centralized and maintainable way
- Defines core data models and interfaces for the application
- Provides event bus system for cross-module communication
- Manages connectivity status monitoring
- Defines repository interfaces for data layer abstraction
- Centralizes navigation definitions and screen routing
- Provides dependency injection modules for coroutine scopes
- Defines export functionality interfaces and data models
- Offers test helper utilities for consistent testing across modules

---

## 🧠 Class Responsibilities

### ``Navigation``
SSOT for navigating using Compose's Navigation component. Defines all navigation routes and destinations
for the application.

### ``Screens``
Defines all screens available in the app. Contains the screen definitions used by the navigation system.

### ``TestTags``
Centralized object containing all UI test tags for consistent testing across the application. Provides
a single source of truth for test identifiers used in UI testing.

### ``EventBus``
Interface for event-driven communication between modules. Provides a generic event bus system that
allows modules to communicate without direct dependencies.

### ``SaveFileEvent``
Events for handling CSV file export operations. Defines the event types used in the file export
workflow including save requests and success/error states.

### ``SavedWord``
Data model representing a saved word with UUID, word text, and language. Core entity used throughout
the application for managing user's saved vocabulary.

### ``ExportSettings``
Configuration model for word export operations. Contains settings and preferences for how words
should be exported to CSV format.

### ``SavedWordsRepository``
Interface for managing saved words CRUD operations. Defines the contract for data operations on
saved words including save, delete, and load operations.

### ``ConnectivityObserver``
Interface for monitoring network connectivity status. Provides a way to observe network state changes
throughout the application.

### ``CoroutineScopeModule``
Hilt module providing coroutine scope qualifiers. Manages dependency injection for different
coroutine scopes used throughout the application.

### ``CsvFile``
Data model for CSV file export operations. Represents the structure of exported CSV files with
their content and metadata.

### ``ExportRepository``
Interface for handling export operations. Defines the contract for exporting saved words to various
formats and destinations.

### ``ExportError``
Error types for export operations. Defines the different types of errors that can occur during
export operations for proper error handling.

---

## 🧬 Class Dependency Graph
Dependency flow within the module. *(Nothing to show)*

## 🧩 Module Dependency Graph
Shows which modules depend on `:common`.

```mermaid
flowchart TD
    APP([:app])
    COMMON([:common])
    CONNECTIVITY([:connectivity])
    SCAFFOLD([:scaffold])
    FEATURE_HOME([:feature:home])
    FEATURE_SAVED([:feature:saved])
    FEATURE_TRANSCRIBE([:feature:transcribe])
    LOCAL([:localData])
    
    COMMON --> CONNECTIVITY
    COMMON --> LOCAL
    COMMON --> FEATURE_HOME
    COMMON --> FEATURE_SAVED
    COMMON --> FEATURE_TRANSCRIBE
    COMMON --> SCAFFOLD
    COMMON --> APP
```