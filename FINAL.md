# Instrucciones Finales para macOS

## DOS COMANDOS

1. Preparar el entorno:
```bash
./prepare_mac.sh
```
Este comando:
- Instala Homebrew si falta
- Instala Java 17 (Zulu OpenJDK)
- Instala Android SDK
- Configura todo el entorno

2. Generar el APK:
```bash
./build_simple.sh
```
Este comando:
- Crea una app Android mínima
- Genera el APK

## El APK

El APK estará en:
```
app/release/CaseApp-v1.1.0.apk
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

3. Vuelve a intentar:
```bash
./prepare_mac.sh
./build_simple.sh
```

## Notas
- Los scripts muestran mensajes detallados
- Si hay errores, te dirán exactamente qué hacer
- Todo está configurado de forma minimalista
- No hay dependencias innecesarias

¡Dos comandos y listo!