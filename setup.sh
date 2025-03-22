#!/bin/bash

# Colores para mensajes
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

echo -e "${YELLOW}Script de configuración de CaseApp${NC}"
echo "----------------------------------------"

# Crear directorios
echo -e "${YELLOW}Creando directorios...${NC}"
mkdir -p app/release gradle/wrapper

# Crear build.gradle
echo -e "${YELLOW}Creando build.gradle...${NC}"
cat > build.gradle << 'EOF'
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
        maven { url 'https://jitpack.io' }
    }
}

tasks.register('clean', Delete) {
    delete rootProject.buildDir
}
EOF

# Crear settings.gradle
echo -e "${YELLOW}Creando settings.gradle...${NC}"
cat > settings.gradle << 'EOF'
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
EOF

# Crear gradle.properties
echo -e "${YELLOW}Creando gradle.properties...${NC}"
cat > gradle.properties << 'EOF'
org.gradle.jvmargs=-Xmx2048m -Dfile.encoding=UTF-8
android.useAndroidX=true
android.enableJetifier=true
kotlin.code.style=official
EOF

# Crear app/build.gradle
echo -e "${YELLOW}Creando app/build.gradle...${NC}"
cat > app/build.gradle << 'EOF'
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
    }

    buildTypes {
        release {
            minifyEnabled true
            shrinkResources true
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
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
    implementation 'androidx.core:core-ktx:1.12.0'
    implementation 'androidx.appcompat:appcompat:1.6.1'
    implementation 'com.google.android.material:material:1.10.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
    implementation 'com.github.PhilJay:MPAndroidChart:v3.1.0'
}
EOF

# Crear install.sh
echo -e "${YELLOW}Creando install.sh...${NC}"
cat > install.sh << 'EOF'
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
EOF

# Dar permisos de ejecución
echo -e "${YELLOW}Dando permisos de ejecución...${NC}"
chmod +x install.sh

echo -e "${GREEN}¡Configuración completada!${NC}"
echo "----------------------------------------"
echo "Ahora puedes ejecutar:"
echo "./install.sh"
echo "----------------------------------------"