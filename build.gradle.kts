plugins {
    kotlin("jvm") version "2.0.21" apply false

    id("com.android.application") version "8.8.0-alpha05" apply false
    id("org.jetbrains.kotlin.android") version "2.0.21" apply false

    id("com.google.devtools.ksp") version "2.0.21-1.0.27" apply false

    id("org.jetbrains.kotlin.plugin.compose") version "2.0.21" apply false
    id("com.google.dagger.hilt.android") version "2.56.2" apply false
    alias(libs.plugins.android.library) apply false
}