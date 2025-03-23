#!/bin/bash

# Colores para mensajes
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

echo -e "${YELLOW}Diagnóstico de Entorno para CaseApp en macOS${NC}"
echo "----------------------------------------"

# Verificar Java
echo -e "\n${YELLOW}1. Verificando Java...${NC}"
if command -v java &> /dev/null; then
    java_version=$(java -version 2>&1 | head -n 1)
    echo -e "${GREEN}✓ Java instalado:${NC}"
    echo "   $java_version"
else
    echo -e "${RED}✗ Java no está instalado${NC}"
fi

# Verificar JAVA_HOME
echo -e "\n${YELLOW}2. Verificando JAVA_HOME...${NC}"
if [ -n "$JAVA_HOME" ]; then
    echo -e "${GREEN}✓ JAVA_HOME configurado:${NC}"
    echo "   $JAVA_HOME"
else
    echo -e "${RED}✗ JAVA_HOME no está configurado${NC}"
fi

# Verificar Android SDK
echo -e "\n${YELLOW}3. Verificando Android SDK...${NC}"
if command -v sdkmanager &> /dev/null; then
    echo -e "${GREEN}✓ Android SDK instalado${NC}"
    echo "Componentes instalados:"
    sdkmanager --list_installed
else
    echo -e "${RED}✗ Android SDK no encontrado${NC}"
fi

# Verificar Homebrew
echo -e "\n${YELLOW}4. Verificando Homebrew...${NC}"
if command -v brew &> /dev/null; then
    echo -e "${GREEN}✓ Homebrew instalado${NC}"
    echo "Versión: $(brew --version)"
else
    echo -e "${RED}✗ Homebrew no está instalado${NC}"
fi

# Verificar estructura de directorios
echo -e "\n${YELLOW}5. Verificando estructura de directorios...${NC}"
directories=(
    "app"
    "app/release"
    "gradle/wrapper"
)

for dir in "${directories[@]}"; do
    if [ -d "$dir" ]; then
        echo -e "${GREEN}✓ Directorio $dir existe${NC}"
    else
        echo -e "${RED}✗ Directorio $dir no existe${NC}"
    fi
done

# Verificar archivos necesarios
echo -e "\n${YELLOW}6. Verificando archivos necesarios...${NC}"
files=(
    "build.gradle"
    "app/build.gradle"
    "settings.gradle"
    "gradle.properties"
    "gradle/wrapper/gradle-wrapper.properties"
    "gradle/wrapper/gradle-wrapper.jar"
)

for file in "${files[@]}"; do
    if [ -f "$file" ]; then
        echo -e "${GREEN}✓ Archivo $file existe${NC}"
    else
        echo -e "${RED}✗ Archivo $file no existe${NC}"
    fi
done

# Verificar permisos
echo -e "\n${YELLOW}7. Verificando permisos...${NC}"
executables=(
    "gradlew"
    "compilar_mac.sh"
    "instalar_dependencias_mac.sh"
)

for exec in "${executables[@]}"; do
    if [ -x "$exec" ]; then
        echo -e "${GREEN}✓ $exec tiene permisos de ejecución${NC}"
    else
        echo -e "${RED}✗ $exec no tiene permisos de ejecución${NC}"
    fi
done

# Verificar espacio en disco
echo -e "\n${YELLOW}8. Verificando espacio en disco...${NC}"
df -h . | awk 'NR==2 {print "Espacio disponible: " $4}'

# Resumen
echo -e "\n${YELLOW}Resumen de Diagnóstico:${NC}"
echo "----------------------------------------"
echo "Para resolver problemas:"

echo -e "\n1. Si falta Java:"
echo "   brew tap homebrew/cask-versions"
echo "   brew install --cask temurin17"

echo -e "\n2. Si falta Android SDK:"
echo "   brew install android-commandlinetools"
echo "   sdkmanager --licenses"

echo -e "\n3. Si faltan permisos:"
echo "   chmod +x gradlew"
echo "   chmod +x compilar_mac.sh"
echo "   chmod +x instalar_dependencias_mac.sh"

echo -e "\n4. Si faltan directorios:"
echo "   mkdir -p app/release gradle/wrapper"

echo -e "\n5. Para configurar JAVA_HOME:"
echo '   echo "export JAVA_HOME=$(/usr/libexec/java_home -v 17)" >> ~/.zshrc'
echo "   source ~/.zshrc"

echo -e "\n${YELLOW}Para continuar:${NC}"
echo "1. Resuelve los problemas marcados con ✗"
echo "2. Ejecuta: ./instalar_dependencias_mac.sh"
echo "3. Ejecuta: ./compilar_mac.sh"