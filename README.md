# CaseApp - Aplicación de Seguimiento Antropométrico

Aplicación Android para el seguimiento antropométrico de pacientes pediátricos, implementando gráficos de crecimiento según los estándares de la OMS.

## Características

- Visualización de detalles del caso con información personal
- Gráficos antropométricos:
  - Peso para Edad
  - Talla para Edad
  - IMC para Edad
- Lista de controles con medidas y z-scores
- Sincronización offline-first con base de datos local
- Interfaz Material Design 3

## Tecnologías Utilizadas

- Kotlin
- MVVM Architecture
- Android Architecture Components
  - ViewModel
  - LiveData
  - Room
  - Navigation
  - WorkManager
- Retrofit para comunicación con API
- MPAndroidChart para gráficos
- Material Design Components

## Estructura del Proyecto

- `data/`: Modelos y acceso a datos (local y remoto)
- `ui/`: Componentes de la interfaz de usuario
- `util/`: Clases utilitarias
- `worker/`: Workers para tareas en segundo plano

## Configuración

1. Clonar el repositorio
2. Abrir en Android Studio
3. Sincronizar el proyecto con Gradle
4. Ejecutar la aplicación

## Licencia

Este proyecto está bajo la Licencia MIT - ver el archivo [LICENSE](LICENSE) para más detalles.

}////////////////////////////////////////////
# CaseApp - Aplicación de Seguimiento Antropométrico

## Generación del APK

### Opción 1: Script Automatizado
```bash
# Dar permisos de ejecución
chmod +x build-release.sh

# Ejecutar script
./build-release.sh
```
El APK se generará como `CaseApp-v1.1.0.apk`

### Opción 2: Compilación Manual
1. Configurar keystore:
   ```bash
   keytool -genkey -v \
           -keystore app/keystore.jks \
           -alias caseapp \
           -keyalg RSA \
           -keysize 2048 \
           -validity 10000 \
           -storepass caseapp123 \
           -keypass caseapp123
   ```

2. Compilar:
   ```bash
   ./gradlew clean assembleRelease
   ```
   El APK se generará en `app/build/outputs/apk/release/app-release.apk`

## Instalación

1. Habilitar "Orígenes desconocidos" en Android:
   - Ajustes > Seguridad > Orígenes desconocidos

2. Transferir el APK al dispositivo

3. Instalar:
   - Abrir el APK
   - Seguir las instrucciones de instalación

## Configuración del Servidor

1. Configurar MySQL:
   ```bash
   cd database
   ./setup_mysql.sh
   ```

2. Credenciales iniciales:
   - Usuario: admin
   - Contraseña: admin123

## Desarrollo

### Requisitos
- Android Studio Hedgehog | 2023.1.1+
- JDK 17
- Gradle 8.4
- MySQL 5.7+

### Configuración
1. Clonar repositorio:
   ```bash
   git clone https://github.com/enriquefnt/blackboxai-1742564595470.git
   cd blackboxai-1742564595470
   ```

2. Abrir en Android Studio

3. Configurar servidor:
   - Editar `app/build.gradle`
   - Actualizar `API_BASE_URL`

### Compilación
```bash
# Debug
./gradlew assembleDebug

# Release
./gradlew assembleRelease
```

## Documentación

- [Guía Rápida](QUICKSTART.md)
- [Registro de Cambios](CHANGELOG.md)
- [Migración](MIGRATION.md)

## Soporte

- Crear issues en GitHub
- Consultar documentación
- Revisar logs de la aplicación
