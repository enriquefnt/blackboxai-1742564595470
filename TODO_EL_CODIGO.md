# Código Completo para CaseApp

## 1. build.gradle (raíz)
```gradle
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:8.1.4'
        classpath 'org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.20'
        classpath 'androidx.navigation:navigation-safe-args-gradle-plugin:2.6.0'
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven { url 'https://jitpack.io' }  // Para MPAndroidChart
    }
}

tasks.register('clean', Delete) {
    delete rootProject.buildDir
}
```

## 2. settings.gradle
```gradle
pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_PROJECT)
    repositories {
        google()
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}

rootProject.name = "CaseApp"
include ':app'
```

## 3. gradle.properties
```properties
org.gradle.jvmargs=-Xmx2048m -Dfile.encoding=UTF-8
android.useAndroidX=true
android.enableJetifier=true
kotlin.code.style=official

kotlin.incremental=true
kotlin.incremental.java=true
kotlin.incremental.js=true
kotlin.caching.enabled=true
kotlin.parallel.tasks.in.project=true

android.nonTransitiveRClass=true
android.defaults.buildfeatures.buildconfig=true
android.defaults.buildfeatures.aidl=false
android.defaults.buildfeatures.renderscript=false
android.defaults.buildfeatures.resvalues=true
android.defaults.buildfeatures.shaders=false

org.gradle.parallel=true
org.gradle.caching=true
org.gradle.configureondemand=true
```

## 4. app/build.gradle
```gradle
plugins {
    id 'com.android.application'
    id 'org.jetbrains.kotlin.android'
    id 'kotlin-kapt'
    id 'androidx.navigation.safeargs.kotlin'
}

def keystorePropertiesFile = rootProject.file("app/keystore.properties")
def keystoreProperties = new Properties()
keystoreProperties.load(new FileInputStream(keystorePropertiesFile))

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

        javaCompileOptions {
            annotationProcessorOptions {
                arguments += ["room.schemaLocation": "$projectDir/schemas".toString()]
            }
        }
    }

    signingConfigs {
        release {
            keyAlias keystoreProperties['keyAlias']
            keyPassword keystoreProperties['keyPassword']
            storeFile file(keystoreProperties['storeFile'])
            storePassword keystoreProperties['storePassword']
        }
    }

    buildTypes {
        release {
            minifyEnabled true
            shrinkResources true
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
            signingConfig signingConfigs.release
            
            buildConfigField "String", "API_BASE_URL", "\"https://tu-servidor-produccion.com/api/\""
        }
        
        debug {
            applicationIdSuffix ".debug"
            versionNameSuffix "-debug"
            debuggable true
            
            buildConfigField "String", "API_BASE_URL", "\"http://localhost:8000/api/\""
        }
    }

    buildFeatures {
        viewBinding true
        buildConfig true
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
    def lifecycle_version = "2.6.2"
    def room_version = "2.6.1"
    def nav_version = "2.7.5"
    def retrofit_version = "2.9.0"
    def okhttp_version = "4.12.0"
    def work_version = "2.9.0"
    def coroutines_version = "1.7.3"

    implementation 'androidx.core:core-ktx:1.12.0'
    implementation 'androidx.appcompat:appcompat:1.6.1'
    implementation 'com.google.android.material:material:1.10.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
    implementation 'androidx.swiperefreshlayout:swiperefreshlayout:1.1.0'

    implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version"
    implementation "androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version"
    implementation "androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle_version"
    implementation "androidx.lifecycle:lifecycle-viewmodel-savedstate:$lifecycle_version"

    implementation "androidx.navigation:navigation-fragment-ktx:$nav_version"
    implementation "androidx.navigation:navigation-ui-ktx:$nav_version"

    implementation "androidx.room:room-runtime:$room_version"
    implementation "androidx.room:room-ktx:$room_version"
    kapt "androidx.room:room-compiler:$room_version"

    implementation "com.squareup.retrofit2:retrofit:$retrofit_version"
    implementation "com.squareup.retrofit2:converter-gson:$retrofit_version"
    implementation "com.squareup.okhttp3:logging-interceptor:$okhttp_version"

    implementation "androidx.work:work-runtime-ktx:$work_version"
    implementation "androidx.security:security-crypto:1.1.0-alpha06"
    implementation 'com.github.PhilJay:MPAndroidChart:v3.1.0'

    implementation "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutines_version"
    implementation "org.jetbrains.kotlinx:kotlinx-coroutines-play-services:$coroutines_version"

    testImplementation 'junit:junit:4.13.2'
    androidTestImplementation 'androidx.test.ext:junit:1.1.5'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.1'
}
```

## 5. app/keystore.properties
```properties
storePassword=caseapp123
keyPassword=caseapp123
keyAlias=caseapp
storeFile=keystore.jks
```

## 6. install.sh
```bash
#!/bin/bash

# Colores para mensajes
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

echo -e "${YELLOW}Instalador de CaseApp${NC}"
echo "----------------------------------------"

# Verificar Java
if ! command -v java &> /dev/null; then
    echo -e "${RED}Error: Java no está instalado${NC}"
    echo "Por favor, instala Java 17 o superior:"
    echo "sudo apt install openjdk-17-jdk  # Para Ubuntu/Debian"
    echo "sudo dnf install java-17-openjdk  # Para Fedora"
    exit 1
fi

# Crear directorio para el APK
mkdir -p app/release

# Generar keystore
echo -e "${YELLOW}Generando keystore...${NC}"
keytool -genkey -v \
        -keystore app/release/keystore.jks \
        -alias caseapp \
        -keyalg RSA \
        -keysize 2048 \
        -validity 10000 \
        -storepass caseapp123 \
        -keypass caseapp123 \
        -dname "CN=CaseApp, OU=Development, O=CaseApp, L=City, ST=State, C=US"

# Compilar APK
echo -e "${YELLOW}Compilando APK...${NC}"
./gradlew clean assembleRelease

# Verificar si se generó el APK
if [ -f "app/build/outputs/apk/release/app-release.apk" ]; then
    cp app/build/outputs/apk/release/app-release.apk app/release/CaseApp-v1.1.0.apk
    echo -e "${GREEN}¡APK generado exitosamente!${NC}"
    echo "Ubicación: app/release/CaseApp-v1.1.0.apk"
else
    echo -e "${RED}Error: No se pudo generar el APK${NC}"
    exit 1
fi
```

## Pasos para Implementar

1. Clona tu repositorio:
```bash
git clone https://github.com/enriquefnt/blackboxai-1742564595470.git
cd blackboxai-1742564595470
```

2. Crea los directorios necesarios:
```bash
mkdir -p app/release gradle/wrapper database
```

3. Copia cada sección de código a su archivo correspondiente:
   - Copia el contenido de cada sección en el archivo correspondiente
   - Mantén la misma estructura de directorios

4. Haz commit de los cambios:
```bash
git add .
git commit -m "Actualización completa con AndroidX"
git push origin main
```

5. Genera el APK:
```bash
chmod +x install.sh
./install.sh
```

El APK se generará en: `app/release/CaseApp-v1.1.0.apk`