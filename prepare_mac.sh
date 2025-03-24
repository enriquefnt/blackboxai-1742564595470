#!/bin/bash

# Colores para mensajes
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

echo -e "${YELLOW}Preparando entorno para CaseApp${NC}"
echo "----------------------------------------"

# Función para verificar comando
check_command() {
    if command -v $1 &> /dev/null; then
        echo -e "${GREEN}✓ $2 instalado${NC}"
        return 0
    else
        echo -e "${RED}✗ $2 no instalado${NC}"
        return 1
    fi
}

# 1. Verificar Homebrew
echo -e "\n${YELLOW}1. Verificando Homebrew...${NC}"
if ! check_command brew "Homebrew"; then
    echo "Instalando Homebrew..."
    /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
fi

# 2. Verificar/Instalar Java
echo -e "\n${YELLOW}2. Verificando Java...${NC}"
if ! check_command java "Java"; then
    echo "Desinstalando versiones anteriores..."
    brew uninstall --cask temurin &>/dev/null || true
    brew uninstall --cask temurin17 &>/dev/null || true
    
    echo "Instalando Zulu OpenJDK 17..."
    brew tap homebrew/cask-versions
    brew install --cask zulu17
fi

# 3. Verificar/Instalar Android SDK
echo -e "\n${YELLOW}3. Verificando Android SDK...${NC}"
if ! check_command sdkmanager "Android SDK"; then
    echo "Instalando Android Command Line Tools..."
    brew install android-commandlinetools
fi

# 4. Aceptar licencias de Android SDK
echo -e "\n${YELLOW}4. Aceptando licencias de Android SDK...${NC}"
yes | sdkmanager --licenses

# 5. Instalar componentes necesarios
echo -e "\n${YELLOW}5. Instalando componentes de Android SDK...${NC}"
sdkmanager "platform-tools" "platforms;android-34" "build-tools;34.0.0"

# 6. Limpiar proyecto
echo -e "\n${YELLOW}6. Limpiando proyecto...${NC}"
rm -rf app/build app/release build .gradle
rm -f local.properties app/release/keystore.jks
mkdir -p app/release gradle/wrapper app/src/main/res/drawable app/src/main/java/com/example/caseapp

# 7. Configurar variables de entorno
echo -e "\n${YELLOW}7. Configurando variables de entorno...${NC}"
ANDROID_SDK_ROOT=$(brew --prefix)/share/android-commandlinetools
echo "sdk.dir=$ANDROID_SDK_ROOT" > local.properties

echo -e "\n${GREEN}¡Entorno preparado exitosamente!${NC}"
echo "----------------------------------------"
echo "Ahora puedes ejecutar:"
echo "./build_simple.sh"
echo "----------------------------------------"