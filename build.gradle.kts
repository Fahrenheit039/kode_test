// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
//    id("org.jetbrains.kotlin.plugin.compose") version "2.1.21" apply false
    alias(libs.plugins.jetbrains.kotlin.compose) apply false

    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false

    id("com.google.devtools.ksp") version "2.1.21-2.0.1" apply false //kapt -> google.ksp
//    id("com.google.") version "2.1.21" apply false //hilt
    id("com.google.dagger.hilt.android") version "2.56.2" apply false

//    alias(libs.plugins.sqldelight) apply false
//    alias(libs.plugins.hilt) apply false
//    id("com.squareup.sqldelight") version "1.5.5" apply false
}



//buildscript {
////    ext.kotlin_version = "2.1.21"
//    repositories {
//        mavenCentral()
//    }
//    dependencies {
////        classpath("com.google.dagger:hilt-android-gradle-plugin:2.56.2") // last success
//
////        classpath("com.squareup.sqldelight:gradle-plugin:1.5.5")
////        classpath("com.google.dagger:hilt-android-gradle-plugin:2.48")
////        classpath("com.squareup.sqldelight:gradle-plugin")
////        classpath("com.google.dagger:hilt-android-gradle-plugin")
//    }
//}
