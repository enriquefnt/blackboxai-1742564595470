# Instrucciones Finales para macOS

## UN SOLO COMANDO

```bash
./generar_apk_mac.sh
```

## ¿Qué hace?

El script:
1. Verifica todos los recursos necesarios
2. Limpia cualquier instalación anterior
3. Configura el entorno
4. Genera el APK
5. Muestra mensajes detallados si hay errores

## Si hay errores

1. Verifica que tengas los requisitos:
```bash
# Instalar Xcode Command Line Tools
xcode-select --install

# Instalar Java 17
brew tap homebrew/cask-versions
brew install --cask temurin17

# Instalar Android SDK
brew install android-commandlinetools
```

2. Verifica los recursos:
```bash
./verificar_recursos.sh
```

3. Si persisten los errores:
```bash
# Ver más detalles
./gradlew assembleRelease --info

# Ver logs completos
./gradlew assembleRelease --debug
```

## El APK

Cuando todo funcione, el APK estará en:
```
app/release/CaseApp-v1.1.0.apk
```

## Credenciales
- Usuario: `admin`
- Contraseña: `admin123`

## Archivos Importantes

- `generar_apk_mac.sh`: Script principal
- `verificar_recursos.sh`: Verifica archivos necesarios
- `app/src/main/AndroidManifest.xml`: Configuración de la app
- `app/src/main/res/values/strings.xml`: Textos de la app
- `app/build.gradle`: Configuración de compilación

## Notas

- El script mostrará mensajes detallados en cada paso
- Si hay errores, te dirá exactamente qué falta
- Los archivos se verifican antes de empezar
- Se genera un nuevo keystore cada vez

¡Un solo comando y listo!