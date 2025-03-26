#!/bin/bash

# Colores para mensajes
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

echo -e "${YELLOW}Limpiando repositorio...${NC}"

# Archivos a conservar
KEEP=(
    "build_apk_mac.sh"
    "README.md"
    ".git"
    ".gitignore"
)

# Crear lista de archivos a eliminar
FILES_TO_DELETE=()
for file in *; do
    if [[ ! " ${KEEP[@]} " =~ " ${file} " ]]; then
        FILES_TO_DELETE+=("$file")
    fi
done

# Eliminar archivos
if [ ${#FILES_TO_DELETE[@]} -gt 0 ]; then
    echo -e "\n${YELLOW}Eliminando archivos innecesarios...${NC}"
    rm -rf "${FILES_TO_DELETE[@]}"
fi

# Crear .gitignore si no existe
if [ ! -f ".gitignore" ]; then
    echo -e "\n${YELLOW}Creando .gitignore...${NC}"
    cat > .gitignore << 'EOF'
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
fi

echo -e "\n${GREEN}¡Repositorio limpiado!${NC}"
echo "----------------------------------------"
echo "Archivos conservados:"
ls -la
echo "----------------------------------------"