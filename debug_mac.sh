#!/bin/bash

# Colores para mensajes
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

echo -e "${YELLOW}Diagnóstico Detallado de Errores para macOS${NC}"
echo "----------------------------------------"

# Función para mostrar el estado
show_status() {
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✓ OK${NC}"
    else
        echo -e "${RED}✗ ERROR${NC}"
        echo -e "${YELLOW}Posible solución:${NC} $1"
    fi
}

# 1. Verificar Xcode
echo -e "\n${YELLOW}1. Verificando Xcode Command Line Tools${NC}"
xcode-select -p
show_status "Ejecuta: xcode-select --install"

# 2. Verificar Java
echo -e "\n${YELLOW}2. Verificando Java${NC}"
echo "Versión de Java:"
java -version 2>&1
show_status "Ejecuta: brew install --cask temurin17"

# 3. Verificar JAVA_HOME
echo -e "\n${YELLOW}3. Verificando JAVA_HOME${NC}"
echo "JAVA_HOME=$JAVA_HOME"
if [ -n "$JAVA_HOME" ]; then
    echo -e "${GREEN}✓ JAVA_HOME está configurado${NC}"
else
    echo -e "${RED}✗ JAVA_HOME no está configurado${NC}"
    echo -e "Solución: Agrega a ~/.zshrc:"
    echo 'export JAVA_HOME=$(/usr/libexec/java_home -v 17)'
fi

# 4. Verificar Android SDK
echo -e "\n${YELLOW}4. Verificando Android SDK${NC}"
if command -v sdkmanager &> /dev/null; then
    echo -e "${GREEN}✓ Android SDK encontrado${NC}"
    echo "Componentes instalados:"
    sdkmanager --list_installed
else
    echo -e "${RED}✗ Android SDK no encontrado${NC}"
    echo "Solución: brew install android-commandlinetools"
fi

# 5. Verificar local.properties
echo -e "\n${YELLOW}5. Verificando local.properties${NC}"
if [ -f "local.properties" ]; then
    echo "Contenido de local.properties:"
    cat local.properties
else
    echo -e "${RED}✗ local.properties no existe${NC}"
    echo "Creando local.properties..."
    ANDROID_SDK_ROOT=$(brew --prefix)/share/android-commandlinetools
    echo "sdk.dir=$ANDROID_SDK_ROOT" > local.properties
fi

# 6. Verificar Gradle
echo -e "\n${YELLOW}6. Verificando Gradle${NC}"
if [ -f "gradle/wrapper/gradle-wrapper.jar" ]; then
    echo -e "${GREEN}✓ gradle-wrapper.jar encontrado${NC}"
else
    echo -e "${RED}✗ gradle-wrapper.jar no encontrado${NC}"
    echo "Solución: Ejecuta ./reiniciar_mac.sh"
fi

# 7. Verificar keystore
echo -e "\n${YELLOW}7. Verificando keystore${NC}"
if [ -f "app/release/keystore.jks" ]; then
    echo -e "${GREEN}✓ keystore.jks encontrado${NC}"
    keytool -list -v -keystore app/release/keystore.jks -storepass caseapp123
else
    echo -e "${RED}✗ keystore.jks no encontrado${NC}"
    echo "Solución: Se creará automáticamente al ejecutar el script"
fi

# 8. Verificar permisos
echo -e "\n${YELLOW}8. Verificando permisos${NC}"
for script in gradlew instalar_mac.sh compilar_mac.sh; do
    if [ -x "$script" ]; then
        echo -e "${GREEN}✓ $script es ejecutable${NC}"
    else
        echo -e "${RED}✗ $script no es ejecutable${NC}"
        echo "Solución: chmod +x $script"
    fi
done

echo -e "\n${YELLOW}Recomendaciones:${NC}"
echo "1. Si hay errores de Java:"
echo "   brew install --cask temurin17"
echo -e "\n2. Si hay errores de Android SDK:"
echo "   brew install android-commandlinetools"
echo "   sdkmanager --licenses"
echo -e "\n3. Si hay errores de Gradle:"
echo "   ./reiniciar_mac.sh"
echo -e "\n4. Si persisten los errores:"
echo "   rm -rf app/release app/build build .gradle"
echo "   ./instalar_mac.sh"

echo -e "\n${YELLOW}Para ver logs detallados:${NC}"
echo "./gradlew assembleRelease --debug"