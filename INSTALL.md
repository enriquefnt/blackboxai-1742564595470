# Guía de Instalación de CaseApp

## 1. Configuración del Servidor

### 1.1 Requisitos del Servidor
- MySQL 5.7 o superior
- PHP 7.4 o superior (para el backend API)
- Servidor web (Apache/Nginx)

### 1.2 Configuración de la Base de Datos
1. Dar permisos de ejecución al script:
   ```bash
   chmod +x database/setup_mysql.sh
   ```

2. Ejecutar el script de configuración:
   ```bash
   ./database/setup_mysql.sh
   ```

3. Verificar la instalación accediendo con el usuario admin:
   - Usuario: admin
   - Contraseña: admin123

## 2. Instalación de la Aplicación Android

### 2.1 Instalación desde APK (Recomendado)
1. Habilitar "Fuentes desconocidas" en tu dispositivo Android:
   - Ajustes > Seguridad > Fuentes desconocidas

2. Descargar el archivo APK:
   - Visitar la sección Releases del repositorio
   - Descargar la última versión de `caseapp.apk`

3. Instalar la aplicación:
   - Abrir el archivo APK descargado
   - Seguir las instrucciones de instalación

### 2.2 Compilación desde Código Fuente
1. Requisitos:
   - Android Studio 4.2 o superior
   - JDK 11 o superior
   - SDK Android (API 33)

2. Clonar el repositorio:
   ```bash
   git clone https://github.com/enriquefnt/blackboxai-1742564595470.git
   cd blackboxai-1742564595470
   ```

3. Abrir en Android Studio:
   - File > Open > Seleccionar el directorio del proyecto

4. Configurar el servidor:
   - Abrir `app/src/main/java/com/example/caseapp/network/RetrofitClient.kt`
   - Modificar `BASE_URL` con la URL de tu servidor

5. Compilar la aplicación:
   - Build > Build Bundle(s) / APK(s) > Build APK(s)

6. Instalar en el dispositivo:
   - Conectar el dispositivo Android por USB
   - Habilitar "Depuración USB" en el dispositivo
   - Run > Run 'app' en Android Studio

## 3. Configuración Post-Instalación

### 3.1 Primer Inicio
1. Abrir la aplicación
2. Iniciar sesión con las credenciales por defecto:
   - Usuario: admin
   - Contraseña: admin123

### 3.2 Configuración Recomendada
1. Cambiar la contraseña del administrador
2. Crear usuarios adicionales según necesidad
3. Verificar la conexión con el servidor

## 4. Solución de Problemas

### 4.1 Problemas Comunes
- **Error de conexión**: Verificar la URL del servidor en la configuración
- **Error de login**: Confirmar que la base de datos está correctamente configurada
- **Problemas de sincronización**: Verificar la conexión a internet

### 4.2 Soporte
- Reportar issues en el repositorio de GitHub
- Consultar la documentación en la wiki del proyecto

## 5. Actualización

### 5.1 Actualización de la App
1. Desinstalar la versión anterior
2. Instalar la nueva versión siguiendo los pasos de instalación

### 5.2 Actualización de la Base de Datos
1. Hacer backup de los datos existentes
2. Ejecutar los scripts de migración (si existen)
3. Verificar la integridad de los datos

## 6. Seguridad

### 6.1 Recomendaciones
- Cambiar las credenciales por defecto
- Mantener el sistema actualizado
- Realizar backups periódicos
- Usar conexiones seguras (HTTPS)