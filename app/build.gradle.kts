plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.gms.google-services")
}

android {
    namespace = "dam.pmdm.rickandmortyapi"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "dam.pmdm.rickandmortyapi"
        minSdk = 26
        targetSdk = 36
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
    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures{
        viewBinding = true
    }

}

dependencies {
    // RED / PETICIONES HTTP

    // Librería para hacer peticiones HTTP
    implementation(libs.retrofit)
    // Para convertir JSON a objetos Kotlin/Java automáticamente
    implementation(libs.converter.gson)


    // ARCHITECTURE / MVVM

    // Permite usar ViewModel con coroutines y sintaxis Kotlin
    implementation(libs.androidx.lifecycle.viewmodel.ktx)


    // IMÁGENES

    // Para cargar imágenes desde URLs de forma simple y eficiente
    implementation(libs.coil)


    // FIREBASE

    // Import the Firebase BoM gestiona versiones de Firebase automáticamente
    implementation(platform("com.google.firebase:firebase-bom:33.13.0"))

    // Firebase Analytics
    implementation("com.google.firebase:firebase-analytics")

    // Firebase Authentication
    implementation(libs.google.firebase.auth.ktx)

    // Firestore (base de datos en la nube)
    implementation("com.google.firebase:firebase-firestore-ktx") // Firestore


    // GRÁFICAS

    // Llibrería para gráficas
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}