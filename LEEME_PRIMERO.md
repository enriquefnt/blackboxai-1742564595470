# Instrucciones Simples para CaseApp

## Lo que necesitas hacer:

1. Abre el archivo `COPIAR_Y_PEGAR.md`
2. Copia y pega cada archivo en tu proyecto
3. Ejecuta los comandos

## Archivos que debes crear:

En la raíz del proyecto:
- `build.gradle`
- `settings.gradle`
- `gradle.properties`
- `install.sh`

En la carpeta app/:
- `app/build.gradle`

## Comandos a ejecutar:

```bash
# 1. Clona el repositorio
git clone https://github.com/enriquefnt/blackboxai-1742564595470.git
cd blackboxai-1742564595470

# 2. Crea el directorio app
mkdir -p app/release

# 3. Copia los archivos del COPIAR_Y_PEGAR.md

# 4. Haz ejecutable el script
chmod +x install.sh

# 5. Genera el APK
./install.sh
```

## ¿Dónde está el APK?
El APK se generará en: `app/release/CaseApp-v1.1.0.apk`

## ¿Problemas?
1. Verifica que tengas Java 17:
```bash
java -version
```

2. Si necesitas instalar Java:
```bash
sudo apt install openjdk-17-jdk  # Ubuntu/Debian
sudo dnf install java-17-openjdk # Fedora
```

## Credenciales
- Usuario: admin
- Contraseña: admin123

¡Eso es todo! Si tienes dudas, revisa COPIAR_Y_PEGAR.md para más detalles.