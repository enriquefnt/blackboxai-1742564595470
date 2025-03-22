#!/bin/bash

echo "Generando APK para CaseApp..."

# Crear directorio para el APK
mkdir -p app/release

# Generar keystore si no existe
if [ ! -f "app/release/keystore.jks" ]; then
    echo "Generando keystore..."
    keytool -genkey -v \
            -keystore app/release/keystore.jks \
            -alias caseapp \
            -keyalg RSA \
            -keysize 2048 \
            -validity 10000 \
            -storepass caseapp123 \
            -keypass caseapp123 \
            -dname "CN=CaseApp, OU=Development, O=CaseApp, L=City, ST=State, C=US"
fi

# Crear archivo de propiedades del keystore
echo "Configurando keystore..."
cat > app/release/keystore.properties << EOF
storePassword=caseapp123
keyPassword=caseapp123
keyAlias=caseapp
storeFile=keystore.jks
EOF

# Compilar APK
echo "Compilando APK..."
./gradlew assembleRelease

# Copiar APK al directorio de release
cp app/build/outputs/apk/release/app-release.apk app/release/CaseApp-v1.1.0.apk

echo "APK generado en: app/release/CaseApp-v1.1.0.apk"