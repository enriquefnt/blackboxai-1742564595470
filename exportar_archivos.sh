#!/bin/bash

# Crear directorio para los archivos exportados
mkdir -p archivos_para_copiar

# Función para exportar un archivo
export_file() {
    local source=$1
    local dest="archivos_para_copiar/$(basename $1).txt"
    echo "=== Contenido de $source ===" > "$dest"
    cat "$source" >> "$dest"
    echo -e "\n=== Fin del archivo ===\n" >> "$dest"
}

# Exportar archivos principales
export_file "build.gradle"
export_file "app/build.gradle"
export_file "settings.gradle"
export_file "gradle.properties"
export_file "install.sh"
export_file "database/schema_mysql.sql"
export_file "README.md"
export_file "MIGRATION.md"
export_file "CHANGELOG.md"
export_file "QUICKSTART.md"

echo "Archivos exportados a la carpeta 'archivos_para_copiar'"
echo "Cada archivo tiene la extensión .txt para fácil lectura"
echo "Copia el contenido de cada archivo (sin incluir las líneas === ) a su ubicación correspondiente"