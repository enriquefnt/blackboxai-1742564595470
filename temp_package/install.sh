#!/bin/bash

# Colores para mensajes
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

echo -e "${YELLOW}Instalador de CaseApp${NC}"
echo "----------------------------------------"

# Verificar Java
if ! command -v java &> /dev/null; then
    echo -e "${RED}Error: Java no está instalado${NC}"
    echo "Por favor, instala Java 17 o superior:"
    echo "sudo apt install openjdk-17-jdk  # Para Ubuntu/Debian"
    echo "sudo dnf install java-17-openjdk  # Para Fedora"
    exit 1
fi

# Verificar que estamos en el directorio correcto
if [ ! -f "generate-apk.sh" ]; then
    echo -e "${RED}Error: Debes ejecutar este script desde el directorio raíz del proyecto${NC}"
    echo "Ejecuta:"
    echo "git clone https://github.com/enriquefnt/blackboxai-1742564595470.git"
    echo "cd blackboxai-1742564595470"
    echo "./install.sh"
    exit 1
fi

# Dar permisos de ejecución
chmod +x generate-apk.sh
chmod +x gradlew

# Crear directorio para el APK
mkdir -p app/release

# Generar keystore
echo -e "${YELLOW}Generando keystore...${NC}"
keytool -genkey -v \
        -keystore app/release/keystore.jks \
        -alias caseapp \
        -keyalg RSA \
        -keysize 2048 \
        -validity 10000 \
        -storepass caseapp123 \
        -keypass caseapp123 \
        -dname "CN=CaseApp, OU=Development, O=CaseApp, L=City, ST=State, C=US"

# Configurar keystore
echo -e "${YELLOW}Configurando keystore...${NC}"
cat > app/release/keystore.properties << EOF
storePassword=caseapp123
keyPassword=caseapp123
keyAlias=caseapp
storeFile=keystore.jks
EOF

# Descargar dependencias
echo -e "${YELLOW}Descargando dependencias...${NC}"
./gradlew --refresh-dependencies

# Compilar APK
echo -e "${YELLOW}Compilando APK...${NC}"
./gradlew clean assembleRelease

# Verificar si se generó el APK
if [ -f "app/build/outputs/apk/release/app-release.apk" ]; then
    # Copiar APK al directorio de release
    cp app/build/outputs/apk/release/app-release.apk app/release/CaseApp-v1.1.0.apk
    echo -e "${GREEN}¡APK generado exitosamente!${NC}"
    echo "Ubicación: app/release/CaseApp-v1.1.0.apk"
    
    # Instrucciones de instalación
    echo
    echo "Para instalar en tu dispositivo Android:"
    echo "1. Copia el archivo app/release/CaseApp-v1.1.0.apk a tu dispositivo"
    echo "2. En tu dispositivo Android:"
    echo "   - Ve a Ajustes > Seguridad"
    echo "   - Activa 'Orígenes desconocidos'"
    echo "3. Instala el APK"
    echo
    echo "Credenciales iniciales:"
    echo "Usuario: admin"
    echo "Contraseña: admin123"
else
    echo -e "${RED}Error: No se pudo generar el APK${NC}"
    echo "Por favor, revisa los errores anteriores"
    exit 1
fi