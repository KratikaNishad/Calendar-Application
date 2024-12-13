buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath (libs.google.services)
        classpath (libs.gradle)
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
}
