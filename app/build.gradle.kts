plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.doit1"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.doit1"
        minSdk = 29
        targetSdk = 34
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {


    implementation(platform("com.google.firebase:firebase-bom:33.9.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth:22.0.0")
    implementation("com.google.firebase:firebase-database:20.2.0")
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.firebase:firebase-storage")
    implementation("com.google.firebase:firebase-database")
    testImplementation(libs.junit)
    implementation("com.github.bumptech.glide:glide:4.15.1")
//    implementation("com.github.bumptech.glide:glide:4.16.0")
//    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")

    implementation("com.squareup.picasso:picasso:2.8")
    annotationProcessor("com.github.bumptech.glide:compiler:4.15.1")
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

}

