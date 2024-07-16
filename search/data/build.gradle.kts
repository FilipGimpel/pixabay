import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
}

val apikeyPropertiesFile = rootProject.file("apikey.properties")
val apiKeyProperties = Properties().apply {
    load(apikeyPropertiesFile.inputStream())
}

android {
    namespace = "com.gimpel.pixabay.search.data"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        buildConfigField("String", "PIXABAY_API_KEY", apiKeyProperties.getProperty("pixabay.api.key"))
        buildConfigField("String", "PIXABAY_API_URL", "\"https://pixabay.com/\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(project(":search:domain"))

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    implementation(libs.hilt.android.core)

    implementation(libs.retrofit)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.converter.kotlinx.serialization)
    implementation(libs.okhttp.logging)

    implementation(libs.arrow.core)
    implementation(libs.retrofit.adapters.arrow)

    testImplementation(libs.mockk.android)
    testImplementation(libs.kotlinx.coroutines.test)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}