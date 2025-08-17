# 📦 Module :saved

## 📝 Overview

A feature module responsible for managing locally saved words. This module provides two main features: viewing/managing saved words and exporting them. It follows Clean Architecture principles with clear separation of concerns, implements modern Android development practices using Jetpack Compose and architectural patterns such as MVVM, MVI and DI.

### 🔧 Key Functionalities

- [**SavedWords**](src/main/java/eu/project/saved/savedWords/README.md): Display, manage, and delete saved words with confirmation dialogs
- [**ExportWords**](src/main/java/eu/project/saved/exportWords/README.md): Select and export saved words with various export methods

---

## 🏗️ Architecture

The module follows a layered architecture pattern with the following structure:

```
saved/
├── savedWords/       # Saved words management feature
└── exportWords/      # Export functionality feature
```

### 🧬 Module dependency graph

```mermaid
flowchart TD
    COMMON[:common]
    LOCAL_DATA[:localData]
    UI[:ui]
    CONNECTIVITY[:connectivity]
    SAVED[:saved]
    SCAFFOLD[:scaffold]

    COMMON --> SAVED
    LOCAL_DATA --> SAVED
    UI --> SAVED
    CONNECTIVITY --> SAVED
    SAVED --> SCAFFOLD
```