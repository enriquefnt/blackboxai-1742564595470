# Instrucciones Súper Simples para macOS

## Dos Comandos y Listo

1. Instala todo lo necesario:
```bash
./instalar_dependencias_mac.sh
```

2. Genera el APK:
```bash
./compilar_mac.sh
```

El APK estará en: `app/release/CaseApp-v1.1.0.apk`

## ¿Problemas?

Si ves algún error al principio:
```bash
# Instala Xcode Command Line Tools
xcode-select --install

# Intenta de nuevo
./instalar_dependencias_mac.sh
```

## Credenciales
- Usuario: admin
- Contraseña: admin123

¡Eso es todo! Solo dos comandos y listo.