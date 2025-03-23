# Instrucciones Finales para macOS

## UN SOLO COMANDO

```bash
./generar_apk_final.sh
```

## ¿Qué hace?

El script hace TODO automáticamente y en orden:
1. Diagnostica el entorno
2. Limpia archivos anteriores
3. Crea recursos necesarios
4. Instala Java si falta
5. Instala Android SDK si falta
6. Configura todo el entorno
7. Genera el APK

## Requisitos

Solo necesitas tener Homebrew:
```bash
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
```

## El APK

El APK estará en:
```
app/release/CaseApp-v1.1.0.apk
```

## Credenciales
- Usuario: `admin`
- Contraseña: `admin123`

## Si hay errores

El script te mostrará exactamente qué hacer, pero en general:

1. Si tienes versiones anteriores de Java:
```bash
brew uninstall --cask temurin
brew uninstall --cask temurin17
```

2. Si hay problemas con Android SDK:
```bash
brew install android-commandlinetools
sdkmanager --licenses
```

3. Si hay otros errores:
```bash
# Ver diagnóstico detallado
./diagnosticar_mac.sh

# Limpiar todo y empezar de nuevo
./limpiar_mac.sh
```

¡Un comando y listo!