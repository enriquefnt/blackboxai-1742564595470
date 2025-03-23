# CaseApp - Instrucciones de Instalación

## Pasos Simples

1. Clona el repositorio:
```bash
git clone https://github.com/enriquefnt/blackboxai-1742564595470.git
cd blackboxai-1742564595470
```

2. Ejecuta el script de configuración:
```bash
chmod +x setup.sh
./setup.sh
```

3. Genera el APK:
```bash
./install.sh
```

El APK se generará en: `app/release/CaseApp-v1.1.0.apk`

## Requisitos

- Java 17 o superior
- Android Studio (opcional)

### Instalar Java 17:

En Ubuntu/Debian:
```bash
sudo apt install openjdk-17-jdk
```

En Fedora:
```bash
sudo dnf install java-17-openjdk
```

## Solución de Problemas

Si encuentras errores:

1. Verifica Java:
```bash
java -version
```

2. Limpia el proyecto:
```bash
./gradlew clean
```

3. Vuelve a intentar:
```bash
./install.sh
```

## Credenciales

- Usuario: admin
- Contraseña: admin123

## Notas

- El script `setup.sh` crea todos los archivos necesarios
- No necesitas copiar archivos manualmente
- Todo está configurado para AndroidX
- El APK se genera automáticamente

## Ayuda

Si necesitas ayuda:
1. Revisa los logs de error
2. Verifica que Java 17 esté instalado
3. Asegúrate de ejecutar los scripts en orden