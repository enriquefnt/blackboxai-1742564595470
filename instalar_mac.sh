#!/bin/bash

# Colores para mensajes
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

echo -e "${YELLOW}Instalador Completo de CaseApp para macOS${NC}"
echo "----------------------------------------"

# Función para verificar resultado
check_result() {
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✓ OK${NC}"
    else
        echo -e "${RED}✗ Error${NC}"
        exit 1
    fi
}

# 1. Verificar Xcode Command Line Tools
echo -e "\n${YELLOW}1. Verificando Xcode Command Line Tools...${NC}"
if ! command -v xcode-select &> /dev/null; then
    echo "Instalando Xcode Command Line Tools..."
    xcode-select --install
    echo "Por favor, espera a que termine la instalación de Xcode Command Line Tools"
    echo "Presiona ENTER cuando haya terminado"
    read
fi
check_result

# 2. Verificar/Instalar Homebrew
echo -e "\n${YELLOW}2. Verificando Homebrew...${NC}"
if ! command -v brew &> /dev/null; then
    echo "Instalando Homebrew..."
    /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
fi
check_result

# 3. Instalar Java 17
echo -e "\n${YELLOW}3. Instalando Java 17...${NC}"
brew tap homebrew/cask-versions
brew install --cask temurin17
check_result

# 4. Configurar JAVA_HOME
echo -e "\n${YELLOW}4. Configurando JAVA_HOME...${NC}"
JAVA_HOME=$(/usr/libexec/java_home -v 17)
export JAVA_HOME
echo "export JAVA_HOME=$JAVA_HOME" >> ~/.zshrc
source ~/.zshrc
check_result

# 5. Instalar Android Command Line Tools
echo -e "\n${YELLOW}5. Instalando Android Command Line Tools...${NC}"
brew install android-commandlinetools
check_result

# 6. Configurar Android SDK
echo -e "\n${YELLOW}6. Configurando Android SDK...${NC}"
ANDROID_SDK_ROOT=$(brew --prefix)/share/android-commandlinetools
echo "sdk.dir=$ANDROID_SDK_ROOT" > local.properties
check_result

# 7. Aceptar licencias
echo -e "\n${YELLOW}7. Aceptando licencias de Android SDK...${NC}"
yes | sdkmanager --licenses
check_result

# 8. Instalar componentes necesarios
echo -e "\n${YELLOW}8. Instalando componentes de Android SDK...${NC}"
sdkmanager "platform-tools" "platforms;android-34" "build-tools;34.0.0"
check_result

# 9. Limpiar instalación anterior
echo -e "\n${YELLOW}9. Limpiando instalación anterior...${NC}"
rm -rf app/release app/build build .gradle
mkdir -p app/release gradle/wrapper
check_result

# 10. Configurar Gradle
echo -e "\n${YELLOW}10. Configurando Gradle...${NC}"
curl -L -o gradle/wrapper/gradle-wrapper.jar https://raw.githubusercontent.com/gradle/gradle/v8.4.0/gradle/wrapper/gradle-wrapper.jar
chmod +x gradlew
check_result

# 11. Generar keystore
echo -e "\n${YELLOW}11. Generando keystore...${NC}"
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
check_result

# 12. Compilar APK
echo -e "\n${YELLOW}12. Compilando APK...${NC}"
./gradlew clean assembleRelease
check_result

# Verificar resultado final
if [ -f "app/build/outputs/apk/release/app-release.apk" ]; then
    cp app/build/outputs/apk/release/app-release.apk app/release/CaseApp-v1.1.0.apk
    echo -e "\n${GREEN}¡Instalación completada exitosamente!${NC}"
    echo "----------------------------------------"
    echo "El APK está en: app/release/CaseApp-v1.1.0.apk"
    echo -e "\nCredenciales:"
    echo "- Usuario: admin"
    echo "- Contraseña: admin123"
else
    echo -e "\n${RED}Error: No se pudo generar el APK${NC}"
    exit 1
fi