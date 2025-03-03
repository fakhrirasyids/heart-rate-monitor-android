plugins {
    alias(libs.plugins.android.library)
}

apply(from = "../shared_dependencies.gradle")

android {
    namespace = "com.fakhrirasyids.heartratemonitor.core"
    compileSdk = 35

    defaultConfig {
        minSdk = 24

        buildConfigField("String", "APP_ENDPOINT", "\"https://api.humanapi.co/v1/human/\"")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }
    buildFeatures {
        buildConfig = true
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


}