#!/bin/bash

# Colores para mensajes
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

echo -e "${YELLOW}Script de configuración de base de datos CaseApp${NC}"
echo "----------------------------------------"

# Solicitar credenciales
read -p "Usuario MySQL: " MYSQL_USER
read -s -p "Contraseña MySQL: " MYSQL_PASS
echo
echo

# Crear base de datos
echo -e "${YELLOW}Creando base de datos...${NC}"
mysql -u "$MYSQL_USER" -p"$MYSQL_PASS" <<EOF
CREATE DATABASE IF NOT EXISTS caseapp;
USE caseapp;
EOF

if [ $? -eq 0 ]; then
    echo -e "${GREEN}Base de datos creada exitosamente${NC}"
else
    echo -e "${RED}Error al crear la base de datos${NC}"
    exit 1
fi

# Importar esquema
echo -e "${YELLOW}Importando esquema...${NC}"
mysql -u "$MYSQL_USER" -p"$MYSQL_PASS" caseapp < schema_mysql.sql

if [ $? -eq 0 ]; then
    echo -e "${GREEN}Esquema importado exitosamente${NC}"
else
    echo -e "${RED}Error al importar el esquema${NC}"
    exit 1
fi

echo
echo -e "${GREEN}¡Configuración completada!${NC}"
echo "----------------------------------------"
echo "Base de datos: caseapp"
echo "Usuario admin: admin"
echo "Contraseña: admin123"
echo "----------------------------------------"