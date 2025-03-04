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

        buildConfigField("String", "BLE_DEVICE_NAME", "\"SimulatedServer\"")

        buildConfigField("String", "HEART_RATE_SERVICE_UUID", "\"0000180D-0000-1000-8000-00805F9B34FB\"")
        buildConfigField("String", "HEART_RATE_CHARACTERISTIC_UUID", "\"00002A37-0000-1000-8000-00805F9B34FB\"")

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