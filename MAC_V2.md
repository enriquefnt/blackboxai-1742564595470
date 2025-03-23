# Instrucciones Actualizadas para macOS

## Requisitos Previos

1. Instala Xcode Command Line Tools:
```bash
xcode-select --install
```
Espera a que termine la instalación.

## Instalación Simple (2 Pasos)

1. Clona el repositorio:
```bash
git clone https://github.com/enriquefnt/blackboxai-1742564595470.git
cd blackboxai-1742564595470
```

2. Ejecuta el instalador:
```bash
chmod +x instalar_mac_v2.sh
./instalar_mac_v2.sh
```

## ¿Qué hace el instalador?

El script automáticamente:
- Instala Java 17
- Instala Android SDK
- Configura el entorno
- Verifica todos los archivos necesarios
- Genera el APK

## ¿Dónde está el APK?

El APK se generará en:
```
app/release/CaseApp-v1.1.0.apk
```

## Credenciales
- Usuario: `admin`
- Contraseña: `admin123`

## Solución de Problemas

Si ves errores:

1. Asegúrate de que Xcode Command Line Tools esté instalado:
```bash
xcode-select --install
```

2. Verifica Java:
```bash
java -version  # Debe mostrar versión 17
```

3. Si hay problemas con permisos:
```bash
chmod +x instalar_mac_v2.sh
chmod +x gradlew
```

4. Si hay problemas con Android SDK:
```bash
brew install android-commandlinetools
sdkmanager --licenses
```

El script mostrará mensajes detallados si algo falla.