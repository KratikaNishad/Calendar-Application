plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.calendarapplication"
    compileSdk = 35

    testOptions {
        unitTests {
            all {
                it.enabled = true
            }
        }
    }

    defaultConfig {
        applicationId = "com.example.calendarapplication"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.junit.ktx)
    implementation(libs.androidx.espresso.core)
    implementation(libs.espresso.contrib)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    testImplementation (libs.mockito.core)
    testImplementation (libs.androidx.core.testing)
    testImplementation (libs.kotlinx.coroutines.test)
    androidTestImplementation("androidx.test:runner:1.6.1") // or use 1.5.2 as well
    androidTestImplementation ("androidx.test:rules:1.6.1") // Ensure this version is the same for both


    implementation(kotlin("test"))

    /** MockWebServer for unit testing network calls **/
    testImplementation (libs.mockwebserver)

    /**view model lib**/
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation ("androidx.activity:activity-ktx:1.7.0")

    /**livedata lib**/
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.8.7")

    /** Retrofit **/
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("androidx.gridlayout:gridlayout:1.0.0")
    implementation (libs.logging.interceptor)
}