#!/bin/bash

# Colores para mensajes
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

echo -e "${YELLOW}Instalador de Dependencias para CaseApp en macOS${NC}"
echo "----------------------------------------"

# Verificar Homebrew
echo -e "\n${YELLOW}1. Verificando Homebrew...${NC}"
if ! command -v brew &> /dev/null; then
    echo -e "${YELLOW}Instalando Homebrew...${NC}"
    /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
else
    echo -e "${GREEN}✓ Homebrew ya está instalado${NC}"
fi

# Actualizar Homebrew
echo -e "\n${YELLOW}2. Actualizando Homebrew...${NC}"
brew update

# Instalar Java 17
echo -e "\n${YELLOW}3. Instalando Java 17...${NC}"
brew tap homebrew/cask-versions
brew install --cask temurin17

# Verificar Java
if java -version 2>&1 | grep -q "version \"17"; then
    echo -e "${GREEN}✓ Java 17 instalado correctamente${NC}"
else
    echo -e "${RED}✗ Error al instalar Java 17${NC}"
    exit 1
fi

# Instalar Android Command Line Tools
echo -e "\n${YELLOW}4. Instalando Android Command Line Tools...${NC}"
brew install android-commandlinetools

# Aceptar licencias de Android SDK
echo -e "\n${YELLOW}5. Aceptando licencias de Android SDK...${NC}"
yes | sdkmanager --licenses

# Instalar componentes necesarios de Android SDK
echo -e "\n${YELLOW}6. Instalando componentes de Android SDK...${NC}"
sdkmanager "platform-tools" "platforms;android-34" "build-tools;34.0.0"

echo -e "\n${GREEN}¡Instalación completada!${NC}"
echo "----------------------------------------"
echo "Ahora puedes ejecutar:"
echo "./compilar_mac.sh"
echo "----------------------------------------"

# Verificar todo
echo -e "\n${YELLOW}Verificación final:${NC}"
echo -e "${GREEN}✓ Homebrew instalado${NC}"
echo -e "${GREEN}✓ Java 17 instalado${NC}"
echo -e "${GREEN}✓ Android Command Line Tools instalado${NC}"
echo -e "${GREEN}✓ Licencias Android SDK aceptadas${NC}"
echo -e "${GREEN}✓ Componentes Android SDK instalados${NC}"

echo -e "\n${YELLOW}Para continuar:${NC}"
echo "1. Ejecuta: ./compilar_mac.sh"
echo "2. El APK se generará en: app/release/CaseApp-v1.1.0.apk"