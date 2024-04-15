plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.registration"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.registration"
        minSdk = 28
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}


dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.drawerlayout:drawerlayout:1.2.0")
    implementation("androidx.activity:activity:1.8.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //volley
    implementation ("com.android.volley:volley:1.2.1")
    //picasso
    implementation ("com.squareup.picasso:picasso:2.8")

    //notification icon for cart
    implementation ("com.nex3z:notification-badge:1.0.4")
    //razorpay
    implementation ("com.razorpay:checkout:1.6.33")

}