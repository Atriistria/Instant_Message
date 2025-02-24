plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    kotlin("kapt")
}

android {
    namespace = "com.example.instant_message"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.instant_message"
        minSdk = 31
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    buildFeatures {
        viewBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.emoji2.emojipicker)
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.androidx.lifecycle.process)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation (libs.retrofit)
    implementation (libs.converter.gson)
    implementation (libs.androidx.lifecycle.viewmodel.ktx)
    implementation (libs.androidx.lifecycle.livedata.ktx)
    implementation (libs.gson)
    implementation (libs.androidx.lifecycle.viewmodel.ktx.v251)
    implementation (libs.androidx.activity.ktx)
    implementation (libs.logging.interceptor)
    implementation (libs.java.websocket.java.websocket)
    implementation (libs.androidx.navigation.fragment.ktx)
    implementation (libs.androidx.navigation.ui.ktx)
    implementation (libs.alibaba.fastjson)
    implementation (libs.greenrobot.eventbus)
    implementation (libs.room.runtime)
    implementation (libs.androidx.room.ktx)
    kapt (libs.androidx.room.room.compiler3)
    implementation(libs.glide)
    kapt(libs.glide.compiler)
    implementation(libs.work.runtime.ktx.v271)


}