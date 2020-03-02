plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
    id("kotlinx-serialization")
}

android {
    compileSdkVersion(29)

    defaultConfig {
        applicationId = "com.priamm.mviexample"

        minSdkVersion(21)
        targetSdkVersion(29)

        versionName = "1.0"
        versionCode = 1

        buildToolsVersion = "29.0.2"


        buildTypes {

            getByName("release") {
                isMinifyEnabled = true

                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    file("proguard-rules.pro")
                )
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    packagingOptions {
        exclude("META-INF/*.kotlin_module")
    }
}

androidExtensions {
        isExperimental = true
}


dependencies {

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.3.61")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.3")
    implementation("androidx.core:core-ktx:1.3.0-alpha01")

    implementation("androidx.appcompat:appcompat:1.2.0-alpha02")
    implementation("androidx.constraintlayout:constraintlayout:2.0.0-beta4")
    implementation("com.google.android.material:material:1.2.0-alpha05")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0-alpha03")
    implementation("androidx.lifecycle:lifecycle-runtime:2.2.0")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation ("androidx.lifecycle:lifecycle-common-java8:2.2.0")

    implementation ("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.14.0")
    implementation ("io.ktor:ktor-client-okhttp:1.3.1")
    implementation ("io.ktor:ktor-client-logging-jvm:1.3.1")
    implementation ("io.ktor:ktor-client-serialization-jvm:1.3.1")

    implementation ("com.jakewharton.timber:timber:4.7.1")
}

