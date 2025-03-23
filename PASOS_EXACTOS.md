# Pasos para Actualizar tu Repositorio

## 1. Preparación Local
```bash
# Clona tu repositorio
git clone https://github.com/enriquefnt/blackboxai-1742564595470.git
cd blackboxai-1742564595470

# Crea los directorios necesarios
mkdir -p app/release
mkdir -p gradle/wrapper
mkdir -p database
```

## 2. Archivos a Copiar y Pegar

### En la raíz del proyecto:
1. `install.sh` - Script principal de instalación
2. `build.gradle` - Configuración de Gradle del proyecto
3. `settings.gradle` - Configuración de módulos
4. `gradle.properties` - Propiedades de Gradle

### En la carpeta app/:
1. `app/build.gradle` - Configuración del módulo app
2. `app/proguard-rules.pro` - Reglas de ProGuard

### En la carpeta database/:
1. `database/schema_mysql.sql` - Esquema de la base de datos
2. `database/README.md` - Documentación de la base de datos

## 3. Pasos de Instalación
```bash
# Dar permisos de ejecución
chmod +x install.sh

# Ejecutar el instalador
./install.sh
```

## 4. Verificación
- El APK se generará en: `app/release/CaseApp-v1.1.0.apk`
- Credenciales por defecto:
  * Usuario: admin
  * Contraseña: admin123

## 5. Solución de Problemas

Si encuentras errores:
1. Verifica que Java 17 esté instalado:
   ```bash
   java -version
   ```

2. Si necesitas instalar Java:
   ```bash
   # En Ubuntu/Debian
   sudo apt install openjdk-17-jdk

   # En Fedora
   sudo dnf install java-17-openjdk
   ```

3. Verifica los permisos:
   ```bash
   chmod +x install.sh
   chmod +x gradlew
   ```

## 6. Archivos Disponibles

Para ver el contenido de cualquier archivo:
```bash
./ver_archivo.sh
```

Selecciona el número del archivo que quieres ver:
1. install.sh
2. app/build.gradle
3. build.gradle
4. settings.gradle
5. gradle.properties
6. database/schema_mysql.sql
7. database/README.md
8. README.md
9. MIGRATION.md
10. CHANGELOG.md
11. QUICKSTART.md

## 7. Notas Importantes
- Todos los archivos están en el directorio `/project/sandbox/user-workspace`
- El script `ver_archivo.sh` te ayudará a ver el contenido de cada archivo
- Copia cada archivo en la misma estructura de directorios en tu repositorio local