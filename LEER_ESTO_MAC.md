# Instrucciones Super Simples para macOS

## UN SOLO COMANDO

```bash
./compilar_mac.sh
```

## ¿Qué hace?

El script hace TODO automáticamente:
- Crea todos los recursos necesarios
- Instala Java (Zulu OpenJDK 17)
- Instala Android SDK
- Configura el entorno
- Genera el APK

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

## ¿Problemas?

Si ves errores de Java:
```bash
# Desinstalar versiones anteriores
brew uninstall --cask temurin
brew uninstall --cask temurin17

# El script instalará la versión correcta
```

¡Un comando y listo!