import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)

    id("com.google.devtools.ksp")

    id("com.google.dagger.hilt.android")
}

android {
    namespace = "eu.project.remotedata"
    compileSdk = 35

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {

        val siaapiDevOnlyKey = gradleLocalProperties(rootDir, providers).getProperty("SIAAPI_DEV_ONLY_KEY") ?: "QWERTY"
        val siaapiBaseUrl = gradleLocalProperties(rootDir, providers).getProperty("SIAAPI_BASE_URL") ?: "\"https://api.example.com\""

        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")

            buildConfigField("String", "SIAAPI_DEV_ONLY_KEY", siaapiDevOnlyKey)
            buildConfigField("String", "SIAAPI_BASE_URL", siaapiBaseUrl)
        }

        debug {

            buildConfigField("String", "SIAAPI_DEV_ONLY_KEY", siaapiDevOnlyKey)
            buildConfigField("String", "SIAAPI_BASE_URL", siaapiBaseUrl)
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //  Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    // Retrofit
    implementation(libs.retrofit)

    // OkHttp
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
    testImplementation(libs.mockwebserver)

    // Jackson converter
    implementation(libs.converter.jackson)

    // MockK
    testImplementation(libs.mockk)
    testImplementation(libs.mockk.android)
    testImplementation(libs.mockk.agent)

    // Coroutines test
    testImplementation(libs.kotlinx.coroutines.test)

    implementation(project(":common"))
}