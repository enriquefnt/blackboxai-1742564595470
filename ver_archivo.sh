#!/bin/bash

# Colores para mensajes
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

echo -e "${YELLOW}Visor de Archivos CaseApp${NC}"
echo "----------------------------------------"
echo "Archivos disponibles:"
echo

# Listar archivos principales
echo -e "${GREEN}1. Archivos de configuración:${NC}"
echo "   1) install.sh"
echo "   2) app/build.gradle"
echo "   3) build.gradle"
echo "   4) settings.gradle"
echo "   5) gradle.properties"

echo -e "\n${GREEN}2. Archivos de base de datos:${NC}"
echo "   6) database/schema_mysql.sql"
echo "   7) database/README.md"

echo -e "\n${GREEN}3. Documentación:${NC}"
echo "   8) README.md"
echo "   9) MIGRATION.md"
echo "   10) CHANGELOG.md"
echo "   11) QUICKSTART.md"

echo -e "\n${YELLOW}Ingresa el número del archivo que quieres ver (o 'q' para salir):${NC}"
read -p "> " opcion

case $opcion in
    1) cat install.sh ;;
    2) cat app/build.gradle ;;
    3) cat build.gradle ;;
    4) cat settings.gradle ;;
    5) cat gradle.properties ;;
    6) cat database/schema_mysql.sql ;;
    7) cat database/README.md ;;
    8) cat README.md ;;
    9) cat MIGRATION.md ;;
    10) cat CHANGELOG.md ;;
    11) cat QUICKSTART.md ;;
    q) exit 0 ;;
    *) echo -e "${RED}Opción no válida${NC}" ;;
esac