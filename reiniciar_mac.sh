#!/bin/bash

# Colores para mensajes
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

echo -e "${YELLOW}Reiniciando configuración de CaseApp${NC}"
echo "----------------------------------------"

# Eliminar archivos generados
echo -e "\n${YELLOW}1. Eliminando archivos generados...${NC}"
rm -rf app/release
rm -rf app/build
rm -rf build
rm -rf .gradle
rm -f local.properties

# Recrear directorios
echo -e "\n${YELLOW}2. Recreando directorios...${NC}"
mkdir -p app/release
mkdir -p gradle/wrapper

# Crear local.properties
echo -e "\n${YELLOW}3. Configurando Android SDK...${NC}"
ANDROID_SDK_ROOT=$(brew --prefix)/share/android-commandlinetools
echo "sdk.dir=$ANDROID_SDK_ROOT" > local.properties

# Dar permisos
echo -e "\n${YELLOW}4. Configurando permisos...${NC}"
chmod +x gradlew
chmod +x compilar_mac.sh
chmod +x instalar_dependencias_mac.sh
chmod +x diagnostico_mac.sh

# Configurar JAVA_HOME
echo -e "\n${YELLOW}5. Configurando JAVA_HOME...${NC}"
JAVA_HOME=$(/usr/libexec/java_home -v 17)
export JAVA_HOME
echo "JAVA_HOME configurado a: $JAVA_HOME"

echo -e "\n${GREEN}¡Reinicio completado!${NC}"
echo "----------------------------------------"
echo "Ahora ejecuta en este orden:"
echo "1. ./diagnostico_mac.sh"
echo "2. ./instalar_dependencias_mac.sh"
echo "3. ./compilar_mac.sh"
echo "----------------------------------------"