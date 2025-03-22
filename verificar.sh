#!/bin/bash

# Colores para mensajes
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

echo -e "${YELLOW}Verificando configuración de CaseApp${NC}"
echo "----------------------------------------"

# Verificar Java
echo -e "\n${YELLOW}1. Verificando Java...${NC}"
if command -v java &> /dev/null; then
    version=$(java -version 2>&1 | head -n 1)
    echo -e "${GREEN}✓ Java instalado: $version${NC}"
else
    echo -e "${RED}✗ Java no está instalado${NC}"
    echo "Instala Java 17:"
    echo "sudo apt install openjdk-17-jdk  # Ubuntu/Debian"
    echo "sudo dnf install java-17-openjdk # Fedora"
fi

# Verificar estructura de directorios
echo -e "\n${YELLOW}2. Verificando estructura de directorios...${NC}"
mkdir -p app/release
if [ -d "app" ]; then
    echo -e "${GREEN}✓ Directorio app existe${NC}"
else
    echo -e "${RED}✗ Directorio app no existe${NC}"
fi

# Verificar archivos necesarios
echo -e "\n${YELLOW}3. Verificando archivos necesarios...${NC}"
files=(
    "build.gradle"
    "app/build.gradle"
    "settings.gradle"
    "gradle.properties"
    "install.sh"
)

all_files_exist=true
for file in "${files[@]}"; do
    if [ -f "$file" ]; then
        echo -e "${GREEN}✓ $file existe${NC}"
    else
        echo -e "${RED}✗ $file no existe${NC}"
        all_files_exist=false
    fi
done

# Verificar permisos
echo -e "\n${YELLOW}4. Verificando permisos...${NC}"
if [ -x "install.sh" ]; then
    echo -e "${GREEN}✓ install.sh tiene permisos de ejecución${NC}"
else
    echo -e "${RED}✗ install.sh no tiene permisos de ejecución${NC}"
    echo "Ejecuta: chmod +x install.sh"
fi

# Resumen
echo -e "\n${YELLOW}Resumen:${NC}"
echo "----------------------------------------"
if $all_files_exist; then
    echo -e "${GREEN}✓ Todos los archivos necesarios están presentes${NC}"
    echo -e "${GREEN}✓ Puedes proceder a ejecutar: ./install.sh${NC}"
else
    echo -e "${RED}✗ Faltan algunos archivos${NC}"
    echo "1. Revisa INSTRUCCIONES_PASO_A_PASO.md"
    echo "2. Copia los archivos faltantes"
    echo "3. Ejecuta este script nuevamente"
fi
echo "----------------------------------------"