# Instrucciones Super Simples para macOS

## UN SOLO COMANDO

```bash
./instalar_mac.sh
```

## ¿Qué hace?

El script hace TODO automáticamente:
- Instala Java 17
- Instala Android SDK
- Configura el entorno
- Genera el APK

## ¿Dónde está el APK?

El APK estará en:
```
app/release/CaseApp-v1.1.0.apk
```

## Credenciales
- Usuario: `admin`
- Contraseña: `admin123`

## ¿Problemas?

Si ves algún error al principio:
```bash
xcode-select --install
```
Y luego vuelve a intentar.

¡Eso es todo! Un comando y listo.