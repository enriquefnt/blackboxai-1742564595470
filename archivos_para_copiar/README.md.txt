=== Contenido de README.md ===
# CaseApp - Aplicación de Seguimiento Antropométrico

## Instalación Rápida (3 Pasos)

1. Clonar el repositorio:
   ```bash
   git clone https://github.com/enriquefnt/blackboxai-1742564595470.git
   cd blackboxai-1742564595470
   ```

2. Ejecutar el instalador:
   ```bash
   chmod +x install.sh
   ./install.sh
   ```

3. Instalar en Android:
   - Copiar `app/release/CaseApp-v1.1.0.apk` al dispositivo
   - Instalar el APK

## Credenciales Iniciales
- Usuario: `admin`
- Contraseña: `admin123`

## Requisitos

### Para Generar el APK
- Java 17 o superior
- Conexión a Internet

### Para el Servidor
- MySQL 5.7+
- PHP 7.4+

## Configuración del Servidor

1. Configurar base de datos:
   ```bash
   cd database
   mysql -u tu_usuario -p < schema_mysql.sql
   ```

2. Verificar instalación:
   - Iniciar sesión en la app
   - Crear un caso de prueba
   - Verificar sincronización

## Solución de Problemas

### Error al Generar APK
```bash
# Instalar Java 17
sudo apt install openjdk-17-jdk  # Ubuntu/Debian
sudo dnf install java-17-openjdk # Fedora
```

### Error de Base de Datos
1. Verificar MySQL:
   ```sql
   mysql -u tu_usuario -p
   SHOW DATABASES;
   USE caseapp;
   SHOW TABLES;
   ```

### Error de Conexión
1. Verificar URL del servidor en la app
2. Comprobar MySQL está ejecutándose
3. Verificar firewall/puertos

## Documentación Adicional

- [Guía Detallada](QUICKSTART.md)
- [Cambios Recientes](CHANGELOG.md)
- [Notas de Migración](MIGRATION.md)

## Soporte

- Crear issue en GitHub
- Incluir logs de error
- Describir pasos para reproducir

## Notas de la Versión 1.1.0

- Migración a AndroidX
- Optimizaciones de rendimiento
- Soporte para Android 14
- Base de datos MySQL optimizada
=== Fin del archivo ===

