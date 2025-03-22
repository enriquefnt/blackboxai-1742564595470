# Guía Rápida de Implementación - CaseApp v1.1.0

## 1. Instalación Rápida

### Opción 1: Instalar APK Pre-compilado
1. Descargar `app-release.apk` desde la sección Releases
2. En el dispositivo Android:
   - Ajustes > Seguridad > Habilitar "Orígenes desconocidos"
   - Instalar el APK descargado

### Opción 2: Compilar desde Código Fuente

#### Requisitos
- Android Studio Hedgehog | 2023.1.1+
- JDK 17
- Gradle 8.4

#### Pasos
1. Clonar el repositorio:
   ```bash
   git clone https://github.com/enriquefnt/blackboxai-1742564595470.git
   cd blackboxai-1742564595470
   ```

2. Configurar el servidor:
   - En `app/build.gradle`, actualizar:
     ```gradle
     buildConfigField "String", "API_BASE_URL", "\"https://tu-servidor.com/api/\""
     ```

3. Compilar:
   ```bash
   # Debug version
   ./gradlew assembleDebug

   # Release version
   ./gradlew assembleRelease
   ```

## 2. Configuración del Servidor

### Base de Datos MySQL
1. Crear base de datos:
   ```sql
   CREATE DATABASE caseapp;
   USE caseapp;
   ```

2. Importar esquema:
   ```bash
   mysql -u tu_usuario -p caseapp < database/schema_mysql.sql
   ```

### Credenciales Iniciales
- Usuario: admin
- Contraseña: admin123

## 3. Verificación de la Instalación

1. Abrir la aplicación
2. Iniciar sesión con las credenciales por defecto
3. Verificar la conexión con el servidor
4. Crear un caso de prueba
5. Agregar un control antropométrico
6. Verificar los gráficos

## 4. Solución de Problemas Comunes

### Error de Compilación
```bash
# Limpiar y reconstruir
./gradlew clean build
```

### Error de Conexión
1. Verificar URL del servidor en build.gradle
2. Comprobar que el servidor esté accesible
3. Verificar credenciales MySQL

### Error de Sincronización
1. Verificar conexión a internet
2. Comprobar logs de la aplicación
3. Verificar permisos de usuario

## 5. Próximos Pasos

1. Cambiar la contraseña del administrador
2. Crear usuarios adicionales
3. Configurar copias de seguridad
4. Revisar la documentación completa

## 6. Notas de la Versión 1.1.0

- Migración completa a AndroidX
- Optimizaciones de rendimiento
- Soporte para Android 14
- Mejoras en la seguridad
- Reducción del tamaño de la APK

## 7. Enlaces Útiles

- [Código Fuente](https://github.com/enriquefnt/blackboxai-1742564595470)
- [Registro de Cambios](CHANGELOG.md)
- [Documentación Completa](README.md)
- [Guía de Contribución](CONTRIBUTING.md)

## 8. Soporte

Para reportar problemas o solicitar ayuda:
- Crear un issue en GitHub
- Consultar la documentación
- Revisar el registro de cambios