# Instrucciones Super Simples

## UN SOLO COMANDO

```bash
./simple_build.sh
```

## ¿Qué hace?

El script:
- Usa un icono vectorial simple
- No depende de archivos PNG
- Usa una configuración mínima
- Genera el APK

## Requisitos

Solo necesitas:
1. Homebrew:
```bash
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
```

2. Java:
```bash
brew tap homebrew/cask-versions
brew install --cask zulu17
```

3. Android SDK:
```bash
brew install android-commandlinetools
```

## Si hay errores

1. Desinstala versiones anteriores de Java:
```bash
brew uninstall --cask temurin
brew uninstall --cask temurin17
```

2. Limpia todo:
```bash
rm -rf app/build app/release build .gradle
rm -f local.properties app/release/keystore.jks
```

3. Intenta de nuevo:
```bash
./simple_build.sh
```

## El APK

El APK estará en:
```
app/release/CaseApp-v1.1.0.apk
```

## Credenciales
- Usuario: `admin`
- Contraseña: `admin123`

¡Un comando y listo!