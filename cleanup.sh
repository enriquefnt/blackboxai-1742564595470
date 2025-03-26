#!/bin/bash

# Colores para mensajes
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

echo -e "${YELLOW}Limpiando repositorio...${NC}"
echo "----------------------------------------"

# Lista de archivos a conservar
KEEP_FILES=(
    "build_apk_mac.sh"
    "README.md"
    ".gitignore"
)

# 1. Crear directorio temporal
echo -e "\n${YELLOW}1. Creando directorio temporal...${NC}"
mkdir -p temp_clean

# 2. Copiar archivos necesarios
echo -e "\n${YELLOW}2. Copiando archivos necesarios...${NC}"
cp build_apk_mac.sh temp_clean/
cp README_FINAL.md temp_clean/README.md

# 3. Crear .gitignore
echo -e "\n${YELLOW}3. Creando .gitignore...${NC}"
cat > temp_clean/.gitignore << 'EOF'
*.iml
.gradle
/local.properties
/.idea
.DS_Store
/build
/captures
.externalNativeBuild
.cxx
local.properties
app/build/
app/release/
EOF

# 4. Eliminar todo excepto el directorio temporal
echo -e "\n${YELLOW}4. Eliminando archivos innecesarios...${NC}"
find . -maxdepth 1 ! -name 'temp_clean' ! -name '.' ! -name '..' -exec rm -rf {} +

# 5. Mover archivos del directorio temporal
echo -e "\n${YELLOW}5. Restaurando archivos necesarios...${NC}"
mv temp_clean/* .
rm -rf temp_clean

echo -e "\n${GREEN}¡Repositorio limpiado exitosamente!${NC}"
echo "----------------------------------------"
echo "Archivos conservados:"
ls -la
echo "----------------------------------------"
echo "Para generar el APK, ejecuta:"
echo "./build_apk_mac.sh"