
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.jetbrains.kotlin.compose)

//    id("kotlin-kapt")  // Обязательно для Hilt!
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.example.feature_heroes_list"
    compileSdk = 35

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    // 1. Основные зависимости
    implementation(project(":core"))

    // Для управления системным интерфейсом
//    implementation("com.google.accompanist:systemuicontroller:0.32.0")
    implementation("androidx.datastore:datastore-preferences:1.1.7") // Preferences

    implementation("androidx.compose.material:material-icons-extended:1.7.8")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")

    // AndroidX Core
    implementation("androidx.core:core-ktx:1.16.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.9.0")
    implementation("androidx.activity:activity-compose:1.10.1")

    // Compose BOM (Bill of Materials) - управляет версиями
    implementation(platform("androidx.compose:compose-bom:2025.05.01"))

    // Material 3
    implementation("androidx.compose.material3:material3") // :1.3.2
    implementation("androidx.compose.material3:material3-window-size-class")

    // Дополнительные Compose зависимости (версии будут из BOM)
    implementation("androidx.compose.ui:ui") // :1.8.2
    implementation("androidx.compose.ui:ui-tooling-preview") // :1.8.2
    implementation("androidx.compose.ui:ui-util")
    implementation("androidx.compose.foundation:foundation")

    // Навигация
    implementation("androidx.navigation:navigation-compose:2.9.0")
//    implementation(libs.androidx.navigation.compose.jvmstubs)

    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.9.0")

    // Coil для загрузки изображений (альтернатива Picasso)
    implementation("io.coil-kt:coil-compose:2.4.0")

    implementation(libs.picasso)
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.recyclerview:recyclerview:1.4.0")

    // SQLDelight
    implementation("app.cash.sqldelight:sqlite-driver:2.1.0")
    implementation("app.cash.sqldelight:android-driver:2.1.0")
    implementation("app.cash.sqldelight:coroutines-extensions-jvm:2.1.0")

    // Сеть. Retrofit интерфейс
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
    implementation("com.squareup.moshi:moshi-kotlin:1.15.0")
//    kapt("com.squareup.moshi:moshi-kotlin-codegen:1.15.0")

//    implementation(libs.moshi)
    implementation("com.squareup.moshi:moshi:1.15.0")
    //noinspection KaptUsageInsteadOfKsp
//    kapt(libs.moshi.kotlin.codegen)

//    implementation(libs.converter.gson)

    // Hilt
    implementation("com.google.dagger:hilt-android:2.56.2")
//    kapt("com.google.dagger:hilt-compiler:2.56.2")
    ksp("com.google.dagger:hilt-compiler:2.56.2")

    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    //other
    implementation(libs.androidx.media3.common.ktx)
    // Для работы с LiveData (если нужно)
    implementation("androidx.compose.runtime:runtime-livedata:1.8.2")


    // Отладочные зависимости
    debugImplementation("androidx.compose.ui:ui-tooling:1.8.2")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.8.2")
//    debugImplementation(libs.androidx.ui.tooling)
//    debugImplementation(libs.androidx.ui.test.manifest)

    // Тестирование
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2025.05.01"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
}


