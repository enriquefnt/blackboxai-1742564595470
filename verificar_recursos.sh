#!/bin/bash

# Colores para mensajes
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

echo -e "${YELLOW}Verificación de Recursos de Android${NC}"
echo "----------------------------------------"

# Función para verificar archivo
check_file() {
    local file=$1
    local description=$2
    if [ -f "$file" ]; then
        echo -e "${GREEN}✓ $description encontrado${NC}"
        echo "   $file"
    else
        echo -e "${RED}✗ $description no encontrado${NC}"
        echo "   Falta: $file"
    fi
}

# Función para verificar directorio
check_dir() {
    local dir=$1
    local description=$2
    if [ -d "$dir" ]; then
        echo -e "${GREEN}✓ Directorio $description existe${NC}"
    else
        echo -e "${RED}✗ Directorio $description no existe${NC}"
        echo "   Creando: $dir"
        mkdir -p "$dir"
    fi
}

echo -e "\n${YELLOW}1. Verificando estructura de directorios...${NC}"
directories=(
    "app/src/main/res/mipmap-anydpi-v26"
    "app/src/main/res/drawable"
    "app/src/main/res/values"
)

for dir in "${directories[@]}"; do
    check_dir "$dir" "$dir"
done

echo -e "\n${YELLOW}2. Verificando archivos de recursos...${NC}"
check_file "app/src/main/AndroidManifest.xml" "AndroidManifest.xml"
check_file "app/src/main/res/values/strings.xml" "strings.xml"
check_file "app/src/main/res/drawable/ic_launcher_foreground.xml" "Icono principal"
check_file "app/src/main/res/values/ic_launcher_background.xml" "Color de fondo del icono"
check_file "app/src/main/res/mipmap-anydpi-v26/ic_launcher.xml" "Configuración del icono"
check_file "app/src/main/res/mipmap-anydpi-v26/ic_launcher_round.xml" "Configuración del icono redondo"

echo -e "\n${YELLOW}3. Verificando contenido del AndroidManifest.xml...${NC}"
if [ -f "app/src/main/AndroidManifest.xml" ]; then
    if grep -q "android:icon=" "app/src/main/AndroidManifest.xml"; then
        echo -e "${GREEN}✓ Icono definido en AndroidManifest.xml${NC}"
    else
        echo -e "${RED}✗ Falta definición del icono en AndroidManifest.xml${NC}"
    fi
fi

echo -e "\n${YELLOW}4. Verificando strings.xml...${NC}"
if [ -f "app/src/main/res/values/strings.xml" ]; then
    if grep -q "app_name" "app/src/main/res/values/strings.xml"; then
        echo -e "${GREEN}✓ Nombre de la aplicación definido${NC}"
    else
        echo -e "${RED}✗ Falta nombre de la aplicación en strings.xml${NC}"
    fi
fi

echo -e "\n${YELLOW}Recomendaciones:${NC}"
echo "1. Si faltan archivos, ejecuta:"
echo "   ./instalar_mac_v2.sh"
echo -e "\n2. Si hay problemas con el AndroidManifest.xml:"
echo "   - Verifica que el icono esté definido correctamente"
echo "   - Asegúrate de que los recursos referenciados existan"
echo -e "\n3. Para ver errores detallados de compilación:"
echo "   ./gradlew assembleRelease --info"

echo -e "\n${YELLOW}Para continuar:${NC}"
echo "1. Corrige cualquier error marcado con ✗"
echo "2. Ejecuta: ./instalar_mac_v2.sh"