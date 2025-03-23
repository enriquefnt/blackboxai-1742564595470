#!/bin/bash

# Colores para mensajes
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

echo -e "${YELLOW}Script de generación de APK Release${NC}"
echo "----------------------------------------"

# Verificar Java
if ! command -v java &> /dev/null; then
    echo -e "${RED}Error: Java no está instalado${NC}"
    exit 1
fi

# Verificar Android SDK
if [ -z "$ANDROID_HOME" ]; then
    echo -e "${RED}Error: ANDROID_HOME no está configurado${NC}"
    exit 1
fi

# Limpiar builds anteriores
echo -e "${YELLOW}Limpiando builds anteriores...${NC}"
./gradlew clean

# Generar keystore si no existe
if [ ! -f "app/keystore.jks" ]; then
    echo -e "${YELLOW}Generando keystore...${NC}"
    keytool -genkey -v \
            -keystore app/keystore.jks \
            -alias caseapp \
            -keyalg RSA \
            -keysize 2048 \
            -validity 10000 \
            -storepass caseapp123 \
            -keypass caseapp123 \
            -dname "CN=CaseApp, OU=Development, O=CaseApp, L=City, ST=State, C=US"
fi

# Generar APK
echo -e "${YELLOW}Generando APK...${NC}"
./gradlew assembleRelease

# Verificar si se generó el APK
APK_PATH="app/build/outputs/apk/release/app-release.apk"
if [ -f "$APK_PATH" ]; then
    echo -e "${GREEN}APK generado exitosamente en:${NC}"
    echo "$APK_PATH"
    
    # Copiar a directorio raíz
    cp "$APK_PATH" "./CaseApp-v1.1.0.apk"
    echo -e "${GREEN}APK copiado a:${NC} CaseApp-v1.1.0.apk"
else
    echo -e "${RED}Error: No se pudo generar el APK${NC}"
    exit 1
fi

echo
echo -e "${GREEN}¡Proceso completado!${NC}"
echo "----------------------------------------"
echo "Para instalar el APK:"
echo "1. Habilitar 'Orígenes desconocidos' en Android"
echo "2. Transferir el APK al dispositivo"
echo "3. Instalar el APK"
echo "----------------------------------------"