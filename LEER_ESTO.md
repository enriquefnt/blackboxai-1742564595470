# Instrucciones Super Simples para Generar el APK

## 1. Instala Java 17

En Ubuntu/Debian:
```bash
sudo apt install openjdk-17-jdk
```

En Fedora:
```bash
sudo dnf install java-17-openjdk
```

## 2. Ejecuta UN comando

```bash
./compilar.sh
```

## 3. Listo!

El APK estará en:
```
app/release/CaseApp-v1.1.0.apk
```

## Credenciales
- Usuario: admin
- Contraseña: admin123

## Problemas?

Si ves errores, asegúrate de:
1. Tener Java 17 instalado
2. Tener conexión a internet
3. Que el script tenga permisos de ejecución:
   ```bash
   chmod +x compilar.sh
   ```

¡Eso es todo! Un solo comando y listo.