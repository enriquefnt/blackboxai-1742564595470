#!/bin/bash

# Colores para mensajes
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

echo -e "${YELLOW}Diagnóstico Detallado para macOS${NC}"
echo "----------------------------------------"

# Función para verificar comando
check_command() {
    if command -v $1 &> /dev/null; then
        echo -e "${GREEN}✓ $2 encontrado${NC}"
        $1 --version 2>&1 || $1 -version 2>&1 || echo "Versión no disponible"
    else
        echo -e "${RED}✗ $2 no encontrado${NC}"
    fi
}

# Función para verificar archivo
check_file() {
    if [ -f "$1" ]; then
        echo -e "${GREEN}✓ $2 existe${NC}"
        echo "   $1"
    else
        echo -e "${RED}✗ $2 no existe${NC}"
        echo "   Falta: $1"
    fi
}

# 1. Verificar sistema
echo -e "\n${YELLOW}1. Información del Sistema${NC}"
echo "Sistema Operativo: $(sw_vers -productName) $(sw_vers -productVersion)"
echo "Arquitectura: $(uname -m)"

# 2. Verificar Homebrew
echo -e "\n${YELLOW}2. Verificando Homebrew${NC}"
check_command brew "Homebrew"

# 3. Verificar Java
echo -e "\n${YELLOW}3. Verificando Java${NC}"
check_command java "Java"
if [ -n "$JAVA_HOME" ]; then
    echo -e "${GREEN}✓ JAVA_HOME configurado: $JAVA_HOME${NC}"
else
    echo -e "${RED}✗ JAVA_HOME no configurado${NC}"
fi

# 4. Verificar Android SDK
echo -e "\n${YELLOW}4. Verificando Android SDK${NC}"
check_command sdkmanager "Android SDK"
if [ -f "local.properties" ]; then
    echo -e "${GREEN}✓ local.properties existe${NC}"
    echo "Contenido:"
    cat local.properties
else
    echo -e "${RED}✗ local.properties no existe${NC}"
fi

# 5. Verificar Gradle
echo -e "\n${YELLOW}5. Verificando Gradle${NC}"
check_file "gradle/wrapper/gradle-wrapper.jar" "Gradle Wrapper JAR"
check_file "gradle/wrapper/gradle-wrapper.properties" "Gradle Wrapper Properties"
if [ -f "gradlew" ]; then
    echo -e "${GREEN}✓ gradlew existe${NC}"
    if [ -x "gradlew" ]; then
        echo -e "${GREEN}✓ gradlew tiene permisos de ejecución${NC}"
    else
        echo -e "${RED}✗ gradlew no tiene permisos de ejecución${NC}"
    fi
else
    echo -e "${RED}✗ gradlew no existe${NC}"
fi

# 6. Verificar archivos del proyecto
echo -e "\n${YELLOW}6. Verificando archivos del proyecto${NC}"
required_files=(
    "app/build.gradle"
    "build.gradle"
    "settings.gradle"
    "app/src/main/AndroidManifest.xml"
    "app/src/main/res/values/strings.xml"
    "app/src/main/res/drawable/ic_launcher_foreground.xml"
)

for file in "${required_files[@]}"; do
    check_file "$file" "$(basename $file)"
done

# 7. Verificar espacio en disco
echo -e "\n${YELLOW}7. Verificando espacio en disco${NC}"
df -h . | awk 'NR==2 {print "Espacio disponible: " $4}'

# 8. Verificar memoria
echo -e "\n${YELLOW}8. Verificando memoria${NC}"
memory=$(sysctl hw.memsize | awk '{print $2/1024/1024/1024 " GB"}')
echo "Memoria total: $memory"

echo -e "\n${YELLOW}Recomendaciones:${NC}"
echo "1. Si falta Java:"
echo "   brew uninstall --cask temurin # Desinstalar versiones anteriores"
echo "   brew install --cask zulu17    # Instalar Zulu OpenJDK 17"

echo -e "\n2. Si falta Android SDK:"
echo "   brew install android-commandlinetools"
echo "   sdkmanager --licenses"

echo -e "\n3. Si faltan archivos:"
echo "   ./crear_recursos.sh"

echo -e "\n4. Si hay problemas con Gradle:"
echo "   rm -rf .gradle"
echo "   ./gradlew clean"

echo -e "\n5. Para ver logs detallados:"
echo "   ./gradlew assembleRelease --info"
echo "   ./gradlew assembleRelease --debug"

echo -e "\n${YELLOW}Para continuar:${NC}"
echo "1. Corrige los problemas marcados con ✗"
echo "2. Ejecuta: ./compilar_mac.sh"