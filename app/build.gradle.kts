import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("kotlin-kapt")
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    alias(libs.plugins.googleGmsGoogleServices)
    alias(libs.plugins.composeCompiler)
}

android {
    namespace = "ru.zakablukov.yourmoviebase"
    compileSdk = 36

    defaultConfig {
        applicationId = "ru.zakablukov.yourmoviebase"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val secretProperties = Properties()
        secretProperties.load(project.rootProject.file("secrets.properties").inputStream())

        buildConfigField(
            "String",
            "X_API_KEY",
            "\"${secretProperties["X_API_KEY"] as String}\""
        )
        buildConfigField(
            "String",
            "WEB_CLIENT_ID",
            "\"${secretProperties["WEB_CLIENT_ID"] as String}\""
        )
    }

    buildFeatures {
        buildConfig = true
        viewBinding = true
        compose = true
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
    kotlin {
        compilerOptions.jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_1_8)
    }
}

kapt {
    correctErrorTypes = true
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //ViewBinding
    implementation(libs.viewbindingpropertydelegate.noreflection)

    //Compose
    val composeBom = platform("androidx.compose:compose-bom:2026.01.00")
    implementation(composeBom)
    androidTestImplementation(composeBom)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.ui.tooling.preview)
    debugImplementation(libs.androidx.ui.tooling)
    implementation(libs.androidx.adaptive)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.androidx.runtime.rxjava2)

    //Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    //Navigation
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.navigation.dynamic.features.fragment)

    //Firebase
    implementation(libs.firebase.auth)

    //Gson
    implementation(libs.gson)

    //OkHttp
    implementation(libs.okhttp)
    implementation(libs.okhttp3.logging.interceptor)

    //Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit2.converter.gson)
    implementation(libs.retrofit2.adapter.rxjava3)

    //Glide
    implementation(libs.glide)

    //Paging
    implementation(libs.androidx.paging.runtime.ktx)

    //Google Play services
    implementation(libs.play.services.auth)

    //Credentials
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)

    //MLKit Translate
    implementation(libs.translate)

    //Room
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    //Kotlin Extensions and Coroutines support for Room
    implementation(libs.androidx.room.ktx)
    //Paging 3 Integration
    implementation(libs.androidx.room.paging)
}