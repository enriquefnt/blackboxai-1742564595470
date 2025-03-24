#!/bin/bash

# Colores para mensajes
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

echo -e "${YELLOW}Compilador Minimalista de CaseApp${NC}"
echo "----------------------------------------"

# 1. Limpiar todo
echo -e "\n${YELLOW}1. Limpiando proyecto...${NC}"
rm -rf app/build app/release build .gradle
rm -f local.properties app/release/keystore.jks
mkdir -p app/release gradle/wrapper app/src/main/res/drawable

# 2. Copiar build.gradle simplificado
echo -e "\n${YELLOW}2. Configurando build.gradle...${NC}"
cp simple_build.gradle app/build.gradle

# 3. Crear build.gradle raíz
echo -e "\n${YELLOW}3. Creando build.gradle raíz...${NC}"
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

# 4. Crear settings.gradle
echo -e "\n${YELLOW}4. Creando settings.gradle...${NC}"
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

# 5. Crear icono simple
echo -e "\n${YELLOW}5. Creando icono...${NC}"
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

# 6. Crear strings.xml
echo -e "\n${YELLOW}6. Creando strings.xml...${NC}"
mkdir -p app/src/main/res/values
cat > app/src/main/res/values/strings.xml << 'EOF'
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="app_name">CaseApp</string>
</resources>
EOF

# 7. Actualizar AndroidManifest.xml
echo -e "\n${YELLOW}7. Actualizando AndroidManifest.xml...${NC}"
cat > app/src/main/AndroidManifest.xml << 'EOF'
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android">
    <application
        android:allowBackup="true"
        android:icon="@drawable/ic_app"
        android:label="@string/app_name"
        android:roundIcon="@drawable/ic_app"
        android:supportsRtl="true"
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

# 8. Configurar Gradle
# echo -e "\n${YELLOW}8. Configurando Gradle...${NC}"
# curl -L -o gradle/wrapper/gradle-wrapper.jar https://raw.githubusercontent.com/gradle/gradle/v8.4.0/gradle/wrapper/gradle-wrapper.jar
# chmod +x gradlew

# 8. Configurar Gradle
echo -e "\n${YELLOW}8. Configurando Gradle...${NC}"
curl -L -o gradle/wrapper/gradle-wrapper.jar https://raw.githubusercontent.com/gradle/gradle/v8.4.0/gradle/wrapper/gradle-wrapper.jar
if [ -f gradlew ]; then
    chmod +x gradlew
else
    echo -e "\n${RED}Error: El archivo gradlew no se encontró.${NC}"
    exit 1
fi

# 9. Configurar Android SDK
echo -e "\n${YELLOW}9. Configurando Android SDK...${NC}"
ANDROID_SDK_ROOT=$(brew --prefix)/share/android-commandlinetools
echo "sdk.dir=$ANDROID_SDK_ROOT" > local.properties

# 10. Compilar APK
echo -e "\n${YELLOW}10. Compilando APK...${NC}"
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