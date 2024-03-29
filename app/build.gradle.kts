plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    //plugin necessário para login com conta google
    id("com.google.gms.google-services")
    id("com.google.secrets_gradle_plugin") version "0.5"
    //id ("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")

}

android {
    namespace = "com.example.snappet"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.snappet"
        //minSdk = 24
        minSdk = 26
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        dataBinding = true
        viewBinding = true
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    // Maps
    implementation ("com.google.maps.android:maps-compose:4.3.0")



    //ThreeTenABP (used to get dates)
    implementation ("com.jakewharton.threetenabp:threetenabp:1.3.0")

    implementation("androidx.compose.runtime:runtime-livedata:1.5.4")


    //Firebase
    //implementation ("com.google.firebase:firebase-auth-ktx:21.1.0")
    implementation ("com.google.android.gms:play-services-auth:20.4.1")
    //Lifecycle
    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.0")
    implementation ("androidx.lifecycle:lifecycle-runtime-compose:2.6.0")
    //Navigation
    implementation ("androidx.navigation:navigation-compose:2.5.3")
    //Coil compose para carregar profile imagens
    implementation ("io.coil-kt:coil-compose:2.2.2")
    implementation("com.google.firebase:firebase-auth:22.3.0")
    //RealTime Database
    implementation("com.google.firebase:firebase-database:20.3.0")
    implementation("com.google.firebase:firebase-storage:20.3.0")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")

    // Retrofit with Kotlin serialization Converter

    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0")
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    // Kotlin serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")



    //-----------------Adicionei o que esta encima-------------------

    //dependency for the navigation.
    //implementation ("androidx.navigation:navigation-compose:2.7.0-rc01")
    implementation("androidx.navigation:navigation-compose:2.4.2")

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")

    implementation("androidx.activity:activity-compose:1.8.0")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui-android:1.5.4")
    implementation("androidx.compose.ui:ui-text-android:1.5.4")

    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation("androidx.navigation:navigation-runtime-ktx:2.7.5")

    val camerax_version = "1.2.2"
    implementation ("androidx.camera:camera-core:${camerax_version}")
    implementation ("androidx.camera:camera-camera2:${camerax_version}")
    implementation ("androidx.camera:camera-lifecycle:${camerax_version}")
    implementation ("androidx.camera:camera-video:${camerax_version}")
    implementation ("androidx.camera:camera-view:${camerax_version}")
    implementation ("androidx.camera:camera-extensions:${camerax_version}")


    implementation("dev.chrisbanes.accompanist:accompanist-glide:0.4.2")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.2")
    implementation ("com.google.android.gms:play-services-tasks:17.2.2")

    // Optionally, you can include the Compose utils library for Clustering,
    // Street View metadata checks, etc.
    implementation ("com.google.maps.android:maps-compose-utils:4.3.0")

    // Optionally, you can include the widgets library for ScaleBar, etc.
    implementation ("com.google.maps.android:maps-compose-widgets:4.3.0")


    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}




