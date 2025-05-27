plugins {
    alias(libs.plugins.android.library)  // Только для Android-специфичных компонентов
//    id("org.jetbrains.kotlin.plugin.compose") version "2.1.21" apply false
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.jetbrains.kotlin.compose)

//    id("com.squareup.sqldelight") //version "2.1.0" apply false
    id("app.cash.sqldelight") version "2.1.0"

//    alias(libs.plugins.jetbrains.kotlin.jvm)

//    id("kotlin-kapt")  // Обязательно для Hilt!
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")

}
android {
    namespace = "com.example.core"
    compileSdk = 35

    defaultConfig {
        minSdk = 24
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
//kotlin {
////    compilerOptions {
////        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
////    }
//    jvmToolchain(11)
//}

dependencies {
    // Базовые зависимости Kotlin
//    implementation(libs.kotlinx.coroutines.core)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")

    // Сеть. Retrofit интерфейс
    implementation(libs.converter.gson)

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
    implementation("com.squareup.moshi:moshi-kotlin:1.15.0")

//    kapt("com.squareup.moshi:moshi-kotlin-codegen:1.15.0")

    // Hilt
    implementation("com.google.dagger:hilt-android:2.56.2")
//    kapt("com.google.dagger:hilt-compiler:2.56.2")
    ksp("com.google.dagger:hilt-compiler:2.56.2")

    implementation("com.squareup.moshi:moshi:1.15.0") // (libs.moshi)
    //noinspection KaptUsageInsteadOfKsp
//    kapt(libs.moshi.kotlin.codegen)






//    // AndroidX Core
//    implementation("androidx.core:core-ktx:1.16.0")
//    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.9.0")
//    implementation("androidx.activity:activity-compose:1.10.1")
//
      // Compose BOM (Bill of Materials) - управляет версиями
//    implementation(platform("androidx.compose:compose-bom:2025.05.01"))
//
//    // Material 3
//    implementation("androidx.compose.material3:material3") // :1.3.2
//    implementation("androidx.compose.material3:material3-window-size-class")
//
//    // Дополнительные Compose зависимости (версии будут из BOM)
//    implementation("androidx.compose.ui:ui") // :1.8.2
//    implementation("androidx.compose.ui:ui-tooling-preview") // :1.8.2
//    implementation("androidx.compose.ui:ui-util")
//    implementation("androidx.compose.foundation:foundation")
//
//    // Навигация
//    implementation("androidx.navigation:navigation-compose:2.9.0")
////    implementation(libs.androidx.navigation.compose.jvmstubs)
//
//    // ViewModel
//    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.9.0")
//
//    // Coil для загрузки изображений (альтернатива Picasso)
//    implementation("io.coil-kt:coil-compose:2.4.0")
//
//    implementation(libs.picasso)
//    implementation("androidx.appcompat:appcompat:1.7.0")
//    implementation("androidx.recyclerview:recyclerview:1.4.0")
//
//    // SQLDelight
//    implementation("app.cash.sqldelight:sqlite-driver:2.1.0")
//    implementation("app.cash.sqldelight:android-driver:2.1.0")
//    implementation("app.cash.sqldelight:coroutines-extensions-jvm:2.1.0")
//
//
//
//    // Hilt
//    implementation("com.google.dagger:hilt-android:2.56.2")
//    kapt("com.google.dagger:hilt-compiler:2.56.2")
//    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
//
//    //other
//    implementation(libs.androidx.media3.common.ktx)


    // Для работы с LiveData (если нужно)
    implementation("androidx.compose.runtime:runtime-livedata:1.8.2")

    // База данных. SQLDelight
    implementation("app.cash.sqldelight:sqlite-driver:2.1.0")
    implementation("app.cash.sqldelight:android-driver:2.1.0")
    implementation("app.cash.sqldelight:coroutines-extensions-jvm:2.1.0")
//    implementation(libs.android.driver)
//    implementation(libs.coroutines.extensions.jvm)
//    implementation(libs.sqldelight.android.driver)
//    implementation(libs.sqldelight.coroutines.extensions)

    // DI (Koin)
//    api(libs.koin.core) // api, чтобы было доступно в feature-модулях
//    implementation(libs.koin.android)
}

sqldelight {
    databases {
        create("HeroDatabase") {
            packageName.set("com.example")
            srcDirs.setFrom("src/main/sqldelight")
//            schemaVersion = 2
        }
    }
//    ("HeroDatabase") { // Это имя будет использоваться в коде
//            packageName = "com.example.core.database"  // только имя, без путей
////        sourceFolders = ["sqldelight"]
//            sourceFolders = listOf("sqldelight")
////        srcDirs = listOf("src/main/sqldelight")  // не нужен в 1.5.5+
//        }
//    }
}
