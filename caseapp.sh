#!/bin/bash

# Colores para mensajes
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

echo -e "${YELLOW}CaseApp Builder para macOS${NC}"
echo "----------------------------------------"

# Función para verificar resultado
check_result() {
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✓ OK${NC}"
    else
        echo -e "${RED}✗ Error: $1${NC}"
        exit 1
    fi
}

# 1. Limpiar todo
echo -e "\n${YELLOW}1. Limpiando proyecto...${NC}"
rm -rf *
rm -rf .gradle .idea
rm -f .gitignore local.properties

# 2. Crear estructura básica
echo -e "\n${YELLOW}2. Creando estructura...${NC}"
mkdir -p app/src/main/{java/com/example/caseapp,res/{drawable,values}}
mkdir -p app/release gradle/wrapper

# 3. Crear .gitignore
echo -e "\n${YELLOW}3. Creando .gitignore...${NC}"
cat > .gitignore << 'EOF'
*.iml
.gradle
/local.properties
/.idea
.DS_Store
/build
/captures
.externalNativeBuild
.cxx
local.properties
app/build/
app/release/
EOF

# 4. Crear README.md
echo -e "\n${YELLOW}4. Creando README...${NC}"
cat > README.md << 'EOF'
# CaseApp Builder para macOS

## UN COMANDO

```bash
./caseapp.sh
```

## Requisitos

```bash
# Instalar Java 17
brew install --cask zulu17

# Instalar Android SDK
brew install android-commandlinetools
```

## El APK

El APK estará en:
```
app/release/CaseApp-v1.1.0.apk
```

¡Un comando y listo!
EOF

# 5. Crear MainActivity.java
echo -e "\n${YELLOW}5. Creando MainActivity...${NC}"
cat > app/src/main/java/com/example/caseapp/MainActivity.java << 'EOF'
package com.example.caseapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(android.R.layout.activity_main);
    }
}
EOF

# 6. Crear AndroidManifest.xml
echo -e "\n${YELLOW}6. Creando AndroidManifest.xml...${NC}"
cat > app/src/main/AndroidManifest.xml << 'EOF'
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android">
    <application
        android:allowBackup="true"
        android:icon="@drawable/ic_app"
        android:label="@string/app_name"
        android:theme="@style/Theme.AppCompat">
        <activity
            android:name=".MainActivity"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>
</manifest>
EOF

# 7. Crear strings.xml
echo -e "\n${YELLOW}7. Creando strings.xml...${NC}"
cat > app/src/main/res/values/strings.xml << 'EOF'
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="app_name">CaseApp</string>
</resources>
EOF

# 8. Crear icono
echo -e "\n${YELLOW}8. Creando icono...${NC}"
cat > app/src/main/res/drawable/ic_app.xml << 'EOF'
<?xml version="1.0" encoding="utf-8"?>
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp"
    android:height="24dp"
    android:viewportWidth="24"
    android:viewportHeight="24">
    <path
        android:fillColor="#000000"
        android:pathData="M12,2C6.48,2 2,6.48 2,12s4.48,10 10,10 10,-4.48 10,-10S17.52,2 12,2zM12,5c1.66,0 3,1.34 3,3s-1.34,3 -3,3 -3,-1.34 -3,-3 1.34,-3 3,-3zM12,19.2c-2.5,0 -4.71,-1.28 -6,-3.22 0.03,-1.99 4,-3.08 6,-3.08 1.99,0 5.97,1.09 6,3.08 -1.29,1.94 -3.5,3.22 -6,3.22z"/>
</vector>
EOF

# 9. Crear build.gradle
echo -e "\n${YELLOW}9. Creando build.gradle...${NC}"
cat > build.gradle << 'EOF'
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:8.1.4'
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}
EOF

# 10. Crear app/build.gradle
cat > app/build.gradle << 'EOF'
plugins {
    id 'com.android.application'
}

android {
    namespace 'com.example.caseapp'
    compileSdk 34

    defaultConfig {
        applicationId "com.example.caseapp"
        minSdk 24
        targetSdk 34
        versionCode 1
        versionName "1.0"
    }

    buildTypes {
        release {
            minifyEnabled false
        }
    }

    compileOptions {
        sourceCompatibility JavaVersion.VERSION_17
        targetCompatibility JavaVersion.VERSION_17
    }
}

dependencies {
    implementation 'androidx.appcompat:appcompat:1.6.1'
}
EOF

# 11. Crear settings.gradle
echo -e "\n${YELLOW}10. Creando settings.gradle...${NC}"
cat > settings.gradle << 'EOF'
pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
rootProject.name = "CaseApp"
include ':app'
EOF

# 12. Configurar Gradle
echo -e "\n${YELLOW}11. Configurando Gradle...${NC}"
curl -L -o gradle/wrapper/gradle-wrapper.jar https://raw.githubusercontent.com/gradle/gradle/v8.4.0/gradle/wrapper/gradle-wrapper.jar
chmod +x gradlew

# 13. Configurar Android SDK
echo -e "\n${YELLOW}12. Configurando Android SDK...${NC}"
ANDROID_SDK_ROOT=$(brew --prefix)/share/android-commandlinetools
echo "sdk.dir=$ANDROID_SDK_ROOT" > local.properties

# 14. Compilar APK
echo -e "\n${YELLOW}13. Compilando APK...${NC}"
./gradlew clean assembleRelease --info

# Verificar resultado
if [ -f "app/build/outputs/apk/release/app-release.apk" ]; then
    cp app/build/outputs/apk/release/app-release.apk app/release/CaseApp-v1.1.0.apk
    echo -e "\n${GREEN}¡APK generado exitosamente!${NC}"
    echo "El APK está en: app/release/CaseApp-v1.1.0.apk"
else
    echo -e "\n${RED}Error: No se pudo generar el APK${NC}"
    echo "Revisa los mensajes de error anteriores"
    exit 1
fi