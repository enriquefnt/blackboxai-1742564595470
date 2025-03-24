#!/bin/bash

# Colores para mensajes
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

echo -e "${YELLOW}Limpieza Completa para CaseApp${NC}"
echo "----------------------------------------"

# 1. Eliminar archivos generados
echo -e "\n${YELLOW}1. Eliminando archivos generados...${NC}"
rm -rf app/build
rm -rf app/release
rm -rf build
rm -rf .gradle
rm -f local.properties
rm -f app/release/keystore.jks

# 2. Eliminar recursos
echo -e "\n${YELLOW}2. Eliminando recursos...${NC}"
rm -rf app/src/main/res/mipmap-*
rm -f app/src/main/res/drawable/ic_launcher_foreground.xml
rm -f app/src/main/res/values/ic_launcher_background.xml

# 3. Limpiar Gradle
echo -e "\n${YELLOW}3. Limpiando Gradle...${NC}"
rm -rf gradle/wrapper
rm -f gradlew
rm -f gradlew.bat

# 4. Recrear directorios necesarios
echo -e "\n${YELLOW}4. Recreando directorios...${NC}"
mkdir -p app/release
mkdir -p app/src/main/res/mipmap-anydpi-v26
mkdir -p app/src/main/res/drawable
mkdir -p app/src/main/res/values
mkdir -p gradle/wrapper

echo -e "\n${GREEN}¡Limpieza completada!${NC}"
echo "----------------------------------------"
echo "Ahora ejecuta en este orden:"
echo "1. ./crear_recursos.sh"
echo "2. ./compilar_mac.sh"
echo "----------------------------------------"