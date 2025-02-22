plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.dagger.hilt.android")
    id("kotlin-kapt")
}

android {
    namespace = "com.globant.imdb2"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.globant.imdb2"
        minSdk = 34
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            merges += "META-INF/LICENSE.md"
            merges += "META-INF/LICENSE-notice.md"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.runtime.android)
    implementation(libs.androidx.room.common)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(libs.junit.jupiter)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(project(":app:domain"))
    implementation(project(":app:data"))
    implementation(project(":app:presentation"))

    //Animations

    implementation(libs.lottie.compose)

    //Retrofit

    implementation (libs.retrofit2.retrofit)
    implementation (libs.retrofit2.converter.gson)

    //Room
    implementation(libs.room.runtime)
    implementation(libs.androidx.room.ktx)
    kapt(libs.room.compiler)

    //Test

    testImplementation(libs.junit.v413)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.inline)
    testImplementation(libs.mockito.kotlin)

    testImplementation (libs.mockito.inline.v520)
    testImplementation (libs.androidx.core.testing)
    testImplementation (libs.kotlinx.coroutines.test)
    testImplementation (libs.robolectric)


    implementation(libs.androidx.constraintlayout.compose)

    implementation (libs.androidx.graphics.shapes)

    implementation (libs.androidx.runtime.livedata)

    //Datastore

    implementation("androidx.datastore:datastore-preferences:1.0.0")



    //Hilt
    implementation(libs.dagger.hilt.android)
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    kapt(libs.hilt.android.compiler)


    // Coroutines for asynchronous programming
    implementation (libs.kotlinx.coroutines.android)

    //Glide
    implementation(libs.landscapist.glide)

    implementation(libs.androidx.material)


    //Navigation
    implementation(libs.androidx.navigation.compose)
}