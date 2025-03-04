plugins {
    alias(libs.plugins.android.application)
}

apply(from = "../shared_dependencies.gradle")

android {
    namespace = "com.fakhrirasyids.heartratemonitor"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.fakhrirasyids.heartratemonitor"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    testOptions {
        unitTests.isReturnDefaultValues = true
    }
    buildFeatures {
        viewBinding = true
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
}

dependencies {
    // Core Module

    implementation(project(":core"))
}