# Contenido para app/build.gradle

```gradle
plugins {
    id 'com.android.application'
    id 'org.jetbrains.kotlin.android'
    id 'kotlin-kapt'
    id 'androidx.navigation.safeargs.kotlin'
}

android {
    namespace 'com.example.caseapp'
    compileSdk 34

    defaultConfig {
        applicationId "com.example.caseapp"
        minSdk 24
        targetSdk 34
        versionCode 2
        versionName "1.1.0"

        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            minifyEnabled true
            shrinkResources true
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }

    buildFeatures {
        viewBinding true
    }

    compileOptions {
        sourceCompatibility JavaVersion.VERSION_17
        targetCompatibility JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = '17'
    }
}

dependencies {
    // AndroidX Core
    implementation 'androidx.core:core-ktx:1.12.0'
    implementation 'androidx.appcompat:appcompat:1.6.1'
    implementation 'com.google.android.material:material:1.10.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
    implementation 'androidx.swiperefreshlayout:swiperefreshlayout:1.1.0'

    // Architecture Components
    def lifecycle_version = "2.6.2"
    implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version"
    implementation "androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version"
    implementation "androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle_version"
    implementation "androidx.lifecycle:lifecycle-viewmodel-savedstate:$lifecycle_version"

    // Navigation
    def nav_version = "2.7.5"
    implementation "androidx.navigation:navigation-fragment-ktx:$nav_version"
    implementation "androidx.navigation:navigation-ui-ktx:$nav_version"

    // Room
    def room_version = "2.6.1"
    implementation "androidx.room:room-runtime:$room_version"
    implementation "androidx.room:room-ktx:$room_version"
    kapt "androidx.room:room-compiler:$room_version"

    // Retrofit + OkHttp
    def retrofit_version = "2.9.0"
    implementation "com.squareup.retrofit2:retrofit:$retrofit_version"
    implementation "com.squareup.retrofit2:converter-gson:$retrofit_version"
    implementation "com.squareup.okhttp3:logging-interceptor:4.12.0"

    // WorkManager
    def work_version = "2.9.0"
    implementation "androidx.work:work-runtime-ktx:$work_version"

    // Security
    implementation "androidx.security:security-crypto:1.1.0-alpha06"

    // Charts
    implementation 'com.github.PhilJay:MPAndroidChart:v3.1.0'

    // Testing
    testImplementation 'junit:junit:4.13.2'
    androidTestImplementation 'androidx.test.ext:junit:1.1.5'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.1'
}
```

## Pasos para Implementar

1. Abre tu proyecto en Android Studio
2. Localiza el archivo `app/build.gradle`
3. Reemplaza todo el contenido con el código de arriba
4. Sincroniza el proyecto con Gradle (Sync Now)

## Notas Importantes

- Este archivo actualiza todas las dependencias a AndroidX
- Configura Java 17 y Kotlin actualizado
- Incluye todas las bibliotecas necesarias para la app
- Habilita ViewBinding
- Configura ProGuard para release

## Posibles Problemas

Si encuentras errores:
1. Verifica que tengas Java 17 instalado
2. Asegúrate de que Android Studio esté actualizado
3. Invalida caché y reinicia (File > Invalidate Caches)
4. Sincroniza el proyecto con Gradle

## Siguiente Paso

Después de actualizar este archivo:
```bash
./gradlew clean build
```

Si todo compila correctamente, puedes proceder a ejecutar:
```bash
./install.sh