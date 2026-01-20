// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    id("com.google.dagger.hilt.android") version "2.57.2" apply false
    alias(libs.plugins.googleGmsGoogleServices) apply false
    id("com.google.devtools.ksp") version "2.2.0-2.0.2" apply false
}