import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.pinwheel.tictactoe"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.pinwheel.tictactoe"
        minSdk = 27
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        setProperty("archivesBaseName", "TicTacToe")

        ndk {
            abiFilters.add("armeabi-v7a")
            abiFilters.add("arm64-v8a")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures.compose = true
    composeOptions.kotlinCompilerExtensionVersion = "1.5.14"

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlin.compilerOptions.jvmTarget.set(JvmTarget.JVM_17)

    /**From android 28, android studio don't compress apk classes. Also PlayStore compress it automatically for the user. And Google suggest to use app bundle for better optimizations.
     * But our FieldX don't support app bundle, we are using legacy apk packaging systems to minimize unoptimized apk download from our server to minimize bandwidth.
     * We have to do it because FieldX don't unpack apk and optimize it for users.*/
    packaging {
        dex.useLegacyPackaging = true
        jniLibs.useLegacyPackaging = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(platform(libs.androidX.compose.bom))
    implementation(libs.androidX.compose.material3)
    implementation(libs.androidX.compose.ui.tooling.preview)
    implementation(libs.androidX.activity.compose)
    implementation(libs.androidX.activity.ktx)
    implementation(libs.androidX.compose.ui)
    implementation(libs.androidX.splashscreen)
    debugImplementation(libs.androidX.compose.ui.tooling)
    debugImplementation(libs.androidX.compose.animation)
    debugImplementation(libs.androidX.compose.foundation)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}