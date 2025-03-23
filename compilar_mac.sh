#!/bin/bash

# Colores para mensajes
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

echo -e "${YELLOW}Compilador Completo de CaseApp para macOS${NC}"
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

# 1. Crear recursos
echo -e "\n${YELLOW}1. Creando recursos necesarios...${NC}"
./crear_recursos.sh
check_result "Creación de recursos"

# 2. Verificar Java
echo -e "\n${YELLOW}2. Verificando Java...${NC}"
if ! command -v java &> /dev/null; then
    echo -e "${RED}Java no está instalado. Instalando...${NC}"
    brew tap homebrew/cask-versions
    brew install --cask zulu17
    check_result "Instalación de Java"
else
    java_version=$(java -version 2>&1 | head -n 1)
    echo -e "${GREEN}Java instalado: $java_version${NC}"
fi

# 3. Verificar Android SDK
echo -e "\n${YELLOW}3. Verificando Android SDK...${NC}"
if ! command -v sdkmanager &> /dev/null; then
    echo -e "${RED}Android SDK no encontrado. Instalando...${NC}"
    brew install android-commandlinetools
    check_result "Instalación de Android SDK"
fi

# 4. Limpiar instalación anterior
echo -e "\n${YELLOW}4. Limpiando instalación anterior...${NC}"
rm -rf app/release app/build build .gradle
mkdir -p app/release gradle/wrapper
check_result "Limpieza"

# 5. Configurar Android SDK
echo -e "\n${YELLOW}5. Configurando Android SDK...${NC}"
ANDROID_SDK_ROOT=$(brew --prefix)/share/android-commandlinetools
echo "sdk.dir=$ANDROID_SDK_ROOT" > local.properties
check_result "Configuración de Android SDK"

# 6. Configurar Gradle
echo -e "\n${YELLOW}6. Configurando Gradle...${NC}"
curl -L -o gradle/wrapper/gradle-wrapper.jar https://raw.githubusercontent.com/gradle/gradle/v8.4.0/gradle/wrapper/gradle-wrapper.jar
chmod +x gradlew
check_result "Configuración de Gradle"

# 7. Generar keystore
echo -e "\n${YELLOW}7. Generando keystore...${NC}"
rm -f app/release/keystore.jks
keytool -genkey -v \
        -keystore app/release/keystore.jks \
        -alias caseapp \
        -keyalg RSA \
        -keysize 2048 \
        -validity 10000 \
        -storepass caseapp123 \
        -keypass caseapp123 \
        -dname "CN=CaseApp, OU=Development, O=CaseApp, L=City, ST=State, C=US"
check_result "Generación de keystore"

# 8. Compilar APK con información detallada
echo -e "\n${YELLOW}8. Compilando APK...${NC}"
./gradlew clean assembleRelease --info
check_result "Compilación"

# Verificar resultado final
if [ -f "app/build/outputs/apk/release/app-release.apk" ]; then
    cp app/build/outputs/apk/release/app-release.apk app/release/CaseApp-v1.1.0.apk
    echo -e "\n${GREEN}¡APK generado exitosamente!${NC}"
    echo "----------------------------------------"
    echo "El APK está en: app/release/CaseApp-v1.1.0.apk"
    echo -e "\nCredenciales:"
    echo "- Usuario: admin"
    echo "- Contraseña: admin123"
else
    echo -e "\n${RED}Error: No se pudo generar el APK${NC}"
    echo "Revisa los mensajes de error anteriores"
    exit 1
fi

# Mostrar logs si hay errores
if [ $? -ne 0 ]; then
    echo -e "\n${YELLOW}Logs de error:${NC}"
    cat app/build/outputs/logs/manifest-merger-release-report.txt 2>/dev/null
    echo -e "\n${YELLOW}Para más detalles, ejecuta:${NC}"
    echo "./gradlew assembleRelease --debug"
fi